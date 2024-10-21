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
import pl.lodz.p.it.isrp.wm.dto.LocationDTO;
import pl.lodz.p.it.isrp.wm.dto.StockDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.StockEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "moveStockPageBean")
@RequestScoped
public class MoveStockPageBean implements Serializable {

    @EJB
    private StockEndpoint stockEndpoint;

    @Inject
    private StockControllerBean stockControllerBean;

    private List<LocationDTO> locations;
    private DataModel<LocationDTO> dataModelLocations;

    private StockDTO stockDTO;

    public MoveStockPageBean() {
    }

    public StockDTO getStockDTO() {
        return stockDTO;
    }

    public void setStockDTO(StockDTO stockDTO) {
        this.stockDTO = stockDTO;
    }

    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> locations) {
        this.locations = locations;
    }

    @PostConstruct
    public void init() {

        try {
            stockDTO = stockControllerBean.getSelectedStockDTO();

            locations = stockEndpoint.listEmptyLocations();
            dataModelLocations = new ListDataModel<>(locations);

        } catch (AppBaseException ex) {
            Logger.getLogger(MoveStockPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
    }

    public String saveMoveStockAction() {
        if (stockDTO.getQuantityToMove() > 0) {
            if (stockDTO.getQuantity() >= stockDTO.getQuantityToMove()) {
                try {
                    stockControllerBean.moveStock(stockDTO);
                } catch (AppBaseException ex) {
                    Logger.getLogger(MoveStockPageBean.class.getName()).log(Level.SEVERE, null, ex);
                    ContextUtils.emitI18NMessage(null, ex.getMessage());
                    return null;
                }
            } else {
                ContextUtils.emitI18NMessage("MoveStockForm:availableQuantity", "quantity.to.move.is.more.than.available.quantity");
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("MoveStockForm:wrongQuantity", "error.stock.no.quantity.problem");
            return null;
        }
        return "listStock";
    }
}
