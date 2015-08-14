package org.rochlitz.kontoNotfier.persistence;

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
		
//	@OneToMany(mappedBy="user",fetch = FetchType.LAZY)
//	private List<KontoDTO> konten  = new ArrayList<KontoDTO>();
	
	public UserDTO(String email2) {
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
	
//	public List<KontoDTO> getKonten() {
//		return konten;
//	}
//	public void setKonten(List<KontoDTO> konten) {
//		this.konten = konten;
//	}

}
