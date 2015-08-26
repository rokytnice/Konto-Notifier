package org.rochlitz.kontoNotfier.persistence;

import java.lang.reflect.Field;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "KONTO")
public class KontoDTO implements IDTO {

	public KontoDTO() {
		super();
	}

	// public KontoDTO(String fieldRest) {
	// super(fieldRest);
	// }

	public KontoDTO(String fieldRest) {
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
				String value = keyValue[1];

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
					field.set(this, keyValue[1]);
				}
			} catch (ArrayIndexOutOfBoundsException | NoSuchFieldException
					| SecurityException | IllegalArgumentException
					| IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Id
	@GeneratedValue
	@Column(name = "KONTO_ID")
	private long id;// PK

	@Column(name = "BLZ")
	private Integer blz;// bankleizahl

	@Column(name = "KONTO_NR")
	private Integer ktonr;// kontonummer

	@Column(name = "BIC")
	private String bic;// later

	@Column(name = "IBAN")
	private String iban;// later

	@Column(name = "ACCOUNT")
	private String account; // zugansgnummer

	@Column(name = "PASSWORD")
	private String password;// password zum account / zugangsnummer

	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "FK_USER_ID")
	private UserDTO user;

//	@OneToMany(mappedBy="konto",fetch = FetchType.LAZY)
//	private List<FilterDTO> filters = new ArrayList<FilterDTO>();

	private static final long serialVersionUID = 1L;

	// TODO FK userID
	// TODO password speichern j/n

	public Integer getBlz() {
		return blz;
	}

	public void setBlz(Integer blz) {
		this.blz = blz;
	}

	public Integer getKtonr() {
		return ktonr;
	}

	public void setKtonr(Integer ktonr) {
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
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

//	public List<FilterDTO> getFilters() {
//		return filters;
//	}
//
//	public void setFilters(List<FilterDTO> filters) {
//		this.filters = filters;
//	}

}
