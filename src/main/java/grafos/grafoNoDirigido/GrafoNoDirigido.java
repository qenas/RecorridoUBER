package grafos.grafoNoDirigido;


import java.util.Scanner;

public class GrafoNoDirigido extends AbsGrafoND{
	public GrafoNoDirigido(int ordenGrafo){
		super(ordenGrafo);
	}

	public int getGrado(int v) { // grado de vertice v
		int grado = 0;
		for(int i = 0; i < getOrden(); i++) {
			if(matrizCosto.areConnected(v,i)) { //verifica la matriz de adyacencia de ese vertice
				grado++;
			}
		}
		return grado;
	}

	public int getGrado() { // grado del grafo
		int maxGrado = 0;
		for(int i = 0; i < getOrden(); i++) {
			if(getGrado(i) > maxGrado) {
				maxGrado = getGrado(i);
			}
		}
		return maxGrado;
	}

	/*public Aristas getAristas(int v) {
		Aristas aristasAdyacentes = new Aristas();
		int pos = 0;
		for(int i = 0; i < getOrden(); i++) {
			double currCost = (double) matrizCosto.devolver(v,i);
			if(matrizCosto.areConnected(v, i) && currCost != infinito) {
				aristasAdyacentes.insertar(new Connection(v, i, currCost), pos);
				pos++;
			}
		}

		return aristasAdyacentes;
	}

	public Aristas getAristas() {
		Aristas aristas = new Aristas();

		int pos = 0;

		for(int i = 0; i < getOrden(); i++) {
			for(int j = i + 1; j < getOrden(); j++) {
				double currCost = (double) matrizCosto.devolver(i,j);
				if(matrizCosto.areConnected(i,j) && currCost != infinito) {
					aristas.insertar(new Connection(i, j, currCost), pos);
				}
			}
		}

		return aristas;
	}
	*/
	
	@Override
	public void cargarGrafo(){
		double currCost;		
		Scanner scanner = new Scanner(System.in);
		
		for (int i=0; i<getOrden();i++){
			for (int j=i;j<getOrden();j++){
				if (i!=j){
					System.out.println("Ingrese costo[" + i + "," + j + "] (sino -1)");
					currCost=scanner.nextDouble();
					if (currCost!=-1){
						this.matrizCosto.actualizar(currCost, i, j);
						this.matrizCosto.actualizar(currCost, j, i);
					}else{
						this.matrizCosto.actualizar(infinito, i, j);
						this.matrizCosto.actualizar(infinito, j, i);
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
		this.matrizCosto.actualizar(costo, j, i);
	}
}
