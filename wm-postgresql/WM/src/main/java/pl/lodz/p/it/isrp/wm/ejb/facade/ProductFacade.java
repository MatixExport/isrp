package pl.lodz.p.it.isrp.wm.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.isrp.wm.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.exception.ProductException;
import pl.lodz.p.it.isrp.wm.model.Product;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
@RolesAllowed("Office")
public class ProductFacade extends AbstractFacade<Product> {

    public ProductFacade() {
        super(Product.class);
    }

    @PersistenceContext(unitName = "WM_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void create(Product entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_PRODUCT_SYMBOL)) {
                throw ProductException.createExceptionProductExists(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @RolesAllowed({"Office", "Warehouse"})
    public List<Product> findProducts() throws AppBaseException {
        List<Product> products;
        try {
            TypedQuery<Product> tq = em.createNamedQuery("Product.findAll", Product.class);
            products = tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
        return products;
    }

    @RolesAllowed({"Office", "Warehouse"})
    public Product findProductBySymbol(String productSymbol) throws AppBaseException {
        try {
            TypedQuery<Product> tq = em.createNamedQuery("Product.findByProductSymbol", Product.class);
            tq.setParameter("pid", productSymbol);
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw ProductException.createExceptionProductNotExists(e);
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException && e.getCause().getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @Override
    public void edit(Product entity) throws AppBaseException {
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
            throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
        }
    }

    @Override
    public void remove(Product entity) throws AppBaseException {
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
}
