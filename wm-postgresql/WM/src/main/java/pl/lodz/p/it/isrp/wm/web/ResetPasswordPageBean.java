package pl.lodz.p.it.isrp.wm.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.dto.AccountDTO;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "resetPasswordPageBean")
@RequestScoped
public class ResetPasswordPageBean {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    public ResetPasswordPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    @PostConstruct
    public void init() {
        accountControllerBean.setSelectedAccountDTO(null);
        accountDTO = new AccountDTO();
    }

    public String selectAccountToResetPasswordAction() {
        try {
            accountControllerBean.selectAccountForChange(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ResetPasswordPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        accountControllerBean.getSelectedAccountDTO().setAnswer(""); // zapobiega sytuacji wyświetlenia się odpowiedzi w formularzu
        return "questionResetPassword";
    }
}
