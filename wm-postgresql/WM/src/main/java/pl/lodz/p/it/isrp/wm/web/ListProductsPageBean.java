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
import pl.lodz.p.it.isrp.wm.ejb.endpoint.ProductEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "listProductsPageBean")
@ViewScoped
public class ListProductsPageBean implements Serializable {

    @EJB
    private ProductEndpoint productEndpoint;

    @Inject
    private ProductControllerBean productControllerBean;

    private List<ProductDTO> listProducts;

    private DataModel<ProductDTO> dataModelProducts;

    public ListProductsPageBean() {
    }

    public DataModel<ProductDTO> getDataModelProducts() {
        return dataModelProducts;
    }

    @PostConstruct
    public void initListProducts() {
        try {
            listProducts = productEndpoint.listProducts();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListProductsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelProducts = new ListDataModel<>(listProducts);
    }

    public String editProductAction(ProductDTO productDTO) {
        try {
            productControllerBean.selectProductForChange(productDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListProductsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListProducts();
        return "editProduct";
    }

    public String deleteSelectedProductAction(ProductDTO productDTO) {
        try {
            productControllerBean.selectProductForChange(productDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListProductsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListProducts();
        return "deleteProduct";
    }

}
