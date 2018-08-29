package com.dena.platform.core.feature.persistence;

import java.util.Objects;

/**
 * @author Javad Alimohammadi [<bs.alimohammadi@gmail.com>]
 */
public class DenaPager {
    public final static String PAGE_SIZE_PARAMETER = "pageSize";

    public final static String START_INDEX_PARAMETER = "startIndex";

    private Integer startIndex = 0;

    private Integer pageSize = 50;


    public DenaPager() {
    }

    public DenaPager(Integer startIndex, Integer pageSize) {
        this.startIndex = startIndex;
        this.pageSize = pageSize;
    }

    /**
     * The start row number from which return result to client.
     *
     * @return
     */
    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    /**
     * The number of records to retrieve in a single page. If null, defaults to the "dena.pager.max.results" property value.
     */
    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }


    public static final class DenaPagerBuilder {
        private DenaPager denaPager;

        private DenaPagerBuilder() {
            denaPager = new DenaPager();
        }

        public static DenaPagerBuilder aDenaPager() {
            return new DenaPagerBuilder();
        }

        public DenaPagerBuilder withStartIndex(Integer startIndex) {
            denaPager.setStartIndex(startIndex);
            return this;
        }

        public DenaPagerBuilder withPageSize(Integer pageSize) {
            denaPager.setPageSize(pageSize);
            return this;
        }

        public DenaPager build() {
            return denaPager;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DenaPager denaPager = (DenaPager) o;
        return Objects.equals(startIndex, denaPager.startIndex) &&
                Objects.equals(pageSize, denaPager.pageSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIndex, pageSize);
    }
}
