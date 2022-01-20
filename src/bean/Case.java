//Mingyuan Fu (mingyuaf)
package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{

	// case information contains following characteristics
	private StringProperty caseDate = new SimpleStringProperty(); 
	private StringProperty caseTitle = new SimpleStringProperty();
	private StringProperty caseType = new SimpleStringProperty();
	private StringProperty caseNumber= new SimpleStringProperty();
	private StringProperty caseLink = new SimpleStringProperty();
	private StringProperty caseCategory = new SimpleStringProperty();
	private StringProperty caseNotes = new SimpleStringProperty();
	
	/**
	 * This constructor set up a new case with its information from a line in opened file
	 * @param caseDate
	 * @param caseTitle
	 * @param caseType
	 * @param caseNumber
	 * @param caseLink
	 * @param caseCategory
	 * @param caseNotes
	 */
	public Case(String caseDate, String caseTitle, String caseType, String caseNumber,
			String caseLink, String caseCategory, String caseNotes) {
		
		this.caseDate.set(caseDate);
		this.caseTitle.set(caseTitle);
		this.caseType.set(caseType);
		this.caseNumber.set(caseNumber);
		this.caseLink.set(caseLink);
		this.caseCategory.set(caseCategory);
		this.caseNotes.set(caseNotes);
	}

	// Getters and setters
	
	public String getCaseDate() {
		return caseDate.get();
	}

	public void setCaseDate(String caseDate) {
		this.caseDate.set(caseDate);
	}

	public StringProperty caseLinkDate() {
		return caseDate;
	}
	
	public String getCaseTitle() {
		return caseTitle.get();
	}

	public void setCaseTitle(String caseTitle) {
		this.caseTitle.set(caseTitle);
	}
	
	public StringProperty caseLinkTitle() {
		return caseTitle;
	}

	public String getCaseType() {
		return caseType.get();
	}

	public void setCaseType(String caseType) {
		this.caseType.set(caseType);
	}
	
	public StringProperty caseLinkType() {
		return caseType;
	}

	public String getCaseNumber() {
		return caseNumber.get();
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber.set(caseNumber);
	}

	public StringProperty caseLinkNumber() {
		return caseNumber;
	}
	
	public String getCaseLink() {
		return caseLink.get();
	}

	public void setCaseLink(String caseLink) {
		this.caseLink.set(caseLink);
	}

	public StringProperty caseLinkProperty() {
		return caseLink;
	}
	
	public String getCaseCategory() {
		return caseCategory.get();
	}

	public void setCaseCategory(String caseCategory) {
		this.caseCategory.set(caseCategory);
	}
	
	public StringProperty caseCategoryProperty() {
		return caseCategory;
	}

	public String getCaseNotes() {
		return caseNotes.get();
	}

	public void setCaseNotes(String caseNotes) {
		this.caseNotes.set(caseNotes);
	}
	
	public StringProperty caseNotesProperty() {
		return caseNotes;
	}

	/** By default, comparing caseDate */
	@Override
	public int compareTo(Case thecase) {
		
		return -(caseDate.get().compareTo(thecase.caseDate.get()));
		
	}
	
	/**
	 * This method will concatenate the case information to a string.
	 */
	
	public String caseInfoString(String Delimiter) {
		StringBuilder sb = new StringBuilder();
		if (caseDate.get().length() != 0) sb.append(caseDate.get()+Delimiter); else sb.append(" "+Delimiter);
		if (caseTitle.get().length() != 0) sb.append(caseTitle.get()+Delimiter); else sb.append(" "+Delimiter);
		if (caseType.get().length() != 0) sb.append(caseType.get()+Delimiter); else sb.append(" "+Delimiter);
		if (caseNumber.get().length() != 0) sb.append(caseNumber.get()+Delimiter); else sb.append(" "+Delimiter);
		if (caseLink.get().length() != 0) sb.append(caseLink.get()+Delimiter); else sb.append(" "+Delimiter);
		if (caseCategory.get().length() != 0) sb.append(caseCategory.get()+Delimiter); else sb.append(" "+Delimiter);
		if (caseNotes.get().length() != 0) sb.append(caseNotes.get()); else sb.append(" ");

		return sb.toString();
	}
}
