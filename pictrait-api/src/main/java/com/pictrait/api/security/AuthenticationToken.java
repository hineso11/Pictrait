package com.pictrait.api.security;

import com.googlecode.objectify.ObjectifyService;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.User;
import io.jsonwebtoken.*;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oliver on 19/05/2017.
 */
public class AuthenticationToken {

    // MARK: Constants
    private static final String AUDIENCE = "AppUser";
    private static final String ISSUER = "PictraitAPI";
    private static final String SIGNING_KEY = "oJArErxUc/A3S+r/Xv6c1jDu8IzU3/0xi3852wjVATyEHPz3n0XVzXMgc0QI2MQlwMa/sRpiQzwCAr2UKhVk8A==";
    private static final int REFRESH_TOKEN_DAYS = 3;
    private static final int AUTH_TOKEN_HOURS = 2;
    private static final String TOKEN_TYPE = "type";
    private static final int REFRESH_TYPE = 0;
    private static final int AUTH_TYPE = 1;

    // MARK: Variables
    private String authToken;
    private String refreshToken;
    private User user;
    private TokenType type;

    // MARK: Enums
    public enum TokenType {

        REFRESH_TOKEN(REFRESH_TYPE),
        AUTH_TOKEN(AUTH_TYPE);

        // MARK: Variables
        private int tokenType;

        // MARK: Constructors
        TokenType(int tokenType) {

            this.tokenType = tokenType;
        }

        // MARK: Getters and Setters
        public int getTokenType () {

            return tokenType;
        }
    }

    // MARK: Constructors

    // Constructor for creating token object with both auth token and refresh token -- for sign up and login
    public AuthenticationToken (User user) {

        this.user = user;
        this.authToken = generateAuthToken(user);
        this.refreshToken = generateRefreshToken(user);
    }

    // Constructor to check the auth token and find the user associated with it


    // Constructor for validating an auth or refresh token
    // Gives a new auth token if refresh token given
    public AuthenticationToken (String token, TokenType tokenType,HttpServletResponse response) throws IOException {

        try {

            // Check the refresh token is as expected
            Jws<Claims> claims = Jwts.parser()
                    .requireAudience(AUDIENCE)
                    .requireIssuer(ISSUER)
                    .setSigningKey(SIGNING_KEY)
                    .parseClaimsJws(token);

            // Get id from the claims
            long id = Long.parseLong(claims.getBody().getSubject());
            // Get the user object from the datastore
            user = ObjectifyService.ofy().load().type(User.class).id(id).now();
            // Get token type from the header
            switch ((int)claims.getHeader().get(TOKEN_TYPE)) {
                case REFRESH_TYPE:
                    type = TokenType.REFRESH_TOKEN;
                    break;
                case AUTH_TYPE:
                    type = TokenType.AUTH_TOKEN;
                    break;
            }

            // Check the token type is correct from one specified in the constructor
            if (!type.equals(tokenType)) {
                // Throw the wrong token type exception if it's not a refresh token
                response.sendError(Errors.WRONG_TOKEN_TYPE.getCode(), Errors.WRONG_TOKEN_TYPE.getMessage());
                return;
            }

            // Check the user exists
            if (user == null) {

                response.sendError(Errors.USER_DOESNT_EXIST.getCode(), Errors.USER_DOESNT_EXIST.getMessage());
                return;
            }

            // A refresh token was supplied successfully, give them a new auth token
            if (type == TokenType.REFRESH_TOKEN) {

                // If no exception has been thrown then create new auth token
                this.refreshToken = refreshToken;
                authToken = generateAuthToken(user);
            }

        } catch (SignatureException e) {
            e.printStackTrace();
            response.sendError(Errors.TOKEN_INVALID.getCode(), Errors.TOKEN_INVALID.getMessage());
        } catch (ExpiredJwtException e) {
            e.printStackTrace();

            switch (tokenType) {

                case AUTH_TOKEN:
                    // Throw the auth expired error
                    response.sendError(Errors.AUTH_EXPIRED.getCode(), Errors.AUTH_EXPIRED.getMessage());
                    break;
                case REFRESH_TOKEN:
                    // Throw the refresh expired error
                    response.sendError(Errors.REFRESH_EXPIRED.getCode(), Errors.REFRESH_EXPIRED.getMessage());
                    break;
            }
        } catch (MalformedJwtException e) {
            e.printStackTrace();
            response.sendError(Errors.TOKEN_INVALID.getCode(), Errors.TOKEN_INVALID.getMessage());
        }

    }

    // MARK: Getters and Setters
    public String getAuthToken () {

        return authToken;
    }
    public String getRefreshToken () {

        return refreshToken;
    }
    public User getUser () {

        return user;
    }

    // MARK: Methods

    // Function to get a new refresh token for a given user
    private static String generateRefreshToken (User user) {

        // get the current time
        Calendar c = Calendar.getInstance();

        // add the extra days to the expiry time
        c.add(Calendar.DATE, REFRESH_TOKEN_DAYS);  // number of days to add
        Date expiryDate = c.getTime();

        // create the token and store it
        String refreshToken = Jwts.builder()
                .setAudience(AUDIENCE)
                .setIssuer(ISSUER)
                .setSubject(user.getUserId().toString())
                .setExpiration(expiryDate)
                .setHeaderParam(TOKEN_TYPE , TokenType.REFRESH_TOKEN.getTokenType())
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();

        return refreshToken;
    }

    // Function to get a new auth token for a given user
    private static String generateAuthToken (User user) {

        // get the current time
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, AUTH_TOKEN_HOURS);  // number of hours to add
        Date expiryDate = c.getTime();
        // add the extra days to the expiry time

        // create the token and store it
        String authToken = Jwts.builder()
                .setAudience(AUDIENCE)
                .setIssuer(ISSUER)
                .setSubject(user.getUserId().toString())
                .setExpiration(expiryDate)
                .setHeaderParam(TOKEN_TYPE , TokenType.AUTH_TOKEN.getTokenType())
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();

        return authToken;
    }

    public String toJson () {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.AuthenticationToken.AUTH_TOKEN, authToken);
            jsonObject.put(Constants.AuthenticationToken.REFRESH_TOKEN, refreshToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

}
