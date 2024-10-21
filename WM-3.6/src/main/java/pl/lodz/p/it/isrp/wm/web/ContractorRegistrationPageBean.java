package pl.lodz.p.it.isrp.wm.web;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.dto.ContractorDTO;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "contractorRegistrationPageBean")
@RequestScoped
public class ContractorRegistrationPageBean implements Serializable {

    @Inject
    private ContractorControllerBean contractorControllerBean;

    private ContractorDTO contractorDTO;

    public ContractorRegistrationPageBean() {
    }

    public ContractorDTO getContractorDTO() {
        return contractorDTO;
    }

    public void setContractorDTO(ContractorDTO contractorDTO) {
        this.contractorDTO = contractorDTO;
    }

    @PostConstruct
    public void init() {
        contractorDTO = new ContractorDTO();
    }

    public String registerContractorAction() {
        try {
            contractorControllerBean.registerContractor(contractorDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ContractorRegistrationPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "main";
    }

}
