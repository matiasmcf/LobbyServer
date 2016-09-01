package server;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

import database.SQLiteDatabase;
import paquetes.Paquete;
import paquetes.PaqueteComunicacion;
import paquetes.PaqueteConexion;
import paquetes.TipoPaquete;
import utils.Configuracion;

public class ThreadServer extends Thread {

	private Server			servidor;
	private SQLiteDatabase	database;
	private User			user;
	private boolean			running;

	public ThreadServer(Server servidor, User usuario) {
		this.servidor = servidor;
		this.database = servidor.getDatabase();
		usuario.setNombre("");
		user = usuario;
		running = true;
	}

	public synchronized void run() {
		try {
			ObjectInputStream is = new ObjectInputStream(new DataInputStream(user.getSocket().getInputStream()));
			while (running) {
				Paquete paquete = (Paquete) is.readObject();
				PaqueteConexion paqCon;
				PaqueteComunicacion paqComu;
				if (!user.getSocket().isClosed()) {
					ObjectOutputStream o = user.getOutputStream();
					// ACCIONES A REALIZAR SEGUN EL TIPO DE PAQUETE RECIBIDO
					System.out.println("Server: tipo de paquete recibido (" + paquete.getTipo() + ")");
					switch (paquete.getTipo()) {

						case LOGIN:
							paqCon = (PaqueteConexion) paquete;
							user.setNombre(paqCon.getNombreUsuario());
							System.out.println(user.getNombre() + " se ha conectado al servidor");
							if (database.verificarDatos(paqCon.getNombreUsuario(), paqCon.getPassword())) {
								if (servidor.agregarUsuario(user))
									paqCon.setResultado(true);
								else {
									paqCon.setResultado(false);
									paqCon.setMotivo("Servidor/Sala llena");
								}
							}
							else {
								paqCon.setResultado(false);
								paqCon.setMotivo("Datos de usuario no validos");
							}
							o.writeObject(paqCon);
							o.flush();
							break;

						case LOGOUT:
							paqCon = (PaqueteConexion) paquete;
							paqCon.setResultado(true);
							o.writeObject(paqCon);
							o.flush();
							servidor.eliminarCliente(user);
							running = false;
							break;

						case REGISTRO:
							paqCon = (PaqueteConexion) paquete;
							paqCon.setResultado(false);
							user.setNombre(paqCon.getNombreUsuario());
							paqCon.setResultado(database.registrarUsuario(paqCon.getNombreUsuario(), paqCon.getPassword()));
							running = false;
							o.writeObject(paqCon);
							o.flush();
							break;

						case SOLICITAR_LISTA_LOBBY:
							paqComu = (PaqueteComunicacion) paquete;
							paqComu.setListaNombresDeSalas(servidor.getListaNombresDeSalas());
							o.writeObject(paqComu);
							o.flush();
							break;

						case ENTRAR_LOBBY:
							paqComu = (PaqueteComunicacion) paquete;
							if (servidor.agregarASala(user, paqComu.getSalaSeleccionada())) {
								paqComu.setListaNombresDeUsuarios((user.getSala()).getListaNombres());
								paqComu.setResultado(true);
							}
							else {
								paqComu.setResultado(false);
								paqComu.setMensaje("Error al unirse a la sala. Sala llena\n");
							}
							o.writeObject(paqComu);
							o.flush();
							// Se informa a los demas usuarios de la sala, que alguien ha ingresado a la misma
							paqComu.setTipo(TipoPaquete.MENSAJE);
							paqComu.setListaNombresDeUsuarios(user.getSala().getListaNombres());
							paqComu.setMensaje(new String("***" + user.getNombre() + " ha ingresado a la sala***\n"));
							for (User u: user.getSala().getUsuarios()) {
								if (u != user) {
									u.getOutputStream().writeObject(paqComu);
									u.getOutputStream().flush();
								}
							}
							break;

						case ABANDONAR_LOBBY:
							paqComu = (PaqueteComunicacion) paquete;
							Sala s = user.getSala();
							servidor.agregarASala(user, Configuracion.SALA_ESPERA.getDescripcion());
							// Se informa al cliente, que ha salido de la sala correctamente
							paqComu.setResultado(true);
							o.writeObject(paqComu);
							o.flush();
							// Se informa a los demas usuarios de la sala, que alguien ha salido de la misma
							paqComu.setTipo(TipoPaquete.MENSAJE);
							paqComu.setListaNombresDeUsuarios(s.getListaNombres());
							paqComu.setMensaje(new String("***" + user.getNombre() + " ha abandonado la sala***\n"));
							for (User u: s.getUsuarios()) {
								u.getOutputStream().writeObject(paqComu);
								u.getOutputStream().flush();
							}
							break;

						case MENSAJE:
							paqComu = (PaqueteComunicacion) paquete;
							paqComu.setListaNombresDeUsuarios((user.getSala()).getListaNombres());
							paqComu.setMensaje(new String("[" + user.getNombre() + "]: " + paqComu.getMensaje() + "\n"));
							for (User u: user.getSala().getUsuarios()) {
								if (paqComu.getDestino().equals("[Todos]") || u.getNombre().equals(paqComu.getDestino()) || u.getNombre().equals(user.getNombre())) {
									u.getOutputStream().writeObject(paqComu);
									u.getOutputStream().flush();
								}
							}
							break;

						default:
							System.out.println("Paquete desconocido.\n");
							break;
					}
				}
			}
			servidor.eliminarCliente(user);
			System.out.println(user.getNombre() + " se ha desconectado del servidor");
		}
		catch (EOFException e) {
			running = false;
			servidor.eliminarCliente(user);
			System.out.println(user.getNombre() + " se ha desconectado del servidor");
		}
		catch (SocketException e) {
			running = false;
			servidor.eliminarCliente(user);
			System.out.println(user.getNombre() + " se ha desconectado del servidor");
		}
		catch (IOException e) {
			running = false;
			servidor.eliminarCliente(user);
			System.out.println(user.getNombre() + " se ha desconectado del servidor");
		}
		catch (ClassNotFoundException e1) {
			System.out.println("Tipo de paquete invalido");
		}
	}

	public void pararThread() {
		running = false;
	}
}
