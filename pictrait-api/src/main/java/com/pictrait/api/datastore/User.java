package com.pictrait.api.datastore;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.security.Encrypter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oliver on 06/07/2017.
 */
@Entity
public class User {

    // MARK: Variables
    @Id
    private Long userId;
    @Index
    private String username;
    private String hashedPassword;
    @Index protected String fullName;
    @Index private String email;

    // MARK: Constructors

    // Blank constructor for Objectify service
    public User () {


    }

    // Constructor to sign up a new user
    public User (String username, String password, String fullName, String email) {

        this.username = username;
        this.hashedPassword = Encrypter.getSaltedHash(password);
        this.fullName = fullName;
        this.email = email;

        ObjectifyService.ofy().save().entity(this).now();
    }

    // MARK: Getters and Setters
    public Long getUserId () {

        return userId;
    }
    public String getHashedPassword () {

        return hashedPassword;
    }
    public String getUsername () {

        return username;
    }
    public String getFullName () {

        return fullName;
    }
    public String getEmail () {

        return email;
    }

    // MARK: Methods

    // Function to change a user's username
    public void changeUsername (String username) {

        // If it is set
        if (username != null && !username.isEmpty()) {

            // Change the username
            this.username = username;

            // Save the entity
            ObjectifyService.ofy().save().entity(this).now();
        }
    }

    // Function to change a user's username
    public void changeFullName (String fullName) {

        // If it is set
        if (fullName != null && !fullName.isEmpty()) {

            // Change the username
            this.fullName = fullName;

            // Save the entity
            ObjectifyService.ofy().save().entity(this).now();
        }
    }

    // Function to change a user's username
    public void changeEmail (String email) {

        // If it is set
        if (email != null && !email.isEmpty()) {

            // Change the username
            this.email = email;

            // Save the entity
            ObjectifyService.ofy().save().entity(this).now();
        }
    }


    // Function to determine if a given username is unique
    public static boolean usernameIsUnique (String username) {

        // Attempt to find the user by querying their username
        User user = ObjectifyService.ofy().load().type(User.class).filter(Constants.User.Datastore.USERNAME, username).first().now();
        // If a user hasn't been found return false, else true
        return user == null;
    }

    // Function to determine if a given email is unique
    public static boolean emailIsUnique (String email) {

        // Attempt to find the user by querying their email
        User user = ObjectifyService.ofy().load().type(User.class).filter(Constants.User.Datastore.EMAIL, email).first().now();
        // If a user hasn't been found return false, else true
        return user == null;
    }

    // Function to determine how many followers they have
    private int followerCount () {

        // Find the entity count for follower relationships where they are the subject
        return ObjectifyService.ofy().load().type(Follower.class)
                .filter(Constants.Follower.Datastore.SUBJECT_ID, userId)
                .count();
    }

    // Function to determine how many users they are following
    private int followingCount () {

        // Find the entity count for follower relationships where they are the follower
        return ObjectifyService.ofy().load().type(Follower.class)
                .filter(Constants.Follower.Datastore.FOLLOWER_ID, userId)
                .count();
    }

    private boolean isFollowing (User currentUser) {

        Follower follower = ObjectifyService.ofy().load().type(Follower.class)
                .filter(Constants.Follower.Datastore.FOLLOWER_ID, currentUser.getUserId())
                .filter(Constants.Follower.Datastore.SUBJECT_ID, userId)
                .first().now();

        if (follower == null) {
            return false;
        } else {

            return true;
        }
    }

    // Function to return a json representation of this object
    public JSONObject toJson (User currentUser) {

        // Send the updated user object with fields updated in the response
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject
                    .put(Constants.User.Datastore.USER_ID, userId)
                    .put(Constants.User.Datastore.USERNAME, username)
                    .put(Constants.User.Datastore.FULL_NAME, fullName)
                    .put(Constants.User.FOLLOWER_COUNT, followerCount())
                    .put(Constants.User.FOLLOWING_COUNT, followingCount())
                    .put(Constants.User.IS_FOLLOWING, isFollowing(currentUser));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
