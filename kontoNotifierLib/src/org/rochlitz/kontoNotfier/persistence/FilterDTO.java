package org.rochlitz.kontoNotfier.persistence;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="FILTER")
public class FilterDTO  implements IDTO {
	
//	public FilterDTO(String fieldRest) {
//		super(fieldRest);
//	}
	
	public FilterDTO() {
		super();
	}
	
	public FilterDTO(String fieldRest) {
		super();
		fieldRest = fieldRest.replaceAll("\"", "");
		String[] fields = fieldRest.split("&");
		
		for(String property : fields){
			String[] keyValue = property.split("=");
			try {
				if(keyValue.length<=1){//no value for key
					continue;
				}
				String key = keyValue[0];
				String value = keyValue[1];
				
				Field field = this.getClass().getDeclaredField(key);
				field.setAccessible(true);
				Class<?> typeOF = field.getType();
				 
				if(typeOF.equals(Integer.class)){
					 field.set(this,Integer.parseInt( value ));
				 }if(typeOF.isPrimitive()){//long sind die einzigsten primitiven die bisher verwendet werden
					 field.set(this,Integer.parseInt( value ));
				 }else{
					 field.set(this,keyValue[1]);
				 }
			} catch (ArrayIndexOutOfBoundsException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "FILTER_ID")
	private Integer Id;
	
	@Column(name = "SEARCH")
	private String search;  
	
	@Column(name = "MIN")
	private Integer minValue;
	
	@Column(name = "MAX")
	private Integer maxValue;
	
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
	public Integer getMinValue() {
		return minValue;
	}
	public void setMinValue(Integer minValue) {
		this.minValue = minValue;
	}
	public Integer getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(Integer maxValue) {
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
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public long getKontoSelection() {
		return kontoSelection;
	}
	public void setKontoSelection(long kontoSelection) {
		this.kontoSelection = kontoSelection;
	}
	

}
