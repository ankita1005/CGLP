package courtreferences.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

import courtreferences.model.*;

public class AddUserDialog extends JDialog {

	/* 
	 * This class contains the components of the "AddUser Dialog box"
	 * Purpose : Functionalities for adding new user to the system 
	 */
	private static final long serialVersionUID = 1500815817804341818L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtNewUserName;
	private JTextField txtDefaultPassword;
	private JButton btnAddUser;
	private JButton cancelButton;
	private JLabel lblEnterNewUser;
	private JLabel lblDefaultPassword;
	private JLabel lblUserRole;
	private JComboBox cmbUserRole;
	private static String[] userroles = {"user","admin"};
	private LoginAuthenticator updateObj = null;

	/**
	 * Create the dialog.
	 */
	public AddUserDialog() {
		setTitle("Add User");
		setResizable(false);
		/*	Initialize the components for the Add User dialog box	*/
		initComponents();
		/*	Initialize the events for the Add User dialog box	*/
		createEvents();
	}
	
	private void initComponents(){
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblEnterNewUser = new JLabel("Enter New User Name");
			lblEnterNewUser.setBounds(54, 53, 195, 39);
			contentPanel.add(lblEnterNewUser);
		}
		{
			lblDefaultPassword = new JLabel("Default Password");
			lblDefaultPassword.setBounds(54, 106, 217, 39);
			contentPanel.add(lblDefaultPassword);
		}
		{
			txtNewUserName = new JTextField();
			txtNewUserName.setBounds(222, 59, 188, 28);
			contentPanel.add(txtNewUserName);
			txtNewUserName.setColumns(10);
		}
		{
			txtDefaultPassword = new JTextField();
			txtDefaultPassword.setBounds(222, 112, 188, 28);
			contentPanel.add(txtDefaultPassword);
			txtDefaultPassword.setColumns(10);
		}
		{
			lblUserRole = new JLabel("User Role");
			lblUserRole.setBounds(54, 163, 188, 39);
			contentPanel.add(lblUserRole);
		}
		
		cmbUserRole = new JComboBox(AddUserDialog.userroles);
		cmbUserRole.setBounds(222, 170, 188, 24);
		contentPanel.add(cmbUserRole);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnAddUser = new JButton("AddUser");
				btnAddUser.setActionCommand("OK");
				buttonPane.add(btnAddUser);
				getRootPane().setDefaultButton(btnAddUser);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void createEvents(){
		txtNewUserName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updatePasswordTextBoxValue();
			}
		});
		
		/*	Adds new user to the system when the button is clicked	*/
		
		btnAddUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				insertUserDetails();
			}
		});
		
		/*	Clicking on cancel button clears all the values and closes the dialog */
		
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearValues();
				closeDialog();
			}
		});
	}
	
	/*	Adds new user to the system - Only administrator can do this	*/
	
	private void insertUserDetails(){
		if(this.updateObj == null){
			this.updateObj = new LoginAuthenticator();
		}
		int returnvalue = this.updateObj.insertNewUser(this.txtNewUserName.getText(), this.txtDefaultPassword.getText(),(String)this.cmbUserRole.getSelectedItem());
		if(returnvalue == 0){
			showFailureInsertDialog();
			clearValues();
		}
		else{
			showSuccessInsertDialog();
			clearValues();
			this.dispose();
		}
	}
	
	private void updatePasswordTextBoxValue(){
		this.txtDefaultPassword.setText(this.txtNewUserName.getText());
	}
	
	/* Dialog to display failure message when the user is already present	*/
	
	private void showFailureInsertDialog(){
		String failure_msg = "The user " + this.txtNewUserName.getText() + " is already present in the system";
		String window_title = "Error adding new User";
		JOptionPane.showInternalConfirmDialog(this.getContentPane(),failure_msg,window_title, JOptionPane.WARNING_MESSAGE);
	}
	
	/* Dialog to display the success message when the user name is currently enrolled	*/

	private void showSuccessInsertDialog(){
		String success_msg = "The new user " + this.txtNewUserName.getText() + " is added to the system";
		JOptionPane.showMessageDialog(null,success_msg);
	}
	
	private void clearValues(){
		this.txtNewUserName.setText("");
		this.txtDefaultPassword.setText("");
	}
	
	private void closeDialog(){
		this.dispose();
	}
}
