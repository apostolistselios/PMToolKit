package application.Controllers;

import application.Utils;
import javafx.event.ActionEvent;

public class CCreateEmployee {
	
	public void onCreateClicked(ActionEvent event) {
		System.out.println("you created an employee");
	}
	
	public void onSaveChangesClicked(ActionEvent event) {
		
	}
	
	public void onCancelClicked(ActionEvent event) {
		Utils.closeWindow(event);
	}

}