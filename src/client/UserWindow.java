package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import paquetes.Paquete;
import paquetes.PaqueteComunicacion;
import paquetes.PaqueteConexion;
import paquetes.PaqueteSala;

public class UserWindow extends JFrame {

	private static final long												serialVersionUID	= 1L;
	private ClientWindow													mainWindow;
	private JPanel															contentPane;
	private JButton															btnCerrarSesion;
	private JButton															btnConfig;
	private JLabel															lblBienvenida;
	private JLabel															lblCantJugadores;
	private Cliente															cliente;
	// CONFIGURACION
	private String															userName;
	private int																arriba;
	private int																abajo;
	private int																izquierda;
	private int																derecha;
	private JButton															btnEntrarEnLobby;
	private JList<String>													listSalas;
	private DefaultListModel<String>										listModelSalas;
	private ArrayList<AbstractMap.SimpleImmutableEntry<String, Integer>>	datos;
	private ListenThread													_threadEscucha;
	private boolean															_cerradoDesdeServer;

	/* UserWindow Constructor */
	public UserWindow(ClientWindow window, String nombre, Cliente client) {
		setResizable(false);
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (mainWindow != null) {
					confirmarCerrarSesion();
				}
			}
		});
		datos = new ArrayList<AbstractMap.SimpleImmutableEntry<String, Integer>>();
		this.cliente = client;
		mainWindow = window;
		userName = nombre;
		setTitle("Menu principal");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 441, 263);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblBienvenida = new JLabel("");
		lblBienvenida.setBounds(110, 5, 210, 26);
		lblBienvenida.setForeground(new Color(51, 153, 204));
		lblBienvenida.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 18));
		lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
		lblBienvenida.setText("Bienvenid@ " + userName + "! ");
		contentPane.add(lblBienvenida);

		btnConfig = new JButton("Configuracion");
		btnConfig.setBounds(10, 78, 150, 25);
		btnConfig.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				// TODO Lanzar ventana de configuracion
			}
		});
		contentPane.add(btnConfig);

		btnCerrarSesion = new JButton("Cerrar sesion");
		btnCerrarSesion.setBounds(10, 114, 150, 25);
		btnCerrarSesion.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				confirmarCerrarSesion();
			}
		});
		contentPane.add(btnCerrarSesion);

		btnEntrarEnLobby = new JButton("Unirse a sala");
		btnEntrarEnLobby.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				cliente.entrarEnSala(listSalas.getSelectedValue());
			}
		});
		btnEntrarEnLobby.setBounds(10, 42, 150, 25);
		contentPane.add(btnEntrarEnLobby);

		listModelSalas = new DefaultListModel<String>();
		listSalas = new JList<String>(listModelSalas);
		listSalas.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent arg0) {
				String s = listSalas.getSelectedValue();
				System.out.println("Seleccionaste la partida " + listSalas.getSelectedValue());
				Integer cant = 0;
				for (AbstractMap.SimpleImmutableEntry<String, Integer> partida: datos) {
					if (partida.getKey().equals(s)) {
						cant = partida.getValue();
					}
				}
				lblCantJugadores.setText(cant.toString());
			}
		});
		listSalas.setBounds(180, 40, 200, 145);
		listSalas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		contentPane.add(listSalas);

		JLabel lblJugadores = new JLabel("Jugadores:");
		lblJugadores.setBounds(180, 195, 60, 15);
		contentPane.add(lblJugadores);

		lblCantJugadores = new JLabel("");
		lblCantJugadores.setBounds(250, 195, 50, 15);
		contentPane.add(lblCantJugadores);
		_threadEscucha = new ListenThread();
		_threadEscucha.start();
		_cerradoDesdeServer = false;
		cargarControles();
		cliente.buscarSalas();
	}

	/* Metodos */
	private void confirmarCerrarSesion() {
		int res = JOptionPane.showConfirmDialog(this, "Esta seguro?", "Cerrando sesion", JOptionPane.YES_NO_OPTION);
		if (res == JOptionPane.YES_OPTION) {
			cerrarVentana();
		}
	}

	public void cerrarVentana() {
		mainWindow.resetUserAndPassword();
		mainWindow.setVisible(true);
		if (!_cerradoDesdeServer)
			cliente.cerrarSesion();
		else
			_threadEscucha.interrupt();
		this.dispose();
	}

	public void iniciarChat(ArrayList<String> lista) {
		LobbyWindow lb = new LobbyWindow(cliente, this, listSalas.getSelectedValue(), lista);
		this.setVisible(false);
		lb.setVisible(true);
	}

	/**
	 * Carga los controles por defecto.
	 */
	private void cargarControles() {
		this.arriba = KeyEvent.VK_W;
		this.abajo = KeyEvent.VK_S;
		this.derecha = KeyEvent.VK_D;
		this.izquierda = KeyEvent.VK_A;
	}

	public void setControles(int arriba, int abajo, int izquierda, int derecha) {
		this.arriba = arriba;
		this.abajo = abajo;
		this.derecha = derecha;
		this.izquierda = izquierda;
	}

	/**
	 * Devuelve un vector de enteros con los keyCode de los controles asignados. El orden es: arriba (0), abajo(1), izquierda(2), derecha(3).
	 * 
	 * @return
	 */
	public int[] getControles() {
		int[] controles = new int[4];
		controles[0] = arriba;
		controles[1] = abajo;
		controles[2] = izquierda;
		controles[3] = derecha;
		return controles;
	}

	/**
	 * Reinicia el thread de escucha
	 */
	public void reiniciarThread() {
		_threadEscucha = new ListenThread();
		_threadEscucha.start();
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCerradoDesdeServer() {
		_cerradoDesdeServer = true;
	}

	/**
	 * Thread que recibe actualizaciones del servidor: creacion/eliminacion de salas
	 */
	private class ListenThread extends Thread {

		private boolean running;

		public void run() {
			running = true;
			System.out.println("INICIANDO LISTEN THREAD");
			while (running) {
				Paquete p = cliente.recibirPaqueteBloqueante();
				PaqueteComunicacion pCom;
				PaqueteConexion pCon;
				PaqueteSala pSala;
				if (p != null) {
					switch (p.getTipo()) {

						case SOLICITUD_LISTA_LOBBY:
							pCom = (PaqueteComunicacion) p;
							ArrayList<SimpleImmutableEntry<String, Integer>> salas = new ArrayList<SimpleImmutableEntry<String, Integer>>();
							for (String s: pCom.getListaSalas()) {
								String[] datos = s.split(" ");
								salas.add(new SimpleImmutableEntry<String, Integer>(datos[0], Integer.valueOf(datos[1])));
							}
							listModelSalas.clear();
							for (AbstractMap.SimpleImmutableEntry<String, Integer> partida: salas) {
								listModelSalas.addElement(partida.getKey());
							}
							break;

						case UNIRSE_A_SALA:
							pSala = (PaqueteSala) p;
							if (pSala.getResultado() == false)
								System.out.println("Motivo: " + pSala.getMensaje());
							if (pSala.getListaUsuarios() != null) {
								iniciarChat(pSala.getListaUsuarios());
								running = false;
							}
							else
								System.out.println("NO SE HA CONSEGUIDO ENTRAR EN LA SALA\n");
							break;

						case LOGOUT:
							pCon = (PaqueteConexion) p;
							System.out.println(pCon.getMotivo());
							cliente.cerrarCliente();
							running = false;
							break;

						case SERVIDOR_CERRADO:
							pCom = (PaqueteComunicacion) p;
							System.out.println(pCom.getMensaje());
							running = false;
							cerrarVentana();
							break;

						default:
							System.out.println("LobbyThread: Tipo de paquete inesperado (" + p.getTipo() + ")");
							break;
					}
				}
				try {
					sleep(200);
				}
				catch (InterruptedException e) {
					running = false;
					System.out.println("UserWindow: thread interrumpido");
				}
			}
			System.out.println("LISTEN THREAD DENETENIDO");
		}
	}

}
