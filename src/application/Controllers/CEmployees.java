package application.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Utils;
import application.Window;
import classes.Activity;
import classes.Employee;
import classes.Project;
import classes.ProjectManager;
import classes.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CEmployees implements Initializable{
	
	private Window window;
	private Project project;
	private String buttonText;
	private User employee;

	//Employees.fxml
	@FXML private ListView<User> employeeListView;
	@FXML private Button editEmployeeButton;
	@FXML private Button createEmployeeButton;
	
	//CreateEmployee.fxml
	@FXML private TextField userNameField;
	@FXML private TextField passwordField;
	@FXML private TextField specialtyField;
	@FXML private TextField firstNameField;
	@FXML private TextField lastNameField;
	@FXML private TextField salaryField;
	@FXML private Button createButton;
	
	//EditEmployee.fxml
	@FXML private TextField editUserNameField;
	@FXML private TextField editPasswordField;
	@FXML private TextField editSpecialtyField;
	@FXML private TextField editFirstNameField;
	@FXML private TextField editLastNameField;
	@FXML private TextField editSalaryField;
	@FXML private TextArea evaluationTextArea;
	@FXML private Button saveChangesButton;
	
	public CEmployees(Project project, String buttonText){
		this.project=project;
		this.buttonText=buttonText;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//if edit employee button is clicked
		if (buttonText.equalsIgnoreCase(editEmployeeButton.getText())){
			fillEmployeeInfo();
		//if create employee is clicked
		}else if(buttonText.equalsIgnoreCase(createEmployeeButton.getText())){
			setProperties();
		}else
			fillEmployeesList();
	}
	
	//Employees.fxml
	
	//Create Employee button
	public void onCreateEmployeeClicked(ActionEvent actionEvent) throws Exception{
		Stage stage= Utils.getStageFromEvent(actionEvent);
		this.buttonText= this.createEmployeeButton.getText();
		window = new Window(stage);
		window.changeScene("CreateEmployee.fxml", this);
	}
	
	//Edit Employee button
	public void onEditEmployeeClicked(ActionEvent actionEvent) throws IOException{
		User selectedEmployee = getSelectedEmployee();
		if(selectedEmployee instanceof Employee) {
			Stage stage= Utils.getStageFromEvent(actionEvent);
			this.buttonText=editEmployeeButton.getText();
			window = new Window(stage);
			window.changeScene("EditEmployee.fxml", this);
		}else if( selectedEmployee instanceof ProjectManager)
			Utils.createInfoAlert("Information", "Select an employee!");
		else
			Utils.createInfoAlert("Information", "Select an employee first!");
	}
	
	//Cancel button
	public void onCancelClicked(ActionEvent actionEvent){
		Utils.closeWindow(actionEvent);
	}
	
	//Delete button
	public void onDeleteClicked(ActionEvent actionEvent) throws IOException{
		User selectedEmployee = getSelectedEmployee();
		if(selectedEmployee instanceof ProjectManager) 
			Utils.createInfoAlert("Information", "You can't delete the Project Manager");
		else {
			deleteEmployeeFromProject((Employee) selectedEmployee);
			fillEmployeesList();
		}
	}
	
	//Deletes the selectedEmployee from the Project
	private void deleteEmployeeFromProject(Employee employee) throws IOException {
		ArrayList<User> employees = project.getEmployees();
		ArrayList<String> projects = employee.getProjects();
		ArrayList<String> activitiesIds = employee.getUnfinishedActivities(project);
		for(String a : activitiesIds)
			System.out.println(a);
		/*ArrayList<Activity> activities = Utils.getActivitiesFromID(activitiesIds);
				
		
		Iterator<String> iter1 = projects.iterator();
		while(iter1.hasNext()) {
			String projectName = iter1.next();
			if(projectName.equalsIgnoreCase(project.getName()))
				iter1.remove();
		}
		
		Utils.saveEmployeeChanges(employee);
		
		Iterator<User> iter2 = employees.iterator();		
		while(iter2.hasNext()) {
			User emp  = iter2.next();
			if(emp.getUsername().equalsIgnoreCase(employee.getUsername()))
				iter2.remove();
		}
		
		Utils.saveProjectChanges(project);*/
		
		/*Iterator<Activity> iter3 = activities.iterator();	
		while(iter3.hasNext()) {
			Activity activity  = iter3.next();
			activity.deleteEmployee(employee.getUsername());
			Utils.saveActivityChanges(activity);
		}*/
		
		
		
		
	}
	
	private User getSelectedEmployee() {
		return employeeListView.getSelectionModel().getSelectedItem();
	}
	
	//Fills the employeeListView
	public void fillEmployeesList(){
		ArrayList<User> employees = project.getEmployees();
		
		ObservableList<User> list = FXCollections.observableArrayList(employees);
		employeeListView.getItems().clear();
		employeeListView.setItems(list);
		employeeListView.setCellFactory(param -> new ListCell<User>(){
			@Override
			protected void updateItem(User employee, boolean empty) {
				super.updateItem(employee, empty);
				if (employee == null )
					setText(null);
				else
					setText(employee.getName()+"-"+employee.getSpecialty());
			}
		});
	}
	
	//CreateEmployee.fxml
	
	//Create button
	public void onCreateClicked(ActionEvent actionEvent) throws IOException {
		if (hasAppropriateArgs()) {
			createEmployee();
			Utils.createInfoAlert("Proccess", "You successfully create employee " + userNameField.getText());
			Stage stage = Utils.getStageFromEvent(actionEvent);
			this.buttonText = createButton.getText();
			window = new Window(stage);
			window.changeScene("Employees.fxml", this);
		} else
			Utils.createInfoAlert("Information", "You have to fill in all the fields");
	}
	
	//Creates an employee object and saves it to projects and employees file
	public void createEmployee() throws IOException{
		String username = userNameField.getText();
		String password = passwordField.getText();
		String speciality = specialtyField.getText();
		String firstname= firstNameField.getText();
		String lastname = lastNameField.getText();
		double salary = Double.parseDouble(salaryField.getText());
		User employee = new Employee(username,password,firstname,lastname,salary,speciality);
		Alert alert=Utils.createCustomConfirmationAlert("Add Employee", null, "Do you want to add employee "+username+" to this project?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get().getText().equals("Yes")){
			project.addEmployee(employee);
        	employee.addProject(project.getName());
        	Utils.saveProjectChanges(project);
		}
		
        Utils.saveEmployeeChanges(employee);       
	}

	//Checks if the user filled all the fields in CreateEmployee.fxml
	private boolean hasAppropriateArgs(){
		if(userNameField.getText().equals("") || passwordField.getText().equals("") || specialtyField.getText().equals("")
				|| firstNameField.getText().equals("") || lastNameField.getText().equals("") || salaryField.getText().equals(""))
			return false;
		return true;
	}
	
	private void setProperties(){
		salaryField.textProperty().addListener((observable, oldValue, newValue) -> {
			try{
				Double.parseDouble(salaryField.getText());
			}catch(NumberFormatException e){
				salaryField.setText("");
			}
		});
	}
	
	//EditEmployee.fxml
	
	//Save Changes button
	public void onSaveChangesClicked(ActionEvent event) throws IOException {
		Employee selectedEmployee = (Employee) getSelectedEmployee();
		selectedEmployee.setUsername(editUserNameField.getText());
		selectedEmployee.setPassword(editPasswordField.getText());
		selectedEmployee.setSpecialty(editSpecialtyField.getText());
		selectedEmployee.setFirstname(editFirstNameField.getText());
		selectedEmployee.setLastname(editLastNameField.getText());
		selectedEmployee.setSalary(Double.parseDouble(editSalaryField.getText()));
		
		Utils.saveEmployeeChanges(selectedEmployee);
		Utils.saveProjectChanges(project);
		
		Stage stage = Utils.getStageFromEvent(event);
		this.buttonText = saveChangesButton.getText();
		window = new Window(stage);
		window.changeScene("Employees.fxml", this);
	}
	
	//Cancel button for CreateEmployee.fxml and EditEmployee.fxml
	public void onCancelEditCreateClicked(ActionEvent actionEvent) throws IOException {
		Stage stage= Utils.getStageFromEvent(actionEvent);
		this.buttonText = "Cancel";
		window = new Window(stage);
		window.changeScene("Employees.fxml",this);
	}
	
	//Fills the components with the selected employee's info
	public void fillEmployeeInfo(){			
		Employee selectedEmployee = (Employee) getSelectedEmployee();
		
		editUserNameField.setText(selectedEmployee.getUsername());
		editPasswordField.setText(selectedEmployee.getPassword());
		editSpecialtyField.setText(selectedEmployee.getSpecialty());
		editFirstNameField.setText(selectedEmployee.getFirstname());
		editLastNameField.setText(selectedEmployee.getLastname());
		editSalaryField.setText(Double.toString(selectedEmployee.getSalary()));
		if(!selectedEmployee.getEvaluation().equals(""))
			evaluationTextArea.setText(selectedEmployee.getEvaluation() + " and his was graded: " 
					+ selectedEmployee.getGrade());
		else
			evaluationTextArea.setText("Not evaluated yet!");
	}
}
