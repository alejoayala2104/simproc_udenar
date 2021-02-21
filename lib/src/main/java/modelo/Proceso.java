package modelo;

import java.util.Random;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Proceso {
	
	//Constantes	
	public final static Random rd = new Random();
	
	private SimpleStringProperty nombre;
	private SimpleIntegerProperty prio;
	private SimpleStringProperty tipoPrio;
	private SimpleIntegerProperty pid;
	private SimpleDoubleProperty tEjecucion; //En milisegundos
	private SimpleStringProperty interactividad;
	private SimpleDoubleProperty quantum;
	private SimpleStringProperty estado;
		
	public Proceso() {
		
	}	
	
	public Proceso(String nombre, int tEjecucion) {
		
		this.nombre = new SimpleStringProperty(nombre);
		this.prio = new SimpleIntegerProperty();
		this.tipoPrio = new SimpleStringProperty("");		
		this.pid = new SimpleIntegerProperty();
		this.tEjecucion = new SimpleDoubleProperty(tEjecucion);
		this.interactividad = new SimpleStringProperty();
		this.quantum = new SimpleDoubleProperty();
		this.estado = new SimpleStringProperty("INACTIVO");
		
	}
	
	public Proceso(String nombre, double tEjecucion, int pid, int prio, String inter, double quantum) {		
		
		this.nombre = new SimpleStringProperty(nombre);
		this.prio = new SimpleIntegerProperty(prio);
		this.tipoPrio = new SimpleStringProperty("");		
		this.pid = new SimpleIntegerProperty(pid);
		this.tEjecucion = new SimpleDoubleProperty(tEjecucion);
		this.interactividad = new SimpleStringProperty();
		this.quantum = new SimpleDoubleProperty();
		this.estado = new SimpleStringProperty("INACTIVO");
		
		//Asignación de parámetros			
		this.estado.set("PREPARADO");
		
		//Valores aleatorios
//		this.prio.set(rd.nextInt(4)); //[0...3]
		if (this.tEjecucion.get() == 0) {this.tEjecucion.set((int)(Math.random()*10+1));} //Si no ha sigo asignado, es aleatorio.
		
		//Asignación de la interactividad en String para la muestra en tablas.		
		this.interactividad.set(inter);	
		
		//Asignación de la prioridad en String para muestra en tablas.
		if (this.prio.get() == 0) {
			this.tipoPrio.set("BAJA");
		}
		else if(this.prio.get() == 1) {
			this.tipoPrio.set("MEDIA");
		}else {
			this.tipoPrio.set("ALTA");
		}
		
		//Cálculo del Quantum
		this.quantum.set(quantum);
		
	}
	
	public void prepararProceso(int rataQuantum, int pid) {
			
		//Asignación de parámetros			
		this.estado.set("PREPARADO");
		this.pid.set(pid);
		
		//Valores aleatorios
		this.prio.set(rd.nextInt(3)); //[0...2]
		if (this.tEjecucion.get() == 0) {this.tEjecucion.set((int)(Math.random()*10+1));} //Si no ha sigo asignado, es aleatorio.
		
		//Asignación de la interactividad en String para la muestra en tablas.
		boolean inter = rd.nextBoolean();
		if (inter) {
			this.interactividad.set("Si");
		}else {
			this.interactividad.set("No");
		}
		
		
		//Asignación de la prioridad en String para muestra en tablas.
		if (this.prio.get() == 0) {
			this.tipoPrio.set("BAJA");
		}
		else if(this.prio.get() == 1) {
			this.tipoPrio.set("MEDIA");
		}else {
			this.tipoPrio.set("ALTA");
		}
		
		//Cálculo del Quantum
		double quantum = Math.round((this.tEjecucion.get()/rataQuantum) * 100.0) / 100.0;
		this.quantum.set(quantum);
	}

	public String getNombre() {
		return this.nombre.get();
	}
	public void setNombre(String nombre) {
		this.nombre.set(nombre);
	}

	public int getPrio() {
		return this.prio.get();
	}
	public void setPrio(int prio) {
		this.prio.set(prio);
	}

	public String getTipoPrio() {
		return this.tipoPrio.get();
	}

	public void setTipoPrio(String tipoPrio) {
		this.tipoPrio.set(tipoPrio);
	}

	public int getPid() {
		return this.pid.get();
	}

	public void setPid(int pid) {
		this.pid.set(pid);
	}

	public double getTEjecucion() {
		return this.tEjecucion.get();
	}

	public void setTEjecucion(double tEjecucion) {
		this.tEjecucion.set(tEjecucion);
	}

	public String getInteractividad() {
		return this.interactividad.get();
	}

	public void setInteractividad(String interactividad) {
		this.interactividad.set(interactividad);
	}

	public double getQuantum() {
		return this.quantum.get();
	}

	public void setQuantum(double quantum) {
		this.quantum.set(quantum);
	}

	public String getEstado() {
		return this.estado.get();
	}

	public void setEstado(String estado) {
		this.estado.set(estado);
	}

	@Override
	public String toString() {
		return "Proceso [nombre=" + nombre.get() + ", prio=" + prio.get() + ", tipoPrio=" + tipoPrio.get() + ", pid=" + pid.get()
				+ ", tEjecucion=" + tEjecucion.get() + ", interactividad=" + interactividad.get() + ", quantum=" + quantum.get()
				+ ", estado=" + estado.get() + "]";
	}
}
