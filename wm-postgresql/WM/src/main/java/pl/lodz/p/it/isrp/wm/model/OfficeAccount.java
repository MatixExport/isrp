package pl.lodz.p.it.isrp.wm.model;

import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.OFFICE)
@NamedQueries({
    @NamedQuery(name = "OfficeAccount.findAll", query = "SELECT o FROM OfficeAccount o")
    ,
    @NamedQuery(name = "OfficeAccount.findByLogin", query = "SELECT o FROM OfficeAccount o WHERE o.login = :lg")
})
public class OfficeAccount extends Account implements Serializable {

    public OfficeAccount() {
    }

    public OfficeAccount(Account account) {
        super(account.getId(), account.getName(), account.getSurname(), account.getEmail(), account.getQuestion(), account.getAnswer(), account.getLogin(), account.getPassword());
    }
}
