package pl.lodz.p.it.isrp.wm.web.utils;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.annotation.FacesConfig;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;


@FacesConfig
@ApplicationScoped
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/faces/common/signIn.xhtml",
                errorPage = "/faces/error/errorSignIn.xhtml",
                useForwardToLogin = false ))

public class SecurityAppConfig {

}
