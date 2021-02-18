package controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CrearProcesoController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txfNomPr;

    @FXML
    private TextField txfTiempoPr;

    @FXML
    private TextField txfInteracPr;
    
    //Sirve para el intercambio de información con la ventana principal.
    private ProcesosGUIController procesosGUIController;

    @FXML
    public void cancelar(ActionEvent event) {
    	//Se cierra la ventana que quedó
		Button boton = (Button) event.getSource();		
		Stage stageCerrar = (Stage) boton.getScene().getWindow();	    
		stageCerrar.close();		
    }

    @FXML
    public void crearProceso(ActionEvent event) {
    	
    	String txfNomPr = this.txfNomPr.getText();
    	String txfTiempoPr = this.txfTiempoPr.getText();
    	int tEjecucion = 0; //Si está vacío, significa que fue opcional. Se la da un valor de 0 y se le da tratamiento en la clase Proceso.
    	
    	//Validación nombre vacío
    	if (txfNomPr.isEmpty()) {
    		this.mostrarAlerta(AlertType.ERROR, "ERROR: Nombre inválido", "Error: Nombre vacío", "Por favor asigne un nombre para el proceso");
    		return;
    	}
    	
    	//Si no está vacío, significa que se ingresó un valor. Se convierte en int y se asigna el valor a la variable.
    	if (!txfTiempoPr.isEmpty()) {
    		try {
        		tEjecucion = Integer.parseInt(txfTiempoPr);
    		}
    		catch (Exception e) {
    			this.mostrarAlerta(AlertType.ERROR, "ERROR: Tiempo de ejecución inválido", "Error: Tiempo de ejecución inválido", "Por favor asigne un número entero para tiempo de ejecución (milisegundos)");
    		}   		
    	}
    	
    	try {
	    	//Abre el FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/procesosGUI.fxml"));
			//Carga el FXML como Parent
			Parent interfazHome = loader.load();   
			//Se modifica el atributo procesosGUIController de la clase actual
	    	procesosGUIController.setCrearProceso(txfNomPr, tEjecucion);
	    	//Se actualiza el controlador procesosGUIController con el objeto de la clase, el cual está actualizado.
	    	loader.setController(procesosGUIController);    	
	    	}catch(Exception e) {
	    	System.out.println(e.getMessage());
    	}
    	
    	this.cancelar(event);
    }
    

    @FXML
    public void initialize() {
     

    }
    
    public void setProcesosGUIController(ProcesosGUIController procesosGUIController) {
    	this.procesosGUIController = procesosGUIController;
    }
    
    
    public void mostrarAlerta(AlertType tipoAlerta,String tituloVentana,String tituloMensaje,String mensaje) {
		Alert alerta = new Alert(tipoAlerta);
		alerta.setTitle(tituloVentana);
		alerta.setHeaderText(tituloMensaje);
		alerta.setContentText(mensaje);
		alerta.showAndWait();		
	}
}
