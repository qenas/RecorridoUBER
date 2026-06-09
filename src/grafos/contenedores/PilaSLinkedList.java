package grafos.contenedores;

import grafos.recursos.Nodo;
import grafos.recursos.OperacionesCL1;

public class PilaSLinkedList implements OperacionesCL1 {
	private Nodo pila;
	
	public PilaSLinkedList() {
		limpiar(); 
	}
	
	public void meter(Object elemento) {
		this.pila = new Nodo(elemento, this.pila);
		// la cabeza es el elemento ingresado!
	} 

	public boolean estaVacia() {
		return (this.pila == null);
	} 
	
	public void limpiar() {
		this.pila = null;
	}
	
	public Object sacar() {
		Object elemento = null;
		if (!estaVacia()){
			elemento = this.pila.getNodoInfo();
			this.pila = this.pila.getNextNodo();
		}else{
			System.out.println("Error sacar. Pila vacia");}
		return elemento; 
	}
	
	public void muestra(){
		Nodo temp;
		if (!estaVacia()){
			temp = this.pila;
			while (temp != null){
				System.out.println("Elemento " + temp.getNodoInfo().toString());
				temp = temp.getNextNodo();
			}
		}else{
			System.out.println("Error muestra. Pila vacia");
		}
	}
	
}
