package org.rochlitz.kontoNotfier.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="FILTER")
public class FilterDTO implements IDTO {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "FILTER_ID")
	private int Id;
	
	@Column(name = "SEARCH")
	private String search;  
	
	@Column(name = "MIN")
	private int minValue;
	
	@Column(name = "MAX")
	private int maxValue;
	
	@Column(name = "OPERATOR_OR")
	private boolean operatorOR;
	
	@Column(name = "OPERATOR_AND")
	private boolean operatorAND;
	
	@Column(name = "NOTIFY_EMAIL")
	private boolean notifyTypeEMail; 
	
	@Column(name = "NOTIFY_PUSH")
	private boolean notifyTypePush; 
	
	@Column(name = "SEARCH_BUCHUNGTXT")
	private boolean searchInBuchungsText; 
	
	@Column(name = "SEARCH_AUFTRAGG")
	private boolean searchInBuchungAuftraggeber; 
	
	
	//this will be filled by rest webservice and contains id from konto during creating new notifier at html form - this needs to set to connected NotifierDTO
	@Transient
	long kontoSelection;

	
	
	public String getBuchungsText() {
		return search;
	}
	public void setBuchungsText(String buchungsText) {
		this.search = buchungsText;
	}
	public int getMinValue() {
		return minValue;
	}
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	public boolean isOperatorOR() {
		return operatorOR;
	}
	public void setOperatorOR(boolean operatorOR) {
		this.operatorOR = operatorOR;
	}
	public boolean isOperatorAND() {
		return operatorAND;
	}
	public void setOperatorAND(boolean operatorAND) {
		this.operatorAND = operatorAND;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public boolean isNotifyTypeEMail() {
		return notifyTypeEMail;
	}
	public void setNotifyTypeEMail(boolean notifyTypeEMail) {
		this.notifyTypeEMail = notifyTypeEMail;
	}
	public boolean isNotifyTypePush() {
		return notifyTypePush;
	}
	public void setNotifyTypePush(boolean notifyTypePush) {
		this.notifyTypePush = notifyTypePush;
	}
	public boolean isSearchInBuchungsText() {
		return searchInBuchungsText;
	}
	public void setSearchInBuchungsText(boolean searchInBuchungsText) {
		this.searchInBuchungsText = searchInBuchungsText;
	}
	public boolean isSearchInBuchungAuftraggeber() {
		return searchInBuchungAuftraggeber;
	}
	public void setSearchInBuchungAuftraggeber(boolean searchInBuchungAuftraggeber) {
		this.searchInBuchungAuftraggeber = searchInBuchungAuftraggeber;
	}
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public long getKontoSelection() {
		return kontoSelection;
	}
	public void setKontoSelection(long kontoSelection) {
		this.kontoSelection = kontoSelection;
	}
	

}
