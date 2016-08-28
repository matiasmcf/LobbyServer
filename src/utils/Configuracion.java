package utils;

/**
 * Contiene los distintos parametros configurables del juego
 */
public enum Configuracion {
	TIEMPO_PARTIDA(60), //En segundos
	MAX_JUGADORES_PARTIDA (4),
	PUERTO_INICIAL (5070),
	RANGO_PUERTOS (50),
	MAX_CLIENTES (1),
	MAX_EN_SALA (3),
	//PACMAN
	PACMAN_VELOCIDAD (2),
	PACMAN_DISTANCIA_PARA_COMER_BOLITA (10),
	PACMAN_PUNTAJE_POR_BOLITA (20),
	PACMAN_PUNTAJE_POR_BOLITA_ESPECIAL (100),
	PACMAN_PUNTAJE_POR_FANTASMA_COMIDO (200),
	PACMAN_PERDIDA_DE_PUNTAJE_POR_MUERTE (10),
	//FANTASMA
	FANTASMA_VELOCIDAD (PACMAN_VELOCIDAD.getValor()/2),
	FANTASMA_PUNTAJE_POR_PACMAN_COMIDO (500),
	FANTASMA_PUNTAJE_POR_FANTASMA_COMIDO (200),
	FANTASMA_PERDIDA_DE_PUNTAJE_POR_MUERTE (10),
	FANTASMA_PERDIDA_DE_PUNTAJE_POR_ATURDIMIENTO (5),
	;
	//Nombre de los campos (en orden)
	private int valor;
	
	//Constructor
	private Configuracion(int v){
		this.valor=v;
	}
	
	/**
	 * Devuelve el valor entero de un parametro
	 */
	public int getValor(){
		return valor;
	}
}
