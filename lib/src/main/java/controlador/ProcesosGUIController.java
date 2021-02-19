package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import modelo.Proceso;

public class ProcesosGUIController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private TableView<Proceso> tblInactivos;
    @FXML
    private TableColumn<Proceso, String> tblColInactivosNombre;
    
    @FXML
    private TableView<Proceso> tblPreparados;
    @FXML
    private TableColumn<Proceso, String> tblColPreparadosNom;
    @FXML
    private TableColumn<Proceso, Integer> tblColPreparadosPrio;
    @FXML
    private TableColumn<Proceso, Integer> tblColPreparadosPid;
    @FXML
    private TableColumn<Proceso, String> tblColPreparadosEst;
    @FXML
    private TableColumn<Proceso, Integer> tblColPreparadosTime;
    @FXML
    private TableColumn<Proceso, Integer> tblColPreparadosQuantum;
    @FXML
    private TableColumn<Proceso, String> tblColPreparadosInter;
    
    
    @FXML
    private TableView<Proceso> tblSuspendidos;
    @FXML
    private TableColumn<Proceso, String> tblColSuspNom;
    @FXML
    private TableColumn<Proceso, String> tblColSuspInter;    
   
    private ObservableList<Proceso> listaEjecucion;
    
    
    @FXML
    private TableView<Proceso> tblEjecucion;
    @FXML
    private TableColumn<Proceso, String> tblColEjeNom;
    @FXML
    private TableColumn<Proceso, String> tblColEjePrio;
    @FXML
    private TableColumn<Proceso, Integer> tblColEjePid;
    @FXML
    private TableColumn<Proceso, Double> tblColEjeTime;
    @FXML
    private TableColumn<Proceso, Double> tblColEjeQua;
    @FXML
    private TableColumn<Proceso, String> tblColEjeInter;
    @FXML
    private TableColumn<Proceso, String> tblColEjeEst;
    
    boolean okVistaOrden = false;
    
    @FXML
    private ProgressBar pBarEjecutando;
    
   
    private int rataQuantum;
    
    private ObservableList<Proceso> listaInactivos;
    private ObservableList<Proceso> listaPreparados;
    private ObservableList<Proceso> listaSuspendidos;  
    

    @FXML
    private Button btnGestionar;


    private int contPID = 0;
    
    ServicioGestionar servicioGestionar = new ServicioGestionar(); 
    

    
    public ProcesosGUIController() {
    	  	
	}

    @FXML
    public void crearProceso(ActionEvent event) throws IOException {
    	
    	//Carga y muestra la ventana de Crear Proceso
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/crearProcesoGUI.fxml"));
    	Parent interfaz = loader.load();
    	CrearProcesoController crearProcesoController = loader.getController();
    	//Se envía el presente controlador a la clase CrearProcesoController para el envío de información.
    	crearProcesoController.setProcesosGUIController(this);
    	Scene escena = new Scene(interfaz);	
		Stage ventana = new Stage();
		ventana.setScene(escena);
		ventana.setTitle("Crear proceso");
		ventana.show();		
    }
    
    public void crearProceso(String nombre, int tEjecucion) {			
		Proceso inactivo = new Proceso(nombre, tEjecucion);
		this.listaInactivos.add(inactivo);
	}
   
    
    @FXML
    public void prepararProceso(ActionEvent event) {
    	
    	//Validación: Cuando no hay nada seleccionado.
    	if(this.tblInactivos.getSelectionModel().isEmpty()) {
    		this.mostrarAlerta(AlertType.ERROR, "Proceso inválido", "Proceso no seleccionado", "Por favor seleccione un proceso para prepararlo.");
    		return;
    	}
    	
    	//Obteniene el proceso seleccionado
    	Proceso preparado = new Proceso();
    	preparado = this.tblInactivos.getSelectionModel().getSelectedItem();
    	
    	//Se prepara el proceso enviandole el rata de Quantum ingresada en la App.
    	preparado.prepararProceso(this.rataQuantum, this.contPID);
    	
    	//Se suma el pid 
    	this.contPID++;
    	
    	//Se elimina de inactivos
    	this.listaInactivos.remove(preparado);
    	
    	//Se agrega a preparados
    	this.listaPreparados.add(preparado);
    	
    }
    
    @FXML
    public void gestionarProcesos(ActionEvent event) throws InterruptedException { 
    	
    	 ServicioOrdenar servicioOrdenar = new ServicioOrdenar();
    	 ServicioSuspendidos serviciosSuspendidos = new ServicioSuspendidos(); 
    	 
    	 if(this.servicioGestionar.isRunning()) {
    		 this.mostrarAlerta(AlertType.ERROR, "Error de programa", "Gestión en proceso", "No presione el botón Gestionar mientras está ocurriendo la simulación.");
    		 return;
    	 }
    	 
    	 if(!listaPreparados.isEmpty()) {//Si hay procesos en ejecución
     		servicioOrdenar.start();
     	}    	 
    	
    	 ejecutarServicioGestionar();   	
     	 serviciosSuspendidos.start();
     	
    	  	    
	
    }
    
    public void ejecutarServicioGestionar() {
       
        this.servicioGestionar = new ServicioGestionar();
        
    	if(!listaPreparados.isEmpty()) {//Si hay procesos en ejecución    	    		
	    	this.pBarEjecutando.progressProperty().bind(servicioGestionar.progressProperty());
	    	servicioGestionar.start();   	    	
    	}	
    	 		
    }
    
    
    public class ServicioOrdenar extends Service<Void>{

		@Override
		protected Task<Void> createTask() {
			
			return new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
					Platform.runLater(() -> ordenarPreparados());
					Thread.sleep(5000);
					okVistaOrden = true;
					
					
					return null;
				}};
		}
    }
    
    public class ServicioGestionar extends ScheduledService<Void>{

		@Override
		protected Task<Void> createTask() {
			
			return new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
					if (!okVistaOrden) {
						return null;
					}
										
					//Delay entre ejecución
					Thread.sleep(1000);
										
					//Gestionar procesos					
					
					Platform.runLater(new Runnable() {
						@Override
						public void run() {							
						
							if (!listaPreparados.isEmpty()) {//Si hay procesos en preparados
									
								//Pasar a ejecución y quitarlo de preparados
								listaPreparados.get(0).setEstado("EJECUCIÓN");
								listaEjecucion.add(listaPreparados.get(0));			  				   
			  				   	listaPreparados.remove(0);
							}
						}						
					});
					
					Thread.sleep(500);
					
					if(!listaEjecucion.isEmpty()) {//Si hay procesos en ejecución
						
						double tEjecTemporal = listaEjecucion.get(0).getTEjecucion();
						
						//Ejecutar el proceso dependiendo de la rata de quantum
						for(int i=1; i<=rataQuantum; i++) {
							
							//Delay de ejecución
							Thread.sleep(1000);
							
							//Si el tiempo de ejecución del proceso no es cubierto por el rata de quantum,
							//entonces muestre el progressbar hasta el número de rata de quantum
							if(tEjecTemporal > rataQuantum) {
								updateProgress(i, rataQuantum);
							}else {//Si es menor o igual, muestre el tiempo de ejecución del proceso ejecutándose.
								updateProgress(i, tEjecTemporal);
							}
								
							//Si está terminado, cambie su estado y salga del for.
							if(listaEjecucion.get(0).getTEjecucion() == 0) {
								listaEjecucion.get(0).setEstado("TERMINADO");
								break;
							}else {
								Platform.runLater(new Runnable() {
									@Override
									public void run() {											
										
										listaEjecucion.get(0).setTEjecucion(listaEjecucion.get(0).getTEjecucion() - 1);		
										tblEjecucion.refresh();
									}
								});								
							}
						}
					}
					
					Thread.sleep(500);
					
					//Proceso después de asignarle el tiempo de ejecución según el rata de Quantum
					if(!listaEjecucion.isEmpty()) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {								
								//Si el proceso terminó, mandelo a inactivos y quitelo de ejecución.
								if(listaEjecucion.get(0).getTEjecucion() == 0) {//Si está terminado
									listaEjecucion.get(0).setEstado("TERMINADO");
									listaInactivos.add(listaEjecucion.get(0));									
									listaEjecucion.remove(0);
								}else {//Sino, envíelo a suspendidos y quitelo de ejecución.
									listaEjecucion.get(0).setEstado("SUSPENDIDO");
									listaSuspendidos.add(listaEjecucion.get(0));
									listaEjecucion.remove(0);
								}							
							}
						});					
					}					
					return null;
				}};
		}
    }
    
    public class ServicioSuspendidos extends ScheduledService<Void>{

		@Override
		protected Task<Void> createTask() {
			
			return new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					
					
					
					if(!listaSuspendidos.isEmpty()) {//Si hay procesos suspendidos						
						
						for (Proceso p: listaSuspendidos) {							
							//Si el proceso tiene interactividad déjelo donde en suspendidos.
							if (p.getInteractividad().compareTo("Si") != 0) {
								Thread.sleep(1000);
								Platform.runLater(new Runnable() {
									@Override
									public void run() {
										p.setEstado("PREPARADO");
										listaPreparados.add(p);
										listaSuspendidos.remove(p);										
									}
								});	
							}
						}
					}
					
					
					return null;
				}};
		}
    }
    	
    public void ordenarPreparados() {    	

    	for (int im=0; im<this.listaPreparados.size(); im++){
		    for (int j=im+1; j<this.listaPreparados.size(); j++){
		    	if(this.listaPreparados.get(im).getQuantum()>this.listaPreparados.get(j).getQuantum()){
		    		Proceso aux = this.listaPreparados.set(im, this.listaPreparados.get(j));
		    		this.listaPreparados.set(j,aux);
		    	}else if(this.listaPreparados.get(im).getQuantum()==this.listaPreparados.get(j).getQuantum() & this.listaPreparados.get(im).getPrio()<this.listaPreparados.get(j).getPrio()) {
		    		Proceso aux = this.listaPreparados.set(im, this.listaPreparados.get(j));
		    		this.listaPreparados.set(j,aux);
		    	}
		    }
		}
    }
    
    @FXML
    public void ejecutarInteractividad(ActionEvent event) {
    	
    	//Validación: Cuando no hay nada seleccionado.
    	if(this.tblSuspendidos.getSelectionModel().isEmpty()) {
    		this.mostrarAlerta(AlertType.ERROR, "Proceso inválido", "Proceso no seleccionado", "Por favor seleccione un proceso con interactividad para ejecutarlo.");
    		return;
    	}
    	
    	//Obteniene el proceso seleccionado
    	Proceso suspendido = new Proceso();
    	suspendido = this.tblSuspendidos.getSelectionModel().getSelectedItem();
    	
    	if(suspendido.getInteractividad().compareTo("Si") != 0) {//Si no tiene interactividad.
    		this.mostrarAlerta(AlertType.ERROR, "Proceso inválido", "Proceso sin interactividad", "Los procesos sin interactividad se ejecutarán automáticamente.");
    		return;
    	}
    	
    	suspendido.setEstado("PREPARADO");
    	this.listaPreparados.add(suspendido);
    	this.listaSuspendidos.remove(suspendido);   	
    }
    
    @FXML
    public void borrarProceso(ActionEvent event) {

    	//Validación: Cuando no hay nada seleccionado.
    	if(this.tblInactivos.getSelectionModel().isEmpty()) {
    		this.mostrarAlerta(AlertType.ERROR, "Proceso inválido", "Proceso no seleccionado", "Por favor seleccione un proceso de la tabla inactivos para borrarlo.");
    		return;
    	}
    	
    	//Obtiene el proceso seleccionado
    	Proceso preparado = new Proceso();
    	preparado = this.tblInactivos.getSelectionModel().getSelectedItem();
    	
    	this.listaInactivos.remove(preparado);    	
    }
 
    
    @FXML
    public void salirPrograma(ActionEvent event) {
    	Platform.exit();
    }
    
    //Pasa los valores de la ventana de Quantum a la aplicación general.
    public void setRataQuantum(int rataQuantum) {
    	this.rataQuantum = rataQuantum;
    }
    
    //Método que sirve para obtener la información insertada en la ventana de Crear Proceso.
    //Se crea un proceso a través del objeto de gestión de procesos.
    public void setCrearProceso(String nombre, int tEjecucion) {
    	this.crearProceso(nombre, tEjecucion);
    }

    @FXML
    public void initialize() {    	
    	
    	//Configuración tabla inactivos
    	this.tblColInactivosNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	this.listaInactivos = FXCollections.observableArrayList();
//    	this.listaInactivos = FXCollections.observableArrayList(new Proceso("p1", 0),
//    			new Proceso("p2", 0),
//    			new Proceso("p3", 0),
//    			new Proceso("p4", 0),
//    			new Proceso("p5", 0),
//    			new Proceso("p6", 0));
    	this.tblInactivos.setItems(this.listaInactivos);  	
    	
    	//Configuración tabla preparados
    	this.tblColPreparadosNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	this.tblColPreparadosPrio.setCellValueFactory(new PropertyValueFactory<>("tipoPrio"));
    	this.tblColPreparadosPid.setCellValueFactory(new PropertyValueFactory<>("pid"));
    	this.tblColPreparadosEst.setCellValueFactory(new PropertyValueFactory<>("estado"));
    	this.tblColPreparadosTime.setCellValueFactory(new PropertyValueFactory<>("tEjecucion"));
    	this.tblColPreparadosQuantum.setCellValueFactory(new PropertyValueFactory<>("quantum"));
    	this.tblColPreparadosInter.setCellValueFactory(new PropertyValueFactory<>("interactividad"));
    	
    	this.listaPreparados = FXCollections.observableArrayList();
    	
    	//Prueba con una rata de quantum = 4
//    	this.listaPreparados = FXCollections.observableArrayList(
//    			new Proceso("p7", 5.0, 0, 0, "Si", 1.25),
//    			new Proceso("p8", 5.0, 1, 2, "No", 1.25),
//    			new Proceso("p9", 7.0, 2, 2, "Si", 1.75),
//    			new Proceso("p10", 10.0, 3, 1, "No", 2.50),
//    			new Proceso("p11", 7.0, 4, 2, "No", 1.75),
//    			new Proceso("p12", 10.0, 5, 2, "No", 2.50),
//    			new Proceso("p13", 15.0, 6, 2, "No", 3.75)
//    			);
//    	this.listaPreparados = FXCollections.observableArrayList(    			
//    			new Proceso("p10", 10.0, 3, "No", 2.0)    			
//    			);    	
    	
    	this.tblPreparados.setItems(this.listaPreparados);
    	
    	//Configuración tabla suspendidos
    	this.tblColSuspNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	this.tblColSuspInter.setCellValueFactory(new PropertyValueFactory<>("interactividad"));
    	this.listaSuspendidos = FXCollections.observableArrayList();
    	this.tblSuspendidos.setItems(this.listaSuspendidos);
    	
    	//Configuración tabla ejecución
    	this.tblColEjeNom.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    	this.tblColEjePrio.setCellValueFactory(new PropertyValueFactory<>("tipoPrio"));
    	this.tblColEjePid.setCellValueFactory(new PropertyValueFactory<>("pid"));
    	this.tblColEjeTime.setCellValueFactory(new PropertyValueFactory<>("tEjecucion"));
    	this.tblColEjeQua.setCellValueFactory(new PropertyValueFactory<>("quantum"));
    	this.tblColEjeInter.setCellValueFactory(new PropertyValueFactory<>("interactividad"));
    	this.tblColEjeEst.setCellValueFactory(new PropertyValueFactory<>("estado"));
    	this.listaEjecucion = FXCollections.observableArrayList();
    	this.tblEjecucion.setItems(this.listaEjecucion);
    	
    }
    
    public void mostrarAlerta(AlertType tipoAlerta,String tituloVentana,String tituloMensaje,String mensaje) {
		Alert alerta = new Alert(tipoAlerta);
		alerta.setTitle(tituloVentana);
		alerta.setHeaderText(tituloMensaje);
		alerta.setContentText(mensaje);
		alerta.showAndWait();		
	}
    
    
}