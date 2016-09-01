package utils;

/**
 * Contiene los distintos parametros configurables del juego
 */
public enum Configuracion {
	TIEMPO_PARTIDA(60), // En segundos
	MAX_JUGADORES_PARTIDA(4),
	PUERTO_INICIAL(5070),
	RANGO_PUERTOS(50),
	MAX_CLIENTES(1),
	MAX_EN_SALA(3),
	SALA_ESPERA("SinAsignar");

	// Nombre de los campos (en orden)
	private int		_valor;
	private String	_cadena;

	// Constructores
	private Configuracion(int valor) {
		_valor = valor;
		_cadena = new String("Sin descripcion");
	}

	private Configuracion(String cadena) {
		_valor = -1;
		_cadena = cadena;
	}

	private Configuracion(String cadena, int valor) {
		_valor = valor;
		_cadena = cadena;
	}

	/**
	 * Devuelve el valor entero de un parametro
	 */
	public int getValor() {
		return _valor;
	}

	/**
	 * Devuelve la cadena que representa la descripcion de un parametro
	 */
	public String getDescripcion() {
		return _cadena;
	}
}
