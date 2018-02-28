package accesoDatos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Query;
import org.hibernate.Session;

import auxiliares.SessionManager;
import logicaRefrescos.Deposito;
import logicaRefrescos.Dispensador;


public class AccesoHibernate implements Datos {

	SessionManager manager;
	
	public AccesoHibernate(){
		System.out.println("ACCESO A DATOS - HIBERNATE");		
		manager = new SessionManager();
	}

	@Override
	public HashMap<Integer, Deposito> obtenerDepositos() {
		
		HashMap<Integer, Deposito> depositosCreados = null;
		
		try {
			
	    	Session session = manager.createSessionFactory().openSession();
	    	
	        Query q= session.createQuery("SELECT dep FROM Deposito dep");
	        List results = q.list();
	        
	        Iterator depositosIterator= results.iterator();
	        
			depositosCreados = new HashMap<Integer, Deposito>();
			int valor;
	        
	        while (depositosIterator.hasNext()){
	            Deposito nuevoDep = (Deposito)depositosIterator.next();
				valor = nuevoDep.getValor();
				depositosCreados.put(valor, nuevoDep);
	        }
	 
	        session.close(); 			
			
		} catch (Exception e) {
			System.out.println("Error leyendo la BD utilizando hibernate (depositos): no se ha podido acceder a los datos");
			System.out.println("Fin de la ejecucion del programa");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Leidos datos de depositos (usando Hibernate)");		
		return depositosCreados; 
	}

	@Override
	public HashMap<String, Dispensador> obtenerDispensadores() {
		HashMap<String, Dispensador> dispensadoresCreados = null;
		
		try {
			
	    	Session session = manager.createSessionFactory().openSession();
	    	
	        Query q= session.createQuery("SELECT dis FROM Dispensador dis");
	        List results = q.list();
	        
	        Iterator dispensadoresIterator= results.iterator();
	        
			dispensadoresCreados = new HashMap<String, Dispensador>();
			String clave;
	        
	        while (dispensadoresIterator.hasNext()){
	            Dispensador nuevoDis = (Dispensador)dispensadoresIterator.next();
				clave = nuevoDis.getClave();
				dispensadoresCreados.put(clave, nuevoDis);
	        }
	 
	        session.close(); 			
			
		} catch (Exception e) {
			System.out.println("Error leyendo la BD utilizando hibernate (dispensadores): no se ha podido acceder a los datos");
			System.out.println("Fin de la ejecucion del programa");
			System.exit(1);
		}

		System.out.println("Leidos datos de dispensadores (usando Hibernate)");		
		return dispensadoresCreados; 
	}

	@Override
	public boolean guardarDepositos(HashMap<Integer, Deposito> depositos) {
		boolean todoOK = true;
		
		try {
			
	    	Session session = manager.createSessionFactory().openSession();

			Deposito auxDep;	
			session.beginTransaction();
			for (HashMap.Entry<Integer, Deposito> entry : depositos.entrySet()) {
			    auxDep = (Deposito) entry.getValue();
			    session.saveOrUpdate(auxDep);
			}	
			session.getTransaction().commit();
		} catch (Exception e) {
			todoOK = false;
			//e.printStackTrace();
		}
	    	
		return todoOK;
	}

	@Override
	public boolean guardarDispensadores(HashMap<String, Dispensador> dispensadores) {
		boolean todoOK = true;

		try {
	    	
			Session session = manager.createSessionFactory().openSession();

			Dispensador auxDis;	
			session.beginTransaction();
			for (HashMap.Entry<String, Dispensador> entry : dispensadores.entrySet()) {
			    auxDis = (Dispensador) entry.getValue();
			    session.saveOrUpdate(auxDis);
			}
			session.getTransaction().commit();
		} catch (Exception e) {
			todoOK = false;
			//e.printStackTrace();
		}		
		
		return todoOK;
	}
	
}
