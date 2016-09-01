package paquetes;

/**
 * Define los posibles tipos de Paquetes
 */
public enum TipoPaquete {
	// PaqueteConexion
	LOGIN,
	LOGOUT,
	REGISTRO,
	// PaqueteComunicacion
	MENSAJE,
	SERVIDOR_CERRADO,
	SOLICITUD_LISTA_LOBBY,
	// PaqueteSala
	ABANDONAR_SALA,
	UNIRSE_A_SALA,
	CREACION_DE_SALA;
}