//Mingyuan Fu (mingyuaf)
package model;

public class CaseReaderFactory {

	/**
	 * This method will read the opened file according to its suffix
	 * @param filename
	 * @return
	 */
	public CaseReader createReader(String filename) {
		if (filename.contains(".csv")) {
			return new CSVCaseReader(filename);
		} else {
			return new TSVCaseReader(filename);
		}
	}
	
}
