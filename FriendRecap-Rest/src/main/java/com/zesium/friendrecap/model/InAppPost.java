package com.zesium.friendrecap.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the in_app_post database table.
 * 
 * @author Katarina Zorbic
 */
@Entity
@Table(name="in_app_post")
@NamedQuery(name="InAppPost.findAll", query="SELECT i FROM InAppPost i")
public class InAppPost implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="post_id")
	private int postId;

	@Column(name="post_text")
	private String postText;

	@Lob
	@Column(name="post_media")
	private String postMedia;
	
	@Lob
	@Column(name="thumbnail")
	private String thumbnail;
	
	@Column(name="timestamp")
	private String timestamp;
	
	@Column(name="media_type")
	private String mediaType;
	
	@Column(name="media_name")
	private String mediaName;
	
	@Column(name="media_height")
	private int mediaHeight;
	
	@Column(name="media_width")
	private int mediaWidth;
	
	@Column(name="location")
	private String location;
	
	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id_post")
	private User userIdPost;
	
	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getMediaHeight() {
		return mediaHeight;
	}

	public void setMediaHeight(int mediaHeight) {
		this.mediaHeight = mediaHeight;
	}

	public int getMediaWidth() {
		return mediaWidth;
	}

	public void setMediaWidth(int mediaWidth) {
		this.mediaWidth = mediaWidth;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}	

	public int getPostId() {
		return this.postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public String getPostText() {
		return this.postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}
	
	public User getUserIdPost() {
		return userIdPost;
	}

	public void setUserIdPost(User userIdPost) {
		this.userIdPost = userIdPost;
	}

	public String getPostMedia() {
		return postMedia;
	}

	public void setPostMedia(String postMedia) {
		this.postMedia = postMedia;
	}
	
}