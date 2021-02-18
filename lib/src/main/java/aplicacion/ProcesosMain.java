package aplicacion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ProcesosMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/vista/pedirQuantumGUI.fxml"));
        primaryStage.setTitle("SIMPROC SIMULADOR PROCESOS");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
	}

	public static void main(String[] args) {		
		launch(args);
	}
}
