package pl.lodz.p.it.isrp.wm.web;

import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;
import jakarta.ejb.EJB;
import pl.lodz.p.it.isrp.wm.dto.StockDTO;
import pl.lodz.p.it.isrp.wm.ejb.endpoint.StockEndpoint;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;

@Named(value = "stockControllerBean")
@SessionScoped
public class StockControllerBean implements Serializable {

    @EJB
    private StockEndpoint stockEndpoint;

    private int lastActionMethod = 0;

    private StockDTO selectedStockDTO;

    public StockControllerBean() {
    }

    public StockDTO getSelectedStockDTO() {
        return selectedStockDTO;
    }

    public void setSelectedStockDTO(StockDTO selectedStockDTO) {
        this.selectedStockDTO = selectedStockDTO;
    }

    void toStockUp(StockDTO stockDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = stockDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = stockEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                stockEndpoint.toStockUp(stockDTO); // wywołanie metody biznesowej punktu dostępowego EJB
                endpointCallCounter--;
            } while (stockEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.createExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;

    }

    void selectDataToStockUp(StockDTO stockDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = stockDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = stockEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedStockDTO = stockEndpoint.rememberSelectedStock(stockDTO.getQuantity(), stockDTO.getContractorNumber(), stockDTO.getProductSymbol(), stockDTO.getLocationSymbol()); // wywołanie metody biznesowej punktu dostępowego EJB
                endpointCallCounter--;
            } while (stockEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.createExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void selectDataToStockChange(StockDTO stockDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = stockDTO.hashCode() + 3;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = stockEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedStockDTO = stockEndpoint.rememberSelectedStockInState(stockDTO); // wywołanie metody biznesowej punktu dostępowego EJB
                endpointCallCounter--;
            } while (stockEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.createExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void stockIssue(StockDTO stockDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = stockDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = stockEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                stockEndpoint.stockIssue(stockDTO); // wywołanie metody biznesowej punktu dostępowego EJB
                endpointCallCounter--;
            } while (stockEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.createExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    void moveStock(StockDTO stockDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = stockDTO.hashCode() + 5;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = stockEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                stockEndpoint.moveStock(stockDTO); // wywołanie metody biznesowej punktu dostępowego EJB
                endpointCallCounter--;
            } while (stockEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.createExceptionForRepeatedTransactionRollback();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

}
