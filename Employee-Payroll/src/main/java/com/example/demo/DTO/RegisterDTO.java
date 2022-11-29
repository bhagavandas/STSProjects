package com.example.demo.DTO;

import java.time.LocalDate;

public class RegisterDTO {

	private int employeeId;
	private String employeeName;
	private String profilePic;
	private String gender;
	private String department;
	private Long salary;
	private LocalDate startDate = LocalDate.now();
	private String notes;
	private String email;
	private String password;
	
	
	
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getProfilePic() {
		return profilePic;
	}
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public Long getSalary() {
		return salary;
	}
	public void setSalary(Long salary) {
		this.salary = salary;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	
	

	public RegisterDTO(int employeeId, String employeeName, String profilePic, String gender, String department,
			Long salary, LocalDate startDate, String notes, String email, String password) {
		super();
		this.employeeId = employeeId;
		this.employeeName = employeeName;
		this.profilePic = profilePic;
		this.gender = gender;
		this.department = department;
		this.salary = salary;
		this.startDate = startDate;
		this.notes = notes;
		this.email = email;
		this.password = password;
	}
	public RegisterDTO() {
		
	}
	
	
	
	
	
}