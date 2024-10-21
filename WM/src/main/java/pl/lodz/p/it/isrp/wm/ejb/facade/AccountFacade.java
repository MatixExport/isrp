package pl.lodz.p.it.isrp.wm.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import java.util.List;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.ExcludeClassInterceptors;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import java.util.logging.Level;
import jakarta.persistence.NonUniqueResultException;
import java.util.logging.Logger;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.isrp.wm.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.isrp.wm.exception.AccountException;
import pl.lodz.p.it.isrp.wm.exception.AppBaseException;
import pl.lodz.p.it.isrp.wm.model.Account;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
@RolesAllowed("Administration")
public class AccountFacade extends AbstractFacade<Account> {

    private static final Logger LOG = Logger.getLogger(AccountFacade.class.getName());

    public AccountFacade() {
        super(Account.class);
    }

    @PersistenceContext(unitName = "WM_PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @PermitAll
    @Override
    public void create(Account entity) throws AppBaseException {
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
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_ACCOUNT_LOGIN)) {
                throw AccountException.createExceptionLoginExists(e, entity);
            } else if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_ACCOUNT_EMAIL)) {
                throw AccountException.createExceptionEmailExists(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @PermitAll // dostęp wymagany przy resetowaniu hasła
    @Override
    public void edit(Account entity) throws AppBaseException {
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
            if (cause instanceof DatabaseException && cause.getMessage().contains(DB_UNIQUE_CONSTRAINT_FOR_ACCOUNT_EMAIL)) {
                throw AccountException.createExceptionEmailExists(e, entity);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    public List<Account> findAuthorizedAccount() throws AppBaseException {
        List<Account> accounts;
        try {
            TypedQuery<Account> tq = em.createNamedQuery("Account.findAuthorizedAccount", Account.class);
            accounts = tq.getResultList();
        } catch (PersistenceException e) {
            if (e.getCause() instanceof DatabaseException && e.getCause().getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
        return accounts;
    }

    public List<Account> findNewRegisteredAccount() throws AppBaseException {
        try {
            TypedQuery<Account> tq = em.createNamedQuery("Account.findNewRegisteredAccount", Account.class);
            return tq.getResultList();
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @PermitAll // dostęp wymagany przy resetowaniu hasła
    public Account findByLogin(String login) throws AppBaseException {
        try {
            TypedQuery<Account> tq = em.createNamedQuery("Account.findByLogin", Account.class);
            tq.setParameter("lg", login);
            return tq.getSingleResult();
        } catch (NoResultException e) {
            throw AccountException.createExceptionAccountNotExists(e);
        } catch (PersistenceException e) {
            final Throwable cause = e.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(cause);
            }
        }
    }

    @Override
    public void remove(Account entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (DatabaseException e) {
            if (e.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.createExceptionDatabaseConnectionProblem(e);
            } else {
                throw AppBaseException.createExceptionDatabaseQueryProblem(e);
            }
        }
    }

    @ExcludeClassInterceptors //Nie chcemy ujawniać w dziennikach skrótu hasła
    @RolesAllowed("AUTHENTICATOR") //Inne role nie mają tu dostępu. Musi pośredniczyć odpowiedni endpoint opisany jako @RunAs("AUTHENTICATOR").
    public Account findActiveAccountByCredentials(String login, String passwordHash) {
        if (null == login || null == passwordHash || login.isEmpty() || passwordHash.isEmpty()) {
            return null;
        }

        TypedQuery<Account> tq = em.createNamedQuery("Account.findActiveAccountByCredentials", Account.class);
        tq.setParameter("lg", login);
        tq.setParameter("hp", passwordHash);
        try {
            return tq.getSingleResult();
        } catch (jakarta.persistence.NoResultException | NonUniqueResultException ex) {
            LOG.log(Level.SEVERE, "Authentication account for login: {0} failed with: {1}", new Object[]{login, ex});
        }
        return null;
    }
}
