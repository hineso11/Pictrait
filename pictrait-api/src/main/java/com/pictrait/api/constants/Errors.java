package com.pictrait.api.constants;

/**
 * Created by oliver on 05/07/2017.
 */

public enum  Errors {

    // List of all possible constants
    GENERAL_ERROR(401, "Error, The request could not be completed"),
    CLIENT_ID_INCORRECT(401, "Client ID was not recognised"),
    USER_DOESNT_EXIST(401, "This user doesn't exist"),
    NULL_FIELDS(501, "Null fields were found"),
    USERNAME_LONG(501, "Username is too long"),
    USERNAME_SHORT(501, "Username is too short"),
    PASSWORD_LONG(501, "Password is too long"),
    PASSWORD_SHORT(501, "Password is too short"),
    EMAIL_LONG(501, "Email is too long"),
    NAME_LONG(501, "Name is too long"),
    EMAIL_FORMAT(501, "Email format is invalid"),
    USERNAME_FORMAT(501, "Username format is invalid"),
    EMAIL_IN_USE(501, "Email is already in use"),
    USERNAME_IN_USE(501, "Username is already in use"),
    TOKEN_INVALID(501, "This token is invalid"),
    TOKEN_EXPIRED(501, "This token has expired"),
    WRONG_TOKEN_TYPE(501, "Token is wrong type"),
    INCORRECT_COMBINATION(501, "Incorrect username and password combination"),
    NO_AUTH_TOKEN(501, "An auth token must be supplied"),
    FILE_NOT_JPEG(501, "The image provided must be a JPEG"),
    IMAGE_NOT_SQUARE(501, "The image must be a square"),
    IMAGE_NOT_UPLOADED(501, "The image could not be uploaded"),
    IMAGE_TOO_BIG(501, "The size of the image provided is too large"),
    PHOTO_DOESNT_EXIST(501, "The photo does not exist"),
    USER_NOT_FOUND(501, "The user requested does not exist"),
    ALREADY_LIKED(501, "The photo has already been liked by the user"),
    ALREADY_FOLLOWING(501, "The user is already following this user"),
    CANNOT_FOLLOW_SELF(501, "User cannot follow themselves");

    // MARK: Variables
    private int code;
    private String message;

    // MARK: Constructor
    Errors(int code, String message) {

        this.code = code;
        this.message = message;
    }

    // MARK: Getters
    public int getCode () {

        return code;
    }

    public String getMessage () {

        return message;
    }
}

