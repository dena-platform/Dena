package com.dena.platform.core.feature.search.lucene;

import com.dena.platform.core.dto.DenaObject;
import com.dena.platform.core.feature.persistence.DenaPager;
import com.dena.platform.core.feature.search.Search;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Nazarpour.
 */
public class LuceneSearch implements Search {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneSearch.class);

    private ConcurrentHashMap<String, IndexWriter> writers = new ConcurrentHashMap();
    private ConcurrentHashMap<String, IndexReader> readers = new ConcurrentHashMap();
    private boolean immediateWriter;

    public LuceneSearch(boolean immediateWriter) {
        this.immediateWriter = immediateWriter;
    }

    @Override
    public void index(String appId, DenaObject object) {
        Document doc = LuceneUtils.createDocument(appId, object.getObjectURI(), object);
        writeIndex(appId, doc);
    }

    private void writeIndex(String appId, Document doc) {
        IndexWriter iWriter = getWriter(appId);
        try {
            if (iWriter != null) {
                iWriter.addDocument(doc);
                if (immediateWriter) {
                    iWriter.commit();
                }
            }
        } catch (IOException e) {
            LOGGER.warn(String.format("unable to write lucene document to writer: %s", iWriter.getDirectory().toString()));
        }
    }

    private IndexWriter getWriter(String appId) {
        if (!writers.contains(appId)) {
            String dataDir = "c:/temp";//todo
            Path path = FileSystems.getDefault().getPath(dataDir, "data", appId);
            Analyzer analyzer = new StandardAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

            //todo: IndexWriter should close and commit on shutdown
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


    private IndexReader getReader(String appId) {
        if (!readers.contains(appId)) {
            String dataDir = "c:/temp";//todo
            Path path = FileSystems.getDefault().getPath(dataDir, "data", appId);
            try {
                IndexReader reader = DirectoryReader.open(FSDirectory.open(path));
            } catch (IOException e) {
                LOGGER.warn(String.format("unable to open index in path %s", path));
            }
        }
    }

    @Override
    public List<DenaObject> query(String appId, String query, String field, DenaPager pager) {
        IndexReader reader = getReader(appId);

    }

    @Override
    public void close() {
        for (Map.Entry<String, IndexWriter> entry : writers.entrySet()) {
            IndexWriter writer = entry.getValue();
            try {
                if (!immediateWriter) {
                    writer.commit();
                }
                writer.close();
            } catch (IOException e) {
                LOGGER.warn(String.format("unable to commit writer content in %s", entry.getKey()));
            }
        }
    }
}
