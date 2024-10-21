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
import pl.lodz.p.it.isrp.wm.dto.LocationDTO;
import pl.lodz.p.it.isrp.wm.dto.ProductDTO;
import pl.lodz.p.it.isrp.wm.dto.StockDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.StockEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "stockUpPageBean")
@RequestScoped
public class StockUpPageBean implements Serializable {

    @EJB
    private StockEndpoint stockEndpoint;

    @Inject
    private StockControllerBean stockControllerBean;

    private List<ProductDTO> products;
    private DataModel<ProductDTO> dataModelProducts;

    private List<LocationDTO> locations;
    private DataModel<LocationDTO> dataModelLocations;

    private List<ContractorDTO> contractors;
    private DataModel<ContractorDTO> dataModelContractors;

    private StockDTO stockDTO;

    public StockUpPageBean() {
    }

    public StockDTO getStockDTO() {
        return stockDTO;
    }

    public void setStockDTO(StockDTO stockDTO) {
        this.stockDTO = stockDTO;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public List<LocationDTO> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDTO> locations) {
        this.locations = locations;
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
            stockControllerBean.setSelectedStockDTO(null);

            products = stockEndpoint.listProducts();
            dataModelProducts = new ListDataModel<>(products);

            locations = stockEndpoint.listEmptyLocations();
            dataModelLocations = new ListDataModel<>(locations);

            contractors = stockEndpoint.listContractors();
            dataModelContractors = new ListDataModel<>(contractors);
        } catch (AppBaseException ex) {
            Logger.getLogger(StockUpPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        stockDTO = new StockDTO();
    }

    public String toStockUpAction() {
        if (stockDTO.getQuantity() > 0) {
            try {
                stockControllerBean.selectDataToStockUp(stockDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(StockUpPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("StockUpForm:wrongQuantity", "error.stock.no.quantity.problem");
            return null;
        }
        return "stockUp2";
    }
}
