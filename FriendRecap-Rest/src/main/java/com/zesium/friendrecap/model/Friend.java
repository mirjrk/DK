package com.zesium.friendrecap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the friends database table.
 * 
 * @author Katarina Zorbic
 */
@Entity
@Table(name="friends")
@NamedQuery(name="Friend.findAll", query="SELECT f FROM Friend f")
public class Friend implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FriendPK id;

	@Column(name="friend_color")
	private String friendColor;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private User userId;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="friend_id", insertable=false, updatable=false)
	private User friendId;

	public FriendPK getId() {
		return this.id;
	}

	public void setId(FriendPK id) {
		this.id = id;
	}

	public String getFriendColor() {
		return this.friendColor;
	}

	public void setFriendColor(String friendColor) {
		this.friendColor = friendColor;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public User getFriendId() {
		return friendId;
	}

	public void setFriendId(User friendId) {
		this.friendId = friendId;
	}

}