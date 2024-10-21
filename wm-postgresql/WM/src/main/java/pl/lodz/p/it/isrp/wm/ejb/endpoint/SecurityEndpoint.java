package pl.lodz.p.it.isrp.wm.ejb.endpoint;

import jakarta.annotation.security.RunAs;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.ejb.facade.AccountFacade;
import pl.lodz.p.it.isrp.wm.model.Account;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@RunAs("AUTHENTICATOR") //wywoływana metoda fasady weryfikująca poświadczenia wymaga podanej roli
public class SecurityEndpoint {

    @Inject
    private AccountFacade accountFacade;

    public Account validateAuthCredential(String login, String passwordHash) {

        // Odwołanie do metody fasady, która realizuje kwerendę sprawdzającą warunki poprawności uwierzytelniania
        return accountFacade.findActiveAccountByCredentials(login, passwordHash);
    }

}
