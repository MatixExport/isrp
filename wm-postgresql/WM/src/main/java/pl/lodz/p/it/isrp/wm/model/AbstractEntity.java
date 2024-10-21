package pl.lodz.p.it.isrp.wm.model;

import java.util.Date;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Version
    private int version;

    @NotNull
    @Column(name = "CREATION_DATE", nullable = false, updatable = false)
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date creationDate;

    @NotNull
    @Column(name = "MODIFICATION_DATE", nullable = true, updatable = true)
    @Temporal(jakarta.persistence.TemporalType.TIMESTAMP)
    private Date modificationDate;

    @PrePersist
    public void initCreationDate() {
        creationDate = new Date();
    }

    @PreUpdate
    public void initModificationDate() {
        modificationDate = new Date();
    }

    public int getVersion() {
        return version;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    @Override
    public int hashCode() {
        int hash = 101 * creationDate.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractEntity other = (AbstractEntity) obj;
        if (this.version != other.version) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (!Objects.equals(this.modificationDate, other.modificationDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AbstractEntity: " + "version=" + version + ", creationDate=" + creationDate;
    }

}
