package pl.lodz.p.it.isrp.wm.web;

import java.io.Serializable;
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
import pl.lodz.p.it.isrp.wm.dto.ProductDTO;
import pl.lodz.p.it.isrp.wm.dto.StockDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.StockEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "listStockPageBean")
@ViewScoped
public class ListStockPageBean implements Serializable {

    @EJB
    private StockEndpoint stockEndpoint;

    @Inject
    private StockControllerBean stockControllerBean;

    private List<StockDTO> listStock;

    private DataModel<StockDTO> dataModelStock;

    private List<ProductDTO> products;
    private DataModel<ProductDTO> dataModelProducts;

    private StockDTO stockDTO;

    public ListStockPageBean() {
    }

    public DataModel<StockDTO> getDataModelStock() {
        return dataModelStock;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public StockDTO getStockDTO() {
        return stockDTO;
    }

    public void setStockDTO(StockDTO stockDTO) {
        this.stockDTO = stockDTO;
    }

    @PostConstruct
    public void initListStock() {
        try {
            products = stockEndpoint.listProducts();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListStockPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelProducts = new ListDataModel<>(products);
        stockDTO = new StockDTO();
    }

    public String showListAction() {
        try {
            listStock = stockEndpoint.listStockForProduct(stockDTO.getProductSymbol());
        } catch (AppBaseException ex) {
            Logger.getLogger(ListStockPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            dataModelStock = null; // Usunięcie danych z tabeli w przypadku braku stanu
        }
        dataModelStock = new ListDataModel<>(listStock);
        return null;
    }

    public String stockIssueAction(StockDTO stockDTO) {
        try {
            stockControllerBean.selectDataToStockChange(stockDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListStockPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "stockIssue";
    }

    public String moveStockAction(StockDTO stockDTO) {
        try {
            stockControllerBean.selectDataToStockChange(stockDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListStockPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "moveStock";
    }

}
