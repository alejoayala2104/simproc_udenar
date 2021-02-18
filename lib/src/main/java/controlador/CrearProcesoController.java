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
    
    //Sirve para el intercambio de informaci�n con la ventana principal.
    private ProcesosGUIController procesosGUIController;

    @FXML
    public void cancelar(ActionEvent event) {
    	//Se cierra la ventana que qued�
		Button boton = (Button) event.getSource();		
		Stage stageCerrar = (Stage) boton.getScene().getWindow();	    
		stageCerrar.close();		
    }

    @FXML
    public void crearProceso(ActionEvent event) {
    	
    	String txfNomPr = this.txfNomPr.getText();
    	String txfTiempoPr = this.txfTiempoPr.getText();
    	int tEjecucion = 0; //Si est� vac�o, significa que fue opcional. Se la da un valor de 0 y se le da tratamiento en la clase Proceso.
    	
    	//Validaci�n nombre vac�o
    	if (txfNomPr.isEmpty()) {
    		this.mostrarAlerta(AlertType.ERROR, "ERROR: Nombre inv�lido", "Error: Nombre vac�o", "Por favor asigne un nombre para el proceso");
    		return;
    	}
    	
    	//Si no est� vac�o, significa que se ingres� un valor. Se convierte en int y se asigna el valor a la variable.
    	if (!txfTiempoPr.isEmpty()) {
    		try {
        		tEjecucion = Integer.parseInt(txfTiempoPr);
    		}
    		catch (Exception e) {
    			this.mostrarAlerta(AlertType.ERROR, "ERROR: Tiempo de ejecuci�n inv�lido", "Error: Tiempo de ejecuci�n inv�lido", "Por favor asigne un n�mero entero para tiempo de ejecuci�n (milisegundos)");
    		}   		
    	}
    	
    	try {
	    	//Abre el FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/procesosGUI.fxml"));
			//Carga el FXML como Parent
			Parent interfazHome = loader.load();   
			//Se modifica el atributo procesosGUIController de la clase actual
	    	procesosGUIController.setCrearProceso(txfNomPr, tEjecucion);
	    	//Se actualiza el controlador procesosGUIController con el objeto de la clase, el cual est� actualizado.
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
