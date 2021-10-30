package boilerplate.java8;

import boilerplate.java8.dao.AppDao;
import boilerplate.java8.dao.AppDaoImpl;
import boilerplate.java8.entity.Employee;
import boilerplate.java8.repository.EmployeeRepository;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;
import org.seasar.doma.slf4j.Slf4jJdbcLogger;

public class Main {

  public static void main(String[] args) {
    Config config = createConfig();
    TransactionManager tm = config.getTransactionManager();

    // setup database
    AppDao appDao = new AppDaoImpl(config);
    tm.required(appDao::create);

    // read and update
    tm.required(
        () -> {
          EmployeeRepository repository = new EmployeeRepository(config);
          Employee employee = repository.selectById(1);
          employee.age += 1;
          repository.update(employee);
        });
  }

  private static Config createConfig() {
    Dialect dialect = new H2Dialect();
    LocalTransactionDataSource dataSource =
        new LocalTransactionDataSource("jdbc:h2:mem:tutorial;DB_CLOSE_DELAY=-1", "sa", null);
    JdbcLogger jdbcLogger = new Slf4jJdbcLogger();
    TransactionManager transactionManager = new LocalTransactionManager(dataSource, jdbcLogger);
    return new DbConfig(dialect, dataSource, jdbcLogger, transactionManager);
  }
}
