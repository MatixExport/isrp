package pl.lodz.p.it.isrp.wm.web;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.dto.ContractorDTO;
import pl.lodz.p.it.isrp.wm.dto.StockDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.StockEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "stockIssuePageBean")
@RequestScoped
public class StockIssuePageBean implements Serializable {

    @EJB
    private StockEndpoint stockEndpoint;

    @Inject
    private StockControllerBean stockControllerBean;

    private List<ContractorDTO> contractors;
    private DataModel<ContractorDTO> dataModelContractors;

    private StockDTO stockDTO;

    public StockIssuePageBean() {
    }

    public StockDTO getStockDTO() {
        return stockDTO;
    }

    public void setStockDTO(StockDTO stockDTO) {
        this.stockDTO = stockDTO;
    }

    public List<ContractorDTO> getContractors() {
        return contractors;
    }

    public void setContractors(List<ContractorDTO> contractors) {
        this.contractors = contractors;
    }

    @PostConstruct
    public void init() {
        try {
            stockDTO = stockControllerBean.getSelectedStockDTO();
            contractors = stockEndpoint.listContractors();
            dataModelContractors = new ListDataModel<>(contractors);
        } catch (AppBaseException ex) {
            Logger.getLogger(StockIssuePageBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String saveStockIssueAction() {
        if (stockDTO.getQuantityToIssue() > 0) {
            if (stockDTO.getQuantity() >= stockDTO.getQuantityToIssue()) {
                try {
                    stockControllerBean.stockIssue(stockDTO);
                } catch (AppBaseException ex) {
                    Logger.getLogger(StockIssuePageBean.class.getName()).log(Level.SEVERE, null, ex);
                    ContextUtils.emitI18NMessage(null, ex.getMessage());
                    return null;
                }
            } else {
                ContextUtils.emitI18NMessage("StockIssueForm:availableQuantity", "quantity.to.issue.is.more.than.available.quantity");
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("StockIssueForm:wrongQuantity", "error.stock.no.quantity.problem");
            return null;
        }
        return "listStock";
    }

}
