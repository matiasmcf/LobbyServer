package paquetes;

import java.io.Serializable;

/**
 * Clase utilizada para la conexion y comunicacion entre cliente y servidor
 */
public abstract class Paquete implements Serializable {

	private static final long serialVersionUID = -6576963960987636722L;

	/**
	 * Devuelve el tipo de paquete.
	 */
	public abstract TipoPaquete getTipo();

	/**
	 * Modifica el tipo de paquete.
	 * 
	 * @see TipoPaquete.java
	 */
	public abstract void setTipo(TipoPaquete tipo);
}
