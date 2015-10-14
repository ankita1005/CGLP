package courtreferences.view;


import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComboBox;

import org.apache.commons.io.FileUtils;

import courtreferences.controller.DocumentParser;
import courtreferences.controller.DocumentParserFactory;
import courtreferences.model.*;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class UploadDocDialog extends JDialog implements FontDefinition {

	/* 
	 * This class contains the components of the "UploadDocument Dialog box"
	 * Purpose : Functionalities for uploading the documents and extract content from the files 
	 */
	
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JFileChooser jFileChooser;
	private JTextField textField;
	private JLabel lblChooseYourDocument;
	private JButton btnUpload;
	private JButton btnCancel;
	private DocumentParser parseObj;
	private Vector<String> CountryNameVector;
	private Vector<String> CourtNameVector;
	private Vector<String> LangVector;
	private JComboBox cmbCourtNames;
	private JComboBox cmbCountryNames;
	private JComboBox cmbLang;
	private JLabel lblChooseTheCourt;
	private JLabel lblChooseTheCountry;
	private JLabel lblChooseTheLang;
	private String processedUser;
	private Main parentWindow;
	
	/**
	 * Create the dialog.
	 */
	public UploadDocDialog() {
		
	}
	
	public UploadDocDialog(Main parentWindow){
		setResizable(false);
		//setModal(true);
		setVisible(true);
		/*	Initializes the components required for collecting the details of the space where the folder which is needed to be uploaded is present	*/
		initComponents();
		/*	Initializes corresponding event handlers	*/
		createEvents();
		this.setParentWindow(parentWindow);
		if(parentWindow == null)
			System.out.println("Null");
	}
	
	/*	Initializes the components required for collecting the details of the space where the folder which is needed to be uploaded is present	*/
	
	private void initComponents(){
		setFont(getDefaultTitleFont());
		setTitle("Upload Your Documents");
		this.CountryNameVector = new Vector<String>();
		setBounds(100, 100, 603, 393);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblChooseYourDocument = new JLabel("Choose your document");
		lblChooseYourDocument.setBounds(313, 40, 193, 43);
		contentPanel.add(lblChooseYourDocument);
		lblChooseYourDocument.setFont(getDefaultTitleFont());
		
		textField = new JTextField();
		textField.setBounds(33, 46, 218, 31);
		textField.setColumns(10);
		textField.setFont(getDefaultControlsFont());
		contentPanel.add(textField);
		
		btnUpload = new JButton("Upload");
		btnUpload.setBounds(107, 320, 100, 30);
		btnUpload.setFont(getDefaultTitleFont());
		contentPanel.add(btnUpload);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(269, 320, 114, 30);
		btnCancel.setFont(getDefaultTitleFont());
		contentPanel.add(btnCancel);
		
		lblChooseTheCountry = new JLabel("Choose the country");
		lblChooseTheCountry.setBounds(313, 122, 193, 37);
		lblChooseTheCountry.setFont(getDefaultControlsFont());
		contentPanel.add(lblChooseTheCountry);
		
		this.getCountryComboValues();
		
		cmbCountryNames = new JComboBox(this.CountryNameVector);
		cmbCountryNames.setBounds(33, 122, 218, 31);
		cmbCountryNames.setFont(getDefaultControlsFont());
		contentPanel.add(cmbCountryNames);
		
		lblChooseTheCourt = new JLabel("Choose the Court");
		lblChooseTheCourt.setBounds(313, 195, 162, 30);
		lblChooseTheCourt.setFont(getDefaultControlsFont());
		contentPanel.add(lblChooseTheCourt);
		
		cmbCourtNames = new JComboBox();
		cmbCourtNames.setBounds(33, 195, 218, 30);
		cmbCourtNames.setFont(getDefaultTitleFont());
		contentPanel.add(cmbCourtNames);
		
		/**lblChooseTheLang = new JLabel("Choose the Language");
		lblChooseTheLang.setBounds(313, 255, 132, 23);
		lblChooseTheLang.setFont(getDefaultControlsFont());
		contentPanel.add(lblChooseTheLang);
		
		this.getLangValues();
		
		cmbLang = new JComboBox(this.LangVector);
		cmbLang.setBounds(33, 255, 218, 29);
		cmbLang.setFont(getDefaultTitleFont());
		contentPanel.add(cmbLang);**/
	}
	
	private void createEvents(){
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		
		/*	Open the FileChooser dialog box when the mouse is clicked within the textbox	
		 * 	Get the directory path as input from the window
		*/
		
		textField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				jFileChooser = new JFileChooser();
				jFileChooser.setFont(getDefaultTitleFont());
				jFileChooser.setCurrentDirectory(new java.io.File("."));
				jFileChooser.setDialogTitle("Upload Files");
				
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				//jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jFileChooser.setAcceptAllFileFilterUsed(false);
				if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	                File directoryPath = jFileChooser.getCurrentDirectory();
	                textField.setText(directoryPath.getPath());
	                //System.out.println("Name of the directory : " + directoryPath.getPath());
	            }
				else {
	            }				
			}
		});
		
		/* Close the dialog box when the Cancel button is pressed	*/
		
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		
		/*  Pass the entire directory structure to the model.
		 * 	Process all the files in the directory 
		 * 	Enter the extracted entities from those pdfs/htmls into database
		 *  Close the dialog box once the process is done
		 */
		
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String countryname = (String) cmbCountryNames.getSelectedItem();
				String courtname = (String) cmbCourtNames.getSelectedItem();
				//System.out.println("Selected country name : " + countryname + "  " + courtname);
				parseCaseDocuments(countryname,courtname);
				closeDialog();
				System.out.println("Inside upload doc dialog");
				updateMainWindowTable();
				new CitationReferencesView(-1);
			}
		});
		
		/*Do nothing when the state of Courts combo box is changed	*/
		
		cmbCourtNames.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		
		/*	
		 * Load the names of courts belonging to corresponding country when the country name is changed	
		 */
		
		cmbCountryNames.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				String countryname = (String) cmbCountryNames.getSelectedItem();
				Vector<String> courtNameVector = getCourtComboValues(countryname);
				cmbCourtNames.removeAllItems();
				Iterator<String> itr = courtNameVector.iterator();
				while(itr.hasNext()){
					String courtname = (String) itr.next();
					cmbCourtNames.addItem(courtname);
				}
			}
		});
	
		/*Do nothing when the state of Lang combo box is changed	*/
		
		/**cmbLang.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				String lang = (String) cmbLang.getSelectedItem();
			}
		});**/
	}
	
		
	private void updateMainWindowTable(){
		String[][] mainTableData = this.getParentWindow().getMainTableData();
		this.getParentWindow().reloadDataIntoJTable(mainTableData);
	}
	
	/*	Method which invokes parsing the pdf content to extract the required information	*/
	
	private void parseCaseDocuments(String countryname,String courtname){
		File dir = new File(this.textField.getText());
		for(File child : dir.listFiles()){
			CourtDocument cdt = null;
			int extensionInd = child.getName().lastIndexOf(".");
			String extension = child.getName().substring(extensionInd+1);
			System.out.println(extension);
			
		//skannan-comment: removed check for country name as there are going to be only two document types pdf and html
			if("pdf".equalsIgnoreCase(extension) || "html".equalsIgnoreCase(extension) || "htm".equalsIgnoreCase(extension)){
				if(this.parseObj == null){
					this.parseObj = DocumentParserFactory.getDocumentParser(extension, countryname, courtname, this.getProcessedUser());
				}	
				System.out.println("Processing :"+child.getName());
				try{
					cdt = this.parseObj.processDocumentForCaseDetails(child.getAbsolutePath(),child.getName());
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				if(displayConfirmSkipDialogBox() == 1)
					break;
				else
					continue;
			}
			if(cdt != null){
				int dcaseID = CaseDetailsModel.checkForDuplicates(cdt.getCountryId(), cdt.getCourtId(), cdt.getCaseId()); 
				if(dcaseID > 0){
					cdt.setDuplicateCaseof(dcaseID);
					cdt.writeToDb();
				}
				else{
					cdt.writeToDb();
				}
			}
		}
	}
	
	private void closeDialog(){
		Window win = SwingUtilities.getWindowAncestor(this);
	      if (win != null) {
	         win.dispose();
	      }
	}
	
	private int displayConfirmSkipDialogBox(){
		String alertMessage = "Error in file format \n Press YES to skip this file and continue processing next.\n Press NO otherwise"; 
		String alertWindowTitle = "Incompatible File format";		
		return JOptionPane.showInternalConfirmDialog(this.getContentPane(), alertMessage, alertWindowTitle, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private int displayDuplicateDialogBox(){
		String alertMessage = "Seems this case already exists.\nPress No to skip this entry or Yes to make a new entry for this case\n"; 
		String alertWindowTitle = "Case already exists";		
		return JOptionPane.showInternalConfirmDialog(this.getContentPane(), alertMessage, alertWindowTitle, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/*	Gets the name of the courts to be loaded into the combo box for corresponding country	*/
	
	public Vector<String> getCourtComboValues(String countryname){
		CountryModel upObj = new CountryModel();
		this.setCourtNameVector(upObj.getCourtValues(countryname));
		return this.getCourtNameVector();
	}
	
	/* Gets the values of the Country name combox box	*/
	
	public void getCountryComboValues(){
		CountryModel upObj = new CountryModel();
		this.setCountryNameVector(upObj.getCountryValues());
	}

	public void getLangValues(){
		Vector<String> langNames = new Vector<String>();
		langNames.add("English");
		langNames.add("Spanish");
		langNames.add("French");
		langNames.add("German");
		this.setLangVector(langNames);
		//return langNames;
	}
	
	/**public void getLangValues(){
		LangModel lObj = new LangModel();
		this.setCountryNameVector(lObj.getLangValues());
	}**/
	
	public Vector<String> getLangVector() {
		return LangVector;
	}

	public void setLangVector(Vector<String> langVector) {
		LangVector = langVector;
	}
		
	public Vector<String> getCountryNameVector() {
		return CountryNameVector;
	}

	public void setCountryNameVector(Vector<String> countryNameVector) {
		CountryNameVector = countryNameVector;
	}

	public Vector<String> getCourtNameVector() {
		return CourtNameVector;
	}

	public void setCourtNameVector(Vector<String> courtNameVector) {
		CourtNameVector = courtNameVector;
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

	public String getProcessedUser() {
		return processedUser;
	}

	public void setProcessedUser(String processedUser) {
		this.processedUser = processedUser;
	}

	public Main getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(Main parentWindow) {
		this.parentWindow = parentWindow;
	}
}
