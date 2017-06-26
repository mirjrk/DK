package com.zesium.friendrecap.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the notifications database table.
 * 
 * @author Katarina Zorbic
 */
@Entity
@Table(name="notifications")
@NamedQuery(name="Notification.findAll", query="SELECT n FROM Notification n")
public class Notification implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="notifications_id")
	private int notificationsId;

	@Column(name="message")
	private String message;

	@Column(name="type")
	private String type;
	
	@Column(name="timestamp")
	private String timestamp;
	
	@Column(name="friend_facebook_id")
	private String friendFacebookId;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	//bi-directional many-to-many association to User
	@ManyToMany
	@JoinColumn(name="user_id_tag")
	private List<User> userIdTagList;
	
	public List<User> getUserIdTagList() {
		return userIdTagList;
	}

	public void setUserIdTagList(List<User> userIdTagList) {
		this.userIdTagList = userIdTagList;
	}

	public String getFriendFacebookId() {
		return friendFacebookId;
	}

	public void setFriendFacebookId(String friendFacebookId) {
		this.friendFacebookId = friendFacebookId;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public int getNotificationsId() {
		return this.notificationsId;
	}

	public void setNotificationsId(int notificationsId) {
		this.notificationsId = notificationsId;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}