/**
 * 
 */
package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import paquetes.Paquete;
import paquetes.PaqueteComunicacion;
import paquetes.PaqueteSala;

/**
 * @author Matías
 *
 */
@SuppressWarnings("serial")
public class LobbyWindow extends JFrame {

	private JPanel						contentPane;
	private JLabel						lblDestinatario;
	private JTextArea					textAreaMensajes;
	private JLabel						labelNombreSala;
	private JLabel						lblCantusuarios;
	private JList<String>				listUsuarios;
	private DefaultListModel<String>	listModelUsuarios;
	private JTextArea					textAreaMensajeAEnviar;

	private ListenThread				_threadEscucha;
	private UserWindow					_userWindow;
	private Cliente						_cliente;
	private boolean						_cerradoDesdeServer;

	/**
	 * Crea una ventana de chat.
	 */
	public LobbyWindow(Cliente cliente, UserWindow userWindow, String sala, ArrayList<String> listaUsuarios) {
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (_userWindow != null) {
					confirmarCerrarSesion();
				}
			}
		});
		setForeground(Color.DARK_GRAY);
		setResizable(false);
		setTitle("Sala");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 641, 416);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.inactiveCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 271, 0, 0, 0, 0, 156, 19 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 205, 0, 54, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNombreDeLaSala = new JLabel("Nombre de la sala:");
		lblNombreDeLaSala.setHorizontalAlignment(SwingConstants.LEFT);
		lblNombreDeLaSala.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		GridBagConstraints gbc_lblNombreDeLaSala = new GridBagConstraints();
		gbc_lblNombreDeLaSala.anchor = GridBagConstraints.WEST;
		gbc_lblNombreDeLaSala.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombreDeLaSala.gridx = 1;
		gbc_lblNombreDeLaSala.gridy = 0;
		contentPane.add(lblNombreDeLaSala, gbc_lblNombreDeLaSala);

		labelNombreSala = new JLabel("<Nombre>");
		lblNombreDeLaSala.setLabelFor(labelNombreSala);
		GridBagConstraints gbc_labelNombreSala = new GridBagConstraints();
		gbc_labelNombreSala.insets = new Insets(0, 0, 5, 5);
		gbc_labelNombreSala.gridx = 3;
		gbc_labelNombreSala.gridy = 0;
		contentPane.add(labelNombreSala, gbc_labelNombreSala);

		JLabel lblCantidadDeUsuarios = new JLabel("Cantidad de usuarios:");
		lblCantidadDeUsuarios.setHorizontalAlignment(SwingConstants.LEFT);
		lblCantidadDeUsuarios.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
		GridBagConstraints gbc_lblCantidadDeUsuarios = new GridBagConstraints();
		gbc_lblCantidadDeUsuarios.anchor = GridBagConstraints.WEST;
		gbc_lblCantidadDeUsuarios.insets = new Insets(0, 0, 5, 5);
		gbc_lblCantidadDeUsuarios.gridx = 1;
		gbc_lblCantidadDeUsuarios.gridy = 1;
		contentPane.add(lblCantidadDeUsuarios, gbc_lblCantidadDeUsuarios);

		lblCantusuarios = new JLabel("cantUsuarios");
		GridBagConstraints gbc_lblCantusuarios = new GridBagConstraints();
		gbc_lblCantusuarios.insets = new Insets(0, 0, 5, 5);
		gbc_lblCantusuarios.gridx = 3;
		gbc_lblCantusuarios.gridy = 1;
		contentPane.add(lblCantusuarios, gbc_lblCantusuarios);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBackground(Color.BLACK);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridwidth = 8;
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		contentPane.add(separator, gbc_separator);

		JLabel lblHistorialDeMensajes = new JLabel("Historial de mensajes");
		GridBagConstraints gbc_lblHistorialDeMensajes = new GridBagConstraints();
		gbc_lblHistorialDeMensajes.anchor = GridBagConstraints.WEST;
		gbc_lblHistorialDeMensajes.insets = new Insets(0, 0, 5, 5);
		gbc_lblHistorialDeMensajes.gridx = 1;
		gbc_lblHistorialDeMensajes.gridy = 3;
		contentPane.add(lblHistorialDeMensajes, gbc_lblHistorialDeMensajes);

		JLabel lblUsuariosEnLa = new JLabel("Usuarios en la sala");
		GridBagConstraints gbc_lblUsuariosEnLa = new GridBagConstraints();
		gbc_lblUsuariosEnLa.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsuariosEnLa.gridx = 6;
		gbc_lblUsuariosEnLa.gridy = 3;
		contentPane.add(lblUsuariosEnLa, gbc_lblUsuariosEnLa);

		JScrollPane scrollPaneHistorial = new JScrollPane();
		GridBagConstraints gbc_scrollPaneHistorial = new GridBagConstraints();
		gbc_scrollPaneHistorial.gridwidth = 4;
		gbc_scrollPaneHistorial.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneHistorial.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneHistorial.gridx = 1;
		gbc_scrollPaneHistorial.gridy = 4;
		contentPane.add(scrollPaneHistorial, gbc_scrollPaneHistorial);

		textAreaMensajes = new JTextArea();
		textAreaMensajes.setLineWrap(true);
		textAreaMensajes.setForeground(Color.BLACK);
		textAreaMensajes.setFont(new Font("Arial", Font.PLAIN, 14));
		scrollPaneHistorial.setViewportView(textAreaMensajes);
		textAreaMensajes.setEditable(false);

		JScrollPane scrollPaneUsuarios = new JScrollPane();
		GridBagConstraints gbc_scrollPaneUsuarios = new GridBagConstraints();
		gbc_scrollPaneUsuarios.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneUsuarios.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneUsuarios.gridx = 6;
		gbc_scrollPaneUsuarios.gridy = 4;
		contentPane.add(scrollPaneUsuarios, gbc_scrollPaneUsuarios);

		JLabel lblMensajeAEnviar = new JLabel("Escribiendo mensaje para: ");
		GridBagConstraints gbc_lblMensajeAEnviar = new GridBagConstraints();
		gbc_lblMensajeAEnviar.anchor = GridBagConstraints.WEST;
		gbc_lblMensajeAEnviar.insets = new Insets(0, 0, 5, 5);
		gbc_lblMensajeAEnviar.gridx = 1;
		gbc_lblMensajeAEnviar.gridy = 5;
		contentPane.add(lblMensajeAEnviar, gbc_lblMensajeAEnviar);

		lblDestinatario = new JLabel("destinatario");
		GridBagConstraints gbc_lblDestinatario = new GridBagConstraints();
		gbc_lblDestinatario.gridwidth = 3;
		gbc_lblDestinatario.insets = new Insets(0, 0, 5, 5);
		gbc_lblDestinatario.gridx = 2;
		gbc_lblDestinatario.gridy = 5;
		contentPane.add(lblDestinatario, gbc_lblDestinatario);

		JScrollPane scrollPaneMensaje = new JScrollPane();
		GridBagConstraints gbc_scrollPaneMensaje = new GridBagConstraints();
		gbc_scrollPaneMensaje.gridwidth = 4;
		gbc_scrollPaneMensaje.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneMensaje.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneMensaje.gridx = 1;
		gbc_scrollPaneMensaje.gridy = 6;
		contentPane.add(scrollPaneMensaje, gbc_scrollPaneMensaje);

		textAreaMensajeAEnviar = new JTextArea();
		scrollPaneMensaje.setViewportView(textAreaMensajeAEnviar);
		textAreaMensajeAEnviar.setWrapStyleWord(true);
		textAreaMensajeAEnviar.setTabSize(4);
		textAreaMensajeAEnviar.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textAreaMensajeAEnviar.setLineWrap(true);
		textAreaMensajeAEnviar.setToolTipText("Escriba el mensaje que desea enviar...");

		JButton btnEnviarMensaje = new JButton("Enviar mensaje");
		btnEnviarMensaje.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if (textAreaMensajeAEnviar.getText().length() != 0)
					_cliente.enviarMensaje(listUsuarios.getSelectedValue(), textAreaMensajeAEnviar.getText());
			}
		});
		btnEnviarMensaje.setForeground(SystemColor.activeCaptionText);
		btnEnviarMensaje.setFont(new Font("Comic Sans MS", Font.PLAIN, 10));
		btnEnviarMensaje.setBackground(SystemColor.activeCaption);
		GridBagConstraints gbc_btnEnviarMensaje = new GridBagConstraints();
		gbc_btnEnviarMensaje.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnEnviarMensaje.insets = new Insets(0, 0, 5, 5);
		gbc_btnEnviarMensaje.gridx = 6;
		gbc_btnEnviarMensaje.gridy = 6;
		contentPane.add(btnEnviarMensaje, gbc_btnEnviarMensaje);

		_userWindow = userWindow;
		_cliente = cliente;
		listModelUsuarios = new DefaultListModel<String>();
		listUsuarios = new JList<String>(listModelUsuarios);
		listUsuarios.addListSelectionListener(new ListSelectionListener(){

			public void valueChanged(ListSelectionEvent e) {
				lblDestinatario.setText(listUsuarios.getSelectedValue());
			}
		});
		scrollPaneUsuarios.setViewportView(listUsuarios);
		for (String s: listaUsuarios) {
			listModelUsuarios.addElement(s);
		}
		listUsuarios.setSelectedIndex(0);
		lblCantusuarios.setText(Integer.toString(listModelUsuarios.size() - 1));
		labelNombreSala.setText(sala);
		_cerradoDesdeServer = false;
		_threadEscucha = new ListenThread();
		_threadEscucha.start();
	}

	private void confirmarCerrarSesion() {
		int res = JOptionPane.showConfirmDialog(this, "Esta seguro?", "Abandonando sala", JOptionPane.YES_NO_OPTION);
		if (res == JOptionPane.YES_OPTION) {
			_cliente.abandonarSala();
		}
	}

	public void cerrarVentana() {
		_userWindow.setVisible(true);
		_userWindow.reiniciarThread();
		if (_cerradoDesdeServer) {
			_userWindow.setCerradoDesdeServer();
			_userWindow.cerrarVentana();
		}
		this.dispose();
	}

	/**
	 * Thread que recibe actualizaciones de los demas usuarios
	 */
	private class ListenThread extends Thread {

		private boolean running;

		public void run() {
			running = true;
			System.out.println("INICIANDO LOBBY THREAD");
			while (running) {
				Paquete p = _cliente.recibirPaqueteBloqueante();
				PaqueteComunicacion pCom;
				PaqueteSala pSala;
				if (p != null) {
					switch (p.getTipo()) {

						case MENSAJE:
							pCom = (PaqueteComunicacion) p;
							textAreaMensajes.append(pCom.getMensaje());
							listModelUsuarios.clear();
							for (String s: pCom.getListaUsuarios()) {
								listModelUsuarios.addElement(s);
							}
							listUsuarios.setSelectedIndex(0);
							lblCantusuarios.setText(Integer.toString(listModelUsuarios.size() - 1));
							break;

						case ABANDONAR_SALA:
							pSala = (PaqueteSala) p;
							System.out.println("ABANDONANDO LOBBY");
							if (pSala.getResultado() == false)
								System.out.println("Motivo: " + pSala.getMensaje());
							else
								cerrarVentana();
							running = false;
							break;

						case SERVIDOR_CERRADO:
							pCom = (PaqueteComunicacion) p;
							System.out.println(pCom.getMensaje());
							_cerradoDesdeServer = true;
							cerrarVentana();
							running = false;
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
					e.printStackTrace();
				}
			}
			System.out.println("LOBBY THREAD DENETENIDO");
		}
	}
}
