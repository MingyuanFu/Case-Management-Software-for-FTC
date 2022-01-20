//Mingyuan Fu (mingyuaf)
package model;
import bean.Case;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TSVCaseReader extends CaseReader {
	
	public TSVCaseReader(String filename){
		super(filename);
	}

	/**
	 * This method will return a list of Case in the file
	 */
	@SuppressWarnings("finally")
	@Override
	List<Case> readCases() {
		String[] line;
		List<Case> cases = new ArrayList<>();
		try {
			int rejectedCount = 0;
			// reading containers
			FileInputStream fin = new FileInputStream(filename);
			InputStreamReader reader = new InputStreamReader(fin);
			BufferedReader bufferReader = new BufferedReader(reader);
			String thisLine;
			while((thisLine = bufferReader.readLine())!=null) {
				line = thisLine.split("\t");
				//checking the required fields
				if (line[0].trim().length() == 0 || line[1].trim().length() == 0 || 
						line[2].trim().length() == 0 || line[3].trim().length() == 0) {
					rejectedCount++;
					continue;
				}
				Case c = new Case(line[0],line[1], line[2], line[3],
		                (line.length > 4 ? line[4].trim() : ""),
		                (line.length > 5 ? line[5].trim() : ""),
		                (line.length > 6 ? line[6].trim() : "")
		                );
				cases.add(c);
			}
			bufferReader.close();
			if (rejectedCount > 0) {
				String errorMessage= rejectedCount + " cases rejected.\n"
						+ "The file must have cases with\n"
						+ "tab separated date, title, type, and case number!";
				throw new DataException(errorMessage);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DataException e) {
			System.out.println("ERROR: When reading cases, "+ e.message);
		} finally {
			return cases;
		}
	}
}
