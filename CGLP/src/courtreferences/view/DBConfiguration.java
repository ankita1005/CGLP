package courtreferences.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import courtreferences.model.ConnectionHandler;

public class DBConfiguration extends JDialog implements FontDefinition {
	
	/* 
	 * This class contains the components for updating the "Database configuration settings"
	 * Purpose : Functionalities for updating the db settings
	 */
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtHostName;
	private JTextField txtPortNumber;
	private JTextField txtDbName;
	private JTextField txtUserName;
	private JPasswordField passwordField;
	private JLabel lblHostName;
	private JLabel lblPortnumber;
	private JLabel lblDatabasename;
	private JLabel lblUserName;
	private JLabel lblPassword;
	private JButton UpdateButton;
	private JButton cancelButton;
	
	/**
	 * Create the dialog.
	 */
	public DBConfiguration() {
		setResizable(false);
		/*	Initialize the components of the window	*/
		initComponents();
		/*	Default settings from the config file will be set in the controls	*/
		loadDefaultValues();
		/*	Initialize event handlers	*/
		createEvents();
	}
	
	private void initComponents(){
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblHostName = new JLabel("HostName");
			lblHostName.setBounds(38, 12, 143, 37);
			lblHostName.setFont(getDefaultControlsFont());
			contentPanel.add(lblHostName);
		}
		{
			lblPortnumber = new JLabel("PortNumber");
			lblPortnumber.setBounds(38, 51, 159, 42);
			lblPortnumber.setFont(getDefaultControlsFont());
			contentPanel.add(lblPortnumber);
		}
		{
			lblDatabasename = new JLabel("DatabaseName");
			lblDatabasename.setBounds(38, 101, 159, 29);
			lblDatabasename.setFont(getDefaultControlsFont());
			contentPanel.add(lblDatabasename);
		}
		{
			lblUserName = new JLabel("UserName");
			lblUserName.setBounds(38, 136, 159, 29);
			lblUserName.setFont(getDefaultControlsFont());
			contentPanel.add(lblUserName);
		}
		{
			lblPassword = new JLabel("Password");
			lblPassword.setBounds(38, 173, 126, 37);
			lblPassword.setFont(getDefaultControlsFont());
			contentPanel.add(lblPassword);
		}
		{
			txtHostName = new JTextField();
			txtHostName.setBounds(188, 21, 219, 29);
			txtHostName.setFont(getDefaultControlsFont());
			contentPanel.add(txtHostName);
			txtHostName.setColumns(10);
		}
		{
			txtPortNumber = new JTextField();
			txtPortNumber.setBounds(188, 60, 219, 33);
			txtPortNumber.setFont(getDefaultTitleFont());
			contentPanel.add(txtPortNumber);
			txtPortNumber.setColumns(10);
		}
		{
			txtDbName = new JTextField();
			txtDbName.setBounds(188, 107, 219, 29);
			txtDbName.setFont(getDefaultTitleFont());
			contentPanel.add(txtDbName);
			txtDbName.setColumns(10);
		}
		{
			txtUserName = new JTextField();
			txtUserName.setBounds(188, 141, 219, 29);
			txtUserName.setFont(getDefaultTitleFont());
			contentPanel.add(txtUserName);
			txtUserName.setColumns(10);
		}
		{
			passwordField = new JPasswordField();
			passwordField.setBounds(188, 179, 219, 31);
			passwordField.setFont(getDefaultTitleFont());
			contentPanel.add(passwordField);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				UpdateButton = new JButton("Update");
				UpdateButton.setActionCommand("OK");
				UpdateButton.setFont(getDefaultTitleFont());
				buttonPane.add(UpdateButton);
				getRootPane().setDefaultButton(UpdateButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.setFont(getDefaultTitleFont());
				buttonPane.add(cancelButton);
			}
		}
	}
	
	/*	Loads default configuration settings to connect to mysql db from config file	*/
	
	private void loadDefaultValues(){
		Map<String,String> configMap = new ConnectionHandler().getDefaultDbConfigParameters();
		txtHostName.setText(configMap.get("hostname"));
		txtPortNumber.setText(configMap.get("port"));
		txtDbName.setText(configMap.get("dbname"));
		txtUserName.setText(configMap.get("username"));
		passwordField.setText(configMap.get("password"));
	}
	
	private void createEvents(){
		
		/*	Prompts the user to enter new db settings and updates it when the update button is pressed	*/
		UpdateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = "Confirm DB Settings Update";
				String message = "Are you sure the DB Settings should be updated?";
				int reply = JOptionPane.showConfirmDialog(null,title,message,JOptionPane.YES_NO_OPTION);
				if(reply == 0){
					updateDbConfigFile();
				}
				closeDialog();
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
	}
	
	private void showSuccessDialog(){
		JOptionPane.showMessageDialog(null,"The Database settings are updated");
	}
	
	private void showFailureDialog(){
		JOptionPane.showMessageDialog(null,"The Database settings cannot be updated \n Please check settings and try again");
	}
	
	private void closeDialog(){
		this.dispose();
	}
	
	/*	Updates the new db settings in the dbconfig.txt file	*/
	
	private void updateDbConfigFile(){
		@SuppressWarnings("deprecation")
		int updateStatus = new ConnectionHandler().updateDBSettings(txtHostName.getText(), txtPortNumber.getText(), txtDbName.getText(), txtUserName.getText(), passwordField.getText());
		if(updateStatus == 1)
			showSuccessDialog();
		else
			showFailureDialog();
	}
	
	@Override
	public Font getDefaultControlsFont() {
		// TODO Auto-generated method stub
		return new Font("Arial",Font.BOLD, 12);
	}

	@Override
	public Font getDefaultTitleFont() {
		// TODO Auto-generated method stub
		return new Font("Arial",Font.BOLD, 12);
	}
}
