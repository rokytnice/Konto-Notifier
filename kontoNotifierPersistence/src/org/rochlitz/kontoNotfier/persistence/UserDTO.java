package org.rochlitz.kontoNotfier.persistence;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sun.istack.internal.NotNull;

@Entity
@Table(name="USER", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserDTO  implements IDTO {
	
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "USER_ID")
	private long id;// PK
	
	@NotNull
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "USERNAME")
	private String username;
	
	@Column(name = "PASSWORD")
	private String password;//TODO check varchar 256???
		
	/**
	 * e.g. if user email is not approved 
	 */
	@Column(name = "ACTIVE")
	private boolean active = true; //TODO
	
	
//	@OneToMany(mappedBy="user",fetch = FetchType.LAZY)
//	private List<KontoDTO> konten  = new ArrayList<KontoDTO>();
	
	public UserDTO(String fieldRest) {
		super();
		System.out.println(" ************** rest request " + fieldRest); //
		fieldRest = fieldRest.replaceAll("\"", "");
		String[] fields = fieldRest.split("&");

		for (String property : fields) {
			String[] keyValue = property.split("=");
			try {
				if (keyValue.length <= 1) {// no value for key
					continue;
				}
				String key = keyValue[0];
				String value = URLDecoder.decode( keyValue[1], "UTF-8" );
				if(key.compareTo("email") == 0){ //email convert to lower case
					value=value.toLowerCase();
				}
				
				Field field = this.getClass().getDeclaredField(key);
				field.setAccessible(true);
				Class<?> typeOF = field.getType();

				if (typeOF.equals(Integer.class)) {
					field.set(this, Integer.parseInt(value));
				} else if (typeOF.isPrimitive()) {// long sind die einzigsten
													// primitiven die bisher
													// verwendet werden
					field.set(this, Integer.parseInt(value));
				} else {
					field.set(this, value);
				}
			} catch (ArrayIndexOutOfBoundsException | NoSuchFieldException
					| SecurityException | IllegalArgumentException
					| IllegalAccessException | UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public UserDTO(String email2, boolean justEmail) {
		email=email2;
	}
	public UserDTO() {
		super();
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
//	public List<KontoDTO> getKonten() {
//		return konten;
//	}
//	public void setKonten(List<KontoDTO> konten) {
//		this.konten = konten;
//	}

}
