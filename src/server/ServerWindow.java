package server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import paquetes.PaqueteComunicacion;
import paquetes.TipoPaquete;
import utils.Configuracion;

public class ServerWindow extends JFrame {

	// Thread de escucha del servidor
	private class ListenThread extends Thread {

		public void run() {
			while (bandera) {
				cliente = servidor.aceptarConexion();
				if (cliente != null) {
					User u = new User(cliente);
					u.setThread(new ThreadServer(servidor, u));
					u.getThread().start();
				}
				actualizarListaDeNombres();
			}
			System.out.println("FIN DEL THREAD PRINCIPAL");
			servidor.pararServidor();
			System.out.println("SERVER CERRADO");
		}

		public void pararThread() {
			bandera = false;
		}
	}

	private static final long		serialVersionUID	= 1L;
	private JPanel					contentPane;
	private JTextField				textFieldNombre;
	private JTextField				textFieldIP;
	private JTextField				textFieldPuerto;
	public static ServerWindow		frame;

	private Server					servidor			= null;
	private boolean					bandera;
	private Socket					cliente				= null;
	private ListenThread			threadEscucha;
	private JLabel					lblClientesConectados;
	private JLabel					lblSalas;
	private JButton					btnEliminarSala;
	private JTextArea				textAreaNombres;
	private JButton					btnCerrarServidor;
	private JLabel					lblCantJugadores;
	private JTextArea				textAreaCantJugadores;
	private JList<Sala>				listSalas;
	private DefaultListModel<Sala>	listModelSalas;
	private JButton					btnDetalles;
	private Sala					salaEspera;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){

			public void run() {
				try {
					frame = new ServerWindow();
					frame.setVisible(true);
				}
				catch (Exception e) {
					System.out.println("Error al lanzar la ventana del servidor");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerWindow() {
		setResizable(false);
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				mensajeSalida();
			}
		});

		setTitle("Server PacMan");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 455, 380);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblServer = new JLabel("SERVER");
		lblServer.setBounds(141, 5, 70, 26);
		lblServer.setForeground(SystemColor.textHighlight);
		lblServer.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 18));
		contentPane.add(lblServer);

		JLabel lblNombre = new JLabel("Nombre");
		lblNombre.setBounds(35, 41, 37, 14);
		contentPane.add(lblNombre);

		textFieldNombre = new JTextField();
		textFieldNombre.setBounds(108, 38, 294, 20);
		textFieldNombre.setEditable(false);
		contentPane.add(textFieldNombre);
		textFieldNombre.setColumns(10);

		JLabel lblIp = new JLabel("IP");
		lblIp.setBounds(49, 71, 10, 14);
		contentPane.add(lblIp);

		textFieldIP = new JTextField();
		textFieldIP.setBounds(108, 68, 294, 20);
		textFieldIP.setEditable(false);
		contentPane.add(textFieldIP);
		textFieldIP.setColumns(10);

		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setBounds(38, 101, 32, 14);
		contentPane.add(lblPuerto);

		textFieldPuerto = new JTextField();
		textFieldPuerto.setBounds(108, 98, 294, 20);
		textFieldPuerto.setEditable(false);
		contentPane.add(textFieldPuerto);
		textFieldPuerto.setColumns(10);

		// SERVIDOR
		crearServidor();

		textFieldNombre.setText(servidor.getNombreHost());
		textFieldPuerto.setText(Integer.toString(servidor.getPuerto()));
		textFieldIP.setText(servidor.getIPHost());

		lblClientesConectados = new JLabel("Clientes conectados");
		lblClientesConectados.setBounds(46, 131, 96, 14);
		contentPane.add(lblClientesConectados);

		lblSalas = new JLabel("Salas");
		lblSalas.setBounds(221, 131, 89, 14);
		contentPane.add(lblSalas);

		lblCantJugadores = new JLabel("Cant. usuarios");
		lblCantJugadores.setBounds(323, 131, 78, 14);
		contentPane.add(lblCantJugadores);

		textAreaNombres = new JTextArea();
		textAreaNombres.setBounds(35, 150, 120, 105);
		textAreaNombres.setEditable(false);
		textAreaNombres.setBackground(SystemColor.control);
		contentPane.add(textAreaNombres);

		btnCerrarServidor = new JButton("Cerrar servidor");
		btnCerrarServidor.setBounds(320, 265, 110, 18);
		btnCerrarServidor.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				mensajeSalida();
			}
		});

		textAreaCantJugadores = new JTextArea();
		textAreaCantJugadores.setBounds(323, 150, 105, 105);
		textAreaCantJugadores.setEditable(false);
		textAreaCantJugadores.setBackground(SystemColor.control);
		contentPane.add(textAreaCantJugadores);
		contentPane.add(btnCerrarServidor);
		listModelSalas = new DefaultListModel<Sala>();
		listSalas = new JList<Sala>(listModelSalas);
		listSalas.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				if (listSalas.getSelectedValue() != null) {
					if (listSalas.getSelectedValue().isEmpty()) {
						btnEliminarSala.setEnabled(true);
						btnDetalles.setEnabled(false);
					}
					else
						btnDetalles.setEnabled(true);
				}
			}
		});
		listSalas.setBackground(SystemColor.control);
		listSalas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSalas.setBounds(165, 150, 145, 105);
		contentPane.add(listSalas);

		JButton buttonCrearSala = new JButton("Crear sala");
		buttonCrearSala.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				ingresarNombreSala();
			}
		});
		buttonCrearSala.setBounds(165, 265, 145, 18);
		contentPane.add(buttonCrearSala);

		btnEliminarSala = new JButton("Eliminar sala");
		btnEliminarSala.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				Sala s = listSalas.getSelectedValue();
				listSalas.setSelectedValue(null, true);
				if (!servidor.eliminarSala(s))
					mostrarError("No se puede eliminar la sala por defecto.", "Error");
				else
					listModelSalas.removeElement(s);
				enviarListaDeSalas();
				btnEliminarSala.setEnabled(false);
				btnDetalles.setEnabled(false);
			}
		});
		btnEliminarSala.setEnabled(false);
		btnEliminarSala.setBounds(165, 294, 145, 17);
		contentPane.add(btnEliminarSala);

		btnDetalles = new JButton("Detalles");
		btnDetalles.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				iniciarDetalles();
			}
		});
		btnDetalles.setEnabled(false);
		btnDetalles.setBounds(165, 322, 145, 17);
		contentPane.add(btnDetalles);
		bandera = true;
		threadEscucha = new ListenThread();
		threadEscucha.start();
		salaEspera = servidor.crearSala("Limbo");
		listModelSalas.addElement(salaEspera);
		System.out.println("SERVIDOR CREADO");
	}

	private void mensajeSalida() {
		int option = JOptionPane.showConfirmDialog(frame, "Esta seguro que quiere salir?", "Saliendo del juego", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			threadEscucha.pararThread();
			for (User u: servidor.getListaUsuarios()) {
				u.getThread().pararThread();
				// TODO informar a los clientes, que el servidor se ha cerrado
			}
			frame.dispose();
		}
	}

	public void actualizarListaDeNombres() {
		textAreaNombres.setText("");
		for (User u: servidor.getListaUsuarios()) {
			textAreaNombres.append(u.getNombre() + "\n");
		}
	}

	private void crearServidor() {
		boolean serverCreado = false;
		int puerto = Configuracion.PUERTO_INICIAL.getValor();
		int maxPuertosABuscar = Configuracion.RANGO_PUERTOS.getValor();
		while (serverCreado != true && maxPuertosABuscar > 0) {
			try {
				servidor = new Server(puerto, Configuracion.MAX_CLIENTES.getValor(), this);
				serverCreado = true;
			}
			catch (IOException e) {
				System.out.println("No se puede escuchar desde el puerto " + puerto + ", buscando nuevo puerto...");
				puerto++;
				maxPuertosABuscar--;
			}
		}
		if (maxPuertosABuscar <= 0) {
			System.out.println("No se encontro ningun puerto de escucha disponible");
			System.exit(-1);
		}
	}

	/**
	 * Crea una nueva partida, lanzando un nuevo ThreadServerPartida.
	 * 
	 * @param nombre
	 *            -El nombre de la partida.
	 */
	public void crearSala(String nombre) {
		if (nombre != null && nombre != "") {
			Sala s = servidor.crearSala(nombre);
			if (s != null)
				listModelSalas.addElement(s);
			else {
				JOptionPane.showMessageDialog(this, "No se pudo crear la sala: ya existe una sala con el mismo nombre.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	private void enviarListaDeSalas() {
		PaqueteComunicacion paqComu = new PaqueteComunicacion(TipoPaquete.SOLICITAR_LISTA_LOBBY);
		paqComu.setListaNombresDeSalas(servidor.getListaNombresDeSalas());
		for (User u: salaEspera.getUsuarios()) {
			try {
				u.getOutputStream().writeObject(paqComu);
				u.getOutputStream().flush();
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println("Error al enviar la lista de salas actualizada.");
			}
		}
	}

	/**
	 * Lanza una ventana para el ingreso del nombre de la sala.
	 */
	public void ingresarNombreSala() {
		String s = (String) JOptionPane.showInputDialog(frame, "Ingrese el nombre de la sala:", "Nueva Partida", JOptionPane.PLAIN_MESSAGE, null, null, null);

		if ((s != null) && (s.length() > 0)) {
			if (s.length() > 15) {
				JOptionPane.showMessageDialog(this, "El nombre de la sala no puede contener mas de 15 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.crearSala(s.trim());
			enviarListaDeSalas();
			return;
		}
		else {
			if (s != null)
				JOptionPane.showMessageDialog(this, "Ingrese un nombre para la partida.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}

	private void iniciarDetalles() {
		new ListaUsuarios(listSalas.getSelectedValue(), this);
		this.setVisible(false);
	}

	/**
	 * Muestra un cartel con un mensaje y un titulo
	 * 
	 * @param mensaje
	 * @param titulo
	 */
	private void mostrarError(String mensaje, String titulo) {
		JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
	}
}
