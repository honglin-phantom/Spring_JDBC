package com.honglin_chen.spring_jdbc_demo.dao;

import java.util.List; 

import com.honglin_chen.spring_jdbc_demo.model.Employee; 

/* DAO 接口定义对数据库数据操作的方法 */
public interface EmployeeDAO {
	/* 向数据库中保存一个员工 */
	public void save(Employee employee); 
	
	/* 通过id读取数据库中的一个员工 */
	public Employee getById(int id); 
	
	/* 更新员工信息 */ 
	public void update(Employee employee); 
	
	/* 通过员工id删除一个员工 */ 
	public void deleteById(int id); 
	
	/* 得到数据库中的所有员工 */ 
	public List<Employee> getAll(); 
}
