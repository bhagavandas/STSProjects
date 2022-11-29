package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.EmailDTO;
import com.example.demo.DTO.LoginDTO;
import com.example.demo.DTO.RegisterDTO;
import com.example.demo.exceptions.UserException;
import com.example.demo.model.EmployeeModel;
import com.example.demo.repository.IEmpRepository;
import com.example.demo.utilities.JavaMailService;
import com.example.demo.utilities.JwtTokenUtil;

@Service
public class EmployeeService implements IEmployeeService {

	@Autowired
	IEmpRepository repo;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	JwtTokenUtil tokenUtil;
	@Autowired
	JavaMailService javaMailService;

	@Override
	public RegisterDTO register(RegisterDTO registerDTO) {
		Optional<EmployeeModel> empModel = repo.findByEmployeeName(registerDTO.getEmployeeName());
		if (empModel.isPresent()) {
			throw new UserException("Username already exists!!");
		}
		//String token = tokenUtil.generateToken(registerDTO.getEmail(), registerDTO.getPassword());

		//javaMailService.sendSimpleMail(registerDTO.getEmail(), token, "verification");
		EmployeeModel registeredemployee = modelMapper.map(registerDTO, EmployeeModel.class);
		registeredemployee.setVerified(true);
		repo.save(registeredemployee);

		System.out.println("Successfully registered");
		return registerDTO;
	}

	@Override
	public RegisterDTO searchByEmployeeName(String employeeName) {
		Optional<EmployeeModel> empModel = repo.findByEmail(employeeName);
		if (empModel.isEmpty()) {
			throw new UserException("Employee doesn't exist!!!");
		}
		RegisterDTO registerDTO = modelMapper.map(empModel.get(), RegisterDTO.class);

		return registerDTO;
	}

	
	@Override
	public RegisterDTO getEmployee(int employeeId) {
		Optional<EmployeeModel> emp = repo.findById(employeeId);
		RegisterDTO empDto = modelMapper.map(emp.get(),RegisterDTO.class);
		System.out.println("get employee : "+employeeId+" "+empDto.getEmployeeName());
		return empDto;
	}
	
	@Override
	public Optional<EmployeeModel> deleteEmail() {
		Optional<EmployeeModel> empModel = repo.findByEmail(null);
//		if (empModel.isEmpty()) {
//			throw new UserException("Email doesn't exist!!!");
//		}
		repo.deleteByEmail(null);
		return empModel;

	}
	
	@Override
	public Optional<EmployeeModel> deleteEmployeeId(int employeeId) {
		Optional<EmployeeModel> empModel = repo.findByEmployeeId(employeeId);
		if (empModel.isEmpty()) {
			throw new UserException("Employee doesn't exist!!!");
		}
		repo.deleteByEmployeeId(employeeId);
		return empModel;

	}

	@Override
	public RegisterDTO updateByEmail(RegisterDTO employee, String email) {
		
		EmployeeModel empModel = modelMapper.map(employee, EmployeeModel.class);
		if(repo.findByEmailAndPassword(employee.getEmail(), employee.getPassword()).isPresent() && repo.findByEmailAndPassword(employee.getEmail(), employee.getPassword()).get().getStatus()==1) {
		
		empModel.setVerified(true);
		empModel.setStatus(1);
		employee.setEmail(email);
		repo.save(empModel);
		return employee;
	}
		else {
			throw new UserException("Please Login!");
		}
	}

	@Override
	public String login(LoginDTO loginDTO) {
		Optional<EmployeeModel> empModel = repo.findByEmailAndPassword(loginDTO.getEmail(),
				loginDTO.getPassword());
		if (empModel.isEmpty()) {
			Optional<EmployeeModel> empEmail = repo.findByEmail(loginDTO.getEmail());
			Optional<EmployeeModel> empPassword = repo.findByPassword(loginDTO.getPassword());
			if (empEmail.isEmpty()) {
				throw new UserException("Entered Email is incorrect");
			} else if (empPassword.isEmpty()) {
				throw new UserException("Entered Password is incorrect");
			}
		}
		String token = tokenUtil.generateToken(loginDTO);
		
		empModel.get().setStatus(1);
		repo.save(empModel.get());
		return token;
	}
	
	@Override
	public String logout(String token) {
		LoginDTO loginDTO = tokenUtil.deCode(token);
		Optional<EmployeeModel> checkUser = repo.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
		checkUser.get().setStatus(0);
		repo.save(checkUser.get());
		return "logout successful";
	}
	
	@Override
	public RegisterDTO update(RegisterDTO registerDTO, String token) {
		LoginDTO loginDTO = tokenUtil.deCode(token);
		EmployeeModel empModel = modelMapper.map(registerDTO, EmployeeModel.class);

		if (repo.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()).isPresent() && repo
				.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()).get().getStatus() == 1) {

			empModel.setEmployeeId(repo.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()).get().getEmployeeId());
			empModel.setVerified(true);
			empModel.setStatus(1);
			repo.save(empModel);
			System.out.println("Updated Successfully");

			return registerDTO;
		} else {
			throw new UserException("Please Login!");
		}
	}
	
	public List<EmployeeModel> getAllEmployees() {
		//if (role.equals("Admin"))
		
//		List<EmployeeModel> empList= repo.findAll();
//		List<RegisterDTO> employeesList = empList.stream().map(emp-> modelMapper.map(emp, RegisterDTO.class)).collect(Collectors.toList());
//		return employeesList;
		
		return repo.findAll().stream().map(employee -> modelMapper.map(employee, EmployeeModel.class))
				.collect(Collectors.toList());	
			
//		else {
//			throw new UserException("You are not an Admin, Please check your Role");
//		}
	}

	@Override
	public String sendMail(EmailDTO mail) {

		return javaMailService.sendSimpleMail(mail);
	}

}