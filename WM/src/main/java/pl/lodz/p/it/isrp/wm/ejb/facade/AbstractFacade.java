
package pl.lodz.p.it.isrp.wm.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;

public abstract class AbstractFacade<T> {

    static final public String DB_UNIQUE_CONSTRAINT_FOR_ACCOUNT_LOGIN = "LOGIN_UNIQUE";
    static final public String DB_UNIQUE_CONSTRAINT_FOR_ACCOUNT_EMAIL = "EMAIL_UNIQUE";
    static final public String DB_UNIQUE_CONSTRAINT_FOR_PRODUCT_SYMBOL = "PRODUCT_UNIQUE";
    static final public String DB_UNIQUE_CONSTRAINT_FOR_LOCATION_SYMBOL = "LOCATION_UNIQUE";
    static final public String DB_UNIQUE_CONSTRAINT_FOR_CONTRACTOR_NUMBER = "CONTRACTOR_UNIQUE";
    static final public String DB_UNIQUE_CONSTRAINT_FOR_LOCATION_SYMBOL_IN_STOCK = "LOCATION_IN_STOCK_UNIQUE";

    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) throws AppBaseException {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    public void edit(T entity) throws AppBaseException {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    public void remove(T entity) throws AppBaseException {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    public List<T> findAll() throws AppBaseException {
        try {
            jakarta.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
            cq.select(cq.from(entityClass));
            return getEntityManager().createQuery(cq).getResultList();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException && e.getCause().getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findRange(int[] range) {
        jakarta.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        jakarta.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        jakarta.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        jakarta.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        jakarta.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
