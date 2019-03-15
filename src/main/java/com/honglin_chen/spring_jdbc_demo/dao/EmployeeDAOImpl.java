package com.honglin_chen.spring_jdbc_demo.dao;

/**
 * 建立和数据库的连接对象(会话). 
 */
import java.sql.Connection; 

/**
 * 一个存储被提前编辑的 SQL 语句的对象, 该对象之后可以多次执行被预编好的 SQL 语句.
 */
import java.sql.PreparedStatement;

/**
 * 数据表格呈现数据库查询结果集合 
 */
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List; 
import java.util.ArrayList; 

/** 
 * DataSource 接口是一个更好的连接数据源的方法. JDBC 1.0 是用 DriverManager 类
 * 来产生一个对数据源的连接; JDBC 2.0 是用 DataSource 来实现同样的功能. 一个 Dat
 * aSource 对象代表了一个真正的数据源. 数据源既可以是关系数据库, 也可以是电子表格. 
 * 
 * 数据源的信息和如何来定位数据源, 例如数据库服务器的名字, 在哪台机器上, 端口号等都包
 * 含在 DataSource 对象的属性里. 
 */
import javax.sql.DataSource; 

import com.honglin_chen.spring_jdbc_demo.model.Employee; 

/* 员工DAO 具体实现 员工接口中的方法 */ 
public class EmployeeDAOImpl implements EmployeeDAO {
	private DataSource dataSource; 
	
	/* 设定数据源 */ 
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource; 
	}
	
	@Override
	public void save(Employee employee) {
		/* 声明一个查询语句将值 (?, ?, ?) 插入员工表格中对应的列 (id, name, role)  */
		String query = "insert into Employee(id, name, role) values (?, ?, ?)";
		Connection conn = null; /* 声明连接变量 */ 
		PreparedStatement ps = null; 
		
		try {
			/* 通过数据源对象建立连接对象 */ 
			conn = dataSource.getConnection();
			/* 将查询语句封装在预编的准备语句对象中 */
			ps = conn.prepareStatement(query); 
			/* 通过准备语句对象设置数据 */ 
			ps.setInt(1, employee.getId()); /* 将当前员工的 Id 设置为数据库当前数据的列下标为 1 的位置 */
			ps.setString(2, employee.getName()); /* 将员工的名字设置为列下标为 2 的数据位置 */ 
			ps.setString(3,  employee.getRole());
			
			/* 执行存储在当前准备语句对象的 SQL 语句, 该语句必须是 DML 类数据即, Data Manipulation Language: INSERT, DELETE, UPDATE */ 
			int out = ps.executeUpdate(); 
			/* 判断查询操作是否抛出异常 */ 
			if(out != 0) {
				System.out.println("数据库ID为: " + employee.getId() + " 的员工存储成功"); 
			} else {
				System.out.println("数据库ID为: " + employee.getId() + " 的员工存储失败"); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			/* 默认操作, 如果没有 SQL 查询语句被成功执行或异常抛出 */ 
			try {
				/* 先关闭预备对象 */ 
				ps.close();
				/* 最后关闭连接对象 */ 
				conn.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Employee getById(int id) {
		String query = "select name, role from Employee where id = ?"; 
		Employee employee = null; /* 建立空员工对象 */ 
		Connection conn = null; 
		PreparedStatement ps = null; 
		ResultSet rs = null; 
		
		try {
			conn = dataSource.getConnection(); 
			ps = conn.prepareStatement(query); 
			/* 将数据库列下标为 1 的数据标记为 id */
			ps.setInt(1, id);
			/* 将寻求结果存储在结果集合中 */ 
			rs = ps.executeQuery();
			
			/* 结果集合默认将指针指向第一排数据之前 */ 
			if(rs.next()) {
				/* 如果集合中有数据 */
				employee = new Employee(); 
				employee.setId(id);
				/* 将数据库中对于员工 id 的数据的名字列部分赋值给当前新建的员工实例 */ 
				employee.setName(rs.getNString("name"));
				employee.setRole(rs.getString("role"));
				System.out.println("通过 id 寻找员工: " + employee + "操作成功"); 
			} else {
				System.out.println("通过 id 寻找员工操作失败");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				/* 先关闭结果集合 */ 
				rs.close(); 
				ps.close(); 
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return employee; 
	}
	
	@Override 
	public void update(Employee employee) {
		String query = "update Employee set name=?, role=? where id=?"; 
		Connection conn = null;
		PreparedStatement ps = null; 
		
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(query); 
			/* 通过参数找到需要修改的对象, 针对查询语句中列出现的位置即问号出现的位置
			 * 将对应的数据参数化到对应的查询列
			 * 查询语句中 name = 1 (相对位置参数)
			 * 			role = 2
			 *          id =   3 
			 */ 
			ps.setString(1, employee.getName()); 
			/* 将员工的角色对应到查询语句中下标为 2 的列位置查询 */ 
			ps.setString(2, employee.getRole());
			/* 将员工的 id 对应到查询语句中下标为 3 的列下标上, 即第 3 个问号出现的位置 */ 
			ps.setInt(3, employee.getId());
			
			int out = ps.executeUpdate(); 
			if(out != 0) {
				System.out.println("ID: " + employee.getId() + " 的员工数据更新操作成功");
			} else {
				System.out.println("ID: " + employee.getId() + " 的员工无法找到"); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close(); 
				conn.close(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override 
	public void deleteById(int id) {
		String query = "delete from Employee where id=?"; 
		Connection conn = null; 
		PreparedStatement ps = null; 
		
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement(query); 
			ps.setInt(1, id);
			int out = ps.executeUpdate();
			if(out != 0) {
				System.out.println("ID: " + id + " 的员工删除操作成功");
			} else {
				System.out.println("ID: " + id + " 的员工无法找到"); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close(); 
				conn.close(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override 
	public List<Employee> getAll() {
		String query = "select id, name, role from Employee"; 
		List<Employee> empList = new ArrayList<Employee>(); 
		Connection conn = null; 
		PreparedStatement ps = null; 
		ResultSet rs = null; 
		
		try {
			conn = dataSource.getConnection(); 
			ps = conn.prepareStatement(query); 
			rs = ps.executeQuery(); 
			
			while(rs.next()) {
				Employee employee = new Employee();
				/* 将返回结果集合中的数据根据其列的属性名赋值给新建的员工实例并通过其
				 * 设置方法构造员工
				 */
				employee.setId(rs.getInt("id"));
				employee.setName(rs.getString("name"));
				employee.setRole(rs.getString("role"));
				/* 将构建好的员工加到返回列表 */ 
				empList.add(employee); 
			}
		} catch (SQLException e) {
			e.printStackTrace(); 
		} finally {
			try {
				rs.close(); 
				ps.close(); 
				conn.close(); 
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return empList; 
	}
}
