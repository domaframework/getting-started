package boilerplate.java17;

import boilerplate.java17.dao.AppDaoImpl;
import boilerplate.java17.repository.EmployeeRepository;
import org.seasar.doma.jdbc.SimpleConfig;
import org.seasar.doma.slf4j.Slf4jJdbcLogger;

public class Main {

  public static void main(String[] args) {
    var config =
        SimpleConfig.builder("jdbc:h2:mem:tutorial;DB_CLOSE_DELAY=-1", "sa", null)
            .jdbcLogger(new Slf4jJdbcLogger())
            .build();
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
}
