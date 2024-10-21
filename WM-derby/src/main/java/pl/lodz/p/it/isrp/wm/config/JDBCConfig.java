package pl.lodz.p.it.isrp.wm.config;

import java.sql.Connection;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataSourceDefinition( // Pula połączeń z domyślnym poziomem izolacji transakcji ReadCommitted
        name = "java:app/jdbc/WM_DS",
        className = "org.apache.derby.jdbc.ClientDataSource",
        serverName = "10.1.0.2",
        portNumber = 1527,
        databaseName = "/WM",
        user = "WM",
        password = "WMpassword",
        isolationLevel = Connection.TRANSACTION_READ_COMMITTED)

@Stateless
public class JDBCConfig {

//    Uczynienie z tej klasy komponentu Stateless i wstrzykniecie zarzadcy encji korzystajacego z WMPU
//    powoduje aktywowanie tej jednostki skladowania, a w konsekwencji utworzenie (z ew. usunieciem!) struktur w bazie danych
    @PersistenceContext(unitName = "WM_PU")
    private EntityManager em;
}
