/**
 * 
 */
package utils;

/**
 * Clase que permite confirmar o rechazar una accion, indicando ademas, un motivo.
 * 
 * @author Matías
 */
public class Resultado {

	/** Indica el resultado en si, true o false. **/
	private boolean	_valor;
	/** Permite especificar una descripcion detallada en caso de error. **/
	private String	_motivo;

	/**
	 * Crea una instancia de la clase para informar que una accion no pudo llevarse a cabo, junto con una descripcion detallada del error.
	 * 
	 * @param valor
	 * @param motivo
	 */
	public Resultado(boolean valor, String motivo) {
		_valor = valor;
		_motivo = motivo;
	}

	/**
	 * Crea una instancia de la clase para informar que una accion se llevo a cabo correctamente.
	 * 
	 * @param valor
	 */
	public Resultado(boolean valor) {
		_valor = valor;
		_motivo = new String("Accion exitosa.");
	}

	/**
	 * Informa si la operacion fue exitosa o no.
	 */
	public boolean getValor() {
		return _valor;
	}

	/**
	 * Informa el motivo por el que la accion no pudo llevarse a cabo.
	 */
	public String getMotivo() {
		return _motivo;
	}
}
