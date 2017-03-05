/**
 * 
 */
package utils;

/**
 * Clase que permite si una accion pudo llevarse a cabo o no, indicando ademas un motivo.
 * 
 */
public class Resultado {

	/** Indica el resultado en si, true o false. **/
	private boolean	_valor;
	/** Permite especificar una descripcion detallada en caso de error. **/
	private String	_descripcion;

	/**
	 * Crea una instancia de la clase para informar que una accion no pudo llevarse a cabo, junto con una descripcion detallada del error.
	 * 
	 * @param descripcion
	 *            Informacion detallada del error.
	 */
	public Resultado(String descripcion) {
		_valor = false;
		_descripcion = descripcion;
	}

	/**
	 * Crea una instancia de la clase para informar el resultado de una operacion.
	 * 
	 * @param valor
	 *            Resultado de la operacion.
	 * 
	 * @param mensaje
	 *            Informacion adicional.
	 */
	public Resultado(boolean valor, String mensaje) {
		_valor = valor;
		_descripcion = mensaje;
	}

	/**
	 * Crea una instancia de la clase para informar un resultado exitoso.
	 */
	public Resultado() {
		_valor = true;
		_descripcion = "Acción exitosa.";
	}

	/**
	 * Informa si la operacion fue exitosa o no.
	 */
	public boolean getValor() {
		return _valor;
	}

	/**
	 * Brinda informacion adicional acerca del resultado.
	 */
	public String getDescripcion() {
		return _descripcion;
	}
}
