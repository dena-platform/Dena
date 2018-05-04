package com.dena.platform.core.feature.search.lucene;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaDataStore;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.search.Search;
import com.dena.platform.core.feature.user.domain.User;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Nazarpour.
 */
public class LuceneSearch implements Search {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneSearch.class);

    private Map<String, IndexWriter> writers = new Hashtable<>();
    private Map<String, IndexReader> readers = new Hashtable<>();
    private Map<String, Boolean> isDirty = new Hashtable<>();

    private int commitDelay;
    private String rootDir;

    private DenaDataStore dataStore;

    public LuceneSearch(int commitDelay, String rootDir, DenaDataStore dataStore) {
        this.commitDelay = commitDelay;
        this.rootDir = rootDir;
        this.dataStore = dataStore;

        init(commitDelay);
    }

    private void init(int commitDelay) {
        if (commitDelay < 1) {
            return;
        }

        LuceneSearch luceneSearch = this;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.schedule(luceneSearch::commitAll, commitDelay, TimeUnit.SECONDS);
    }

    private void commitAll() {
        LuceneUtils.commitAllWriters(writers);
    }

    @Override
    public void index(String appId, String collectionName, User user, DenaObject... object) {
        List<Document> docs = LuceneUtils.createDocuments(appId, collectionName, user, object);
        writeIndex(appId, user, docs);
    }

    private void writeIndex(String appId, User user, List<Document> docs) {
        IndexWriter iWriter = getWriter(appId, user.getEmail());
        try {
            if (iWriter != null) {
                for (Document doc : docs) {
                    iWriter.addDocument(doc);
                }
                isDirty.put(appId, true);
                if (needsCommit()) {
                    iWriter.forceMerge(1);
                    iWriter.commit();
                }
            }
        } catch (IOException e) {
            LOGGER.warn(String.format("unable to write lucene document to writer: %s", iWriter.getDirectory().toString()));
        }
    }

    private boolean needsCommit() {
        return commitDelay == 0;
    }

    private IndexWriter getWriter(String appId, String username) {
        if (!writers.containsKey(appId)) {
            Path path = FileSystems.getDefault().getPath(this.rootDir, appId, username);
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

            try {
                IndexWriter writer = new IndexWriter(FSDirectory.open(path), config);
                writers.put(appId, writer);
                return writer;
            } catch (IOException e) {
                LOGGER.warn(String.format("unable to open lucene index data in path %s;", path));
            }
            return null;
        } else {
            return writers.get(appId);
        }
    }

    private IndexReader getReader(String appId, String username) {
        if (!isDirty.containsKey(appId) && readers.containsKey(appId)) {
            return readers.get(appId);
        } else {
            Path path = FileSystems.getDefault().getPath(rootDir, appId, username);
            try {
                IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
                readers.put(appId, reader);
                return reader;
            } catch (IOException e) {
                LOGGER.warn(String.format("unable to open index in path %s", path));
                return null;
            }
        }
    }

    @Override
    public List<DenaObject> query(String appId, String collectionName, User user, String query, DenaPager pager) {
        IndexReader reader = getReader(appId, user.getEmail());

        if (reader == null)
            return Collections.emptyList();

        IndexSearcher searcher = new IndexSearcher(reader);

        try {
            Query q = LuceneUtils.getParseQuery(query);

            Sort sort = new Sort();
            @SuppressWarnings("deprecation")
            TopFieldCollector collector = TopFieldCollector.create(sort, 100, false, false, false);
            searcher.search(q, collector);

            TopDocs topDocs = collector.topDocs(pager.getStartIndex(), pager.getPageSize());
            if (topDocs.scoreDocs.length < 1)
                return Collections.emptyList();
            List<DenaObject> results = new ArrayList<>();
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                DenaObject dObj = LuceneUtils.createDenaObject(doc);
                results.add(dObj);
            }

            results = loadEntireObjectFromDataStore(appId, collectionName, results);
            return results;
        } catch (IOException e) {
            LOGGER.warn(String.format("can not search query %s through Dena indexes", query));
        }
        return Collections.emptyList();
    }

    private List<DenaObject> loadEntireObjectFromDataStore(String appId, String collectionName, List<DenaObject> results) {
        List<String> ids = results.stream().map(DenaObject::getObjectId).collect(Collectors.toList());
        return dataStore.find(appId, collectionName, ids.toArray(new String[0]));
    }

    @Override
    public void close() {
        for (Map.Entry<String, IndexWriter> entry : writers.entrySet()) {
            IndexWriter writer = entry.getValue();
            try {
                writer.forceMerge(1);
                writer.commit();
                writer.close();
            } catch (IOException e) {
                LOGGER.warn(String.format("unable to commit writer content in %s", entry.getKey()));
            }
        }
    }


    @Override
    public void updateIndex(String appId, String collectionName, User user, DenaObject... objects) {
        deleteIndex(appId, collectionName, user, objects);
        index(appId, collectionName, user, objects);
    }

    @Override
    public void deleteIndex(String appId, String collectionName, User user, DenaObject... objects) {
        List<DenaObject> denaObjects = Arrays.asList(objects);
        List<String> ids = denaObjects.stream().map(DenaObject::getObjectId).collect(Collectors.toList());
        unindex(appId, user, ids);
    }

    @Override
    public void deleteIndexByIds(String appId, User user, String... ids) {
        unindex(appId, user, Arrays.asList(ids));
    }

    private void unindex(String appId, User user, List<String> ids) {
        IndexWriter writer = getWriter(appId, user.getEmail());

        if (writer == null) {
            LOGGER.warn(String.format("couldn't acquire writer for app %s with user %s ", appId, user.getEmail()));
            return;
        }

        List<Term> terms = new ArrayList<>();

        for (String id : ids) {
            terms.add(LuceneUtils.createTerm(id));
        }
        try {
            writer.deleteDocuments(terms.toArray(new Term[0]));
            writer.forceMerge(1);
            writer.commit();
        } catch (IOException e) {
            LOGGER.warn(String.format("couldn't delete terms for app %s with user %s ", appId, user.getEmail()));
        }
    }
}