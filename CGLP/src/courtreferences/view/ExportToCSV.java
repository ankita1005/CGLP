package courtreferences.view;

import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ExportToCSV extends JDialog implements FontDefinition{
	/*
	 * This class contains controls and functioanlities to export the content of input jtable into a csv file
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public ExportToCSV(){
		
	}
	
	public static void exportToCSV(JTable tableToExport, String exportPath, String[] tableHeader){
		/*
		 * Gets the input jtable, path of the file and the tableHeader as input
		 * Produces the output csv file with specified filename
		 */
		File file = new File(exportPath);
		if (file.exists()) {
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "The filename already exists. Would you like to overwrite?","File already exists",dialogButton);
			if(dialogResult == JOptionPane.NO_OPTION)
				return;
		}
		try{
			file.createNewFile();
			writeToFile(tableToExport,file, tableHeader);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void writeToFile(JTable tableToExport, File f, String[] tableHeader){
		FileWriter fw;
		try {
			fw = new FileWriter(f.getAbsoluteFile());
			DefaultTableModel tableModel = (DefaultTableModel) tableToExport.getModel();
			BufferedWriter bw = new BufferedWriter(fw);
			int nRow = tableModel.getRowCount();
			int nCol = tableModel.getColumnCount();
			String headerString = "";
			for(int i = 0; i<tableHeader.length; i++)
				headerString += tableHeader[i] + "\t";
			headerString = headerString.trim() + "\r\n";
			bw.write(headerString);
			for (int i = 0; i < nRow ; i++){
				StringBuilder currentString = new StringBuilder();
		        for (int j = 0 ; j < nCol ; j++){
		        	currentString.append(tableModel.getValueAt(i, j).toString().replaceAll("\t", " ")); 
					currentString.append("\t");
		        }
		        String currentRow = currentString.toString().trim();
				bw.write(currentRow + "\r\n");
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	@Override
	public Font getDefaultControlsFont() {
		// TODO Auto-generated method stub
		return new Font("Arial",Font.PLAIN, 12);
	}

	@Override
	public Font getDefaultTitleFont() {
		// TODO Auto-generated method stub
		return new Font("Arial",Font.BOLD, 12);
	}
}
