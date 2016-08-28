package server;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class ListaUsuarios extends JFrame {

	private JPanel contentPane;
	private JList<User> list;
	private DefaultListModel<User> listModelUsuers = new DefaultListModel<User>();
	private Sala _sala;
	private ServerWindow _main;

	/**
	 * Create the frame.
	 */
	public ListaUsuarios(Sala sala, ServerWindow window) {
		_sala=sala;
		_main=window;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if(_main != null) {
					confirmarCerrar();
				}
			}
		});
		setTitle("Lista de usuarios de la sala");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 199, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblNombreDeLa = new JLabel("NOMBRE DE LA SALA");
		GridBagConstraints gbc_lblNombreDeLa = new GridBagConstraints();
		gbc_lblNombreDeLa.insets = new Insets(0, 0, 5, 5);
		gbc_lblNombreDeLa.gridx = 3;
		gbc_lblNombreDeLa.gridy = 0;
		contentPane.add(lblNombreDeLa, gbc_lblNombreDeLa);
		
		list = new JList<User>(listModelUsuers);
		list.setBackground(SystemColor.control);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 3;
		gbc_list.gridy = 2;
		contentPane.add(list, gbc_list);
		for(User u : _sala.getUsuarios()){
			listModelUsuers.addElement(u);
		}
		setVisible(true);
	}
	
	private void confirmarCerrar(){
		int res= JOptionPane.showConfirmDialog(this,
			    "Esta seguro?",
			    "Cerrando vista detallada",
			    JOptionPane.YES_NO_OPTION);
		if(res == JOptionPane.YES_OPTION) {
			_main.setVisible(true);
			this.dispose();
		}
	}
}
