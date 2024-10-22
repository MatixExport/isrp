package pl.lodz.p.it.isrp.wm.model;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "LOCATION", uniqueConstraints = {
    @UniqueConstraint(name = "LOCATION_UNIQUE", columnNames ="LOCATION_SYMBOL")
})
@TableGenerator(name = "LocationGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "LocationGen")
@NamedQueries({
    @NamedQuery(name = "Location.findAll", query = "SELECT l FROM Location l")
    ,
    @NamedQuery(name = "Location.findByLocation", query = "SELECT l FROM Location l WHERE l.locationSymbol = :lid")
    ,
    @NamedQuery(name = "Location.findEmptyLocation", query = "SELECT l FROM Location l WHERE l.emptyLocation=true")
})
public class Location extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "LocationGenerator")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @OneToOne(mappedBy = "location")
    private Stock stock;

    @NotNull
    @Column(name = "LOCATION_TYPE", nullable = false, updatable = true)
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @NotNull
    @Size(min = 11, max = 11)
    @Column(name = "LOCATION_SYMBOL", nullable = false, updatable = false)
    private String locationSymbol;

    @NotNull
    @Column(name = "MAX_WEIGHT_IN_G", nullable = false)
    private int maxWeight;

    @NotNull
    @Column(name = "EMPTY_LOCATION", nullable = false, updatable = true)
    private boolean emptyLocation;

    @NotNull
    @JoinColumn(name = "CREATED_BY", nullable = false, updatable = false)
    @OneToOne
    private WarehouseAccount createdBy;

    @NotNull
    @JoinColumn(name = "MODIFICATED_BY", nullable = true)
    @OneToOne
    private WarehouseAccount modificatedBy;

    public Location() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public void setLocationType(LocationType locationType) {
        this.locationType = locationType;
        this.maxWeight = locationType.getWeightLimit();
    }

    public String getLocationSymbol() {
        return locationSymbol;
    }

    public void setLocationSymbol(String locationSymbol) {
        this.locationSymbol = locationSymbol;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public boolean isEmptyLocation() {
        return emptyLocation;
    }

    public void setEmptyLocation(boolean emptyLocation) {
        this.emptyLocation = emptyLocation;
    }

    public WarehouseAccount getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(WarehouseAccount createdBy) {
        this.createdBy = createdBy;
    }

    public WarehouseAccount getModificatedBy() {
        return modificatedBy;
    }

    public void setModificatedBy(WarehouseAccount modificatedBy) {
        this.modificatedBy = modificatedBy;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public int hashCode() {
        int hash = 7;
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
        final Location other = (Location) obj;
        if (!Objects.equals(this.locationSymbol, other.locationSymbol)) {
            return false;
        }
        return true;
    }

}
