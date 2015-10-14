package courtreferences.view;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import courtreferences.model.CountryModel;

public class SearchWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JLabel lblCaseId;
	private JButton btnSearch;
	private JButton btnCancel;
	private Vector<String> CountryNameVector;
	private Vector<String> CourtNameVector;
	private JComboBox cmbCourtNames;
	private JComboBox cmbCountryNames;
	private JLabel lblChooseTheCourt;
	private JLabel lblChooseTheCountry;
	private JScrollPane scrollPane;
	private JTable resultTable;
	
	/**
	 * Create the Search Window.
	 */
	public SearchWindow() {
		/*	Initializes the components required for collecting the details of the space where the folder which is needed to be uploaded is present	*/
		initComponents();
		/*	Initializes corresponding event handlers	*/
		createEvents();
	}
	
	/*	Initializes the components required for collecting the details of the space where the folder which is needed to be uploaded is present	*/
	
	private void initComponents(){
		//setLayout(new BorderLayout());
		setTitle("Search Court References");
		this.CountryNameVector = new Vector<String>();
		setBounds(100, 100, 1112, 693);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		//contentPanel.setLayout(null);
		
		lblCaseId = new JLabel("Case Id");
		lblCaseId.setBounds(12, 199, 52, 15);
		contentPanel.add(lblCaseId);
		
		textField = new JTextField();
		textField.setBounds(86, 194, 160, 25);
		//textField.setBounds(30, 110, 182, 24);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.setBounds(12, 291, 100, 25);
		//btnSearch.setBounds(307, 109, 160, 24);
		contentPanel.add(btnSearch);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(146, 291, 100, 25);
		contentPanel.add(btnCancel);
		
		lblChooseTheCountry = new JLabel("Country");
		lblChooseTheCountry.setBounds(12, 90, 56, 15);
		contentPanel.add(lblChooseTheCountry);
		
		this.getCountryComboValues();
		
		cmbCountryNames = new JComboBox(this.CountryNameVector);
		cmbCountryNames.setBounds(86, 85, 160, 24);
		contentPanel.add(cmbCountryNames);
		
		lblChooseTheCourt = new JLabel("Court");
		lblChooseTheCourt.setBounds(12, 137, 56, 15);
		contentPanel.add(lblChooseTheCourt);
		
		cmbCourtNames = new JComboBox();
		cmbCourtNames.setBounds(86, 137, 160, 24);
		contentPanel.add(cmbCourtNames);
		
		String header[] = {"number", "name"};
		String data[][] = {{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"},{"1","satheesh"},{"2","kumar"}};
		resultTable = new JTable(data,header);
		resultTable.setBounds(34, 182, 882, 376);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		//contentPanel.add(resultTable);
		
		scrollPane = new JScrollPane(resultTable);
		scrollPane.setBounds(262, 12, 836, 654);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPanel.add(scrollPane);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(12, 359, 100, 25);
		contentPanel.add(btnUpdate);
		
		JButton btnExport = new JButton("Export");
		btnExport.setBounds(146, 359, 100, 25);
		contentPanel.add(btnExport);
		setVisible(true);
		
		//scrollPane.setVisible(true);
		//contentPanel.setVisible(true);
	}
	
	/*	Initializes corresponding event handlers	*/
	
	private void createEvents(){
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
		 * 	Enter the extracted entities from those pdfs into database
		 *  Close the dialog box once the process is done
		 */
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String countryname = (String) cmbCountryNames.getSelectedItem();
				String courtname = (String) cmbCourtNames.getSelectedItem();
				System.out.println("Selected country name : " + countryname + "  " + courtname);
			}
		});
		
		/*Do nothing when the state of Courts combo box is changed	*/
		
		cmbCourtNames.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
			}
		});
		
		/*	Load the names of courts belonging to corresponding country when the country name is changed	*/
		
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
	}
	
	/*	Method which invokes parsing the pdf content to extract the required information	*/
	
	private void closeDialog(){
		Window win = SwingUtilities.getWindowAncestor(this);
	      if (win != null) {
	         win.dispose();
	      }
	}
	
	/*	Gets the name of the courts to be loaded into the combo box for corresponding country	*/
	
	public Vector<String> getCourtComboValues(String countryname){
		CountryModel upObj = new CountryModel();
		this.setCourtNameVector(upObj.getCourtValues(countryname));
		return this.getCourtNameVector();
	}
	
	/* Gets the values of the Country name combo box	*/
	
	public void getCountryComboValues(){
		CountryModel upObj = new CountryModel();
		this.setCountryNameVector(upObj.getCountryValues());
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

	public JLabel getLblCaseId() {
		return lblCaseId;
	}

	public void setLblCaseId(JLabel lblCaseId) {
		this.lblCaseId = lblCaseId;
	}

	public JButton getBtnSearch() {
		return btnSearch;
	}

	public void setBtnSearch(JButton btnSearch) {
		this.btnSearch = btnSearch;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public JTable getResultTable() {
		return resultTable;
	}

	public void setResultTable(JTable resultTable) {
		this.resultTable = resultTable;
	}
}
