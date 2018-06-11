package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import classes.Project;
import classes.Task;
import classes.User;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Utils {
	
	
	public static void closeWindow(ActionEvent actionEvent){
		Stage stage=getStageFromEvent(actionEvent);
	    stage.close();
	}
	
	public static Alert createConfirmationAlert(String title, String headerText, String contentText){ 
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
	}
	
	public static Alert createCustomConfirmationAlert(String title, String headerText, String contentText){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		ButtonType buttonYes = new ButtonType("Yes");
		ButtonType buttonNo = new ButtonType("No");
		

		alert.getButtonTypes().setAll(buttonYes,buttonNo);
		
		return alert;
	}
	
	public static void createErrorAlert(String title, String headerText, String contentText){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
        alert.showAndWait();
	}
	
	public static void createInfoAlert(String title, String contentText){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
	public static void closeProgram(Stage stage){
		/*Alert alert = Utils.createConfirmationAlert("Exit","Exit Confirmation", "Are sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        	stage.close();
        }*/
		stage.close();
	}
	
	public static Stage getStageFromEvent(ActionEvent actionEvent){
		Node  source = (Node)  actionEvent.getSource(); 
	    Stage stage  = (Stage) source.getScene().getWindow();
	    return stage;
	}
	
	public static void saveEmployeeChanges(User employee) throws IOException{
		ArrayList<User> employees = getEmployeesFromFile();
		
		for (Iterator<User> iterator = employees.iterator(); iterator.hasNext(); ) {
		    User emp = iterator.next();
		    if (emp.getUsername().equalsIgnoreCase(employee.getUsername())) {
		        iterator.remove();
		    }
		}
		
		employees.add(employee);
		writeToEmployeeFile(employees);
		
	}
	
	public static void saveProjectChanges(Project project) throws IOException{
		ArrayList<Project> projects = getProjectsFromFile();
		
		for (Iterator<Project> iterator = projects.iterator(); iterator.hasNext(); ) {
		    Project pr = iterator.next();
		    if (pr.getName().equalsIgnoreCase(project.getName())) {
		        iterator.remove();
		    }
		}
		
		projects.add(project);
		writeToProjectFile(projects);
	}
	
	public static void saveTaskChanges(Task task) throws IOException{
		ArrayList<Task> tasks= getTasksFromFile();
		
		for(Iterator<Task> iterator = tasks.iterator(); iterator.hasNext();){
			Task t = iterator.next();
			if(t.getId().equalsIgnoreCase(task.getId())){
				iterator.remove();
			}
		}
		
		tasks.add(task);
		writeToTaskFile(tasks);
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<User> getEmployeesFromFile(){
		ArrayList<User> employees=null;
		String filename="Employees";
		ObjectInputStream inputStream=null;
		try{
			inputStream=new ObjectInputStream(new FileInputStream(filename));
			employees= (ArrayList<User>) inputStream.readObject();
			inputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return employees;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Project> getProjectsFromFile(){
		ArrayList<Project> projects=null;
		String filename="Projects";
		ObjectInputStream inputStream=null;
		try{
			inputStream=new ObjectInputStream(new FileInputStream(filename));
			projects= (ArrayList<Project>) inputStream.readObject();
			inputStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return projects;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Task> getTasksFromFile(){
		ArrayList<Task> tasks=null;
		String filename="Tasks";
		ObjectInputStream inputStream= null;
		try{
			inputStream=new ObjectInputStream(new FileInputStream(filename));
			tasks= (ArrayList<Task>) inputStream.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		return tasks;
	}
	
	public static void writeToEmployeeFile(ArrayList<User> employees) throws IOException{
		ObjectOutputStream  outStream=null;
		String filename="Employees";
	    try{
	    	outStream=new ObjectOutputStream(new FileOutputStream(filename));
	    	outStream.writeObject(employees);
	    	outStream.close();
	    }catch(IOException e){
	    	System.out.println("Error writing to file "+filename);
	    	e.printStackTrace();
	    }
		
	}
	
	public static void writeToProjectFile(ArrayList<Project> projects) throws IOException{
		ObjectOutputStream  outStream=null;
		String filename="Projects";
	    try{
	    	outStream=new ObjectOutputStream(new FileOutputStream(filename));
	    	outStream.writeObject(projects);
	    	outStream.close();
	    }catch(IOException e){
	    	System.out.println("Error writing to file "+filename);
	    	e.printStackTrace();
	    }
		
	}
	
	public static void writeToTaskFile(ArrayList<Task> tasks) throws IOException{
		ObjectOutputStream  outStream=null;
		String filename="Tasks";
		try{
			outStream=new ObjectOutputStream(new FileOutputStream(filename));
	    	outStream.writeObject(tasks);
	    	outStream.close();
		}catch(IOException e){
			System.out.println("Error writing to file "+filename);
	    	e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Task> getTasksFromID(ArrayList<String> tasksId){
		ArrayList<Task> allTasks = getTasksFromFile();
		ArrayList<Task> returnedTasks= new ArrayList<Task>();
		for(Iterator<Task> iterator = allTasks.iterator(); iterator.hasNext();){
			Task t=iterator.next();
			if(tasksId.contains(t.getId()))
				returnedTasks.add(t);
		}
		return returnedTasks;
	}
	
	public static Task getSingleTaskFromFile(String taskId){
		ArrayList<Task> allTasks = getTasksFromFile();
		for(Iterator<Task> iterator = allTasks.iterator(); iterator.hasNext();){
			Task t= iterator.next();
			if(t.getId().equalsIgnoreCase(taskId))
				return t;
		}
		return null;
	
	}
}
