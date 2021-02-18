package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class QuantumController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txfQuantum;

    @FXML
    private Button btnAceptar;

    @FXML
    private Button btnSalir;

    @FXML
    public void entrar(ActionEvent event) throws IOException {
    	
    	//Se obtiene el rata de Quantum
    	String cadenaRataQ = this.txfQuantum.getText();
    	if (cadenaRataQ.isEmpty()) {
    		this.mostrarAlerta(AlertType.ERROR, "ERROR: Rata de Quantum inválida", "Rata de Quantum vacía", "Por favor ingrese un valor entero.");
    		return;
    	}
    	try {
    		
    		//Validar que sea un entero positivo
    		if(!isNumeric(cadenaRataQ)) {
    			this.mostrarAlerta(AlertType.ERROR, "ERROR: Rata de Quantum inválida", "Rata de Quantum inválida", "Por favor ingrese un valor entero positivo.");
    			return;
    		}
    		
	    	int rataQuantum = Integer.parseInt(cadenaRataQ);    
	    	
	
	    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/procesosGUI.fxml"));
	    	Parent interfazHome = loader.load();
	    	
	    	ProcesosGUIController procesosGUIController = loader.getController();
	    	procesosGUIController.setRataQuantum(rataQuantum);
	    	Scene escenaHome = new Scene(interfazHome);	
			Stage ventanaHome = new Stage();
			ventanaHome.setScene(escenaHome);
			ventanaHome.setTitle("Inicio");
			ventanaHome.show();  
    	}catch(Exception e) {
    		this.mostrarAlerta(AlertType.ERROR, "ERROR: Rata de Quantum inválida", "Rata de Quantum vacía", "Por favor ingrese un valor entero.");
    	}
		
		//Se cierra la ventana que quedó
		Button boton = (Button) event.getSource();		
		Stage stageCerrar = (Stage) boton.getScene().getWindow();	    
		stageCerrar.close();
		
    }
    
    public static boolean isNumeric(String str) { 
    	  try {  
		    int num = Integer.parseInt(str);
		    if(num <= 0) {
		    	return false;
		    }else {
		    	return true;
		    }    	    
    	  } catch(NumberFormatException e){  
    	    return false;  
    	  }  
    	}

    @FXML
    public void salir(ActionEvent event) throws IOException {
    	Platform.exit();
    }

    @FXML
    public void initialize() {       

    }
    
    public void mostrarAlerta(AlertType tipoAlerta,String tituloVentana,String tituloMensaje,String mensaje) {
		Alert alerta = new Alert(tipoAlerta);
		alerta.setTitle(tituloVentana);
		alerta.setHeaderText(tituloMensaje);
		alerta.setContentText(mensaje);
		alerta.showAndWait();		
	}
}
