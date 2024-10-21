package pl.lodz.p.it.isrp.wm.web;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.dto.ProductDTO;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "createNewProductPageBean")
@RequestScoped
public class CreateNewProductPageBean {

    private ProductDTO productDTO;

    @Inject
    private ProductControllerBean productControllerBean;

    public CreateNewProductPageBean() {
    }

    public ProductDTO getProductDTO() {
        return productDTO;
    }

    public void setProductDTO(ProductDTO productDTO) {
        this.productDTO = productDTO;
    }

    @PostConstruct
    public void init() {
        productDTO = new ProductDTO();
    }

    public String createNewProductAction() {
        if (productDTO.getPrice(productDTO.getPriceFromForm()).compareTo(BigDecimal.ZERO) > 0) {
            if (productDTO.getWeight() > 0) {
                try {
                    productControllerBean.createNewProduct(productDTO);
                } catch (AppBaseException ex) {
                    Logger.getLogger(CreateNewProductPageBean.class.getName()).log(Level.SEVERE, null, ex);
                    ContextUtils.emitI18NMessage(null, ex.getMessage());
                    return null;
                }
            } else {
                ContextUtils.emitI18NMessage("CreateProductForm:wrongWeight", "error.product.no.weight.problem");
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("CreateProductForm:wrongPrice", "error.product.no.price.problem");
            return null;
        }
        return "main";
    }

}
