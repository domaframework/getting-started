package boilerplate.java17.repository;

import boilerplate.java17.entity.Employee;
import boilerplate.java17.entity.Employee_;
import java.util.List;
import java.util.Objects;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.criteria.QueryDsl;

public class EmployeeRepository {

  private final QueryDsl queryDsl;

  public EmployeeRepository(Config config) {
    Objects.requireNonNull(config);
    this.queryDsl = new QueryDsl(config);
  }

  public List<Employee> selectAll() {
    var e = new Employee_();
    return queryDsl.from(e).fetch();
  }

  public Employee selectById(Integer id) {
    var e = new Employee_();
    return queryDsl.from(e).where(c -> c.eq(e.id, id)).fetchOne();
  }

  public void insert(Employee employee) {
    var e = new Employee_();
    queryDsl.insert(e).single(employee).execute();
  }

  public void update(Employee employee) {
    var e = new Employee_();
    queryDsl.update(e).single(employee).execute();
  }

  public void delete(Employee employee) {
    var e = new Employee_();
    queryDsl.delete(e).single(employee).execute();
  }
}
