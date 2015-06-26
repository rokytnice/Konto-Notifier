package org.rochlitz.kontoNotfier.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="KONTO")
public class KontoDTO implements IDTO {

	public KontoDTO() {
		super();
	}


	public KontoDTO(String fieldRest) {
		super();
		fieldRest = fieldRest.replaceAll("\"\"", "");
		String[] fields = fieldRest.split("&");
		System.out.println(fields);
	}
	
	@Id
	@GeneratedValue
	@Column(name = "KONTO_ID")
	private long Id;// PK
	
	@Column(name = "BLZ")
	private int blz;// bankleizahl
	
	@Column(name = "KONTO_NR")
	private int ktonr;// kontonummer
	
	@Column(name = "BIC")
	private String bic;// later
	
	@Column(name = "IBAN")
	private String iban;// later
	
	@Column(name = "ACCOUNT")
	private String account; // zugansgnummer
	
	@Column(name = "PASSWORD")
	private String password;// password zum account / zugangsnummer
	
	@OneToOne
	@JoinColumn(name = "FK_USER_ID")
	private UserDTO user;
	
	
	private static final long serialVersionUID = 1L;

	//TODO FK userID
	//TODO password speichern j/n
	
	public int getBlz() {
		return blz;
	}

	public void setBlz(int blz) {
		this.blz = blz;
	}

	public int getKtonr() {
		return ktonr;
	}

	public void setKtonr(int ktonr) {
		this.ktonr = ktonr;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(String bic) {
		this.bic = bic;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

 

}
