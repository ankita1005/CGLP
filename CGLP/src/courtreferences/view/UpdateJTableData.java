package courtreferences.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JButton;

public class UpdateJTableData extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblCurrentValue;
	private JLabel lblNewValue;
	private JTextPane cValuePane;
	private JTextPane uValuePane;
	private JButton btnUpdate;
	private JButton btnCancel;
	private int row;
	private int column;
	private String className = null;

	public UpdateJTableData(){
	}
	public UpdateJTableData(String fieldName, int row, int column, String currentContent, String className) {
		setBounds(100, 100, 808, 472);
		setResizable(false);
		setModal(true);
		setTitle("Update " + fieldName);
		
		this.setRow(row);
		this.setColumn(column);
		this.setClassName(className);
		/*	Initializes all the components required for the main window	*/
		initComponents(fieldName, currentContent);
		/*	Initializes the events associated with the components of the main window	*/
		createEvents();
	}
	
	/*	Creates all the components and adds to the main window	*/
	public void initComponents(String fieldName, String currentContent){	
		getContentPane().setLayout(null);
		lblCurrentValue = new JLabel("Current Value");
		lblCurrentValue.setBounds(29, 70, 157, 39);
		String titleText = "Current Value for " + fieldName;
		lblCurrentValue.setText(titleText);
		getContentPane().add(lblCurrentValue);
		
		lblNewValue = new JLabel("New Value");
		lblNewValue.setBounds(29, 171, 157, 14);
		titleText = "New Value for " + fieldName;
		lblNewValue.setText(titleText);
		getContentPane().add(lblNewValue);
		
		cValuePane = new JTextPane();
		cValuePane.setText(currentContent);
		cValuePane.setBounds(209, 47, 224, 74);
		getContentPane().add(cValuePane);
		
		uValuePane = new JTextPane();
		uValuePane.setText(currentContent);
		uValuePane.setBounds(209, 147, 224, 74);
		getContentPane().add(uValuePane);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setBounds(216, 265, 89, 23);
		getContentPane().add(btnUpdate);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(344, 265, 89, 23);
		getContentPane().add(btnCancel);
	}	
	
	public void createEvents(){
		btnUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				updateJTableValues();
				closeDialog();
			}
		});
		
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				closeDialog();
			}
		});
	}
	
	/*
	 * Update the public view data of a table corresponding to the particular class 
	 */
	private void updateJTableValues(){
		if(this.getClassName().equals("Main") && Main.tableData[this.row][this.column] != null){
			Main.tableData[this.row][this.column] = this.uValuePane.getText();
		}
		else if(this.getClassName().equals("CitationReferencesView") && CitationReferencesView.tableData[this.row][this.column] != null){
			CitationReferencesView.tableData[this.row][this.column] = this.uValuePane.getText();
		}
	}
	
	private void closeDialog(){
		this.dispose();
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
}
