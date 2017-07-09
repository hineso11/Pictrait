package com.pictrait.api.datastore;

import com.google.api.client.util.Base64;
import com.google.api.services.sqladmin.SQLAdminScopes;
import com.google.appengine.api.appidentity.AppIdentityService;
import com.google.appengine.api.appidentity.AppIdentityServiceFactory;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pictrait.api.constants.Constants;

import javax.print.attribute.standard.Media;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by oliver on 08/07/2017.
 */
@Entity
public class Photo {

    // MARK: Variables
    @Id
    private Long photoId;
    @Index Long userId;
    boolean photoAvailable;

    // MARK: Constructors
    // Blank constructor for Objectify service
    public Photo () {


    }

    // Constructor to create a new photo object in datastore
    public Photo (User user) {

        userId = user.getUserId();
        ObjectifyService.ofy().save().entity(this).now();
        photoAvailable = false;
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
        String httpVerb = "GET";
        Calendar expiryDate = Calendar.getInstance();
        expiryDate.add(Calendar.DATE, Constants.Photo.PHOTO_ACCESS_DAYS);
        long expiryTime = expiryDate.getTimeInMillis() / 1000L;
        String path = "/" + Constants.BUCKET_NAME + "/" + Constants.Photo.FOLDER + "/" + String.valueOf(photoId) + "." + Constants.Photo.FILE_TYPE;
        String unsignedString = httpVerb + "\n\n\n" + expiryTime + "\n" + path;

        String baseUrl = "https://storage.googleapis.com/";

        final AppIdentityService identityService = AppIdentityServiceFactory.getAppIdentityService();
        final AppIdentityService.SigningResult signingResult = identityService
                .signForApp(unsignedString.getBytes());

        final String encodedSignature = new String(Base64.encodeBase64(
                signingResult.getSignature()), "UTF-8");

        String url = baseUrl +
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
}
