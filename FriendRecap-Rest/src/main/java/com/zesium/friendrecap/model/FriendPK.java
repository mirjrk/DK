 package com.zesium.friendrecap.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the friends database table.
 * 
 * @author Katarina Zorbic
 */
@Embeddable
public class FriendPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="user_id", insertable=false, updatable=false)
	private int userId;

	@Column(name="friend_id", insertable=false, updatable=false)
	private int friendId;

	public int getUserId() {
		return this.userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getFriendId() {
		return this.friendId;
	}
	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FriendPK)) {
			return false;
		}
		FriendPK castOther = (FriendPK)other;
		return 
			(this.userId == castOther.userId)
			&& (this.friendId == castOther.friendId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.userId;
		hash = hash * prime + this.friendId;
		
		return hash;
	}
}