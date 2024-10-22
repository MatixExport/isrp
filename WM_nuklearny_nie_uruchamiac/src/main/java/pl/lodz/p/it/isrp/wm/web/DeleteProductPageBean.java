package pl.lodz.p.it.isrp.wm.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.dto.ProductDTO;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "deleteProductPageBean")
@RequestScoped
public class DeleteProductPageBean {

    @Inject
    private ProductControllerBean productControllerBean;

    private ProductDTO productDTO;

    public DeleteProductPageBean() {
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    @PostConstruct
    public void init() {
        productDTO = productControllerBean.getSelectedProductDTO();
    }

    public String deleteProductAction() {
        try {
            productControllerBean.deleteProduct(productDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(DeleteProductPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "listProducts";
    }

}
