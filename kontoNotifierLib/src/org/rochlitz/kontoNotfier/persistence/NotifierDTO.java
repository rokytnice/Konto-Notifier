package org.rochlitz.kontoNotfier.persistence;

import java.lang.reflect.Field;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NOTIFIER")
public class NotifierDTO implements IDTO {

//	public NotifierDTO(String fieldRest) {
//		super(fieldRest);
//	}

	public NotifierDTO() {
		super();
	}
	
	public NotifierDTO(String fieldRest) {
		super();
		System.out.println(" ************** rest request " + fieldRest ); //
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
				 
				if(typeOF.isPrimitive()){//long sind die einzigsten primitiven die bisher verwendet werden
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
	@Column(name = "NOTIFIER_ID")
	private long Id;// PK

	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "USER_ID")
	private UserDTO user;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="KONTO_ID")
	private KontoDTO konto;
	
	@OneToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name="FILTER_ID")
	private FilterDTO filter;
	
	@Column(name = "LAST_EXEC")
	private Date lastExecution;
	
	@Column(name = "ENABLED")
	private boolean enable;// default = true later - disable

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public KontoDTO getKonto() {
		return konto;
	}

	public void setKonto(KontoDTO konto) {
		this.konto = konto;
	}

	public FilterDTO getFilter() {
		return filter;
	}

	public void setFilter(FilterDTO filter) {
		this.filter = filter;
	}

	public Date getLastExecution() {
		return lastExecution;
	}

	public void setLastExecution(Date lastExecution) {
		this.lastExecution = lastExecution;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

}
