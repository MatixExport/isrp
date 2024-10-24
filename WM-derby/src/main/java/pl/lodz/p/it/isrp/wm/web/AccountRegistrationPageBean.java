package pl.lodz.p.it.isrp.wm.web;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.dto.AccountDTO;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "accountRegistrationPageBean")
@RequestScoped
public class AccountRegistrationPageBean implements Serializable {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    private String passwordRepeat;

    public AccountRegistrationPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    @PostConstruct
    public void init() {
        accountDTO = new AccountDTO();
    }

    public String registerAccountAction() {
        if (passwordRepeat.equals(accountDTO.getPassword())) {
            try {
                accountControllerBean.registerAccount(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(AccountRegistrationPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:passwordRepeat", "error.passwords.not.matching");
            return null;
        }
        return "main";
    }

}
