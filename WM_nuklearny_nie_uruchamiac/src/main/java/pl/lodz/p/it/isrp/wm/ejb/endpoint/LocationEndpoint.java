package pl.lodz.p.it.isrp.wm.ejb.endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jakarta.annotation.Resource;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.EJB;
import jakarta.ejb.SessionContext;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.isrp.wm.dto.LocationDTO;
import pl.lodz.p.it.isrp.wm.ejb.facade.LocationFacade;
import pl.lodz.p.it.isrp.wm.ejb.facade.StockFacade;
import pl.lodz.p.it.isrp.wm.ejb.facade.WarehouseAccountFacade;
import pl.lodz.p.it.isrp.wm.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.exception.LocationException;
import pl.lodz.p.it.isrp.wm.model.Location;
import pl.lodz.p.it.isrp.wm.model.Stock;
import pl.lodz.p.it.isrp.wm.model.WarehouseAccount;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
@RolesAllowed("Warehouse")
public class LocationEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private StockFacade stockFacade;

    @EJB
    private LocationFacade locationFacade;

    @EJB
    private WarehouseAccountFacade warehouseAccountFacade;

    private Location locationState;

    @Resource
    private SessionContext sessionContext;

    private WarehouseAccount loadCurrentWarehouse() throws AppBaseException {
        //przypisanie konta użytkownika, który tworzył lub edytował lokalizację
        String userLogin = sessionContext.getCallerPrincipal().getName();
        WarehouseAccount account = warehouseAccountFacade.findByLogin(userLogin);
        if (account != null) {
            return account;
        } else {
            throw AppBaseException.createExceptionNotAuthorizedAction();
        }
    }

    public void createNewLocation(LocationDTO locationDTO) throws AppBaseException {
        Location location = new Location();
        location.setLocationSymbol(locationDTO.getLocationSymbol());
        location.setLocationType(locationDTO.getLocationType());
        location.setEmptyLocation(true);
        location.setCreatedBy(loadCurrentWarehouse());
        locationFacade.create(location);
    }

    @RolesAllowed({"Office", "Warehouse"})
    public List<LocationDTO> listLocations() throws AppBaseException {
        List<Location> listAllLocations = locationFacade.findLocations();
        List<LocationDTO> listLocations = new ArrayList<>();
        for (Location location : listAllLocations) {
            LocationDTO locationDTO = new LocationDTO(location.getLocationSymbol(), location.getLocationType(), location.isEmptyLocation(), location.getMaxWeight());

            listLocations.add(locationDTO);
        }
        Collections.sort(listLocations);
        return listLocations;
    }

    public void editLocation(LocationDTO locationDTO) throws AppBaseException {
        if (locationState.getLocationSymbol().equals(locationDTO.getLocationSymbol())) {
            Stock stock;
            stock = stockFacade.findByLocation(locationDTO.getLocationSymbol(), false);

            // obiekt powołany w celu sprawdzenia wagi lokalizacji docelowej
            Location location = locationState;
            location.setLocationType(locationDTO.getLocationType());

            if (stock != null && (stock.getProduct().getWeight() * stock.getQuantity() > location.getMaxWeight())) {
                throw LocationException.createExceptionLocationOverweighted(locationState);
            }
            locationState.setLocationType(locationDTO.getLocationType());
            locationState.setModificatedBy(loadCurrentWarehouse());
            locationFacade.edit(locationState);
        } else {
            throw LocationException.createExceptionWrongState(locationState);
        }
    }

    public LocationDTO rememberSelectedLocationInState(String locationSymbol) throws AppBaseException {
        locationState = locationFacade.findLocationBySymbol(locationSymbol);
        return new LocationDTO(locationState.getLocationSymbol(), locationState.getLocationType(), locationState.isEmptyLocation(), locationState.getMaxWeight());
    }

    public void deleteLocation(LocationDTO locationDTO) throws AppBaseException {
        if (locationState.getLocationSymbol().equals(locationDTO.getLocationSymbol())) {
            Stock stock;
            stock = stockFacade.findByLocation(locationDTO.getLocationSymbol(), false);
            if (locationDTO.isEmptyLocation() && stock == null) {
                Location location = locationFacade.findLocationBySymbol(locationDTO.getLocationSymbol());
                if (location.getLocationSymbol().equals(locationState.getLocationSymbol())) {
                    locationFacade.remove(locationState);
                } else {
                    throw AppBaseException.createExceptionEditedObjectData();
                }
            } else {
                throw LocationException.createExceptionLocationIsNotEmpty(locationState);
            }
        } else {
            throw LocationException.createExceptionWrongState(locationState);
        }
    }
}
