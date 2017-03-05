package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import database.SQLiteDatabase;
import utils.Configuracion;
import utils.Resultado;

/**
 * Objeto que permite aceptar y manejar conexiones
 */
public class Server {

	private ServerSocket	servidor;
	private Socket			cliente;
	private int				max_clientes;
	private int				puerto;
	private String			nombreHost;
	private String			IPHost;
	private SQLiteDatabase	database;
	private ServerWindow	serverWindow;
	private ArrayList<User>	usuarios;
	private ArrayList<Sala>	salas;

	/**
	 * Crea un nuevo servidor en un puerto determinado, con un maximo de clientes a manejar, y la ventana principal, donde escribirá su lista de nombres.
	 * 
	 * @param port
	 * @param max_conexiones
	 */
	public Server(int port, int max_conexiones, ServerWindow serverWindow) throws IOException {
		nombreHost = InetAddress.getLocalHost().getHostName().toString();
		IPHost = InetAddress.getLocalHost().getHostAddress().toString();
		this.serverWindow = serverWindow;
		puerto = port;
		max_clientes = max_conexiones;
		database = new SQLiteDatabase();
		servidor = new ServerSocket(puerto);
		usuarios = new ArrayList<User>();
		salas = new ArrayList<Sala>();
	}

	/**
	 * Devuelve el nombre del servidor
	 */
	public String getNombreHost() {
		return nombreHost;
	}

	/**
	 * Devuelve el ip del servidor
	 */
	public String getIPHost() {
		return IPHost;
	}

	/**
	 * Devuelve la cantidad maxima de clientes que maneja el servidor
	 */
	public int getMax_clientes() {
		return max_clientes;
	}

	/**
	 * Devuelve el puerto de escucha del servidor
	 */
	public int getPuerto() {
		return puerto;
	}

	/**
	 * Elimina una sala del servidor
	 * 
	 * @param sala
	 *            a eliminar
	 */
	public boolean eliminarSala(Sala sala) {
		if (sala.obtenerNombre().equals(Configuracion.SALA_ESPERA.getDescripcion()))
			return false;
		return salas.remove(sala);
	}

	/**
	 * Acepta una conexion
	 * 
	 * @return Socket con la informacion del cliente
	 */
	public Socket aceptarConexion() {
		cliente = null;
		try {
			servidor.setSoTimeout(1000);
			cliente = servidor.accept();
		}
		catch (SocketTimeoutException e) {
			return null;
		}
		catch (IOException e) {
			System.out.println("Error al aceptar conexiones, Cerrando el Servidor...");
			System.exit(-1);
		}
		return cliente;
	}

	/**
	 * Detiene el servidor
	 */
	public void pararServidor() {
		try {
			if (!servidor.isClosed())
				servidor.close();
		}
		catch (IOException e) {
			System.out.println("Error al cerrar el servidor");
		}
	}

	/**
	 * Actualiza el contador de clientes conectados, y lo elimina de la lista de nombres del MainWindow
	 */
	public void eliminarCliente(User usuario) {
		if (!usuario.getSocket().isClosed()) {
			try {
				usuario.getSocket().close();
				usuarios.remove(usuario);
				if (usuario.getSala() != null)
					usuario.getSala().removerUsuario(usuario);
				serverWindow.actualizarListaDeNombres();
			}
			catch (IOException e) {
				System.out.println("El socket del cliente " + usuario.getNombre() + " ya estaba cerrado.");
			}
		}

	}

	/**
	 * Devuelve el objeto DataBase utilizado por el servidor
	 */
	public SQLiteDatabase getDatabase() {
		return database;
	}

	/**
	 * Agrega un usuario al servidor y lo coloca en la sala de espera.
	 * 
	 * @param usuario
	 * @return En caso de error, se adjuntara una descripcion informando la causa del mismo.
	 * @see Resultado.java
	 */
	public Resultado agregarUsuario(User usuario) {
		usuarios.add(usuario);
		if (usuarios.size() > max_clientes) {
			return new Resultado("El servidor ha alcanzado el limite de clientes conectados.");
		}
		for (Sala l: salas) {
			if (l.obtenerNombre().equals(Configuracion.SALA_ESPERA.getDescripcion())) {
				return l.agregarUsuario(usuario);
			}
		}
		return new Resultado("No se encuentra la sala de espera.");
	}

	/**
	 * Crea una sala con los parametros especificados
	 * 
	 * @param nombre
	 * @param password
	 * @param capacidad
	 * @param permanente
	 * @return La sala creada, en caso contrario, devuelve null
	 */
	public Sala crearSala(String nombre, String password, int capacidad, boolean permanente) {
		boolean existe = false;
		for (Sala s: salas) {
			if (s.obtenerNombre().equals(nombre)) {
				existe = true;
				break;
			}
		}
		if (existe)
			return null;
		Sala s;
		if (password.equals(""))
			s = new Sala(nombre, capacidad, permanente);
		else
			s = new Sala(nombre, password, capacidad);
		salas.add(s);
		return s;
	}

	/**
	 * @return lista de usuarios en el servidor
	 */
	public ArrayList<User> getListaUsuarios() {
		return usuarios;
	}

	public int getCantidadUsuariosConectados() {
		return usuarios.size();
	}

	/**
	 * @return lista de salas en el servidor
	 */
	public ArrayList<Sala> getSalas() {
		return salas;
	}

	/**
	 * Devuelve ArrayList con los nombres de las salas activas.
	 */
	public ArrayList<String> getListaNombresDeSalas() {
		ArrayList<String> nombres = new ArrayList<String>();
		for (Sala s: salas) {
			if (!s.toString().equals(Configuracion.SALA_ESPERA.getDescripcion()))
				nombres.add(s.toString() + " " + s.getCantUsuarios());
		}
		return nombres;
	}

	/**
	 * Agrega un usuario a la sala seleccionada.
	 * 
	 * @param usuario
	 * @param sala
	 * @return En caso de error, se adjuntara una descripcion informando la causa del mismo.
	 */
	public Resultado agregarASala(User usuario, String sala) {
		for (Sala s: salas) {
			if (s.obtenerNombre().equals(sala))
				return s.agregarUsuario(usuario);
		}
		return new Resultado("Sala inexistente.");
	}
}