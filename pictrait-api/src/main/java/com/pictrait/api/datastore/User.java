package com.pictrait.api.datastore;

import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.security.Encrypter;

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
    protected String fullName;
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

        // Change the username
        this.username = username;

        // Save the entity
        ObjectifyService.ofy().save().entity(this).now();
    }

    // Function to change a user's username
    public void changeFullName (String fullName) {

        // Change the username
        this.fullName = fullName;

        // Save the entity
        ObjectifyService.ofy().save().entity(this).now();
    }

    // Function to change a user's username
    public void changeEmail (String email) {

        // Change the username
        this.email = email;

        // Save the entity
        ObjectifyService.ofy().save().entity(this).now();
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

}
