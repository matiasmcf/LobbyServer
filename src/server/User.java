package server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.locks.ReentrantLock;

public class User {
	private String nombre;
	private Socket socket;
	private ThreadServer thread;
	private ObjectOutputStream outputStream;
	private ReentrantLock semaforoStream;
	private Sala sala;
	
	public User(Socket socket){
		this.socket = socket;
		try {
			this.outputStream = new ObjectOutputStream(new DataOutputStream(this.socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.semaforoStream = new ReentrantLock();
	}

	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	
	public void setThread(ThreadServer thread){
		this.thread = thread;
	}
	
	public String getNombre(){
		return nombre;
	}
	
	public Socket getSocket(){
		return socket;
	}
	
	public ThreadServer getThread(){
		return thread;
	}

	public ObjectOutputStream getOutputStream(){
		return outputStream;
	}

	public ReentrantLock getSemaforo(){
		return semaforoStream;
	}
	
	public void setSala(Sala s){
		sala=s;
	}
	
	public Sala getSala(){
		return sala;
	}
	@Override
	public String toString(){
		return nombre;
	}
}
