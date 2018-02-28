package accesoDatos;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import auxiliares.LeeProperties;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;

/*
 * Todas los accesos a datos implementan la interfaz de Datos
 */

public class AccesoJSONRemoto implements Datos {

	private String USER_AGENT, SERVER_PATH, GET_DEP, GET_DIS, userName; // Datos de la conexion

	public AccesoJSONRemoto(){
		System.out.println("ACCESO A DATOS - Acceso JSON Remoto");
		
		try {
			HashMap<String,String> datosConexion;
			
			LeeProperties properties = new LeeProperties("Ficheros/config/acceso_JSON_REMOTO.properties");
			datosConexion = properties.getHash();		
			
			USER_AGENT = datosConexion.get("USER_AGENT");
			SERVER_PATH = datosConexion.get("SERVER_PATH");
			GET_DEP = datosConexion.get("GET_DEP");
			GET_DIS = datosConexion.get("GET_DIS");

			System.out.println("LEIDAS PROPIEDADES DE CONEXION JSON REMOTO");
			
		} catch (Exception e) {
			System.out.println("ERROR: No se han leido las propiedades de conexión");
			//e1.printStackTrace();
			System.out.println("No se ha podido inicializar la maquina\n Finaliza la ejecucion");
			System.exit(1);
		}
		
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		HashMap<Integer, Deposito> depositosCreados = new HashMap<Integer, Deposito>();
		// Creamos un objeto JSON
		JSONObject jsonObj = new JSONObject();
		// Añadimos nombre del usuario
		jsonObj.put("user", userName);

		// Generamos el String JSON
		String jsonString = JSONValue.toJSONString(jsonObj);
		// System.out.println("JSON GENERADO:");
		// System.out.println(jsonString);
		System.out.println("");

		try {
			// Codificar el json a URL
			jsonString = URLEncoder.encode(jsonString, "UTF-8");
			// Generar la URL
			String url = SERVER_PATH + GET_DEP;
			// Creamos un nuevo objeto URL con la url donde queremos enviar el
			// JSON
			URL obj = new URL(url);
			// Creamos un objeto de conexion
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// Añadimos la cabecera
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			// Creamos los parametros para enviar
			String urlParameters = "json=" + jsonString;
			// Enviamos los datos por POST
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			// Capturamos la respuesta del servidor
			int responseCode = con.getResponseCode();
			// System.out.println("\nSending 'POST' request to URL : " + url);
			// System.out.println("Post parameters : " + urlParameters);
			// System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			//Mostramos la respuesta del servidor por consola
			//System.out.println("RESPUESTA JSON: " + response.toString());
			//System.exit(1);	
			// cerramos la conexion
			in.close();

			// Crear un Array JSON a partir del string JSON
			
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());
			
			JSONArray arrayDepositos = (JSONArray) respuesta.get("depositos");

			// Declaramos variables
			Deposito nuevoDep;
			String nombreMoneda;
			int valor;
			int cantidad;
			// Iterar el array y extraer la informacion
			for (int i = 0; i < arrayDepositos.size(); i++) {
				JSONObject row = (JSONObject) arrayDepositos.get(i);
				
				nombreMoneda = row.get("nombre").toString();
				valor = Integer.parseInt(row.get("valor").toString());
				cantidad = Integer.parseInt(row.get("cantidad").toString());
				nuevoDep = new Deposito(nombreMoneda,valor,cantidad);
				// Una vez creado el deposito con valor de la moneda y cantidad lo metemos en el hashmap
				depositosCreados.put(valor, nuevoDep);				
				
			}			

		} catch (Exception e) {
			
			System.out.println("Error leyendo el JSON de depositos: no se ha podido acceder a los datos");
			System.out.println("Fin de la ejecucion del programa");
			
			e.printStackTrace();
			
			System.exit(1);	
			
		}

		System.out.println("Leidos datos del JSON Depositos");		
		return depositosCreados; 

	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		
		HashMap<String, Dispensador> dispensadoresCreados = new HashMap<String, Dispensador>();;
		// Creamos un objeto JSON
		JSONObject jsonObj = new JSONObject();
		// Añadimos nombre Usuario
		jsonObj.put("user", userName);

		// Generamos el String JSON
		String jsonString = JSONValue.toJSONString(jsonObj);
		// System.out.println("JSON GENERADO:");
		// System.out.println(jsonString);
		System.out.println("");

		try {
			// Codificar el json a URL
			jsonString = URLEncoder.encode(jsonString, "UTF-8");
			// Generar la URL
			String url = SERVER_PATH + GET_DIS;
			// Creamos un nuevo objeto URL con la url donde queremos enviar el
			// JSON
			URL obj = new URL(url);
			// Creamos un objeto de conexion
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// Añadimos la cabecera
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			// Creamos los parametros para enviar
			String urlParameters = "json=" + jsonString;
			// Enviamos los datos por POST
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			// Capturamos la respuesta del servidor
			int responseCode = con.getResponseCode();
			// System.out.println("\nSending 'POST' request to URL : " + url);
			// System.out.println("Post parameters : " + urlParameters);
			// System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			//Mostramos la respuesta del servidor por consola
			//System.out.println(response.toString());
			// cerramos la conexion
			in.close();


			// Obtenemos los dispensadores del json leido
			
			JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());
			
			JSONArray arrayDispensadores = (JSONArray) respuesta.get("dispensadores");
			
			Dispensador nuevoDis;
			String nombreProducto;
			String clave;
			int precio;
			int cantidad;			
			
			// Iterar el array y extraer la informacion
			for (int i = 0; i < arrayDispensadores.size(); i++) {
				JSONObject row = (JSONObject) arrayDispensadores.get(i);
				
				clave = row.get("clave").toString();
				nombreProducto = row.get("nombre").toString();
				precio = Integer.parseInt(row.get("precio").toString());
				cantidad = Integer.parseInt(row.get("cantidad").toString());
				nuevoDis = new Dispensador(clave,nombreProducto, precio, cantidad);
				// Una vez creado el dispensador lo metemos en el hashmap
				dispensadoresCreados.put(clave, nuevoDis);				
				
				//nombreMoneda = row.get("nombre").toString();

				
			}			


		} catch (Exception e) {
			
			System.out.println("Error leyendo el JSON de dispensadores: no se ha podido acceder a los datos");
			System.out.println("Fin de la ejecucion del programa");
			
			e.printStackTrace();
			
			System.exit(1);	
			
		}

		System.out.println("Leidos datos del JSON Dispensadores");		
		return dispensadoresCreados; 
		
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		System.out.println("Opcion guardar datos de Depositos no disponible por el momento");
		return false;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		System.out.println("Opcion guardar datos de Dispensadores no disponible por el momento");
		return false;
	}

} // Fin de la clase
