package pl.lodz.p.it.isrp.wm.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import pl.lodz.p.it.isrp.wm.dto.LocationDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.LocationEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.model.LocationType;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "listLocationsPageBean")
@ViewScoped
public class ListLocationsPageBean implements Serializable {

    @EJB
    private LocationEndpoint locationEndpoint;

    @Inject
    private LocationControllerBean locationControllerBean;

    private List<LocationDTO> listLocations;

    private List<LocationType> listLocationTypes;

    private DataModel<LocationDTO> dataModelLocations;

    public ListLocationsPageBean() {
    }

    public DataModel<LocationDTO> getDataModelLocations() {
        return dataModelLocations;
    }

    public List<LocationType> getListLocationTypes() {
        return listLocationTypes;
    }

    @PostConstruct
    public void initListLocations() {
        try {
            listLocations = locationEndpoint.listLocations();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListLocationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelLocations = new ListDataModel<>(listLocations);
        LocationType[] listAllLocationTypes = LocationType.values();

        for (LocationType locationType : listAllLocationTypes) {
            locationType.setLoactionTypeI18NValue(ContextUtils.getI18NMessage(locationType.getLocationTypeKey()));
        }
        listLocationTypes = new ArrayList<>(Arrays.asList(listAllLocationTypes));

    }

    public String editLocationAction(LocationDTO locationDTO) {
        try {
            locationControllerBean.selectLocationForChange(locationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListLocationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "editLocation";
    }

    public String deleteSelectedLocationAction(LocationDTO locationDTO) {
        if (locationDTO.isEmptyLocation() == true) {
            try {
                locationControllerBean.selectLocationForChange(locationDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(ListLocationsPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
                return null;
            }
        }
        initListLocations();
        return "deleteLocation";
    }
}
