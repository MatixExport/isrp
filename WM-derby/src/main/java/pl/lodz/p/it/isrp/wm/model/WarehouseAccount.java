package pl.lodz.p.it.isrp.wm.model;

import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.WAREHOUSE)
@NamedQueries({
    @NamedQuery(name = "WarehouseAccount.findAll", query = "SELECT w FROM WarehouseAccount w"),
    @NamedQuery(name = "WarehouseAccount.findByLogin", query = "SELECT w FROM WarehouseAccount w WHERE w.login = :lg")
})
public class WarehouseAccount extends Account implements Serializable {

    public WarehouseAccount() {
    }

    public WarehouseAccount(Account account) {
        super(account.getId(), account.getName(), account.getSurname(), account.getEmail(), account.getQuestion(), account.getAnswer(), account.getLogin(), account.getPassword());
    }
}
