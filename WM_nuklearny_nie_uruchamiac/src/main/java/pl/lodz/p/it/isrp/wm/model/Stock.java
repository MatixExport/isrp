package pl.lodz.p.it.isrp.wm.model;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "STOCK", uniqueConstraints = {
    @UniqueConstraint(name = "LOCATION_IN_STOCK_UNIQUE", columnNames ="LOCATION")
})
@TableGenerator(name = "StockGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "StockGen")
@NamedQueries({
    @NamedQuery(name = "Stock.findAll", query = "SELECT s FROM Stock s")
    ,
    @NamedQuery(name = "Stock.findAllStock", query = "SELECT s FROM Stock s WHERE s.quantity>0")
    ,
    @NamedQuery(name = "Stock.findByProduct", query = "SELECT s FROM Stock s WHERE s.product.productSymbol = :p")
    ,
    @NamedQuery(name = "Stock.findByLocation", query = "SELECT s FROM Stock s WHERE s.location.locationSymbol = :loc")
    ,
    @NamedQuery(name = "Stock.findByContractor", query = "SELECT s FROM Stock s WHERE s.contractor.contractorNumber = :cn")
})
public class Stock extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "StockGenerator")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;

    @NotNull
    @Size(min = 1, max = Integer.MAX_VALUE)
    @Column(name = "QUANTITY", nullable = false, updatable = true)
    private int quantity;

    @NotNull
    @JoinColumn(name = "LOCATION_ID", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Location location;

    @NotNull
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Product product;

    @NotNull
    @JoinColumn(name = "CONTRACTOR_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Contractor contractor;

    @NotNull
    @JoinColumn(name = "CREATED_BY", nullable = false, updatable = false)
    @OneToOne
    private WarehouseAccount createdBy;

    @NotNull
    @JoinColumn(name = "MODIFICATED_BY", nullable = true)
    @OneToOne
    private WarehouseAccount modificatedBy;

    public Stock() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Contractor getContractor() {
        return contractor;
    }

    public void setContractor(Contractor contractor) {
        this.contractor = contractor;
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

}
