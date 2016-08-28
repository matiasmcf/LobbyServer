package server;

import java.util.ArrayList;

/**
 * Clase que permite agrupar usuarios e intercambiar mensajes exclusivamente con usuarios del mismo grupo
 * 
 * @author Matías
 */
public class Sala {

	private ArrayList<User>	_usuarios;
	private String			_nombre;
	private int				_slots;

	/**
	 * Crea una sala de espera con chat exclusivo
	 * 
	 * @param nombre
	 *            de la sala
	 * @param slots
	 *            (cantidad de usuarios que pueden entrar en la sala)
	 */
	public Sala(String nombre, int slots) {
		_nombre = nombre;
		_usuarios = new ArrayList<User>();
		_slots = slots;
	}

	/**
	 * Agrega un usuario a la sala
	 * 
	 * @param usuario
	 */
	public boolean agregarUsuario(User usuario) {
		if (_usuarios.size() >= _slots)
			return false;
		_usuarios.add(usuario);
		if (usuario.getSala() != null)
			usuario.getSala().removerUsuario(usuario);
		usuario.setSala(this);
		return true;
	}

	/**
	 * Elimina un usuario de la sala
	 * 
	 * @param usuario
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

	@Override
	public String toString() {
		return _nombre;
	}
}
