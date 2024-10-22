package pl.lodz.p.it.isrp.wm.model;

import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.ADMINISTRATION)
@NamedQueries({
    @NamedQuery(name = "AdministrationAccount.findAll", query = "SELECT a FROM AdministrationAccount a")
    ,
    @NamedQuery(name = "AdministrationAccount.findByLogin", query = "SELECT a FROM AdministrationAccount a WHERE a.login = :lg")
})
public class AdministrationAccount extends Account implements Serializable {

    public AdministrationAccount() {
    }

    public AdministrationAccount(Account account) {
        super(account.getId(), account.getName(), account.getSurname(), account.getEmail(), account.getQuestion(), account.getAnswer(), account.getLogin(), account.getPassword());
    }
}
