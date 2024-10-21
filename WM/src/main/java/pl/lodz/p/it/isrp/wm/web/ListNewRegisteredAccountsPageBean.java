package pl.lodz.p.it.isrp.wm.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.dto.AccountDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.model.AccessLevel;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "listNewRegisteredAccountsPageBean")
@ViewScoped
public class ListNewRegisteredAccountsPageBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountControllerBean accountControllerBean;

    private List<AccountDTO> listAccounts;

    private List<AccessLevel> listAccessLevels;

    public List<AccessLevel> getListAccessLevels() {
        return listAccessLevels;
    }

    private DataModel<AccountDTO> dataModelAccounts;

    public ListNewRegisteredAccountsPageBean() {
    }

    public DataModel<AccountDTO> getDataModelAccounts() {
        return dataModelAccounts;
    }

    @PostConstruct
    public void initListNewAccounts() {
        try {
            listAccounts = accountEndpoint.listNewRegisteredAccount();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListNewRegisteredAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelAccounts = new ListDataModel<>(listAccounts);

        AccessLevel[] listAllAccessLevels = AccessLevel.values();
        //internacjonalizacja poziomów dostępu względem preferencji językowych przęglądarki internetowej
        for (AccessLevel accessLevel : listAllAccessLevels) {
            accessLevel.setAccessLevelI18NValue(ContextUtils.getI18NMessage(accessLevel.getAccessLevelKey()));
        }

        listAccessLevels = new ArrayList<>(Arrays.asList(listAllAccessLevels));
        listAccessLevels.remove(AccessLevel.ACCOUNT);  //usuwa z listy poziom dostępu ACCOUNT aby nie można było go wybrać z listy rozwijanej poziomów dostępu
        listAccessLevels.remove(AccessLevel.NEWREGISTERED);  //usuwa z listy poziom dostępu NEWREGISTERED aby nie można było go wybrać z listy rozwijanej poziomów dostępu
    }

    public String deleteSelectedAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.selectAccountForChange(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListNewRegisteredAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListNewAccounts();
        return "deleteNewRegisteredAccount";
    }

    public String setAccessLevelSelectedAccountAction(AccountDTO accountDTO) {
        if (accountDTO.getAccessLevel() != null) {
            try {
                accountControllerBean.setAccessLevelAccount(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(ListNewRegisteredAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
            }
            initListNewAccounts();
        }
        return null;
    }
}
