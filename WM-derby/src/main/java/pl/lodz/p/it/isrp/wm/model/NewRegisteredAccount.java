package pl.lodz.p.it.isrp.wm.model;

import java.io.Serializable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.NEWREGISTERED)
public class NewRegisteredAccount extends Account implements Serializable {

    public NewRegisteredAccount() {
    }

}
