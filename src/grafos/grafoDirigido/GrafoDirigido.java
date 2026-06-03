package grafos.grafoDirigido;


import grafos.recursos.Connection;

import java.util.Scanner;

public class GrafoDirigido extends AbsGrafoD{
	public GrafoDirigido(int ordenGrafo){
		super(ordenGrafo);
	}


	public int getOrdenSalida(int v) {
		int ordenSalida = 0;
		for(int j = 0; j < getOrden(); j++) {
			if(matrizCosto.areConnected(v, j)) {
				ordenSalida++;
			}
		}
		return ordenSalida;
	}

	public int getOrdenEntrada(int v) {
		int ordenEntrada = 0;
		for(int i = 0; i < getOrden(); i++) {
			if(matrizCosto.areConnected(i, v)) {
				ordenEntrada++;
			}
		}
		return ordenEntrada;
	}

	/*public Arcos getArcos(int v) {
		Arcos arcos = new Arcos();

		int pos = 0;

		for(int i = 0; i < getOrden(); i++) {
			double currCost = (double) matrizCosto.devolver(v, i);
			if(matrizCosto.areConnected(v, i) && currCost != infinito) {
				arcos.insertar(new Connection(v, i, currCost), pos);
				pos++;
			}
		}
		return arcos;
	}

	public Arcos getArcos() {
		Arcos arcos = new Arcos();

		int pos = 0;

		for(int i = 0; i < getOrden(); i++) {
			for(int j = 0; j < getOrden(); j++) {
				double currCost = (double) matrizCosto.devolver(i,j);
				if(matrizCosto.areConnected(i,j) && currCost != infinito) {
					arcos.insertar(new Connection(i, j, currCost), pos);
					pos++;
				}
			}
		}
		return arcos;
	}*/




	@Override
	public void cargarGrafo(){
		double currCost;		
		Scanner scanner = new Scanner(System.in);
		
		for (int i=0; i<getOrden();i++){
			for (int j=0;j<getOrden();j++){
				if (i!=j){
					System.out.println("Ingrese costo[" + i + "," + j + "] (sino -1)");
					currCost=scanner.nextDouble();
					if (currCost!=-1){
						this.matrizCosto.actualizar(currCost, i, j);	
					}else{
						this.matrizCosto.actualizar(infinito, i, j);
					}					
				}else{
					this.matrizCosto.actualizar(infinito, i, j);
				}
			}
		} 
   	
	}

	@Override
	public void actualizarArista(double costo, int i, int j) {
		this.matrizCosto.actualizar(costo, i, j);
	}

	public double getArista(int i, int j) {
		return (double) this.matrizCosto.devolver(i, j);
	}

}
