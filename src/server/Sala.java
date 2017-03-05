package server;

import java.util.ArrayList;

import utils.Configuracion;
import utils.Resultado;

/**
 * Clase que permite agrupar usuarios e intercambiar mensajes exclusivamente con usuarios del mismo grupo
 * 
 */
public class Sala {

	private ArrayList<User>	_usuarios;
	private String			_nombre;
	private String			_password;
	private boolean			_protegida;
	private int				_capacidad;
	private boolean			_permanente;

	/**
	 * Crea una sala con chat exclusivo. Las salas no permanentes seran eliminadas automaticamente al quedar vacias.
	 * 
	 * @param nombre
	 *            Nombre de la sala.
	 * @param slots
	 *            Cantidad de usuarios que pueden entrar en la sala. Un numero menor o igual que 0 (cero) indica que se establecera la cantidad maxima por defecto.
	 * @param permanente
	 *            Indica si la sala fue creada por el servidor.
	 * @see Configuracion.java
	 */
	public Sala(String nombre, int slots, boolean permanente) {
		_nombre = nombre;
		_usuarios = new ArrayList<User>();
		_permanente = permanente;
		if (slots <= 0)
			_capacidad = Configuracion.MAX_DEFAULT.getValor();
		else
			_capacidad = slots;
	}

	/**
	 * Crea una sala con chat exclusivo. Las salas creadas por usuarios seran eliminadas automaticamente al quedar vacias.
	 * 
	 * @param nombre
	 *            Nombre de la sala.
	 * @param contraseña
	 *            Contraseña de la sala.
	 * @param slots
	 *            Cantidad de usuarios que pueden entrar en la sala. Un numero menor o igual que 0 (cero) indica que se establecera la cantidad maxima por defecto.
	 * @see Configuracion.java
	 */
	public Sala(String nombre, String password, int slots) {
		_nombre = nombre;
		_password = password;
		_protegida = true;
		_permanente = false;
		_usuarios = new ArrayList<User>();
		if (slots <= 0)
			_capacidad = Configuracion.MAX_DEFAULT.getValor();
		else
			_capacidad = slots;
	}

	/**
	 * Agrega un usuario a la sala
	 * 
	 * @param usuario
	 *            Usuario a agregar.
	 * @return En caso de error, se adjuntara una descripcion informando la causa del mismo.
	 */
	public Resultado agregarUsuario(User usuario) {
		if (_protegida == true)
			return new Resultado("Se requiere una contraseña para acceder a la sala.");
		if (_usuarios.size() >= _capacidad)
			return new Resultado("Superada la cantidad maxima de usuarios permitida en la sala.");
		if (usuario.getSala() != null)
			usuario.getSala().removerUsuario(usuario);
		usuario.setSala(this);
		_usuarios.add(usuario);
		return new Resultado();
	}

	/**
	 * Agrega un usuario a la sala protegida por contraseña.
	 * 
	 * @param usuario
	 *            Usuario a agregar.
	 * @param password
	 *            Contraseña de la sala.
	 * @return En caso de error, se adjuntara una descripcion informando la causa del mismo.
	 * @see Resultado.java
	 */
	public Resultado agregarUsuario(User usuario, String password) {
		if (!_password.equals(password))
			return new Resultado("Contraseña incorrecta.");
		if (_usuarios.size() >= _capacidad)
			return new Resultado("Superada la cantidad maxima de usuarios permitida en la sala.");
		if (usuario.getSala() != null)
			usuario.getSala().removerUsuario(usuario);
		usuario.setSala(this);
		_usuarios.add(usuario);
		return new Resultado();
	}

	/**
	 * Elimina un usuario de la sala. Se recomienda revisar la cantidad de usuarios restantes en la sala para decidir si eliminarla o no.
	 * 
	 * @param usuario
	 *            Usuario a remover.
	 */
	public void removerUsuario(User usuario) {
		_usuarios.remove(usuario);
	}

	/**
	 * Devuelve el nombre de la sala.
	 */
	public String obtenerNombre() {
		return _nombre;
	}

	/**
	 * Devuelve un ArrayList con los usuarios en la sala.
	 */
	public ArrayList<User> getUsuarios() {
		return _usuarios;
	}

	/**
	 * Devuelve un ArrayList con los nombres de los usuarios en la sala.
	 */
	public ArrayList<String> getListaNombres() {
		ArrayList<String> nombres = new ArrayList<String>();
		nombres.add(new String("[Todos]"));
		for (User u: _usuarios) {
			nombres.add(u.getNombre());
		}
		return nombres;
	}

	/**
	 * Indica si la sala esta vacia.
	 */
	public boolean isEmpty() {
		return _usuarios.size() == 0;
	}

	/**
	 * Devuelve la cantidad actual de usuarios en la sala.
	 */
	public int getCantUsuarios() {
		return _usuarios.size();
	}

	/**
	 * Indica si la sala se debe eliminar, es decir, si fue creada por un usuario y ademas esta vacia.
	 */
	public boolean debeEliminarse() {
		return _permanente == false && _usuarios.size() == 0;
	}

	@Override
	public String toString() {
		return _nombre;
	}
}
