package pl.lodz.p.it.isrp.wm.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.isrp.wm.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.model.OfficeAccount;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
@RolesAllowed("Administration")
public class OfficeAccountFacade extends AbstractFacade<OfficeAccount> {

    @PersistenceContext(unitName = "WM_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OfficeAccountFacade() {
        super(OfficeAccount.class);
    }

    @RolesAllowed({"Administration", "Office"})
    public OfficeAccount findByLogin(String login) throws AppBaseException {
        try {
            TypedQuery<OfficeAccount> tq = em.createNamedQuery("OfficeAccount.findByLogin", OfficeAccount.class);
            tq.setParameter("lg", login);
            return tq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }
}