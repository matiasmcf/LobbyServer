package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Calendar;

import paquetes.Paquete;
import paquetes.PaqueteComunicacion;
import paquetes.PaqueteConexion;
import paquetes.PaqueteSala;
import paquetes.TipoPaquete;
import utils.Resultado;

public class Cliente {

	private Socket				cliente;
	private int					puerto;
	private ObjectOutputStream	outputStream;
	private ObjectInputStream	inputStream;
	private boolean				logged;

	public int getPuerto() {
		return puerto;
	}

	public Cliente(String direccion, int port) throws UnknownHostException, IOException {
		puerto = port;
		cliente = new Socket(direccion, port);
		logged = false;
		outputStream = new ObjectOutputStream(new DataOutputStream(cliente.getOutputStream()));
	}

	public Socket getSocket() {
		return cliente;
	}

	public void cerrarCliente() {
		try {
			if (cliente != null && !cliente.isClosed()) {
				cliente.close();
			}
		}
		catch (IOException e) {
			System.out.println("Error al cerar el cliente");
		}
	}

	public static String horaDelMensaje() {
		Calendar cal = Calendar.getInstance();
		return +cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
	}

	/**
	 * Inicia sesion en el servidor
	 * 
	 * @return En caso de error, se adjuntara una descripcion informando la causa del mismo.
	 * @see Resultado.java
	 */
	public Resultado iniciarSesion(String user, String pass) {
		try {
			// Datos a enviar
			PaqueteConexion paquete = new PaqueteConexion(TipoPaquete.LOGIN, user, pass);
			outputStream.writeObject(paquete);
			outputStream.flush();
			// Datos a recibir
			inputStream = new ObjectInputStream(new DataInputStream(cliente.getInputStream()));
			try {
				paquete = (PaqueteConexion)inputStream.readObject();
				if (paquete.getResultado()) {
					logged = true;
					return new Resultado();
				}
				else
					return new Resultado(paquete.getMotivo());
			}
			catch (ClassCastException e) {
				System.out.println("Error: No se recibio un paquete de Login.");
			}
		}
		catch (EOFException e) {
			System.out.println("Error en la comunicación con el servidor (iniciarSesion)");
			cerrarCliente();
		}
		catch (IOException e) {
			System.out.println("Error: IOException (iniciarSesion)");
			cerrarCliente();
		}
		catch (ClassNotFoundException e1) {
			cerrarCliente();
		}
		System.out.println("CLIENTE (iniciarSesion): NO SE DEBERIA LLEGAR AQUI");
		return new Resultado("NO SE DEBERIA LLEGAR AQUI.");
	}

	/**
	 * Registra el usuario en el servidor
	 * 
	 * @return si el registro fue exitoso o no.
	 */
	public boolean registrarUsuario(String user, String pass) {
		boolean respuesta = false;
		try {
			// Datos a enviar
			PaqueteConexion paquete = new PaqueteConexion(TipoPaquete.REGISTRO, user, pass);
			outputStream.writeObject(paquete);
			outputStream.flush();
			// Datos a recibir
			inputStream = new ObjectInputStream(new DataInputStream(cliente.getInputStream()));
			// inputStream = new ObjectInputStream(new
			// BufferedInputStream(cliente.getInputStream()));
			try {
				paquete = (PaqueteConexion)inputStream.readObject();
				if (paquete.getResultado()) {
					respuesta = true;
				}
			}
			catch (ClassCastException e) {
				System.out.println("Error: No se recibio un paquete de registro de usuario.");
			}
		}
		catch (EOFException e) {
			System.out.println("Error en la comunicación con el servidor (registrarUsuario)");
			cerrarCliente();
		}
		catch (IOException e) {
			System.out.println("Error: IOException (registrarUsuario)");
			cerrarCliente();
		}
		catch (ClassNotFoundException e1) {
			cerrarCliente();
		}
		return respuesta;
	}

	/**
	 * Cierra una sesion de usuario, desconectandose del servidor
	 */
	public void cerrarSesion() {
		try {
			// Datos a enviar
			PaqueteConexion paquete = new PaqueteConexion(TipoPaquete.LOGOUT, "user", "pwd");
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (EOFException e) {
			System.out.println("Error en la comunicación con el servidor (cerrarSesion)");
			cerrarCliente();
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error: IOException (cerrarSesion)");
			cerrarCliente();
		}
	}

	public void enviarPaquete(Paquete paquete) {
		try {
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (EOFException e) {
			System.out.println("Error en la comunicación con el servidor (enviarPaquete)");
		}
		catch (IOException e) {
			System.out.println("Error: IOException (enviarPaquete)");
		}
	}

	/**
	 * Solicita la lista de salas disponibles al servidor.
	 */
	public void buscarSalas() {
		try {
			// Datos a enviar
			PaqueteComunicacion paquete = new PaqueteComunicacion(TipoPaquete.SOLICITUD_LISTA_LOBBY);
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (EOFException e) {
			System.out.println("Error en la comunicación con el servidor (buscarPartidas)");
		}
		catch (IOException e) {
			System.out.println("Error: IOException (buscarPartidas)");
		}
	}

	/**
	 * Solicita la creacion de una sala no protegida.
	 * 
	 * @param nombre
	 */
	public void crearSala(String nombre) {
		PaqueteSala paquete = new PaqueteSala(TipoPaquete.CREAR_SALA, nombre);
		try {
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Solicita la creacion de una sala protegida con contraseña.
	 * 
	 * @param nombre
	 * @param password
	 */
	public void crearSala(String nombre, String password) {
		PaqueteSala paquete = new PaqueteSala(TipoPaquete.CREAR_SALA, nombre, password);
		try {
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Solicita la creacion de una sala no protegida, con la capacidad maxima especificada.
	 * 
	 * @param nombre
	 * @param capacidad
	 */
	public void crearSala(String nombre, int capacidad) {
		PaqueteSala paquete = new PaqueteSala(TipoPaquete.CREAR_SALA, nombre, capacidad);
		try {
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Solicita la creacion de una sala protegida con contraseña, con la capacidad maxima especificada.
	 * 
	 * @param nombre
	 * @param capacidad
	 * @param password
	 */
	public void crearSala(String nombre, String password, int capacidad) {
		PaqueteSala paquete = new PaqueteSala(TipoPaquete.CREAR_SALA, nombre, password, capacidad);
		try {
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia un paquete al servidor, solicitando entrar en la sala seleccionada.
	 * 
	 * @see paquetes.PaqueteComunicacion.java
	 * @param nombre
	 *            Nombre de la sala a la que se desea entrar
	 */
	public void entrarEnSala(String nombre) {
		PaqueteSala paquete = new PaqueteSala(TipoPaquete.UNIRSE_A_SALA, nombre);
		try {
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Indica al servidor que se desea abandonar la sala.
	 */
	public void abandonarSala() {
		PaqueteSala paquete = new PaqueteSala(TipoPaquete.ABANDONAR_SALA);
		try {
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Envia un mensaje al destino seleccionado
	 * 
	 * @param destino
	 * @param mensaje
	 */
	public void enviarMensaje(String destino, String mensaje) {
		PaqueteComunicacion paquete = new PaqueteComunicacion(TipoPaquete.MENSAJE);
		try {
			paquete.setDestino(destino);
			paquete.setMensaje(mensaje);
			outputStream.writeObject(paquete);
			outputStream.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isLogged() {
		return logged;
	}

	public Paquete recibirPaqueteBloqueante() {
		try {
			// TODO Revisar el setSoTimeout. Una vez seteado podría traer conflictos con las llamadas que deben ser bloqueantes.
			cliente.setSoTimeout(0);
			Paquete paq = (Paquete)inputStream.readObject();
			return paq;
		}
		catch (ClassCastException e) {
			return null;
		}
		catch (SocketTimeoutException e) {
			// System.out.println("TimeOut");
			return null;
		}
		catch (ClassNotFoundException e) {
			// System.out.println("Error de serializacion (recibirPaqueteBloqueante)");
			return null;
		}
		catch (IOException e) {
			// System.out.println("Error: IOException (recibirPaqueteBloqueante)");
			return null;
		}
	}

	public Paquete recibirPaqueteNoBloqueante() {
		try {
			// TODO Revisar el setSoTimeout. Una vez seteado podría traer conflictos con las llamadas que deben ser bloqueantes.
			cliente.setSoTimeout(1000); // TODO verificar este tiempo, posiblemente lo pongamos en una Configuracion
			Paquete paq = (Paquete)inputStream.readObject();
			return paq;
		}
		catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
		catch (SocketTimeoutException e) {
			// System.out.println("TimeOut");
			return null;
		}
		catch (ClassNotFoundException e) {
			// System.out.println("Error de serializacion (recibirPaqueteNoBloqueante)");
			return null;
		}
		catch (IOException e) {
			// System.out.println("Error: IOException (recibirPaqueteNoBloqueante)");
			return null;
		}
	}
}
