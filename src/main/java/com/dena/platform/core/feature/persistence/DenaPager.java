package com.dena.platform.core.feature.persistence;

import com.dena.platform.common.config.DenaConfigReader;

/**
 * <p> Borrowed from "Erudika Para" project which is licensed under the Apache License, Version 2.0.
 * See http://www.apache.org/licenses/LICENSE-2.0.
 *
 * @author Javad Alimohammadi [<bs.alimohammadi@yahoo.com>]
 */
public class DenaPager {
    public final static String PAGE_PARAMETER = "page";
    public final static String ITEM_PER_PAGE_PARAMETER = "itemPerPage";

    private long page; // the page number
    private long count; // total count of results
    private boolean desc;
    private int limit; // the max number of results in one page
    private String name; // Name of this pager object (optional). Used to distinguish between multiple pagers
    private String lastKey; //the last key from last page. Used for scanning and pagination


    /**
     * Default constructor with a page, count, desc and limit.
     *
     * @param page  the page number
     * @param desc  sort order
     * @param limit the results limit
     */
    public DenaPager(long page, boolean desc, int limit) {
        this.page = page;
        this.count = 0;
        this.desc = desc;
        this.limit = limit;
    }

    /**
     * Default constructor with a page and count.
     *
     * @param page  the page number
     * @param limit the results limit
     */
    public DenaPager(long page, int limit) {
        this(page, true, limit);
    }

    /**
     * Default constructor with limit.
     *
     * @param limit the results limit
     */
    public DenaPager(int limit) {
        this(1, true, limit);
    }

    /**
     * No-args constructor.
     */
    public DenaPager() {
        this(1, true, DenaConfigReader.readIntProperty("pager.max.item.per.page"));
    }

    /**
     * Page number. Usually starts from 1...
     *
     * @return the page number
     */

    public long getPage() {
        return page;
    }

    /**
     * Set the value of page.
     *
     * @param page the page number
     */
    public void setPage(long page) {
        this.page = page;
    }

    /**
     * The total number of results for a query.
     *
     * @return total count of results
     */
    public long getCount() {
        return count;
    }

    /**
     * Set the value of count.
     *
     * @param count total count
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * The sort order. Default: descending (true)
     *
     * @return true if descending
     */
    public boolean isDesc() {
        return desc;
    }

    /**
     * Sets the value of desc.
     *
     * @param desc true if descending order
     */
    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    /**
     * Limits the maximum number of results to return in one page.
     *
     * @return the max number of results in one page
     */
    public int getLimit() {
        if (limit > DenaConfigReader.readIntProperty("pager.max.item.per.page")) {
            limit = DenaConfigReader.readIntProperty("pager.max.item.per.page");
        }
        return limit;
    }

    /**
     * Set the value of limit.
     *
     * @param limit the max number of results in one page
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Name of this pager object (optional). Used to distinguish between multiple pagers.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name.
     *
     * @param name the name (optional)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Name of this pager object (optional). Used to distinguish between multiple pagers.
     *
     * @return the name
     */
    public String getLastKey() {
        return lastKey;
    }

    /**
     * Sets the last key from last page. Used for scanning and pagination.
     *
     * @param lastKey last id
     */
    public void setLastKey(String lastKey) {
        this.lastKey = lastKey;
    }

    @Override
    public String toString() {
        return "Pager{" + "page=" + page + ", count=" + count + ", desc=" + desc +
                ", limit=" + limit + ", name=" + name + ", lastKey=" + lastKey + '}';
    }


    public static final class DenaPagerBuilder {
        private DenaPager denaPager;

        private DenaPagerBuilder() {
            denaPager = new DenaPager();
        }

        public static DenaPagerBuilder aDenaPager() {
            return new DenaPagerBuilder();
        }

        public DenaPagerBuilder withPage(long page) {
            if (page > 0) {
                denaPager.setPage(page);
            }

            return this;
        }

        public DenaPagerBuilder withCount(long count) {
            denaPager.setCount(count);
            return this;
        }

        public DenaPagerBuilder withDesc(boolean desc) {
            denaPager.setDesc(desc);
            return this;
        }

        public DenaPagerBuilder withLimit(int limit) {
            if (limit > 0) {
                denaPager.setLimit(limit);
            }

            return this;
        }

        public DenaPagerBuilder withName(String name) {
            denaPager.setName(name);
            return this;
        }

        public DenaPagerBuilder withLastKey(String lastKey) {
            denaPager.setLastKey(lastKey);
            return this;
        }

        public DenaPager build() {
            return denaPager;
        }
    }
}
