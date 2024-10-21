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
import pl.lodz.p.it.isrp.wm.dto.ContractorDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.ContractorEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "listContractorsPageBean")
@ViewScoped
public class ListContractorsPageBean implements Serializable {

    @EJB
    private ContractorEndpoint contractorEndpoint;

    @Inject
    private ContractorControllerBean contractorControllerBean;

    private List<ContractorDTO> listContractors;

    private DataModel<ContractorDTO> dataModelContractors;

    public ListContractorsPageBean() {
    }

    public DataModel<ContractorDTO> getDataModelContractors() {
        return dataModelContractors;
    }

    @PostConstruct
    public void initListContractors() {
        try {
            listContractors = contractorEndpoint.listContractors();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListContractorsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelContractors = new ListDataModel<>(listContractors);
    }

    public String editContractorAction(ContractorDTO contractorDTO) {
        try {
            contractorControllerBean.selectContractorForChange(contractorDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListContractorsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListContractors();
        return "editContractor";
    }

    public String deleteSelectedContractorAction(ContractorDTO contractorDTO) {
        try {
            contractorControllerBean.selectContractorForChange(contractorDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListContractorsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListContractors();
        return "deleteContractor";
    }

    public String resetSelectedContractorAction(ContractorDTO contractorDTO) {
        try {
            contractorControllerBean.selectContractorForChange(contractorDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListContractorsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListContractors();
        return "resetContractor";
    }
}
