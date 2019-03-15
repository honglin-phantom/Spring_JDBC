package com.honglin_chen.spring_jdbc_demo.model;

public class Employee {
	/* 创建实例变量匹配数据库表格的 column */
	private int id; 
	private String name; 
	private String role; 
	
	/* 得到当前员工的 id */
	public int getId() {
		return id;
	}
	
	/* 为当前员工设置 id */
	public void setId(int id) {
		this.id = id;
	}
	
	/* 得到当前员工的名字 */
	public String getName() {
		return name;
	}
	
	/* 设置当前员工的名字 */ 
	public void setName(String name) {
		this.name = name;
	}
	
	/* 得到当前员工的角色 */
	public String getRole() {
		return role;
	}
	
	/* 设置当前员工的角色 */ 
	public void setRole(String role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "ID = " + id + ", 名字 = " + name + ", 角色 = " + role;
	}
}
