package courtreferences.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import courtreferences.model.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RemoveUserDialog extends JDialog implements FontDefinition{

	/* 
	 * This class contains the components of the "RemoveUser Dialog box"
	 * Purpose : Functionalities for removing user from the system 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtUserName;
	private LoginAuthenticator updateObj = null;
	private JButton btnDeleteUser;
	private JButton cancelButton;
	private JLabel lblEnterUserName;

	 /**
	 * Create the dialog.
	 */
	public RemoveUserDialog() {
		/*	Creates the components required for this window	*/
		initiateComponents();
		/*	Initializes the events associated with the components	*/
		createEvents();
	}
	
	/*	Creates the components required for this window	*/
	
	private void initiateComponents(){
		setResizable(false);
	 	setTitle("Remove User");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			lblEnterUserName = new JLabel("Enter User Name");
			lblEnterUserName.setBounds(52, 101, 149, 33);
			lblEnterUserName.setFont(getDefaultControlsFont());
			contentPanel.add(lblEnterUserName);
		}
		{
			txtUserName = new JTextField();
			txtUserName.setBounds(206, 104, 171, 27);
			txtUserName.setFont(getDefaultControlsFont());
			contentPanel.add(txtUserName);
			txtUserName.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnDeleteUser = new JButton("RemoveUser");
				btnDeleteUser.setActionCommand("OK");
				buttonPane.add(btnDeleteUser);
				btnDeleteUser.setFont(getDefaultControlsFont());
				getRootPane().setDefaultButton(btnDeleteUser);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.setFont(getDefaultControlsFont());
				buttonPane.add(cancelButton);
			}
		}
	}
	
	/*	Initilizes the events associated with the components	*/
	
	private void createEvents(){
		/*	Remove the details of the user from the database	*/
		btnDeleteUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeUserDetails();
			}
		});
		/* Closes this dialog	*/
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
	}
	
	/*	Shows failure message when the user tries to delete who is not in the system	*/
	
	private void showFailureDeleteDialog(){
		String failure_msg = "The user " + this.txtUserName.getText() + " is not present in the system";
		JOptionPane.showMessageDialog(null,failure_msg);
	}
	
	/*	Shows success message after the user is successdully removed from the system	*/
	
	private void showSuccessDeleteDialog(){
		String success_msg = "The user " + this.txtUserName.getText() + " has been succesfully removed the system";
		JOptionPane.showMessageDialog(null,success_msg);
	}
	
	/*	Communicates with the model and removes the user details	*/
	
	private void removeUserDetails(){
		if(this.updateObj == null){
			this.updateObj = new LoginAuthenticator();
		}
		int returnvalue = this.updateObj.deleteExistingUser(this.txtUserName.getText());
		if(returnvalue == 0){
			showFailureDeleteDialog();
			clearValues();
		}
		else{
			showSuccessDeleteDialog();
			clearValues();
			this.dispose();
		}
	}
	
	private void clearValues(){
		this.txtUserName.setText("");
	}
	
	private void closeDialog(){
		this.dispose();
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
