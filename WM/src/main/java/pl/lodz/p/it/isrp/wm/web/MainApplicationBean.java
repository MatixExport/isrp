package pl.lodz.p.it.isrp.wm.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "mainApplicationPageBean")
@ApplicationScoped
public class MainApplicationBean {

    public MainApplicationBean() {
    }

    public String signOutAction() {
        ContextUtils.invalidateSession();
        return "cancelAction";
    }

    public String getMyLogin() {
        return ContextUtils.getUserName();
    }
}
