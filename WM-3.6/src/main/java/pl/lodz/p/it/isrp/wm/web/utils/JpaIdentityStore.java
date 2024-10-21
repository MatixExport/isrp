package pl.lodz.p.it.isrp.wm.web.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.SecurityEndpoint;
import pl.lodz.p.it.isrp.wm.model.Account;


@ApplicationScoped
public class JpaIdentityStore implements IdentityStore {

    private static final Logger LOG = Logger.getLogger(JpaIdentityStore.class.getName());

    @Inject
    private SecurityEndpoint securityEndpoint;

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return IdentityStore.super.getCallerGroups(validationResult);
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
            Account account = securityEndpoint.validateAuthCredential(usernamePasswordCredential.getCaller(), usernamePasswordCredential.getPasswordAsString());
            return (null!=account ? new CredentialValidationResult(account.getLogin(), new HashSet<>(Arrays.asList(account.getAccessLevel()))) : CredentialValidationResult.INVALID_RESULT);
        }
        return CredentialValidationResult.NOT_VALIDATED_RESULT;

    }

}
