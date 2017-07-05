package com.pictrait.api.constants;

/**
 * Created by oliver on 05/07/2017.
 */

public enum  Errors {

    // List of all possible constants
    CLIENT_ID_INCORRECT(401, "Client ID was not recognised"),
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
    USER_DOESNT_EXIST(501, "This user doesn't exist"),
    TOKEN_INVALID(501, "This token is invalid"),
    TOKEN_EXPIRED(501, "This token has expired"),
    WRONG_TOKEN_TYPE(501, "Token must be refresh token"),
    INCORRECT_PASSWORD(501, "Incorrect username or password");

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

