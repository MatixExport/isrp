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
import pl.lodz.p.it.isrp.wm.dto.IssueDTO;
import pl.lodz.p.it.isrp.wm.ejb.facade.ContractorFacade;
import pl.lodz.p.it.isrp.wm.ejb.facade.IssueFacade;
import pl.lodz.p.it.isrp.wm.ejb.facade.LocationFacade;
import pl.lodz.p.it.isrp.wm.ejb.facade.ProductFacade;
import pl.lodz.p.it.isrp.wm.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.model.Issue;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
@RolesAllowed({"Office", "Warehouse"})
public class IssueEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private ProductFacade productFacade;

    @EJB
    private ContractorFacade contractorFacade;

    @EJB
    private LocationFacade locationFacade;

    @EJB
    private IssueFacade issueFacade;

    @Resource
    private SessionContext sessionContext;

    private Issue issue;

    public List<String> listProducts() throws AppBaseException {
        List<Issue> listIssuesForProduct = issueFacade.findAll();
        List<String> listProductsSymbols = new ArrayList<>();
        for (Issue issue : listIssuesForProduct) {
            String productSymbol = issue.getProductSymbol();
            if (!listProductsSymbols.contains(productSymbol)) {
                listProductsSymbols.add(productSymbol);
            }
        }
        Collections.sort(listProductsSymbols);
        return listProductsSymbols;
    }

    public List<String> listContractors() throws AppBaseException {
        List<Issue> listIssuesForContractor = issueFacade.findAll();
        List<String> listContractorsNumbers = new ArrayList<>();
        for (Issue issue : listIssuesForContractor) {
            String contractorNumber = issue.getContractorNumber();
            if (!listContractorsNumbers.contains(contractorNumber)) {
                listContractorsNumbers.add(contractorNumber);
            }
        }
        Collections.sort(listContractorsNumbers);
        return listContractorsNumbers;
    }

    public List<String> listLocations() throws AppBaseException {
        List<Issue> listIssuesForLocation = issueFacade.findAll();
        List<String> listLocationsSymbols = new ArrayList<>();
        for (Issue issue : listIssuesForLocation) {
            String locationSymbol = issue.getLocationSymbol();
            if (!listLocationsSymbols.contains(locationSymbol)) {
                listLocationsSymbols.add(locationSymbol);
            }
        }
        Collections.sort(listLocationsSymbols);
        return listLocationsSymbols;
    }

    public List<IssueDTO> listIssuesForProduct(String productSymbol) throws AppBaseException {
        List<Issue> listIssuesForProduct = issueFacade.findIssuesForProduct(productSymbol);
        List<IssueDTO> listIssues = new ArrayList<>();
        for (Issue issue : listIssuesForProduct) {
            IssueDTO issueDTO = new IssueDTO(issue.getQuantity(), issue.getContractorNumber(), issue.getProductSymbol(), issue.getDescription(), issue.getPrice(), issue.getLocationSymbol(), issue.getLogin(), issue.getCreationDate());
            listIssues.add(issueDTO);
        }
        Collections.reverse(listIssues);
        return listIssues;
    }

    public List<IssueDTO> listIssuesForLocation(String locationSymbol) throws AppBaseException {
        List<Issue> listIssuesForLocation = issueFacade.findIssuesForLocation(locationSymbol);
        List<IssueDTO> listIssues = new ArrayList<>();
        for (Issue issue : listIssuesForLocation) {
            IssueDTO issueDTO = new IssueDTO(issue.getQuantity(), issue.getContractorNumber(), issue.getProductSymbol(), issue.getDescription(), issue.getPrice(), issue.getLocationSymbol(), issue.getLogin(), issue.getCreationDate());
            listIssues.add(issueDTO);
        }
        Collections.reverse(listIssues);
        return listIssues;
    }

    public List<IssueDTO> listIssuesForContractor(String contractorNumber) throws AppBaseException {
        List<Issue> listIssuesForContractor = issueFacade.findIssuesForContractorNumber(contractorNumber);
        List<IssueDTO> listIssues = new ArrayList<>();
        for (Issue issue : listIssuesForContractor) {
            IssueDTO issueDTO = new IssueDTO(issue.getQuantity(), issue.getContractorNumber(), issue.getProductSymbol(), issue.getDescription(), issue.getPrice(), issue.getLocationSymbol(), issue.getLogin(), issue.getCreationDate());
            listIssues.add(issueDTO);
        }
        Collections.reverse(listIssues);
        return listIssues;
    }

}
