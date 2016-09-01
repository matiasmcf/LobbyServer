package paquetes;

import java.util.ArrayList;

/**
 * Paquete utilizado para la comunicacion entre cliente/servidor.
 * 
 * @see PaqueteConexion.java
 */
public class PaqueteComunicacion extends Paquete {

	private static final long	serialVersionUID	= -6543131620687160111L;
	private TipoPaquete			_tipo;
	private String				_mensaje;
	private boolean				_resultado;
	private ArrayList<String>	_salas;
	private ArrayList<String>	_usuarios;
	private String				_destino;

	/**
	 * Crea un paquete para la comuncacion entre cliente/servidor
	 * 
	 * @param tipo
	 *            lista de salas/entrar en sala/salir de sala
	 */
	public PaqueteComunicacion(TipoPaquete tipo) {
		_tipo = tipo;
		_resultado = false;
		_mensaje = new String();
		_destino = new String();
		_salas = new ArrayList<String>();
		_usuarios = new ArrayList<String>();
	}

	/**
	 * @return true/false si la operacion fue exitosa o no
	 */
	public boolean getResultado() {
		return _resultado;
	}

	/**
	 * @param resultado
	 *            Confirmacion o rechazo de la solicitud.
	 */
	public void setResultado(boolean resultado) {
		_resultado = resultado;
	}

	/**
	 * Devuelve el tipo de paquete.
	 */
	public TipoPaquete getTipo() {
		return _tipo;
	}

	/**
	 * Obtiene el mensaje almacenado en el paquete.
	 */
	public String getMensaje() {
		return _mensaje;
	}

	/**
	 * @param mensaje
	 *            Mensaje a enviar.
	 */
	public void setMensaje(String mensaje) {
		_mensaje = mensaje;
	}

	/**
	 * @param sala
	 *            Nombre de la sala
	 */
	public void addSala(String sala) {
		_salas.add(sala);
	}

	/**
	 * Devuelve un ArrayList conteniendo los nombres de las salas existentes.
	 */
	public ArrayList<String> getListaSalas() {
		return _salas;
	}

	/**
	 * @param sala
	 *            Nombre de usuario
	 */
	public void addUsuario(String usuario) {
		_usuarios.add(usuario);
	}

	/**
	 * Prepara una lista con los nombres de las salas para enviar al usuario.
	 * 
	 * @param nombres
	 *            ArrayList con los nombres de las salas activas.
	 */
	public void setListaNombresDeSalas(ArrayList<String> nombres) {
		_salas = nombres;
	}

	/**
	 * Prepara una lista con los nombres de los usuarios.
	 * 
	 * @param nombres
	 *            ArrayList con los nombres de los usuarios en la sala.
	 */
	public void setListaNombresDeUsuarios(ArrayList<String> nombres) {
		_usuarios = nombres;
	}

	/**
	 * Devuelve un ArrayList conteniendo los nombres de los usuarios en la sala.
	 */
	public ArrayList<String> getListaUsuarios() {
		return _usuarios;
	}

	/**
	 * Indica el destinatario del mensaje
	 * 
	 * @param usuario
	 *            Nombre del destinatario
	 */
	public void setDestino(String usuario) {
		_destino = usuario;
	}

	/**
	 * Devuelve un String con el nombre del destinatario del mensaje.
	 */
	public String getDestino() {
		return _destino;
	}

	/**
	 * Modifica el tipo de paquete.
	 */
	public void setTipo(TipoPaquete tipo) {
		_tipo = tipo;
	}
}