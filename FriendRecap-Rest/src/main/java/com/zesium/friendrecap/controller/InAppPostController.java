package com.zesium.friendrecap.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.zesium.friendrecap.dto.StringResponseDto;
import com.zesium.friendrecap.model.Friend;
import com.zesium.friendrecap.model.InAppPost;
import com.zesium.friendrecap.model.Notification;
import com.zesium.friendrecap.model.Token;
import com.zesium.friendrecap.model.User;
import com.zesium.friendrecap.service.FriendsService;
import com.zesium.friendrecap.service.InAppPostService;
import com.zesium.friendrecap.service.NotificationService;
import com.zesium.friendrecap.service.TokenService;
import com.zesium.friendrecap.service.UserService;
import com.zesium.friendrecap.util.APNSPath;
import com.zesium.friendrecap.util.TimeParserForDelete;

import net.coobird.thumbnailator.Thumbnails;


/**
* The InAppPostController implements a two methods - 
* for save or edit posts and delete posts in applications. 
*
* @author  Katarina Zorbic
* @version 1.0
* @since   2017-02-01
* @see InAppPost
*/ 
@Controller
public class InAppPostController {
	
	private static final Logger logger = LoggerFactory.getLogger(ReminderController.class);
	
	@Autowired 
    private UserService userService;
	
	@Autowired 
    private InAppPostService postService;
	
	@Autowired 
    private FriendsService friendsService;
	
	@Autowired 
    private NotificationService notificationService;
	
	@Autowired 
    private TokenService tokenService;
	
	/**
	* This method is used to save or edit posts.
	* 
	* @param postText This parameter receiving text from post
	* @param postMedia This parameter receiving MultipartFile (image or video) from post, not required
	* @param mediaType This parameter is info type about post (video, image, text)
	* @param facebookIdPost This parameter is facebook ID from user which posted
	* @param mediaName This parameter is name of media
	* @param location This parameter is location of user
	* @return response This returns message about success or error.
	* @throws IOException 
	*/
	@RequestMapping(value = "/api/saveOrEditInAppPost", method = RequestMethod.POST)
	@ResponseBody
	public StringResponseDto saveOrEditInAppPost(@RequestParam("postText") String postText, 
									@RequestParam(value = "postMedia", required = false) MultipartFile postMedia, 
									@RequestParam("mediaType") String mediaType, 
									@RequestParam("facebookIdPost") String facebookIdPost, 
									@RequestParam("mediaName") String mediaName,
									@RequestParam("location") String location) throws  IOException{
		
		Boolean okPicture = false;
		Boolean okVideo = false;
		
		logger.debug("savePost() : {}", postMedia);
		
		User userPost = userService.findByFacebookID(facebookIdPost);
		
		InAppPost post  = new InAppPost();
		
		StringResponseDto response = new StringResponseDto();
		
		// Environment variables
		String serverFile = System.getenv("FR_MEDIA_SERVER_FILE");
		String url = System.getenv("FR_MEDIA_SERVER_URL");
		
		String mediaNameNew = mediaName.substring(0, 13);
		
		if (postMedia != null) {

			byte[] bytes = postMedia.getBytes();
				
			if ("video".equals(mediaType)){
					
				String mediaNameMP4 = mediaNameNew + ".mp4";
				String mediaNameThumb = mediaNameNew + "_thumbnail";
					
				// Creating the directory to store file
				File dir = new File(serverFile);
					

				dir.mkdirs();

				// Create the file on server
				File full = new File(dir.getAbsolutePath() + File.separator + mediaNameMP4);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(full));
				stream.write(bytes);
				stream.close();
					
				post.setPostMedia(url + mediaNameMP4);
				post.setMediaType(mediaType);			
					
				String video = full.getAbsolutePath();
				String snapShotTime = new String("00:00:00");
				File thumb = new File(dir.getAbsolutePath() + File.separator + mediaNameThumb);
				String image = thumb.getAbsolutePath();
					
				StringBuilder output = new StringBuilder("Starting =>");
					
				Runtime rt = Runtime.getRuntime();
					
			
				// ffmpeg is added to the environment
				Process process = rt.exec("ffmpeg -i " + video + " -ss " + snapShotTime + " -vframes 1 " + image);
						
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						
				String line;
						
				while ((line = reader.readLine()) != null ) {
					output.append(line);
				}
										
				output.append("\n|Finished");
					
				BufferedImage img = ImageIO.read(thumb);
				post.setMediaHeight(img.getHeight());
				post.setMediaWidth(img.getWidth());
					
				post.setThumbnail(url + mediaNameThumb);
				    
				post.setMediaName(mediaNameNew);
				    
				okVideo = true;
					
			} else {
					
				//images
					
				String mediaNameJPG = mediaNameNew + ".jpg";
				String mediaNameThumb = mediaNameNew + "_thumbnail";
					
				// Creating the directory to store file
				File dir = new File(serverFile);
					
				dir.mkdirs();

				// Create the file on server
				File full = new File(dir.getAbsolutePath() + File.separator + mediaNameJPG);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(full));
				stream.write(bytes);
				stream.close();
					
				post.setPostMedia(url + mediaNameJPG);
				post.setMediaType(mediaType);
					
				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				File thumb = new File(dir.getAbsolutePath() + File.separator + mediaNameThumb);
					
				Thumbnails.of(bis).scale(0.5, 0.5).outputFormat("jpg").toFile(thumb);
					
				BufferedImage imageThumb = ImageIO.read(thumb);
				int heightThumb = imageThumb.getHeight();
				int widthThumb = imageThumb.getWidth();
						
				post.setMediaHeight(heightThumb);
				post.setMediaWidth(widthThumb);
				post.setThumbnail(url + mediaNameThumb);
											
				post.setMediaName(mediaNameNew);
					
				okPicture = true;
					
			}
						
		} else {
			
			// text
			
			post.setMediaHeight(1);
			post.setMediaWidth(1);
			
			post.setPostMedia(" ");
			post.setMediaType("text");
			post.setThumbnail(" ");
			post.setMediaName("text");
			
		}
		
	
		post.setPostText(postText);

	
		if (okPicture || okVideo){
			
			post.setUserIdPost(userPost);
				
			Timestamp timestampPost = new Timestamp(System.currentTimeMillis());
			long tPost = timestampPost.getTime();
			String tsPost = String.valueOf(tPost);
			post.setTimestamp(tsPost);
			
			post.setLocation(location);
				
			post = this.postService.save(post);
			
			//notifications
			List<Friend> ownersOfFriendship = friendsService.findFriendsOwner(userPost);
				
			for (Friend f : ownersOfFriendship){
				
				Notification notif = new Notification();
				notif.setType("INAPPPOST");
				notif.setFriendFacebookId(userPost.getFacebookID());
				notif.setMessage(" posted in FriendRecap");
				notif.setUser(f.getUserId());
					
				notif.setTimestamp(post.getTimestamp());
					
				notificationService.save(notif);
				
				response.setResponse("Successfuly posted in FR");
				
				//push notification - APNS
				String timestampCount = f.getUserId().getTimestamp();
				
				List<Notification> list = notificationService.findNotificationListByTimestamp(f.getUserId(), timestampCount);
				
				int count = 1;
				
				if (list != null) {
					count = list.size();
				} 
				
				APNSPath apnspath = new APNSPath(); 
				String path = apnspath.getPath();
					
				List<Token> tokens = tokenService.findTokensList(f.getUserId());
					
				for (Token token : tokens){
					
					ApnsService service = APNS.newService()
							.withCert(path, "Test1234") 
							.withProductionDestination()
							.build(); 
						
					String payload = APNS.newPayload()
							.alertBody(userPost.getFirstName() + " posted in FriendRecap")
							.badge(count)
							.build();
						
					String tokenToString = token.getTokenForUser();
							
					service.push(tokenToString, payload);
				}
			}
		}

		
		if (response.getResponse() == null){
			response.setError("Bad data");
		}else{
			response.setError(null);
		}
		
		return response;
	}
	
	
	/**
	* This method is used to delete posts after 7 days. 
	* Also, this method is being deleted images and videos from file system after deleting post.
	* This method is running with cron job every day at 10am.
	* @throws IOException 
	* 
	*/
	@Transactional
	@Scheduled(cron = "0 0 10 ? * *") //Fire at 10am every day
	public void cronTaskDeletePosts() throws IOException{
		
		List<InAppPost> postList = postService.findAll();
		
		for (InAppPost post : postList){
			
			String ts = post.getTimestamp();
			TimeParserForDelete tr = new TimeParserForDelete(ts);
			String parsedTimeString = tr.getParsedTime();
			
			float parsedTime = Float.parseFloat(parsedTimeString);
		    
		    if (parsedTime >= 7){
		    	
		    	// delete notifications for post
		    	List<Friend> ownersOfFriendship = friendsService.findFriendsOwner(post.getUserIdPost());
				
				for (Friend f : ownersOfFriendship){
				
					List<Notification> notificationList = notificationService.findNotificationList(f.getUserId(), "INAPPPOST", post.getUserIdPost().getFacebookID());
					
					Notification n = (Notification) notificationList.iterator();
					notificationService.delete(n);
					
				}				
				
				// delete videos and images from file
				String serverFile = System.getenv("FR_MEDIA_SERVER_FILE");
				
				String mediaName = post.getMediaName();
				
				String mediaNameMP4 = mediaName + ".mp4";
				String mediaNameThumbV = mediaName + "_thumbnail.jpg";
				
				String mediaNameJPG = mediaName + ".jpg";
				String mediaNameThumbP = mediaName + "_thumbnail.jpg";
				
				if ("video".equals(post.getMediaType())) {
					
					Path file = Paths.get(serverFile + "/" + mediaNameMP4);
					Path fileThumb = Paths.get(serverFile + "/" + mediaNameThumbV);
					
					Files.delete(file);
					Files.delete(fileThumb);
					
				
				} else if ("image".equals(post.getMediaType())){
					
					Path file = Paths.get(serverFile + "/" + mediaNameJPG);
					Path fileThumb = Paths.get(serverFile + "/" + mediaNameThumbP);
				
					Files.delete(file);
					Files.delete(fileThumb);
					
				}
				
				postService.delete(post);
		    	
		    }
			
		}
		
	}

}
