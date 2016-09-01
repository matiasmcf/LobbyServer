package client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import utils.Resultado;

public class ClientWindow extends JFrame {

	/* Members */
	private static final long	serialVersionUID	= 1L;
	private JPanel				contentPane;
	private JButton				btnLogin;
	private JButton				btnRegistrarUsuario;
	private JLabel				lblNombre;
	private JLabel				lblPassword;
	private JTextField			textFieldNombre;
	private JPasswordField		pwdFieldPassword;
	private JTextField			txtServidor;
	private JTextField			txtPuerto;
	public static ClientWindow	frame;
	private UserWindow			userWindow;
	private Cliente				cliente;

	/* Main Application */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){

			public void run() {
				try {
					frame = new ClientWindow();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				}
				catch (Exception e) {
					System.out.println("Error al lanzar la ventana del cliente");
				}
			}
		});
	}

	/* MainWindow Constructor */
	public ClientWindow() {
		setResizable(false);
		frame = this;
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				mensajeSalida();
			}
		});
		setTitle("Pac-Man");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 350, 427);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitle = new JLabel("PAC-MAN");
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Sylfaen", Font.PLAIN, 24));
		lblTitle.setBounds(100, 11, 120, 35);
		contentPane.add(lblTitle);
		JButton btnExit = new JButton("Salir");
		btnExit.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				mensajeSalida();
			}
		});
		btnExit.setBounds(110, 322, 120, 25);
		contentPane.add(btnExit);
		JButton btnCredits = new JButton("Cr\u00E9ditos");
		btnCredits.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Grupo 4: \n-Barja, Alex\n-Figueroa, Matias\n-Maidana, Diego\n-Maita, Martin\n-Silva Hernan");
			}
		});
		btnCredits.setBounds(110, 286, 120, 25);
		contentPane.add(btnCredits);

		textFieldNombre = new JTextField();
		textFieldNombre.setToolTipText("Introduzca su nombre aqu\u00ED");
		textFieldNombre.addKeyListener(new KeyAdapter(){

			@Override
			public void keyReleased(KeyEvent e) {
				verificarTextFieldsUser();
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					pwdFieldPassword.grabFocus();
			}
		});
		textFieldNombre.setBounds(110, 156, 170, 20);
		contentPane.add(textFieldNombre);
		textFieldNombre.setColumns(10);

		pwdFieldPassword = new JPasswordField();
		pwdFieldPassword.setToolTipText("Introduzca su contrase\u00F1a aqu\u00ED");
		pwdFieldPassword.addKeyListener(new KeyAdapter(){

			@Override
			// ¿keyTyped registra la 1ra letra recien la 2da vez que se llama?
			public void keyReleased(KeyEvent e) {
				verificarTextFieldsUser();
				// if(e.getKeyCode()==KeyEvent.VK_ENTER&&btnLogin.isEnabled()){ //LOGIN CON LA TECLA ENTER
				// lanzarVentanaUsuario(textFieldNombre.getText());
				// }
			}
		});
		pwdFieldPassword.setBounds(110, 187, 170, 20);
		contentPane.add(pwdFieldPassword);
		pwdFieldPassword.setColumns(20);

		btnLogin = new JButton("Iniciar sesi\u00F3n");
		btnLogin.setEnabled(false);
		btnLogin.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (conectar()) {
					Resultado r = cliente.iniciarSesion(textFieldNombre.getText(), new String(pwdFieldPassword.getPassword()));
					if (r.getValor()) {
						lanzarVentanaUsuario(textFieldNombre.getText());
					}
					else {
						JOptionPane.showMessageDialog(frame, r.getMotivo() + "\nIntentelo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
						cliente.cerrarCliente();
						return;
					}
				}
			}
		});
		btnLogin.setBounds(110, 218, 120, 23);
		contentPane.add(btnLogin);

		lblNombre = new JLabel("Usuario");
		lblNombre.setBounds(20, 159, 46, 14);
		contentPane.add(lblNombre);

		lblPassword = new JLabel("Contrase\u00F1a");
		lblPassword.setBounds(20, 190, 56, 14);
		contentPane.add(lblPassword);

		btnRegistrarUsuario = new JButton("Registrarse");
		btnRegistrarUsuario.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				if (conectar()) {
					// cliente.setDatos(textFieldNombre.getText(), new String(pwdFieldPassword.getPassword()));
					if (cliente.registrarUsuario(textFieldNombre.getText(), new String(pwdFieldPassword.getPassword()))) {
						JOptionPane.showMessageDialog(frame, "Registro exitoso.", "Info", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						JOptionPane.showMessageDialog(frame, "Usuario ya registrado.\nIntentelo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					cliente.cerrarCliente();
				}
			}
		});
		btnRegistrarUsuario.setEnabled(false);
		btnRegistrarUsuario.setBounds(110, 252, 120, 23);
		contentPane.add(btnRegistrarUsuario);

		JLabel lblServidor = new JLabel("Servidor");
		lblServidor.setBounds(20, 59, 46, 14);
		contentPane.add(lblServidor);

		JLabel lblPuerto = new JLabel("Puerto");
		lblPuerto.setBounds(20, 84, 46, 14);
		contentPane.add(lblPuerto);

		txtServidor = new JTextField();
		txtServidor.setToolTipText("Introduzca el nombre o la ip del servidor");
		txtServidor.setText("localhost");
		txtServidor.setBounds(110, 56, 86, 20);
		contentPane.add(txtServidor);
		txtServidor.setColumns(10);

		txtPuerto = new JTextField();
		txtPuerto.setToolTipText("Introduzca el puerto del servidor");
		txtPuerto.setBounds(110, 81, 86, 20);
		contentPane.add(txtPuerto);
		txtPuerto.setColumns(10);

		JLabel lblSeparador = new JLabel("--------------------------------------------------------------");
		lblSeparador.setBounds(43, 120, 280, 14);
		contentPane.add(lblSeparador);
	}

	/* Metodos */
	/**
	 * Activa los botones de login y registro si los campos de usuario y password no estan en blanco, en caso contrario, los desactiva.
	 */
	private void verificarTextFieldsUser() {
		if (textFieldNombre.getText().length() > 0 && pwdFieldPassword.getPassword().length > 0) {
			btnLogin.setEnabled(true);
			btnRegistrarUsuario.setEnabled(true);
		}
		else {
			btnLogin.setEnabled(false);
			btnRegistrarUsuario.setEnabled(false);
		}
	}

	private void lanzarVentanaUsuario(String username) {
		if (username.length() > 10) {
			JOptionPane.showMessageDialog(frame, "El nick debe contener diez caracteres como maximo.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		frame.setVisible(false);
		userWindow = new UserWindow(frame, username, cliente);
		userWindow.setLocationRelativeTo(null);
		userWindow.setVisible(true);
	}

	private void mensajeSalida() {
		int option = JOptionPane.showConfirmDialog(frame, "Esta seguro que quiere salir?", "Saliendo del juego", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			if (cliente != null)
				cliente.cerrarCliente();
			frame.dispose();
		}
	}

	/**
	 * Reinicia los textfield nombre y paswword
	 */
	public void resetUserAndPassword() {
		textFieldNombre.setText(null);
		pwdFieldPassword.setText(null);
		btnLogin.setEnabled(false);
		btnRegistrarUsuario.setEnabled(false);
	}

	/**
	 * Conecta con el servidor
	 * 
	 * @return -True/False, Informando si la conexion fue exitosa, o no.
	 */
	private boolean conectar() {
		if (txtServidor.getText().equals(null) || txtServidor.getText().equals("")) {
			JOptionPane.showMessageDialog(frame, "Ingrese un servidor al que conectarse.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		else
			if (txtPuerto.getText().equals(null) || txtPuerto.getText().equals("")) {
				JOptionPane.showMessageDialog(frame, "Ingrese un puerto al que conectarse.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			else {
				String server = txtServidor.getText();
				int puerto;
				try {
					puerto = Integer.parseInt(txtPuerto.getText());
				}
				catch (NumberFormatException nfe) {
					JOptionPane.showMessageDialog(frame, "Los datos del puerto son invalidos.\nIngrese un numero entero", "Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				try {
					cliente = new Cliente(server, puerto);
					return true;
				}
				catch (UnknownHostException e1) {
					JOptionPane.showMessageDialog(frame, "No se pudo conectar con el servidor.\nPuede que este ocupado o no este en linea.", "Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				catch (IOException e2) {
					JOptionPane.showMessageDialog(frame, "No se pudo crear el socket.\nIntentelo nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
	}
}
