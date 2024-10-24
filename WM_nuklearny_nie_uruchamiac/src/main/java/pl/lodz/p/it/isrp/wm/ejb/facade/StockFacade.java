package pl.lodz.p.it.isrp.wm.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.isrp.wm.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.exception.StockException;
import pl.lodz.p.it.isrp.wm.model.Stock;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
@RolesAllowed("Warehouse")
public class StockFacade extends AbstractFacade<Stock> {

    @PersistenceContext(unitName = "WM_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public StockFacade() {
        super(Stock.class);
    }

    @Override
    public void create(Stock entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (IllegalStateException e) {
            throw StockException.createExceptionStockUpFail(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_LOCATION_SYMBOL_IN_STOCK)) {
                throw StockException.createExceptionLocationInStockExists(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Office", "Warehouse"})
    public List<Stock> findStockByProduct(String productSymbol) throws AppBaseException {
        List<Stock> stocks;
        try {
            TypedQuery<Stock> tq = em.createNamedQuery("Stock.findByProduct", Stock.class);
            tq.setParameter("p", productSymbol);
            stocks = tq.getResultList();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException && e.getCause().getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
        return stocks;
    }

    public Stock findByLocation(String locationSymbol, boolean isStock) throws AppBaseException {
        Stock stock;
        try {
            TypedQuery<Stock> tq = em.createNamedQuery("Stock.findByLocation", Stock.class);
            tq.setParameter("loc", locationSymbol);
            stock = tq.getSingleResult();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException && e.getCause().getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else if (isStock) {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            } else {
                // wartość zwracana potrzebna do sprawdzenia czy do lokalizacji jest przypisany stan w metodach LocationEndpoint
                return null;
            }
        }
        return stock;
    }

    @Override
    public void edit(Stock entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.createExceptionOptimisticLock(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_LOCATION_SYMBOL_IN_STOCK)) {
                throw StockException.createExceptionLocationInStockExists(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @Override
    public void remove(Stock entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (OptimisticLockException e) {
            throw AppBaseException.createExceptionOptimisticLock(e);
        }
    }

    @RolesAllowed({"Office", "Warehouse"})
    public List<Stock> findStockByContractorNumber(String contractorNumber) throws AppBaseException {
        List<Stock> stocks;
        try {
            TypedQuery<Stock> tq = em.createNamedQuery("Stock.findByContractor", Stock.class);
            tq.setParameter("cn", contractorNumber);
            stocks = tq.getResultList();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException && e.getCause().getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
        return stocks;
    }
}
