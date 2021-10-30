package boilerplate.java15;

import boilerplate.java15.dao.AppDaoImpl;
import boilerplate.java15.repository.EmployeeRepository;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.dialect.H2Dialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.slf4j.Slf4jJdbcLogger;

public class Main {

  public static void main(String[] args) {
    var config = createConfig();
    var tm = config.getTransactionManager();

    // setup database
    var appDao = new AppDaoImpl(config);
    tm.required(appDao::create);

    // read and update
    tm.required(
        () -> {
          var repository = new EmployeeRepository(config);
          var employee = repository.selectById(1);
          employee.age += 1;
          repository.update(employee);
        });
  }

  private static Config createConfig() {
    var dialect = new H2Dialect();
    var dataSource =
        new LocalTransactionDataSource("jdbc:h2:mem:tutorial;DB_CLOSE_DELAY=-1", "sa", null);
    var jdbcLogger = new Slf4jJdbcLogger();
    var transactionManager = new LocalTransactionManager(dataSource, jdbcLogger);
    return new DbConfig(dialect, dataSource, jdbcLogger, transactionManager);
  }
}
