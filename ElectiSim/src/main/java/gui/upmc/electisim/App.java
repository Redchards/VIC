package gui.upmc.electisim;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("ElectiSim");
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/graphics/main.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			
			MainController controller = loader.getController();
			controller.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		primaryStage.show();
	}

}
