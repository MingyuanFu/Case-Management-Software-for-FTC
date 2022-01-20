//Mingyuan Fu (mingyuaf)
package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import bean.Case;

public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename : the name of file opened
	 */
	void readCases(String filename) {
		CaseReaderFactory crf = new CaseReaderFactory();
		CaseReader caseReader = crf.createReader(filename);
		List<Case> readCase = caseReader.readCases();
		Collections.sort(readCase);
		for (Case c : readCase) {
			caseList.add(c);
			caseMap.put(c.getCaseNumber(), c);
		}
	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		String year;
		
		yearList.clear();
		yearMap.clear();
		
		// populate yearList with year String
		for (Case c : caseList) {
			year = c.getCaseDate().split("-")[0];
			if (!yearList.contains(year)) {
				yearList.add(year);
			}
		}
		
		// populate yearMap, key is a four-digit year, eg. '2021'
		for (String y : yearList) {
			List<Case> thisyears = new ArrayList<>();
			for(Case c : caseList) {
				if (y.equals(c.getCaseDate().split("-")[0])) {
					thisyears.add(c);
				}
			}
			yearMap.put(y, thisyears);
		}
		
		
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		List<Case> foundCases = new ArrayList<>();
		for (Case c: caseList) {
			/*
			 * Only when four criteria are met at the same time, the case will be added to the returned list. 
			 * Otherwise, the method will skip the loop.
			 */
			if(!(title==null||title.length()==0)) {
				if(!c.getCaseTitle().toLowerCase().contains(title.toLowerCase())) {
					continue;
				}
			}
			
			if(!(caseType==null||caseType.length()==0)) {
				if(!c.getCaseType().toLowerCase().contains(caseType.toLowerCase())) {
					continue;
				}
			}
			
			if(!(caseNumber==null||caseNumber.length()==0)) {
				if(!c.getCaseNumber().contains(caseNumber)) {
					continue;
				} 
			}
			
			if(!(year==null||year.length()==0)) {
				if(!c.getCaseDate().split("-")[0].equals(year)){
					continue;
				}
			}
			
			foundCases.add(c);
		}
		return foundCases;
	}
	
	/**
	 * This method will write the current cases to a new file given the filename.
	 * @param fileName : the path of the to be saved file.
	 * @return true if saving completed, otherwise false.
	 */
	public boolean writeCases(String fileName) {
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
			//String lineSeparator = System.getProperty("line.separator", "\n");
			for (Case c : caseList) {
				//System.out.println(c.caseInfoString("\t"));
				String line = String.format("%s\n",  c.caseInfoString("\t"));
				bufferedWriter.write(line);
			}
			bufferedWriter.close();
			return true;
			} catch (IOException e) {
			return false;
		}
	}
}
