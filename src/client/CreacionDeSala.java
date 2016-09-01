/**
 * 
 */
package client;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import utils.Configuracion;

/**
 * Ventana para el ingreso de los parametros de la sala a crear.
 * 
 * @author Matías
 */
public class CreacionDeSala extends JFrame {

	private static final long	serialVersionUID	= 1L;
	private JPanel				contentPane;
	private UserWindow			_userWindow;

	/**
	 * Ventana para el ingreso de los parametros de la sala a crear.
	 */
	public CreacionDeSala(UserWindow userWindow) {
		setResizable(false);
		setTitle("Creacion de sala");
		_userWindow = userWindow;
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent arg0) {
				if (_userWindow != null) {
					cerrarVentana();
				}
			}
		});
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 284);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNombre = new GridBagConstraints();
		gbc_lblNombre.anchor = GridBagConstraints.WEST;
		gbc_lblNombre.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombre.gridx = 1;
		gbc_lblNombre.gridy = 1;
		contentPane.add(lblNombre, gbc_lblNombre);

		JFormattedTextField textFieldNombre = new JFormattedTextField();
		GridBagConstraints gbc_textFieldNombre = new GridBagConstraints();
		gbc_textFieldNombre.gridwidth = 8;
		gbc_textFieldNombre.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldNombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombre.gridx = 2;
		gbc_textFieldNombre.gridy = 1;
		contentPane.add(textFieldNombre, gbc_textFieldNombre);

		JLabel lblContrasea = new JLabel("Contraseña:");
		lblContrasea.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblContrasea = new GridBagConstraints();
		gbc_lblContrasea.anchor = GridBagConstraints.WEST;
		gbc_lblContrasea.insets = new Insets(0, 0, 5, 5);
		gbc_lblContrasea.gridx = 1;
		gbc_lblContrasea.gridy = 3;
		contentPane.add(lblContrasea, gbc_lblContrasea);

		JFormattedTextField textFieldPassword = new JFormattedTextField();
		GridBagConstraints gbc_textFieldPassword = new GridBagConstraints();
		gbc_textFieldPassword.gridwidth = 8;
		gbc_textFieldPassword.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPassword.gridx = 2;
		gbc_textFieldPassword.gridy = 3;
		contentPane.add(textFieldPassword, gbc_textFieldPassword);

		JLabel lblCapacidad = new JLabel("Capacidad:");
		lblCapacidad.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblCapacidad = new GridBagConstraints();
		gbc_lblCapacidad.anchor = GridBagConstraints.WEST;
		gbc_lblCapacidad.insets = new Insets(0, 0, 5, 5);
		gbc_lblCapacidad.gridx = 1;
		gbc_lblCapacidad.gridy = 5;
		contentPane.add(lblCapacidad, gbc_lblCapacidad);

		JFormattedTextField textFieldCapacidad = new JFormattedTextField();
		GridBagConstraints gbc_textFieldCapacidad = new GridBagConstraints();
		gbc_textFieldCapacidad.gridwidth = 8;
		gbc_textFieldCapacidad.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldCapacidad.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldCapacidad.gridx = 2;
		gbc_textFieldCapacidad.gridy = 5;
		contentPane.add(textFieldCapacidad, gbc_textFieldCapacidad);

		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				// Validacion de parametros
				if (textFieldNombre.getText() == null || textFieldNombre.getText().trim().equals("")) {
					System.out.println("El nombre de la sala no puede estar en blanco.");
					return;
				}
				else {
					_userWindow.crearSala(textFieldNombre.getText(), textFieldPassword.getText() == null ? "" : textFieldPassword.getText(),
							textFieldCapacidad.getText() == null ? Configuracion.MAX_EN_SALA.getValor() : Integer.parseInt(textFieldCapacidad.getText()));
					// TODO Corregir las validaciones de los formattedTextFields
				}

			}
		});
		GridBagConstraints gbc_btnAceptar = new GridBagConstraints();
		gbc_btnAceptar.gridwidth = 3;
		gbc_btnAceptar.insets = new Insets(0, 0, 0, 5);
		gbc_btnAceptar.gridx = 3;
		gbc_btnAceptar.gridy = 7;
		contentPane.add(btnAceptar, gbc_btnAceptar);

		JButton btnCancelar = new JButton("Cancelar");
		GridBagConstraints gbc_btnCancelar = new GridBagConstraints();
		gbc_btnCancelar.insets = new Insets(0, 0, 0, 5);
		gbc_btnCancelar.gridx = 6;
		gbc_btnCancelar.gridy = 7;
		contentPane.add(btnCancelar, gbc_btnCancelar);
	}

	public void cerrarVentana() {
		_userWindow.setVisible(true);
		this.dispose();
	}

}
