package org.rochlitz.kontoNotfier.persistence;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "KONTOAUZUG")
public class KontoauszugDTO extends AbstractDTO {

	public KontoauszugDTO() {
		super();
	}

	public KontoauszugDTO(String fieldRest) {
		super(fieldRest);
	}

	public KontoauszugDTO(String message, UserDTO user2, String subject) {
		this.message = message;
		this.user = user2;
		this.subject = subject;
	}

	@Id
	@GeneratedValue
	@Column(name = "KONTOAUZUG_ID")
	private long id;// PK

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate = new Date();

	@Column(columnDefinition = "LONGTEXT")
	private String message;

	@Column
	private String subject;

	@ManyToOne(cascade={CascadeType.PERSIST})
	@JoinColumn(name = "FK_USER_ID")
	private UserDTO user;

	private static final long serialVersionUID = 1L;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

}
