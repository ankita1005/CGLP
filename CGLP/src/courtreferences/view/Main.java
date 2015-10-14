package courtreferences.view;
/*
 *  unique courtid in the citation results 
 *  exclude the searched terms and include some
 *  send table details of courtid and country id currently being saved
 *  results for canada and a general approach
 */

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JDesktopPane;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import courtreferences.model.CaseDetailsModel;
import courtreferences.model.CountryModel;
import courtreferences.model.CourtDocument;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class Main extends JFrame implements FontDefinition, FocusListener{

	/*
	 * This class contains the components of the main launching window of the application
	 * An empty main window with the default menu bars at the top will be displayed
	 * This class is the "Entry point of the application" 
	 */
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuItem mntmExit;
	private JMenu mnFile;
	private JMenu mnSearch;
	private JMenuItem mntmSearch;
	private JMenu mnReports;
	private JMenu mnSettings;
	private JMenuItem mntmConfiguresettings;
	private JMenu mnAbout;
	private JMenuItem mntmLogin;
	private JMenuItem mntmLogout;
	private JMenuItem mntmAdduser;
	private JMenuItem mntmRemoveuser;
	private JMenuItem mntmChangepassword;
	private JMenuItem mntmUploaddoc;
	private LoginDialog dialog1;
	private DBConfiguration dbConfigDialog;
	private JDesktopPane desktopPane;
	private String username;
	private int userstatus;
	private AddUserDialog adduserdialog;
	private RemoveUserDialog removeuserdialog;
	private UploadDocDialog uploaddocdialog;
	private JScrollPane scrollPane;
	private JTable table = null;
	private JButton btnSearch;
	private JTextField txtSearch;
	private JPanel btnPanel;
	private JButton btnDelete;
	private JComboBox cmbCountryName;
	private JComboBox cmbCourtName;
	private JComboBox cmbSearchOptions;
	private JButton btnExport;
	private JButton btnSave;
	private JButton btnExit;
	public static String tableData[][];
	private static String tableHeader[] = {"CaseRefID", "CountryName","CourtId", "CourtName", "CaseId", "ParticipantsName", "DecisionDate", "HeardDate", "ProcessedDate", "UserId", "SourceFileName"};
	private static boolean isEditableHeader[] = {false,false,false,false,true,true,true,true,false,false,false};
	
	public void setUserStatus(int status){
		this.userstatus = status;
	}
	
	public int getUserStatus(){
		return this.userstatus;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public JComboBox getCmbSearchOptions() {
		return cmbSearchOptions;
	}

	public void setCmbSearchOptions(JComboBox cmbSearchOptions) {
		this.cmbSearchOptions = cmbSearchOptions;
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			/*	Defines the look and feel of the native system where the application runs	*/
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
					frame.setExtendedState( frame.getExtendedState()|JFrame.MAXIMIZED_BOTH );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setTitle("Court References - International Relations");
		this.setUserStatus(-1);
		/*	Initializes all the components required for the main window	*/
		initComponents();
		/*	Initializes the events associated with the components of the main window	*/
		createEvents();
		//this.setUserStatus(dialog1.getUserStatus());
	}
	
	/*	Creates all the components and adds to the main window	*/
	
	private void initComponents(){
		setTitle("Court Reference Extractor - USC International Relations");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 808, 472);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.LIGHT_GRAY);
		menuBar.setForeground(Color.DARK_GRAY);
		setJMenuBar(menuBar);
		
		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		mnFile.setFont(getDefaultTitleFont());
		
		mntmLogin = new JMenuItem("LogIn");
		mntmLogin.setFont(getDefaultTitleFont());
		mnFile.add(mntmLogin);
		
		mntmLogout = new JMenuItem("LogOut");
		mntmLogout.setEnabled(false);
		mntmLogout.setFont(getDefaultTitleFont());
		mnFile.add(mntmLogout);
		
		mntmAdduser = new JMenuItem("AddUser");
		mntmAdduser.setEnabled(false);
		mntmAdduser.setFont(getDefaultTitleFont());
		mnFile.add(mntmAdduser);
		
		mntmRemoveuser = new JMenuItem("RemoveUser");
		mntmRemoveuser.setEnabled(false);
		mntmRemoveuser.setFont(getDefaultTitleFont());
		mnFile.add(mntmRemoveuser);
		
		mntmChangepassword = new JMenuItem("ChangePassword");
		mntmChangepassword.setEnabled(false);
		mntmChangepassword.setFont(getDefaultTitleFont());
		mnFile.add(mntmChangepassword);
		
		mntmExit = new JMenuItem("Exit");
		mnFile.add(mntmExit);
		mntmExit.setFont(getDefaultTitleFont());
		
		mnSearch = new JMenu("Search");
		mnSearch.setEnabled(false);
		mnSearch.setFont(getDefaultTitleFont());
		menuBar.add(mnSearch);
		
		mntmUploaddoc = new JMenuItem("UploadDoc");
		mnSearch.add(mntmUploaddoc);
		mntmUploaddoc.setFont(getDefaultTitleFont());
		
		mntmSearch = new JMenuItem("Search");
		mnSearch.add(mntmSearch);
		mntmSearch.setFont(getDefaultTitleFont());
		
		mnReports = new JMenu("Reports");
		mnReports.setEnabled(false);
		menuBar.add(mnReports);
		mnReports.setFont(getDefaultTitleFont());
		
		mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		mnSettings.setFont(getDefaultTitleFont());
		
		mntmConfiguresettings = new JMenuItem("ConfigureSettings");
		mnSettings.add(mntmConfiguresettings);
		mntmConfiguresettings.setFont(getDefaultTitleFont());
		mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		mnAbout.setFont(getDefaultTitleFont());
	}
	
	private void addScrollPanelControls(){
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setRequestFocusEnabled(false);
        contentPane.addFocusListener((FocusListener) this);
		
		desktopPane = new JDesktopPane();
		contentPane.add(desktopPane);
		
		scrollPane = new JScrollPane();
		desktopPane.setLayout(new BorderLayout(0, 0));
		desktopPane.add(scrollPane);
		table = new JTable(getMainTableData(), getMainTableHeader());
		table.setModel(new DefaultTableModel(getMainTableData(), getMainTableHeader()));
		table.setFont(getDefaultControlsFont());
		table.getTableHeader().setFont(getDefaultTitleFont());
		listenToJTableEvents();
		scrollPane.setViewportView(table);
  	  	scrollPane.setEnabled(false);
  	  	scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
  	  	scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
  	  	adjustJTableColumnWidth();
	}
	
	private void addButtonPanelControls(){
		btnPanel = new JPanel();
		desktopPane.add(btnPanel, BorderLayout.NORTH);
		
		btnExport = new JButton("Export");
		btnPanel.add(btnExport);
		btnExport.setFont(getDefaultControlsFont());
		
		btnDelete = new JButton("Delete");
		btnPanel.add(btnDelete);
		btnDelete.setFont(getDefaultControlsFont());
		
		btnSave = new JButton("Save");
		btnPanel.add(btnSave);
		btnSave.setFont(getDefaultControlsFont());
		
		btnExit = new JButton("Exit");
		btnPanel.add(btnExit);
		btnExit.setFont(getDefaultControlsFont());
		
		Vector<String> countryNameVector = getCountryComboValues();
		cmbCountryName = new JComboBox(countryNameVector);
		btnPanel.add(cmbCountryName);
		cmbCountryName.setFont(getDefaultControlsFont());
		
		Vector<String> courtNameVector = getCourtComboValues(cmbCountryName.getSelectedItem().toString());
		cmbCourtName = new JComboBox(courtNameVector);
		btnPanel.add(cmbCourtName);
		cmbCourtName.setFont(getDefaultControlsFont());
		
		String[] searchOptions = {"Search By CaseId", "Search By Country and CourtName", "Search By Title", "Search By Processed Date", "Search By CaseReference Id", "Search By User"};
		cmbSearchOptions = new JComboBox(searchOptions);
		btnPanel.add(cmbSearchOptions);
		cmbSearchOptions.setFont(getDefaultControlsFont());
		
		txtSearch = new JTextField();
		btnPanel.add(txtSearch);
		txtSearch.setColumns(10);
		txtSearch.setFont(getDefaultControlsFont());
		
		btnSearch = new JButton("Search");
		btnPanel.add(btnSearch);
		btnSearch.setFont(getDefaultControlsFont());
	}
	
	/* Contains all the event handler mechanism	for Main Window Menu events*/
	
	private void createEvents(){
		
		/*	Exit when the "Exit" button is pressed	*/
		
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disableControls();
				System.exit(0);
			}
		});
		
		/*	Open "Login dialog box" when the Login button is pressed	*/
		
		mntmLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				openDialogAction();				
			}
		});	
		
		/*	Disable all the entities and close the db connection when the logout button is pressed	*/
		
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disableControls();
			}
		});
		
		/*	Launch Open db setting window when this button is pressed */
		
		mntmConfiguresettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configureDbSettings();
			}
		});
		
		/*	Launch "Add New User dialog box" when the "Add User" button is pressed	*/
		
		mntmAdduser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addNewUserToDB();
			}
		});
		
		
		/*	Launch "Delete Existing User dialog box" when the "Delete User" button is pressed	*/
		
		mntmRemoveuser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeUserFromDB();
			}
		});
		
		/*	Launch the upload documents dialog box when this buttons is pressed	*/
		
		mntmUploaddoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayUploadDocDialog();
			}
		});
		
		mntmSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new CitationReferencesView(-1);
			}
		});
	}
	/* Contains all the event handler mechanism	for after Login Window Menu events */
	private void listenButtonPanelEvents(){
		
		btnExport.addActionListener(new ActionListener() {
			/*
			 * The content in the current jtable will be exported to csv file when the export button is pressed
			 */
			public void actionPerformed(ActionEvent arg0) {
				FileDialog fDialog = new FileDialog(new JFrame(),"Export Content", FileDialog.SAVE);
		        fDialog.setVisible(true);
		        fDialog.setFont(getDefaultTitleFont());
		        String path = fDialog.getDirectory() + fDialog.getFile();
		        ExportToCSV.exportToCSV(table, path, getMainTableHeader());
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			/*
			 * The selected record will be removed from database when this button is pressed 
			 */
			public void actionPerformed(ActionEvent arg0) {
				int selectedRows[] = table.getSelectedRows();
				int numberOfRowsDeleted = 0;
				if(displayConfirmDeleteDialogBox(selectedRows.length) == 0){
					for(int rowId : selectedRows){
						int caseRefId = Integer.parseInt(getValueforCell(rowId,0));
						int deleteStatus = CaseDetailsModel.deleteCaseDetails(caseRefId);
						if(deleteStatus == 1){
							numberOfRowsDeleted += 1;
						}
						else if(deleteStatus == 0){
							int caseId = Integer.parseInt(getValueforCell(rowId, 0));
							displayForeignKeyAlertBox(caseId);
						}
						else if(deleteStatus == -1){
							displayExceptionAlertBox();
						}
					}
					displayDeleteStatusDialogBox(numberOfRowsDeleted, selectedRows.length);
					reloadDataIntoJTable(getMainTableData());
				}
			}
		});
		
		btnSearch.addActionListener(new ActionListener() {
			/*
			 * The cases with specified search criteria will be searched in the database when the Search button is pressed 
			 */
			public void actionPerformed(ActionEvent e) {
				String[][] searchResult = searchCases(cmbSearchOptions.getSelectedIndex(), txtSearch.getText().toString());
				if(searchResult != null)
					reloadDataIntoJTable(searchResult);
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			/*
			 * Application is closed on pressing this button 
			 */
			public void actionPerformed(ActionEvent e) {
				disableControls();
				System.exit(0);
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			/*
			 * Pressing on this button saves all these updates in the table and converts the status of those updated records
			 */
			public void actionPerformed(ActionEvent e) {
				int selectedRows[] = table.getSelectedRows();
				for(int rowId : selectedRows){
					int caseRefId = Integer.parseInt(getValueforCell(rowId,0));
					CaseDetailsModel.updateProcessedStatus(caseRefId);
				}
				reloadDataIntoJTable(getMainTableData());
			}
		});
		
		cmbCountryName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				addCourtNameComboValues();
			}
		});
		
		txtSearch.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
	
	private void listenToJTableEvents(){
		table.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e){
				
			}
		});
		
		/*
		 * Tracks mouse pressed and mouse released events of jtable 
		 * When a record is right clicked the case references for the corresponding case will be opened in other window
		 */
		
		table.addMouseListener( new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if(e.isPopupTrigger()){
                	JTable source = (JTable)e.getSource();
                	int row = source.rowAtPoint( e.getPoint() );
                    int column = source.columnAtPoint( e.getPoint() );
                    if(!source.isRowSelected(row))
                        source.changeSelection(row, column, false, false);
                    int refId = Integer.parseInt(getValueforCell(row, 0));
                    new CitationReferencesView(refId);
                }
            }
            
            public void mouseClicked(MouseEvent e){
            	JTable source = (JTable)e.getSource();
            	int row = source.rowAtPoint( e.getPoint());
                int column = source.columnAtPoint( e.getPoint());
                if(Main.isEditableHeader[column]){
                	new UpdateJTableData(Main.tableHeader[column], row, column, Main.tableData[row][column], "Main").setVisible(true);
                	updateJTableValuesToDB(row);
                	reloadDataIntoJTable(getMainTableData());
                }
            }

            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    JTable source = (JTable)e.getSource();
                    int row = source.rowAtPoint( e.getPoint() );
                    int column = source.columnAtPoint( e.getPoint() );
                    if(!source.isRowSelected(row))
                        source.changeSelection(row, column, false, false);
                    int refId = Integer.parseInt(getValueforCell(row, 0));
                    new CitationReferencesView(refId);
                }
            }
        });
	}
	
	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		reloadDataIntoJTable(getMainTableData());
	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
	}
	
	/*	Enables all the controls in the main window	*/
	
	private void enableControls(){
		if(this.getUserStatus() == 0){
			this.mntmRemoveuser.setEnabled(true);
			this.mntmAdduser.setEnabled(true);
		}
		addScrollPanelControls();
		addButtonPanelControls();
		listenButtonPanelEvents();
		this.mntmLogout.setEnabled(true);
		this.mntmChangepassword.setEnabled(true);
		this.mnReports.setEnabled(true);
		this.mnSearch.setEnabled(true);
		this.table.setVisible(true);
	}
	
	/*	Disable all the controls in the main window	*/
	
	private void disableControls(){
		this.mntmRemoveuser.setEnabled(false);
		this.mntmAdduser.setEnabled(false);
		this.mntmLogout.setEnabled(false);
		this.mntmChangepassword.setEnabled(false);
		this.mnReports.setEnabled(false);
		this.mnSearch.setEnabled(false);
		if(this.table != null)
			this.table.setVisible(false);
		System.exit(0);
	}
	
	
	/*	Create a new window object for uploading a Document	*/
	
	private void displayUploadDocDialog(){
		uploaddocdialog = new UploadDocDialog(this);
		//uploaddocdialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		uploaddocdialog.setProcessedUser(this.getUsername());
	}
	
	/*	Create a new Add user dialog box object	*/
	
	private void addNewUserToDB(){
		if (this.adduserdialog == null)
	         this.adduserdialog = new AddUserDialog();
	      this.adduserdialog.setVisible(true);
	}
	
	/*	Create a new Remove user dialog box object	*/
	
	private void removeUserFromDB(){
		if (this.removeuserdialog == null) {
	         this.removeuserdialog = new RemoveUserDialog();
	      }
		this.removeuserdialog.setVisible(true);
	}
	
	/*	Create a Configure Db Settings dialog box object	*/
	
	private void configureDbSettings(){
		 if (this.dbConfigDialog == null) {
	         this.dbConfigDialog = new DBConfiguration();
	      }
	      this.dbConfigDialog.setVisible(true);
	}
	
	/*	Method to check user validity and enable all the controls if the user is legitimate	*/
	
	private void openDialogAction(){
	      if (dialog1 == null) {
	         dialog1 = new LoginDialog();
	      }
	      dialog1.setVisible(true);
	      this.setUsername(dialog1.getUsername());
	      this.setUserStatus(dialog1.getUserStatus());
	      	      
	      /*	Enable the controls in the main window only when the user is being authenticated by our authentication system	*/
	      
	      if(this.getUserStatus() == 0 || this.getUserStatus() == 1){
	    	  enableControls();
	      }
	}

	private String[][] searchCases(int selectedIndex, String searchTerm){
		/*
		 * Search cases depending upon the input criteria
		 */
		if(selectedIndex == 1)
			searchTerm = cmbCountryName.getSelectedItem() + "\t" + cmbCourtName.getSelectedItem();
		try{
			if(selectedIndex == 4 && Integer.parseInt(searchTerm) > 0)
				System.out.println("Correct Integer");
		}
		catch(NumberFormatException ex){
			JOptionPane.showMessageDialog(this, "Please enter number as Case Reference Id");
			return null;
		}
		return convertTableDataToString(CaseDetailsModel.selectCaseDetails(selectedIndex, searchTerm));		
	}
	
	/*
	 * Selects records from Database table
	 */
	public String[][] getMainTableData(){
		return convertTableDataToString(CaseDetailsModel.selectUnprocessedRecords());
	}
		
	public void reloadDataIntoJTable(String[][] tableData){
		/*
		 * Reloads data into jtable from CitationDetails table
		 */
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		for(String[] record : tableData){
			tableModel.addRow(record);
		}
		table.setModel(tableModel);
		tableModel.fireTableDataChanged();
	}
	
	private void adjustJTableColumnWidth(){
		/*
		 * Adjusts the column width in the jtable
		 */
		int columnSize[] = {6,10,10,10,400,30,30,30,10};
		for(int i=0; i<columnSize.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(columnSize[i]);
	}
	
	
	private String[] getMainTableHeader(){
		/*
		 * Sets the header of the jtable
		 */
		return tableHeader;
	}
	
	private String[][] convertTableDataToString(List<CourtDocument> hcList){
		/*
		 * Converts data from CaseDetails table into displayable format in jtable
		 */
		tableData = new String[hcList.size()][12];
		int i = 0;
		for(CourtDocument currentHeaderData : hcList){
			tableData[i][0] = String.valueOf(currentHeaderData.getCaseRefId());
			tableData[i][1] = currentHeaderData.getCountryName();
			tableData[i][2] = String.valueOf(currentHeaderData.getCourtId());
			tableData[i][3] = currentHeaderData.getCourtName();
			tableData[i][4] = currentHeaderData.getCaseId();
			tableData[i][5] = currentHeaderData.getParticipantsName();
			tableData[i][6] = currentHeaderData.getDecisionDate();
			tableData[i][7] = currentHeaderData.getHeardDate();
			tableData[i][8] = currentHeaderData.getProcessedDate().toString();
			tableData[i][9] = currentHeaderData.getProcessedUser();
			tableData[i][10] = currentHeaderData.getSourceFileName();
			tableData[i][11] = String.valueOf(currentHeaderData.getDuplicateCaseof());
			i += 1;
		}
		return tableData;
	}
	
	/*
	 * Returns the current data of the cell with row and column in the jtable view
	 */
	private static String getUpdatedCellValue(int row, int col){
		return Main.tableData[row][col];
	}
	
	/*
	 * Returns the current data of the cell with row and column in the jtable view
	 */
	
	private String getValueforCell(int row, int col){
        return (String)table.getModel().getValueAt(row,col);
    }
	
	private Vector<String> getCountryComboValues(){
		CountryModel upObj = new CountryModel();
		return upObj.getCountryValues();
	}
	
	private Vector<String> getCourtComboValues(String countryname){
		CountryModel upObj = new CountryModel();
		return upObj.getCourtValues(countryname);
	}
	
	private void addCourtNameComboValues(){
		/*
		 * Adds court names to the court name combo box
		 */
		String countryname = (String) cmbCountryName.getSelectedItem();
		Vector<String> courtNameVector = getCourtComboValues(countryname);
		cmbCourtName.removeAllItems();
		Iterator<String> itr = courtNameVector.iterator();
		while(itr.hasNext()){
			String courtname = (String) itr.next();
			cmbCourtName.addItem(courtname);
		}
	}
	
	private int displayConfirmDeleteDialogBox(int numberOfRows){
		/*
		 * Delete confirm dialog box
		 */
		String alertMessage = "Are you sure you want to remove " + numberOfRows + " case(s) from your database?"; 
		String alertWindowTitle = "Warning! - Removes Records from db";		
		return JOptionPane.showInternalConfirmDialog(this.getContentPane(), alertMessage, alertWindowTitle, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}
	
	private int displayDeleteStatusDialogBox(int deletedRows, int numberOfRows){
		/*
		 * Delete Status dialog box
		 */
		String alertMessage = deletedRows + " out of " + numberOfRows + " are successfully removed"; 
		String alertWindowTitle = "Delete Status";		
		return JOptionPane.showInternalConfirmDialog(this.getContentPane(), alertMessage, alertWindowTitle, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private int displayForeignKeyAlertBox(int caseId){
		/*
		 * Case Details cannot be deleted when they have references associated with them
		 * Case Refereces should be deleted first and then only case can be deleted
		 */
		String alertMessage = "The CaseId " + caseId + " has case references associated with it. Please remove the references by right cliking on that row and then try again"; 
		String alertWindowTitle = "Delete Status";		
		return JOptionPane.showInternalConfirmDialog(this.getContentPane(), alertMessage, alertWindowTitle, JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
	}
	
	private int displayExceptionAlertBox(){
		/*
		 * This is a random error. It may be a result of some programming bug or any other random bug.
		 */
		String alertMessage = "There is some issue with the system. Please check whether everything is all right."; 
		String alertWindowTitle = "Some Random Error";		
		return JOptionPane.showInternalConfirmDialog(this.getContentPane(), alertMessage, alertWindowTitle, JOptionPane.OK_OPTION, JOptionPane.WARNING_MESSAGE);
	}
	
	/* 
	 * Reads updated values from the maintable view using the static public variable tableData and updates the database
	 */
	
	private void updateJTableValuesToDB(int row){
        int caseRefId = Integer.parseInt(getUpdatedCellValue(row,0));
        String caseId = getUpdatedCellValue(row, 3);
		String participantsName = getUpdatedCellValue(row,4);
		String decisionDate = getUpdatedCellValue(row,5);
		String heardDate = getUpdatedCellValue(row, 6);
		CaseDetailsModel.updateCaseDetails(caseRefId, caseId, participantsName, decisionDate, heardDate);
	}
	
	@Override
	public Font getDefaultControlsFont(){
		return new Font("Arial",Font.BOLD, 10);
	}
	
	@Override
	public Font getDefaultTitleFont(){
		return new Font("Arial",Font.BOLD, 12);
	}
}