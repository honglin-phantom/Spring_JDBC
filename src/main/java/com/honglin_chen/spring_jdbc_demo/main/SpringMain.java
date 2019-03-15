package com.honglin_chen.spring_jdbc_demo.main;

import java.util.List; 
import java.util.Random; 

import org.springframework.context.support.ClassPathXmlApplicationContext; 

import com.honglin_chen.spring_jdbc_demo.dao.EmployeeDAO;
import com.honglin_chen.spring_jdbc_demo.model.Employee;

/* Spring JDBC Test Class */ 
public class SpringMain {
	public static void main(String[] args) {
		/* 得到 Spring 文本 */ 
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml"); 
	
		/* 得到 EmployeeDAO bean */ 
		EmployeeDAO employeeDAO = context.getBean("employeeDAO", EmployeeDAO.class);
	
		/* 创建测试通过 CRUD 操作 */ 
		Employee employee = new Employee(); 
		
		/* 从 0 - 1000 创建随机数 */ 
		int rand = new Random().nextInt(1000);
		employee.setId(rand); 
		employee.setName("泓霖"); 
		employee.setRole("C++/Java 开发"); 
	
		/* 数据库存储操作 */ 
		employeeDAO.save(employee); 
	
		/* 数据库读取操作 */ 
		Employee curEmp = employeeDAO.getById(rand); 
		System.out.println("读取的员工是: " + curEmp); 
	
		/* 更新员工数据 */ 
		employee.setRole("CEO"); 
		employeeDAO.update(employee); 
	
		/* 得到数据库中所有员工 */ 
		List<Employee> list = employeeDAO.getAll(); 
		System.out.println("数据库中的所有员工是: " + list); 
	
		/* 随机删除一个员工 */ 
		employeeDAO.deleteById(rand); 
	
		/* 关闭 Spring 文本 */ 
		context.close(); 
	
		System.out.println("任务完成"); 
	}
}
