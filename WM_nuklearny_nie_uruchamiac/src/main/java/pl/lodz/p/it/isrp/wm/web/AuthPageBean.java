package pl.lodz.p.it.isrp.wm.web;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.lodz.p.it.isrp.wm.web.utils.HashGenerator;

@Named(value = "authPageBean")
@RequestScoped
public class AuthPageBean implements Serializable {

    @NotNull
    private String username;

    @NotNull
    private String password;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private FacesContext facesContext;

    @Inject
    private HashGenerator hashGenerator;

    /**
     * Dokonuje programowo uwierzytelnienia na podstawie loginu i hasła. Dane
     * pochodzą z formularza uwierzytelniania. Dzięki samodzielnemu wywoływaniu
     * login() można przechwycić wyjątek który jest rzucany w przypadku
     * niepoprawnego uwierzytelnienia. Można to wykorzystać np. do blokowania
     * konta po pewnej liczbie nieudanych prób.
     */
    public String loginAction() {
        Credential credential = new UsernamePasswordCredential(username, new Password(password));
        AuthenticationStatus status = securityContext.authenticate(getRequest(), getResponse(), withParams().credential(credential));
        if (status.equals(AuthenticationStatus.SEND_CONTINUE)) {
            facesContext.responseComplete();
            return "";
        } else if (status.equals(AuthenticationStatus.SEND_FAILURE)) {
            return "loginError";
        }
        return "main";
    }

    private HttpServletResponse getResponse() {
        return (HttpServletResponse) facesContext.getExternalContext().getResponse();
    }

    private HttpServletRequest getRequest() {
        return (HttpServletRequest) facesContext.getExternalContext().getRequest();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // Hasło jest od razu zamieniane na skrót, który posłuży do jego porównania z hasłem zapisanym także jako skrót w bazie
    public void setPassword(String password) {
        this.password = hashGenerator.generateHash(password);
    }
}
