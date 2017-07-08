package com.pictrait.api.datastore;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

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
}
