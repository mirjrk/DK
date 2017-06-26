package com.zesium.friendrecap.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 * @author Katarina Zorbic
 */
@Entity
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="user_id")
	private int userId;

	@Temporal(TemporalType.DATE)
	@Column(name="birthday")
	private Date birthday;

	@Column(name="email", nullable = false, unique = true)
	private String email;
	
	@Column(name="facebook_ID")
	private String facebookID;
	
	@Column(name="instagram_ID")
	private String instagramID;

	@Column(name="instagram_username")
	private String instagramUsername;
	
	@Column(name="name")
	private String name;
	
	@Column(name="first_name")
	private String firstName;

	@Column(name="twitter_ID")
	private String twitterID;

	@Column(name="twitter_username")
	private String twitterUsername;
	
	@Column(name="unlock_friends")
	private int unlockFriends = 5;
	
	@Column(name="time_zone")
	private String timeZone;
	
	@Column(name="timestamp")
	private String timestamp;

	//bi-directional many-to-one association to InAppPost
	@OneToMany(mappedBy="userIdPost")
	private List<InAppPost> inAppPosts;

	//bi-directional many-to-one association to Reminder
	@OneToMany(mappedBy="userIdPost")
	private List<Reminder> remindersPost;
	
	//bi-directional many-to-one association to Notification
	@OneToMany(mappedBy="user")
	private List<Notification> notifications;
	
	//bi-directional many-to-one association to Token
	@OneToMany(mappedBy="user")
	private List<Token> tokens;
	
	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public int getUnlockFriends() {
		return unlockFriends;
	}
	
	public void setUnlockFriends(int unlockFriends) {
		this.unlockFriends = unlockFriends;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFacebookID() {
		return this.facebookID;
	}

	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}

	public String getInstagramID() {
		return this.instagramID;
	}

	public void setInstagramID(String instagramID) {
		this.instagramID = instagramID;
	}

	public String getInstagramUsername() {
		return this.instagramUsername;
	}

	public void setInstagramUsername(String instagramUsername) {
		this.instagramUsername = instagramUsername;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTwitterID() {
		return this.twitterID;
	}

	public void setTwitterID(String twitterID) {
		this.twitterID = twitterID;
	}

	public String getTwitterUsername() {
		return this.twitterUsername;
	}

	public void setTwitterUsername(String twitterUsername) {
		this.twitterUsername = twitterUsername;
	}
	
	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public List<InAppPost> getInAppPosts() {
		return inAppPosts;
	}

	public void setInAppPosts(List<InAppPost> inAppPosts) {
		this.inAppPosts = inAppPosts;
	}

	public List<Reminder> getRemindersPost() {
		return remindersPost;
	}

	public void setRemindersPost(List<Reminder> remindersPost) {
		this.remindersPost = remindersPost;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}