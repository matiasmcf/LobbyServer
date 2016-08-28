package database;

import java.sql.*;

import javax.swing.JOptionPane;

public class SQLiteDatabase {
	private String databaseURL="jdbc:sqlite:resources/database/database.sqlite";
	/**
	 * Objeto que permite conectarse a una base de datos y realizar consultas sobre la misma
	 */

	/**
	 * Ejecuta una consulta en la base de datos
	 * @param query (consulta)
	 * @return si la cunsulta fue exitosa o no.
	 */
	public boolean consultar(String query){
		boolean estado = false;
		try{
			Class.forName("org.sqlite.JDBC");
			Connection conexion = DriverManager.getConnection(databaseURL);
			PreparedStatement pst = conexion.prepareStatement(query);
			ResultSet rs = pst.executeQuery();	
			if(!rs.next())
				estado = false;
			else
				estado = true;
			pst.close();
			rs.close();
			conexion.close();
		}
		catch(SQLException ex){
			JOptionPane.showMessageDialog(null,"No se pudo lograr la conexion con la base de datos","Error",JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return estado;
	}
	
	/**
	 * @param usuario
	 * @param password
	 * @return Si la combinacion de usuario + password es valida o no.
	 */
	public boolean verificarDatos(String usuario, String password){
		String query = "SELECT * FROM USUARIO WHERE Usuario='"+usuario+"' AND Password='"+password+"'";
		boolean estado= false;
		try{
			estado =consultar(query);
		}
		catch(Exception ex){
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null,"Error usuario y contraseña","Error",JOptionPane.ERROR_MESSAGE);
		}
		return estado;
	}
	
	/**
	 * @param usuario
	 * @return si el usuario existe o no en la base de datos.
	 */
	private boolean existeUsuario(String usuario){
		String query = "SELECT * FROM USUARIO WHERE Usuario='"+usuario+"'";
		boolean estado= false;
		try{
			estado = consultar(query);
		}
		catch(Exception ex){
			JOptionPane.showMessageDialog(null,"Error al comprobar existencia usuario","Error",JOptionPane.ERROR_MESSAGE);
		}
		return estado;
	}
	
	/**
	 * Registra un usuario en la base de datos
	 * @param usuario
	 * @param password
	 */
	public boolean registrarUsuario( String usuario, String password) {
		if(existeUsuario(usuario)==true)
			return false;
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conexion = DriverManager.getConnection(databaseURL);
			Statement st=conexion.createStatement();
			String query="Insert into Usuario values('"+usuario+"','"+password+"')";
			st.executeUpdate(query);
			st.close();
			conexion.close();
			return true;
			} 
		catch(SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null,"No se pudo lograr la coneccion con la base de datos","Error",JOptionPane.ERROR_MESSAGE);
			return false;
			} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
		
	public boolean registrarScore( String usuario ,Date fecha,String personaje,int puntuacion,Time duracion ,int muertes) {
		if(existeUsuario(usuario)==true)
			return false;
		try {
			Class.forName("org.sqlite.JDBC");
			Connection conexion = DriverManager.getConnection(databaseURL);
			PreparedStatement st=conexion.prepareStatement("Insert into Score values(?, ?, ?, ?, ?, ?)");
			st.setString(1, usuario);
			st.setDate(2, fecha);
			st.setString(3, personaje);
			st.setInt(4, puntuacion);
			st.setTime(5, duracion);
			st.setInt(6, muertes);
			st.execute();
			st.close();
			conexion.close();
			return true;
			} 
		catch(SQLException sqle) {
			sqle.printStackTrace();
			JOptionPane.showMessageDialog(null,"No se pudo lograr la coneccion con la base de datos","Error",JOptionPane.ERROR_MESSAGE);
			return false;
			} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

}
