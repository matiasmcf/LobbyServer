package paquetes;

/**
 * Paquete utilizado para la conexion/desconexion con el servidor, incluyendo el registro/eliminacion de usuarios
 */
public class PaqueteConexion extends Paquete {

	private static final long	serialVersionUID	= -6543131620687160111L;
	private TipoPaquete			_tipo;
	private String				_usuario;
	private String				_password;
	private String				_motivo;
	private boolean				_resultado;

	/**
	 * Crea un paquete para administrar la conexion con el servidor
	 * 
	 * @param tipo
	 *            login/logout/registro
	 * @param usuario
	 *            nombre de usuario
	 * @param password
	 *            contraseña
	 */
	public PaqueteConexion(TipoPaquete tipo, String usuario, String password) {
		_tipo = tipo;
		_usuario = usuario;
		_password = password;
		_resultado = false;
		_motivo = new String();
	}

	/**
	 * @return nombre de usuario
	 */
	public String getNombreUsuario() {
		return _usuario;
	}

	/**
	 * @return contraseña del usuario
	 */
	public String getPassword() {
		return _password;
	}

	/**
	 * @return si la operacion fue exitosa o no
	 */
	public boolean getResultado() {
		return _resultado;
	}

	/**
	 * @param resultado:
	 *            confirmacion o rechazo de la solicitud
	 */
	public void setResultado(boolean resultado) {
		_resultado = resultado;
	}

	/**
	 * @return tipo de paquete/solicitud
	 */
	public TipoPaquete getTipo() {
		return _tipo;
	}

	/**
	 * @return motivo por el que la solicitud fue rechazada
	 */
	public String getMotivo() {
		return _motivo;
	}

	/**
	 * @param motivo
	 *            por el que se rechazo la solicitud
	 */
	public void setMotivo(String motivo) {
		_motivo = motivo;
	}

	/**
	 * Modifica el tipo de paquete.
	 */
	public void setTipo(TipoPaquete tipo) {
		_tipo = tipo;
	}

}