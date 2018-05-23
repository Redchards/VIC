package gui.upmc.electisim;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DialogBoxHelper {
    public static void displayError(String heading, String content) {
    	displayDialogBox(heading, content, AlertType.ERROR);
    }
    
	
    public static void displayWarning(String heading, String content) {
    	displayDialogBox(heading, content, AlertType.WARNING);
    }
    
    public static void displayInfo(String heading, String content) {
    	displayDialogBox(heading, content, AlertType.INFORMATION);
    }
    
    public static void displayDialogBox(String heading, String content, AlertType type) {
        Alert alert = new Alert(type);

        String title = type.toString().toLowerCase();
        title = Character.toUpperCase(title.charAt(0)) + title.substring(1);
        
        alert.setTitle(title);
        alert.setHeaderText(heading);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
