package com.honglin_chen.spring_jdbc_demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Spring JdbcTemplate 是 Spring JDBC 核心包的中心类
 * 并提供了方法执行查询语句, 自动解析返回结果的集合以便得到
 * Objects 或 Objects 的列表
 */
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * JdbcTemplate 使用的一个接口用于将结果结合中的每一排数据
 * 映射到结果 object
 */
import org.springframework.jdbc.core.RowMapper;

import com.honglin_chen.spring_jdbc_demo.model.Employee; 

/**
 * Key Points: 
 * 
 * 1. Use of Object array to pass PreparedStatement arguments
 *    we could also use PreparedStatementSetter implementatio
 *    n but passing Object array seems easy to use.
 * 
 * 2. No code related to opening and closing connections, sta
 * 	  tements or result set. All that is handled internally b
 *    y Spring JdbcTemplate class.
 *    
 * 3. RowMapper anonymous class implementation to map the Res
 * 	  ultSet data to Employee bean object in queryForObject() 
 *    method.
 *    
 * 4. queryForList() method returns list of Map whereas Map c
 * 	  ontains the row data mapped with key as the column name 
 *    and value from the database row matching the criteria.
 */

public class EmployeeDAOJDBCTemplateImpl implements EmployeeDAO {
	private DataSource dataSource; 
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource; 
	}
	
	@Override 
	public void save(Employee employee) {
		String query = "insert into Employee (id, name, role) values (?, ?, ?)"; 
		/* 通过数据源创建 JdbcTemplate 实例 */ 
		JdbcTemplate jt = new JdbcTemplate(dataSource); 
		/* 定义 Object 类的数组并初始化其为员工的 ID, name, role */
		Object[] args = new Object[] {employee.getId(), employee.getName(), employee.getRole()}; 
		/* 向查询语句中插入数据时通过 Object 类型的数组将值传递进查询语句中 */ 
		int out = jt.update(query, args); 
		
		if(out != 0) {
			System.out.println("向数据库保存 ID 为: " + employee.getId() + " 的员工数据操作成功");
		} else {
			System.out.println("向数据库保存 ID 为: " + employee.getId() + " 的员工数据操作失败"); 
		}
	}
	
	@Override
	public Employee getById(int id) {
		String query = "select id, name, role from Employee where id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		/* 使用 RowMapper 的匿名类创建一个可以重复使用的 RowMapper */ 
		/**
		 * queryForObject:
		 * 从 SQL 中创建一个 prepared statement 和一个绑定到查询语句的参数列表, 并通过 RowMapper
		 * 将一个单一的结果映射到 Java Object 上
		 */
		Employee emp = jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Employee>() {

			@Override
			/* 将结果集合中的每一排数据映射到 Java object */ 
			public Employee mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Employee emp = new Employee();
				emp.setId(rs.getInt("id"));
				emp.setName(rs.getString("name"));
				emp.setRole(rs.getString("role"));
				return emp;
			}});
		
		return emp;
	}
	
	@Override 
	public void update (Employee employee) {
		String query = "update Employee set name=?, role=? where id=?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		Object[] args = new Object[] {employee.getName(), employee.getRole(), employee.getId()};
		
		int out = jdbcTemplate.update(query, args);
		
		if(out != 0){
			System.out.println("ID 为: " + employee.getId() + " 的员工更新操作成功");
		}else {
			System.out.println("ID 为: " + employee.getId() + " 的员工更新操作失败");
		}
	}
	
	@Override
	public void deleteById(int id) {
		String query = "delete from Employee where id = ?";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		
		/* id 被解析成一个 Object 类数组的成员 */ 
		int out = jdbcTemplate.update(query, id);
		if(out != 0){
			System.out.println("删除 ID 为: " + id + " 的员工操作成功");
		} else {
			System.out.println("删除 ID 为: " + id + " 的员工操作失败");
		}
	}
	
	@Override 
	public List<Employee> getAll() {
		String query = "select id, name, role from Employee"; 
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); 
		List<Employee> list = new ArrayList<Employee>(); 
		/* 为了得到结果列表执行一次查询 */
		List<Map<String, Object>> employeeRows = jdbcTemplate.queryForList(query);
		
		for(Map<String, Object> employeeRow : employeeRows) {
			/* 通过每一排的数据构造一个员工并存储在返回列表中 */
			Employee employee = new Employee(); 
			/**
			 * Map<String, Object> 存储着列(键)对应的数据(值), 通过 String.valueOf(Object) 
			 * 方法将其转化为字符串值最后解析成整型数值
			 */
			employee.setId(Integer.parseInt(String.valueOf(employeeRow.get("id"))));
			employee.setName(String.valueOf(employeeRow.get("name")));
			employee.setRole(String.valueOf(employeeRow.get("role")));
			list.add(employee); 
		}
		
		return list; 
	}
}
