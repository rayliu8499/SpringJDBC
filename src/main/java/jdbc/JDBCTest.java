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

    //��ȡ�����е�ֵ,����ͳ�Ʋ�ѯ
    @Test
    public void testQueryForObject2() {
        String sql = "SELECT count(id) FROM employees";
        long count = jdbcTemplate.queryForObject(sql, Long.class);
        System.out.println(count);
    }


    //��ѯһ��ʵ����ļ���
    @Test
    public void testQueryForList() {
        String sql = "SELECT id, last_name, email FROM employees WHERE id > ? ";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        List<Employee> employees = jdbcTemplate.query(sql, rowMapper, 0);
        System.out.println(employees);
    }


    //�����ݿ��л�ȡһ����¼,ʵ�ʵõ���Ӧ��һ������
    //ע�ⲻ�ǵ���queryForObject(String sql, Class<Employee> requiredType, Object...args)����
    //����Ҫ����queryForObject(String sql, RowMapper<Employee> requiredType, Object...args)����
    //1.���е�RowMapperָ�����ȥӳ����������,���õ�ʵ����ΪBeanPropertyRowMapper 2.ʹ��SQL���еı�����������������������ӳ��.����last_name lastName 3.��֧�ּ�������,jdbcTemplate������һ��JDBC��С���߶�����ORM���
    @Test
    public void testQueryForObject() {
        String sql = "SELECT id, last_name, email FROM employees WHERE id = ? ";
        RowMapper<Employee> rowMapper = new BeanPropertyRowMapper<>(Employee.class);
        Employee employee = jdbcTemplate.queryForObject(sql, rowMapper, 1);
        System.out.println(employee);
    }

    //ִ����������:����insert update delete
    //���һ��������Object[]��List����:��Ϊ�޸�һ����¼��Ҫһ��Object������,��ô����������Ҫ���Object������
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

    //ִ��insert update delete
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
