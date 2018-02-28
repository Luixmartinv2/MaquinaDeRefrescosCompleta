package accesoDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import auxiliares.LeeProperties;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

public class AccesoJDBC implements Datos {

	private String driver, urlbd, user, password; // Datos de la conexion
	private Connection conn1;

	public AccesoJDBC() {
		System.out.println("ACCESO A DATOS - Acceso JDBC");
		
		try {
			HashMap<String,String> datosConexion;
			
			LeeProperties properties = new LeeProperties("Ficheros/config/accesoJDBC.properties");
			datosConexion = properties.getHash();		
			
			driver = datosConexion.get("driver");
			urlbd = datosConexion.get("urlbd");
			user = datosConexion.get("user");
			password = datosConexion.get("password");
			conn1 = null;
			
			Class.forName(driver);
			conn1 = DriverManager.getConnection(urlbd, user, password);
			if (conn1 != null) {
				System.out.println("Conectado a la base de datos");
			} 

		} catch (ClassNotFoundException e1) {
			System.out.println("ERROR: No Conectado a la base de datos. No se ha encontrado el driver de conexion");
			//e1.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		} catch (SQLException e) {
			System.out.println("ERROR: No se ha podido conectar con la base de datos");
			System.out.println(e.getMessage());
			//e.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		}
	}

	public int cerrarConexion() {
		try {
			conn1.close();
			System.out.println("Cerrada conexion");
			return 0;
		} catch (Exception e) {
			System.out.println("ERROR: No se ha cerrado corretamente");
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {

		HashMap<Integer, Deposito> depositosCreados = null;

		Deposito nuevoDep;
		String nombreMoneda;
		int valor;
		int cantidad;

		// CRAR UN ESTATEMENT PARA UNA CONSULTA SELECT
		try {
			depositosCreados = new HashMap<Integer, Deposito>();
			Statement stmt = conn1.createStatement();
			String query = "SELECT * FROM depositos";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				nombreMoneda = rs.getString("nombre");
				valor = Integer.parseInt(rs.getString("valor"));
				cantidad = Integer.parseInt(rs.getString("cantidad"));
				nuevoDep = new Deposito(nombreMoneda, valor, cantidad);
				// Una vez creado el deposito con valor de la moneda y cantidad
				// lo metemos en el hashmap
				depositosCreados.put(valor, nuevoDep);
			}
			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			System.out
					.println("Error leyendo la bbdd: no se ha podido acceder a los datos");
			System.out.println("Fin de la ejecucion del programa");
			System.exit(1);
		}

		System.out.println("Leidos datos de la tabla de Depositos");
		return depositosCreados;

	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = null;
		
		Dispensador nuevoDis;
		String nombre;
		String clave;
		int precio;
		int cantidad;

		// CRAR UN ESTATEMENT PARA UNA CONSULTA SELECT
		try {
			dispensadoresCreados = new HashMap<String, Dispensador>();
			Statement stmt = conn1.createStatement();
			String query = "SELECT * FROM dispensadores";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				nombre = rs.getString("nombre");
				clave = rs.getString("clave");
				//System.out.println(clave);
				precio = Integer.parseInt(rs.getString("precio"));
				cantidad = Integer.parseInt(rs.getString("cantidad"));
				nuevoDis = new Dispensador(clave,nombre, precio, cantidad);
				// Una vez creado el dispensador lo metemos en el hashmap
				dispensadoresCreados.put(clave, nuevoDis);

			}
			rs.close();
			stmt.close();

		} catch (SQLException ex) {
			System.out
					.println("Error leyendo la bbdd: no se ha podido acceder a los datos");
			System.out.println("Fin de la ejecucion del programa");
			System.exit(1);
		}

		System.out.println("Leidos datos de la tabla de Dispensadores");
		return dispensadoresCreados;
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;
		try {

			
			Deposito auxDep;
			// Para que salga ordenado el hashmap de monedas (de stackoverflow)
			SortedSet<Integer> keys = new TreeSet<Integer>(depositos.keySet());
			for (int key : keys) {
				auxDep = (Deposito) depositos.get(key);
				
				Statement stmt = conn1.createStatement();
				String query = "UPDATE depositos SET cantidad = " + auxDep.getCantidad() + " WHERE valor = " + auxDep.getValor() ;
				stmt.executeUpdate(query);

			}
			

		} catch (Exception e) {
			todoOK = false;
			//e.printStackTrace();
		}

		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(
			HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;
		try {
			Dispensador auxDis;
			
			for (HashMap.Entry<String, Dispensador> entry : dispensadores.entrySet()) {
			    auxDis = (Dispensador) entry.getValue();
			    
				Statement stmt = conn1.createStatement();
				String query = "UPDATE dispensadores SET cantidad = " + auxDis.getCantidad() + " WHERE clave = '" + auxDis.getClave() + "'" ;
				System.out.println(query);
				stmt.executeUpdate(query);
			}			

		} catch (Exception e) {
			todoOK = false;
			e.printStackTrace();
		}

		return todoOK;
	}

} // Fin de la clase
