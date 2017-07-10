package com.pictrait.api.datastore;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pictrait.api.constants.Constants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oliver on 10/07/2017.
 */
@Entity
public class Like {

    // MARK: Variables
    @Id private Long likeId;
    @Index Long userId;
    @Index Long photoId;

    // MARK: Constructors

    // Blank constructor for ofy service
    public Like () {


    }

    // Constructor to like a new photo
    public Like (User user, Long photoId) {

        this.userId = user.getUserId();
        this.photoId = photoId;

        ObjectifyService.ofy().save().entity(this).now();
    }

    // MARK: Methods

    public String toJson () {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.Like.Datastore.USER_ID, userId);
            jsonObject.put(Constants.Like.Datastore.PHOTO_ID, photoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

}
