package courtreferences.view;

import java.awt.Font;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

import courtreferences.model.*;

import java.awt.Color;

public class LoginDialog extends JDialog implements FontDefinition{
	/* 
	 * This class contains the components for prompting the user to enter login details
	 * Purpose : Functionalities for getting the login credentials and authenticating the user
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JPasswordField passwordField;
	private int userStatus;
	private String username;
	private JButton btnLogin;
	private JButton btnCancel;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblInvalidPassword;

	public void setUserStatus(int ustatus){
		this.userStatus = ustatus; 
	}
	
	public int getUserStatus(){
		return this.userStatus;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * Create the dialog.
	 */
	public LoginDialog() {
		this.setUserStatus(-1);
		/*	Initialize all the components	*/
		initComponents();
		/*	Initialize the Event handlers	*/
		createEvents();
	}
	
	private void initComponents(){
		setResizable(false);
		setModal(true);
		setTitle("Login Window");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		
		JPanel controlsPanel = new JPanel();
		controlsPanel.setBounds(0, 24, 450, 187);
		getContentPane().add(controlsPanel);
		controlsPanel.setLayout(null);
		
		lblUsername = new JLabel("UserName");
		lblUsername.setBounds(59, 30, 93, 38);
		controlsPanel.add(lblUsername);
		lblUsername.setFont(getDefaultControlsFont());
		
		lblPassword = new JLabel("Password");
		lblPassword.setBounds(59, 96, 87, 45);
		controlsPanel.add(lblPassword);
		lblPassword.setFont(getDefaultControlsFont());
		
		textField = new JTextField();
		textField.setBounds(181, 34, 145, 31);
		controlsPanel.add(textField);
		textField.setColumns(10);
		textField.setFont(getDefaultControlsFont());
		
		passwordField = new JPasswordField();
		passwordField.setBounds(180, 103, 145, 31);
		controlsPanel.add(passwordField);
		passwordField.setFont(getDefaultControlsFont());
		
		lblInvalidPassword = new JLabel("Invalid Credentials");
		lblInvalidPassword.setVisible(false);
		lblInvalidPassword.setForeground(Color.RED);
		lblInvalidPassword.setBackground(Color.LIGHT_GRAY);
		lblInvalidPassword.setBounds(59, 140, 208, 35);
		lblInvalidPassword.setFont(getDefaultTitleFont());
		controlsPanel.add(lblInvalidPassword);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBounds(0, 212, 450, 76);
		getContentPane().add(buttonsPanel);
		buttonsPanel.setLayout(null);
		
		btnLogin = new JButton("LogIn");
		btnLogin.setBounds(72, 12, 106, 33);
		btnLogin.setFont(getDefaultTitleFont());
		buttonsPanel.add(btnLogin);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(223, 12, 106, 33);
		btnCancel.setFont(getDefaultTitleFont());
		buttonsPanel.add(btnCancel);
	}
	
	private void createEvents(){
		
		/* Gets the credentials in this window and passes to the model for authentication	*/
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verifyCredentials();
			}
		});
		
		/* Clicking on cancel button will just close the dialog box	*/
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButtonAction();
			}
		});
	}
	
	/*	The obtained credential from the user through the dialog box and the result of the authenticity can be given to the main window	*/ 
	
	private void verifyCredentials(){
		LoginAuthenticator ln = new LoginAuthenticator();
		String username = textField.getText();
		@SuppressWarnings("deprecation")
		String password = passwordField.getText();
		int userstatus = ln.verifyAuthentication(username,password);
		this.setUsername(username);
		this.setUserStatus(userstatus);
		displayStatus();
	}
	
	/*	When the credentials are wrong error message will be displayed
	 * 	When the credentials are right the dialog box will be closed
	 * */
	private void displayStatus(){
		if(this.getUserStatus() == -1){
			this.lblInvalidPassword.setVisible(true);
			this.lblInvalidPassword.setText("Invalid Credentials...");
		}
		else{
			//System.out.println("Closing the window");
			this.textField.setText("");
			this.passwordField.setText("");
			this.dispose();
		}
	}
	
	/* Clicking on cancel button will just close the window	*/
	
	private void cancelButtonAction(){
		Window win = SwingUtilities.getWindowAncestor(this);
	      if (win != null) {
	         win.dispose();
	      }
	}
	
	public Font getDefaultControlsFont(){
		return new Font("Arial",Font.BOLD, 12);
	}
	
	public Font getDefaultTitleFont(){
		return new Font("Arial",Font.BOLD, 12);
	}
}