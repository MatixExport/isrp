package pl.lodz.p.it.isrp.wm.web;

import java.io.Serializable;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import jakarta.faces.view.ViewScoped;
import pl.lodz.p.it.isrp.wm.dto.IssueDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.IssueEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "listIssuesForProductPageBean")
@ViewScoped
public class ListIssuesForProductPageBean implements Serializable {

    @EJB
    private IssueEndpoint issueEndpoint;

    private List<IssueDTO> listIssues;
    private DataModel<IssueDTO> dataModelIssues;

    private List<String> products;
    private DataModel<String> dataModelProducts;

    private IssueDTO issueDTO;
    private TimeZone timeZone;

    public ListIssuesForProductPageBean() {
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public List<IssueDTO> getListIssues() {
        return listIssues;
    }

    public void setListIssues(List<IssueDTO> listIssues) {
        this.listIssues = listIssues;
    }

    public IssueDTO getIssueDTO() {
        return issueDTO;
    }

    public void setIssueDTO(IssueDTO issueDTO) {
        this.issueDTO = issueDTO;
    }

    public TimeZone getTimeZone() {
        timeZone = TimeZone.getDefault();
        return timeZone;
    }

    @PostConstruct
    public void initListIssues() {
        try {
            products = issueEndpoint.listProducts();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListIssuesForProductPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelProducts = new ListDataModel<>(products);
        issueDTO = new IssueDTO();
    }

    public String showListAction() {
        try {
            listIssues = issueEndpoint.listIssuesForProduct(issueDTO.getProductSymbol());
            dataModelIssues = new ListDataModel<>(listIssues);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListIssuesForProductPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        return null;
    }

}
