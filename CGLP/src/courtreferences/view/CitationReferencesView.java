
package courtreferences.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import courtreferences.model.CitationCases;
import courtreferences.model.CitationReferenceModel;

public class CitationReferencesView extends JFrame implements FontDefinition{

	/* 
	 * This class contains the components for displaying Citation References for each case
	 * Purpose : Functionalities for displaying citation references
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JDesktopPane desktopPane;
	private JScrollPane scrollPane;
	private JTable table;
	private JPanel btnPanel;
	private JButton btnDelete;
	private JTextField txtSearch;
	private JButton btnSearch;
	private JButton btnExit;
	private JButton btnExport;
	private int refId;
	private JButton btnSave;
	private JComboBox cmbSearchOptions;
	public static String tableData[][];
	private static String tableHeader[] = {"CaseRefID", "CitationRefID", "CitationType", "CitationID", "Page Number", "CountryName", "CourtName", "CaseId", "Other Details"};
	private static boolean isEditableHeader[] = {false,false,false,false,true,false,false,true,true};

	public CitationReferencesView(){
		
	}
	
	public CitationReferencesView(int refId){
		this.refId = refId;
		initComponents();
	}
	
	private void initComponents(){
		/*
		 * Creates controls for main window
		 */
		
		setVisible(true);
		setExtendedState( getExtendedState()|JFrame.MAXIMIZED_BOTH );
		setTitle("Citation References Window");
		
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.window);
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		desktopPane = new JDesktopPane();
		contentPane.add(desktopPane);
		desktopPane.setLayout(new BorderLayout(0, 0));
		addScrollPanelControls();
		addButtonPanelControls();
	}
	
	private void addScrollPanelControls(){
		/*
		 * Creates controls for Scroll panel
		 */
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
		/*
		 * Creates controls for button panel which are used to trigger events from jtable 
		 */
		btnPanel = new JPanel();
		desktopPane.add(btnPanel, BorderLayout.NORTH);
		
		btnExport = new JButton("Export");
		btnExport.setFont(getDefaultControlsFont());
		btnPanel.add(btnExport);
		
		btnDelete = new JButton("Delete");
		btnDelete.setFont(getDefaultControlsFont());
		btnPanel.add(btnDelete);
		
		String[] searchOptions = {"Search All References", "Search Foreign Court References", "Search International Court References", "Search SoftLaws", "Search Treaties"};
		cmbSearchOptions = new JComboBox(searchOptions);
		btnPanel.add(cmbSearchOptions);
		cmbSearchOptions.setFont(getDefaultControlsFont());
		
		txtSearch = new JTextField();
		txtSearch.setFont(getDefaultControlsFont());
		btnPanel.add(txtSearch);
		txtSearch.setColumns(10);
		
		btnSearch = new JButton("Search");
		btnSearch.setFont(getDefaultControlsFont());
		btnPanel.add(btnSearch);
		
		btnSave = new JButton("Save");
		btnSave.setFont(getDefaultControlsFont());
		btnPanel.add(btnSave);
		
		btnExit = new JButton("Exit");
		btnExit.setFont(getDefaultControlsFont());
		btnPanel.add(btnExit);
		listenToButtonPanelEvents();
	}
	
	private void listenToJTableEvents(){
		table.getModel().addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e){
			}
		});
		table.addMouseListener( new MouseAdapter()
        {            
            public void mouseClicked(MouseEvent e){
            	JTable source = (JTable)e.getSource();
            	int row = source.rowAtPoint( e.getPoint());
                int column = source.columnAtPoint( e.getPoint());
                if(CitationReferencesView.isEditableHeader[column]){
                	new UpdateJTableData(CitationReferencesView.tableHeader[column], row, column, CitationReferencesView.tableData[row][column],"CitationReferencesView").setVisible(true);
                	updateJTableValuesToDB(row);
                	reloadDataIntoJTable(getMainTableData());
                }
            }
        });
	}
	
	
	private void listenToButtonPanelEvents(){
		/*
		 * Button panel events
		 */
		
		btnDelete.addActionListener(new ActionListener() {
			/*
			 * Deletes the selected jtable row from the CitationReferences table
			 */
			public void actionPerformed(ActionEvent arg0) {
				int selectedRows[] = table.getSelectedRows();
				int numberOfRows = selectedRows.length;
				if(displayConfirmDeleteDialogBox(numberOfRows) == 0){
					int deletedRows = 0;
					for(int rowId : selectedRows){
						int citationRefId = Integer.parseInt(getValueforCell(rowId, 1));
						int caseRefId = Integer.parseInt(getValueforCell(rowId, 0));
						CitationReferenceModel.deleteCitationReference(citationRefId, caseRefId);
						deletedRows += 1;
					}
					reloadDataIntoJTable(getMainTableData());
					displayDeleteStatusDialogBox(deletedRows, numberOfRows);
				}
			}
		});
		
		txtSearch.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
			}
		});
		
		btnExport.addActionListener(new ActionListener() {
			/*
			 * Exports the current jtable output to the csv file 
			 */
			public void actionPerformed(ActionEvent arg0) {
				FileDialog fDialog = new FileDialog(new JFrame(),"Export Content", FileDialog.SAVE);
		        fDialog.setVisible(true);
		        String path = fDialog.getDirectory() + fDialog.getFile();
		        ExportToCSV.exportToCSV(table, path, getMainTableHeader());
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			/*
			 * Closes the current application window on pressing Exit button 
			 */
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		btnSearch.addActionListener(new ActionListener() {
			/*
			 * Searches the database with selected criteria
			 */
			public void actionPerformed(ActionEvent e) {
				String[][] searchResult = searchReferences(cmbSearchOptions.getSelectedIndex(), Integer.parseInt(txtSearch.getText()));
				if(searchResult != null)
					reloadDataIntoJTable(searchResult);
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
					int citationRefId = Integer.parseInt(getValueforCell(rowId,1));
					CitationReferenceModel.updateProcessedStatus(caseRefId, citationRefId);
				}
				reloadDataIntoJTable(getMainTableData());
			}
		});
	}
	
	
	private String[] getMainTableHeader(){
		/*
		 * Sets the headers for the jtable
		 */
		return CitationReferencesView.tableHeader;
	}
	
	private String[][] getMainTableData(){
		/*
		 * Gets the data from CitationReferences table for displaying in the output jtable
		 */
		if(this.refId == -1)
			return convertTableDataToString(CitationReferenceModel.selectUnprocessedRecords());
		else
			return convertTableDataToString(CitationReferenceModel.selectRecordsWithRefId(this.refId));
	}
	
	private String[][] convertTableDataToString(List<CitationCases> ccList){
		/*
		 * Converts the extracted data from database CitationReferences table into printable format in jtable 
		 */
		CitationReferencesView.tableData = new String[ccList.size()][9];
		int i = 0;
		for(CitationCases currentRecord : ccList){
			tableData[i][0] = String.valueOf(currentRecord.getCaseRefId());
			tableData[i][1] = String.valueOf(currentRecord.getCitationRefId());
			tableData[i][2] = String.valueOf(currentRecord.getCitationType());
			tableData[i][3] = String.valueOf(currentRecord.getCitationid());
			tableData[i][4] = String.valueOf(currentRecord.getPageNumber());
			tableData[i][5] = currentRecord.getCountryName();
			tableData[i][6] = currentRecord.getCourtName();
			tableData[i][7] = currentRecord.getCaseid();
			tableData[i][8] = currentRecord.getOtherDetails();
			i += 1;
		}
		return tableData;
	}
	
	private String getValueforCell(int row, int col){
		/*
		 * Given a row and column id the value in that cell is returned
		 */
		System.out.println("Is cell editable" + table.isCellEditable(row, col));
        return (String)table.getModel().getValueAt(row,col);
    }
	
	
	private void reloadDataIntoJTable(String[][] tableData){
		/*
		 * Refreshes data in the jtable
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
		 * Adjusts the size of each column in jtable view
		 */
		int columnSize[] = {10,10,10,10,10,200, 10, 500};
		for(int i=0; i<columnSize.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(columnSize[i]);
	}	
	
	private String[][] searchReferences(int selectedIndex, int caseRefId){
		/*
		 * Performs search based on specified criteria in view
		 */
		return convertTableDataToString(CitationReferenceModel.searchCaseReferences(selectedIndex,caseRefId));
	}

	@Override
	public Font getDefaultControlsFont() {
		// TODO Auto-generated method stub
		return new Font("Arial",Font.BOLD, 12);
	}

	@Override
	public Font getDefaultTitleFont() {
		// TODO Auto-generated method stub
		return new Font("Arial",Font.PLAIN, 12);
	}
	
	private int displayDeleteStatusDialogBox(int deletedRows, int numberOfRows){
		String alertMessage = deletedRows + " out of " + numberOfRows + " are successfully removed"; 
		String alertWindowTitle = "Delete Status";		
		return JOptionPane.showInternalConfirmDialog(this.getContentPane(), alertMessage, alertWindowTitle, JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}
	
	private int displayConfirmDeleteDialogBox(int numberOfRows){
		String alertMessage = "Are you sure you want to remove " + numberOfRows + " case references from your database?"; 
		String alertWindowTitle = "Warning! - Removes Records from db";		
		return JOptionPane.showInternalConfirmDialog(this.getContentPane(), alertMessage, alertWindowTitle, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/* 
	 * Reads updated values from the maintable view using the static public variable tableData and updates the database
	 */
	private void updateJTableValuesToDB(int row){
		int citationRefId = Integer.parseInt(getUpdatedCellValue(row,1));
		int caseRefId = Integer.parseInt(getUpdatedCellValue(row, 0));
		int citationId = Integer.parseInt(getUpdatedCellValue(row, 3));
		int pageNumber = Integer.parseInt(getUpdatedCellValue(row,4));
		String caseId = getUpdatedCellValue(row, 7);
		String otherDetails = getUpdatedCellValue(row, 8);
		CitationReferenceModel.updateCitationReference(caseRefId,citationRefId, citationId,pageNumber, caseId,otherDetails);
	}
	
	/*
	 * Returns the current data of the cell with row and column in the jtable view
	 */
	
	private static String getUpdatedCellValue(int row, int col){
		return CitationReferencesView.tableData[row][col];
	}
}
