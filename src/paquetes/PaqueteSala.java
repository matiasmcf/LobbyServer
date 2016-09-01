/**
 * 
 */
package paquetes;

import java.util.ArrayList;

import utils.Configuracion;

/**
 * Paquete utilizado para la creacion, entrada y salida de salas.
 * 
 * @author Matías
 */
public class PaqueteSala extends Paquete {

	private static final long	serialVersionUID	= 1L;
	private String				_nombre;
	private String				_password;
	private int					_capacidad;
	private boolean				_protegida;
	private TipoPaquete			_tipo;
	private ArrayList<String>	_usuarios;
	private boolean				_resultado;
	private String				_mensaje;

	/**
	 * Paquete para solicitar ser eliminado de la sala en la que se encuentra actualmente.
	 * 
	 * @param tipo
	 *            ABANDONAR_SALA
	 */
	public PaqueteSala(TipoPaquete tipo) {
		_usuarios = new ArrayList<String>();
		_tipo = tipo;
	}

	/**
	 * Solicita la creacion de una sala sin proteccion, con la capacidad maxima por defecto. <br>
	 * Solicita la union a una sala no protegida.
	 * 
	 * @param tipo
	 *            CREAR_SALA / UNIRSE_A_SALA
	 * @param nombre
	 */
	public PaqueteSala(TipoPaquete tipo, String nombre) {
		_usuarios = new ArrayList<String>();
		_nombre = nombre;
		_tipo = tipo;
		_protegida = false;
		_capacidad = Configuracion.MAX_DEFAULT.getValor();
		_password = new String("");
		// TODO Agregar validacion automatica de tipo de sala, con EXECPTION
	}

	/**
	 * Solicita la creacion de una sala sin proteccion, con la capacidad maxima especificada.
	 * 
	 * @param tipo
	 *            CREAR_SALA
	 * @param nombre
	 * @param capacidad
	 */
	public PaqueteSala(TipoPaquete tipo, String nombre, int capacidad) {
		_usuarios = new ArrayList<String>();
		_nombre = nombre;
		_tipo = tipo;
		_protegida = false;
		_capacidad = capacidad;
		_password = new String();
	}

	/**
	 * Solicita la creacion de una sala protegida con contraseña, con la capacidad maxima por defecto.<br>
	 * Solicita la union a una sala protegida con contraseña.
	 * 
	 * @param tipo
	 *            CREAR_SALA / UNIRSE_A_SALA
	 * @param nombre
	 * @param password
	 */
	public PaqueteSala(TipoPaquete tipo, String nombre, String password) {
		_usuarios = new ArrayList<String>();
		_nombre = nombre;
		_password = password;
		_tipo = tipo;
		_protegida = true;
		_capacidad = Configuracion.MAX_DEFAULT.getValor();
		_password = new String();
	}

	/**
	 * Solicita la creacion de una sala protegida con contraseña, con la capacidad maxima especificada.
	 * 
	 * @param tipo
	 *            CREAR_SALA
	 * @param nombre
	 * @param capacidad
	 * @param password
	 */
	public PaqueteSala(TipoPaquete tipo, String nombre, String password, int capacidad) {
		_usuarios = new ArrayList<String>();
		_nombre = nombre;
		_password = password;
		_tipo = tipo;
		_protegida = true;
		_capacidad = capacidad;
		_password = new String();
	}

	/**
	 * Devuelve el tipo de paquete.
	 */
	public TipoPaquete getTipo() {
		return _tipo;
	}

	/**
	 * Modifica el tipo de paquete.
	 * 
	 * @see TipoPaquete.java
	 */
	public void setTipo(TipoPaquete tipo) {
		_tipo = tipo;
	}

	/**
	 * Devuelve el nombre de la sala.
	 */
	public String getNombre() {
		return _nombre;
	}

	/**
	 * Devuelve la contraseña de la sala.
	 */
	public String getPassword() {
		return _password;
	}

	/**
	 * Devuelve la capacidad de la sala.
	 * 
	 * @return
	 */
	public int getCapacidad() {
		return _capacidad;
	}

	/**
	 * Indica si la sala esta protegida con contraseña o no.
	 */
	public boolean isProtegida() {
		return _protegida;
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
}
