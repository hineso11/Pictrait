package com.pictrait.api.datastore;

import com.google.api.client.util.Base64;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pictrait.api.constants.Constants;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oliver on 08/07/2017.
 */
@Entity
public class Photo {

    // MARK: Variables
    @Id private Long photoId;
    @Index Long userId;
    @Index boolean photoAvailable;
    @Index Date createdAt;

    // MARK: Constructors
    // Blank constructor for Objectify service
    public Photo () {


    }

    // Constructor to create a new photo object in datastore
    public Photo (User user) {

        userId = user.getUserId();
        photoAvailable = false;
        createdAt = new Date();
        ObjectifyService.ofy().save().entity(this).now();
    }

    // MARK: Getters and Setters
    public Long getPhotoId () {

        return photoId;
    }
    public boolean isPhotoAvailable () {

        return photoAvailable;
    }

    public void setPhotoAvailable (boolean photoAvailable) {

        this.photoAvailable = photoAvailable;
        ObjectifyService.ofy().save().entity(this).now();
    }

    // MARK: Methods
    // Function to get a signed download url for the image
    public String getDownloadUrl () throws UnsupportedEncodingException {

        // Construct parameters for the signed url
        String httpVerb = "GET"; // http method
        Calendar expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.DATE, Constants.Photo.PHOTO_ACCESS_DAYS); // make expiry date 1 day after current date
        long expiryTime = expiryDate.getTimeInMillis() / 1000L; // get time since epoch (seconds)
        String path = "/" + Constants.BUCKET_NAME + "/" + Constants.Photo.FOLDER + "/" + String.valueOf(photoId) + "." + Constants.Photo.FILE_TYPE;
        String unsignedString = httpVerb + "\n\n\n" + expiryTime + "\n" + path; // construct string

        // Sign the usnigned string using App Engine identifying service
        final AppIdentityService identityService = AppIdentityServiceFactory.getAppIdentityService();
        final AppIdentityService.SigningResult signingResult = identityService
                .signForApp(unsignedString.getBytes());

        // Encode the signing result using base64
        final String encodedSignature = new String(Base64.encodeBase64(
                signingResult.getSignature()), "UTF-8");

        // Construct the download url
        String url = Constants.BASE_FILE_URL +
                Constants.BUCKET_NAME +
                "/" +
                Constants.Photo.FOLDER +
                "/" +
                String.valueOf(photoId) + "." + Constants.Photo.FILE_TYPE +
                "?GoogleAccessId=" +
                identityService.getServiceAccountName() +
                "&Expires=" +
                expiryTime +
                "&Signature=" +
                URLEncoder.encode(encodedSignature, "UTF-8");


        return url;
    }

    // Function to get number of likes for photo
    private int likesCount () {

        // Get the count of likes for a corresponding photo id
        return ObjectifyService.ofy().load().type(Like.class)
                .filter(Constants.Like.Datastore.PHOTO_ID, photoId)
                .count();
    }

    private boolean userHasLiked (User currentUser) {

        Like like = ObjectifyService.ofy().load().type(Like.class)
                .filter(Constants.Like.Datastore.PHOTO_ID, photoId)
                .filter(Constants.Like.Datastore.USER_ID, currentUser.getUserId())
                .first().now();
        if (like == null) {
            return false;
        } else {
            return true;
        }
    }

    private String getUsername () {

        User user = ObjectifyService.ofy().load().type(User.class)
                .id(userId).now();
        return user.getUsername();
    }

    // Function to return a json representation of this object
    public JSONObject toJson (User currentUser) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.Photo.Datastore.PHOTO_ID, photoId);
            jsonObject.put(Constants.Photo.DOWNLOAD_URL, getDownloadUrl());
            jsonObject.put(Constants.User.Datastore.USERNAME, getUsername());
            jsonObject.put(Constants.Photo.LIKES_COUNT, likesCount());
            jsonObject.put(Constants.Photo.Datastore.CREATED_AT, createdAt.toString());
            jsonObject.put(Constants.Photo.HAS_LIKED, userHasLiked(currentUser));
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
