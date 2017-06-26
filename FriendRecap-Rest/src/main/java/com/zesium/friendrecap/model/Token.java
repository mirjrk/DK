package com.zesium.friendrecap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tokens database table.
 * 
 * @author Katarina Zorbic
 */
@Entity
@Table(name="tokens")
@NamedQuery(name="Token.findAll", query="SELECT t FROM Token t")
public class Token implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="tokens_id")
	private int tokensId;

	private String tokenForUser;
	
	@Column(name="device_uid")
	private String deviceUID;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public String getDeviceUID() {
		return deviceUID;
	}

	public void setDeviceUID(String deviceUID) {
		this.deviceUID = deviceUID;
	}
	
	public int getTokensId() {
		return this.tokensId;
	}

	public void setTokensId(int tokensId) {
		this.tokensId = tokensId;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTokenForUser() {
		return tokenForUser;
	}

	public void setTokenForUser(String tokenForUser) {
		this.tokenForUser = tokenForUser;
	}

}