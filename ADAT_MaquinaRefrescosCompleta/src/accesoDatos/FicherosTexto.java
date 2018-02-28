package accesoDatos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

/*
 * Todas los accesos a datos implementan la interfaz de Datos
 */

public class FicherosTexto implements Datos {
	
	File fDis; // FicheroDispensadores
	File fDep; // FicheroDepositos

	public FicherosTexto(){
		System.out.println("ACCESO A DATOS - FICHEROS DE TEXTO");
	}
	
	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		
		HashMap<Integer, Deposito> depositosCreados = null;
		FileReader frDep = null;
		BufferedReader br = null;
		
		try {
			fDep = new File("Ficheros\\datos\\depositos.txt");
			depositosCreados = new HashMap<Integer, Deposito>();
			frDep = new FileReader(fDep);
			br = new BufferedReader(frDep);
			
			String linea;
			Deposito nuevoDep;
			String nombreMoneda;
			int valor;
			int cantidad;
			while ((linea = br.readLine()) != null){
				// Creamos depositos
				String[] datosDeposito = linea.split(";");
				if(datosDeposito.length==3){	// Si se leen menos o mas datos es que ha habido un error. ¿Lanzar excepcion?
					nombreMoneda = datosDeposito[0];
					valor = Integer.parseInt(datosDeposito[1]);
					cantidad = Integer.parseInt(datosDeposito[2]);
					nuevoDep = new Deposito(nombreMoneda,valor,cantidad);
					// Una vez creado el deposito con valor de la moneda y cantidad lo metemos en el hashmap
					depositosCreados.put(valor, nuevoDep);
				}
				else{
					throw new Exception();
				}
			}
			
			if (null != frDep) {
				frDep.close();
				br.close();
			}
		
		} catch (Exception e) {
			System.out.println("Error leyendo el fichero: no se ha podido acceder a los datos");
			System.out.println("Fin de la ejecucion del programa");
			System.exit(1);
		}
		
		System.out.println("Leidos datos del fichero Depositos");		
		return depositosCreados;
	}
	

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		
		HashMap<String, Dispensador> dispensadoresCreados = null;
		FileReader frDis = null;
		BufferedReader br = null;
		
		try {
			fDis = new File("Ficheros\\datos\\dispensadores.txt");
			dispensadoresCreados = new HashMap<String, Dispensador>();
			frDis = new FileReader(fDis);
			br = new BufferedReader(frDis);
			
			String linea;
			Dispensador nuevoDis;
			String nombreProducto;
			String clave;
			int precio;
			int cantidad;
			while ((linea = br.readLine()) != null){
				// Creamos dispensadores
				String[] datosDispensadores = linea.split(";");
				if(datosDispensadores.length==4){	// Si se leen menos o mas datos es que ha habido un error. ¿Lanzar excepcion?
					clave = datosDispensadores[0];
					nombreProducto = datosDispensadores[1];
					precio = Integer.parseInt(datosDispensadores[2]);
					cantidad = Integer.parseInt(datosDispensadores[3]);
					nuevoDis = new Dispensador(clave,nombreProducto, precio, cantidad);
					// Una vez creado el dispensador lo metemos en el hashmap
					dispensadoresCreados.put(clave, nuevoDis);
				}
				else{
					throw new Exception();
				}
			}
			
			if (null != frDis) {
				frDis.close();
				br.close();
			}
		
		} catch (Exception e) {
			System.out.println("Error leyendo el fichero: no se ha podido acceder a los datos");
			System.out.println("Fin de la ejecucion del programa");
			System.exit(1);
		}
		
		System.out.println("Leidos datos del fichero Dispensadores");
		return dispensadoresCreados;
		
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		FileWriter fwDep;
		boolean todoOK = true;
		try {
			fwDep = new FileWriter(fDep);
			
			Deposito auxDep;
			String linea;			
			// Para que salga ordenado el hashmap de monedas (de stackoverflow)
			SortedSet<Integer> keys = new TreeSet<Integer>(depositos.keySet());
			for (int key : keys) {
				auxDep = (Deposito) depositos.get(key);
				linea = auxDep.getNombreMoneda()+";"+auxDep.getValor()+";"+auxDep.getCantidad()+"\n";
				fwDep.write(linea);
			}
			
			fwDep.close();
		} catch (Exception e) {
			todoOK = false;
			//e.printStackTrace();
		}

		return todoOK;

	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		FileWriter fwDis;
		boolean todoOK = true;
		try {
			fwDis = new FileWriter(fDis);
			
			String linea;
			String clave;
			Dispensador auxDis;

			for (HashMap.Entry<String, Dispensador> entry : dispensadores.entrySet()) {
			    auxDis = (Dispensador) entry.getValue();
			    clave = entry.getKey();
			    linea = clave+";"+auxDis.getNombreProducto()+";"+auxDis.getPrecio()+";"+auxDis.getCantidad()+"\n";
				fwDis.write(linea);
			}
			
			fwDis.close();
		} catch (Exception e) {
			todoOK = false;
			//e.printStackTrace();
		}

		return todoOK;
	}

} // Fin de la clase
