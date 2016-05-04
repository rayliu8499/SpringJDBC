package jdbc;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ray.liu on 2016/5/3.
 */
public class JDBCTest {

    private ApplicationContext ctx = null;
    private JdbcTemplate jdbcTemplate = null;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate = null;

    {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
        namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) ctx.getBean(NamedParameterJdbcTemplate.class);
    }

    @Test
    public void testNamedParameterJdbcTemplate2(){
        String sql = "INSERT INTO employees (last_name,email,dept_id) values(:lastName,:email,:deptId)";
        Employee employee = new Employee();
        employee.setDeptId(3);
        employee.setEmail("123123");
        employee.setLastName("XYZ");
        SqlParameterSource paramSource = new BeanPropertySqlParameterSource(employee);

        namedParameterJdbcTemplate.update(sql,paramSource);
    }


    @Test
    public void testNamedParameterJdbcTemplate() {
        String sql = "INSERT INTO employees (last_name,email,dept_id) values(:ln,:email,:deptid)";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("ln", "FF");
        paramMap.put("email", "123@123.com");
        paramMap.put("deptid", 2);
        namedParameterJdbcTemplate.update(sql, paramMap);
    }

    //获取单个列的值,或作统计查询
    @Test
    public void testQueryForObject2() {
        String sql = "SELECT count(id) FROM employees";
        long count = jdbcTemplate.queryForObject(sql, Long.class);
        System.out.println(count);
    }


    //查询一组实体类的集合
    @Test
    public void testQueryForList() {
        String sql = "SELECT id, last_name, email FROM employees WHERE id > ? ";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        List<Employee> employees = jdbcTemplate.query(sql, rowMapper, 0);
        System.out.println(employees);
    }


    //从数据库中获取一条记录,实际得到对应的一个对象
    //注意不是调用queryForObject(String sql, Class<Employee> requiredType, Object...args)方法
    //而需要调用queryForObject(String sql, RowMapper<Employee> requiredType, Object...args)方法
    //1.其中的RowMapper指定如何去映射结果集的行,常用的实现类为BeanPropertyRowMapper 2.使用SQL中列的别名完成列名和类的属性名的映射.例如last_name lastName 3.不支持级联属性,jdbcTemplate到底是一个JDBC的小工具而不是ORM框架
    @Test
    public void testQueryForObject() {
        String sql = "SELECT id, last_name, email FROM employees WHERE id = ? ";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        Employee employee = jdbcTemplate.queryForObject(sql, rowMapper, 1);
        System.out.println(employee);
    }

    //执行批量更新:批量insert update delete
    //最后一个参数是Object[]的List类型:因为修改一条记录需要一个Object的数组,那么多条不就需要多个Object数组吗
    @Test
    public void testBatchUpdate() {
        String sql = "INSERT INTO employees (last_name,email,dept_id) values(?,?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        batchArgs.add(new Object[]{"AA", "aa@atguigu.com", 1});
        batchArgs.add(new Object[]{"BB", "bb@atguigu.com", 1});
        batchArgs.add(new Object[]{"CC", "cc@atguigu.com", 1});
        batchArgs.add(new Object[]{"DD", "dd@atguigu.com", 1});
        jdbcTemplate.batchUpdate(sql, batchArgs);
    }

    //执行insert update delete
    @Test
    public void testUpdate() {
        String sql = "UPDATE employees SET last_name=? WHERE id=?";
        jdbcTemplate.update(sql, "Jack", 5);
    }

    @Test
    public void testDataSource() throws SQLException {
        DataSource dataSource = ctx.getBean(DataSource.class);
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void testArrays() {
        List<BigDecimal> bigDecimals = new ArrayList<>();
        bigDecimals.add(BigDecimal.valueOf(1.0));
        bigDecimals.add(BigDecimal.valueOf(2.0));
        bigDecimals.add(BigDecimal.valueOf(3.0));
        bigDecimals.add(BigDecimal.valueOf(4.0));
        bigDecimals.add(BigDecimal.valueOf(5.0));
        bigDecimals.add(BigDecimal.valueOf(6.0));
        bigDecimals.add(BigDecimal.valueOf(7.0));
        int size = bigDecimals.size();
        BigDecimal[] result = bigDecimals.toArray(new BigDecimal[size]);
        for (BigDecimal b : result) {
            System.out.println(b);
        }
    }
}
