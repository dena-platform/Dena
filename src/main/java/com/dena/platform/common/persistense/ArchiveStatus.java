package com.dena.platform.common.persistense;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Author: Javad Alimohammadi Email:bc_alimohammadi@yahoo.com
 */

@Embeddable
public class ArchiveStatus {
    @Column(name = "ARCHIVED")
    private Character archived = 'N';

    public Character getArchived() {
        return archived;
    }

    public void setArchived(Character archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArchiveStatus that = (ArchiveStatus) o;

        return !(archived != null ? !archived.equals(that.archived) : that.archived != null);

    }

    @Override
    public int hashCode() {
        return archived != null ? archived.hashCode() : 0;
    }
}
