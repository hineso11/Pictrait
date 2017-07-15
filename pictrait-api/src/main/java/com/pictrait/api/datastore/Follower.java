package com.pictrait.api.datastore;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pictrait.api.constants.Constants;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oliver on 15/07/2017.
 */
@Entity
public class Follower {

    // MARK: Variables
    @Id private Long followId;
    @Index private Long followerId;
    @Index private Long subjectId;

    // MARK: Constructors

    // Empty constructors for ofy service to use
    public Follower () {


    }

    // Constructor to create a new follower relationship
    public Follower (Long followerId, Long subjectId) {

        // Set class variables
        this.followerId = followerId;
        this.subjectId = subjectId;

        // Save the new entity
        ObjectifyService.ofy().save().entity(this).now();
    }

    // MARK: Methods

    public Long getSubjectId () {

        return subjectId;
    }

    public JSONObject toJson () {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.Follower.Datastore.FOLLOWER_ID, followerId);
            jsonObject.put(Constants.Follower.Datastore.SUBJECT_ID, subjectId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
