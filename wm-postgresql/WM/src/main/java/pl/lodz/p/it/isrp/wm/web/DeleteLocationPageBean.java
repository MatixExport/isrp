package pl.lodz.p.it.isrp.wm.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import pl.lodz.p.it.isrp.wm.dto.LocationDTO;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.web.utils.ContextUtils;

@Named(value = "deleteLocationPageBean")
@RequestScoped
public class DeleteLocationPageBean {

    @Inject
    private LocationControllerBean locationControllerBean;

    private LocationDTO locationDTO;

    public DeleteLocationPageBean() {
    }

    public LocationDTO getLocationDTO() {
        return locationDTO;
    }

    public void setLocationDTO(LocationDTO locationDTO) {
        this.locationDTO = locationDTO;
    }

    @PostConstruct
    public void init() {
        locationDTO = locationControllerBean.getSelectedLocationDTO();
    }

    public String deleteLocationAction() {
        try {
            locationControllerBean.deleteLocation(locationDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(DeleteLocationPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "listLocations";
    }

}
