package com.pictrait.api.security;

import com.googlecode.objectify.ObjectifyService;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.User;
import com.sun.tools.internal.jxc.ap.Const;
import io.jsonwebtoken.*;

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
    private static final int AUTH_TOKEN_HOURS = 1;
    private static final String TOKEN_TYPE = "type";
    private static final String REFRESH_TYPE = "refresh";
    private static final String AUTH_TYPE = "auth";

    // MARK: Variables
    private String authToken;
    private String refreshToken;

    // MARK: Constructors

    // Constructor for creating token object with both auth token and refresh token -- for sign up and login
    public AuthenticationToken (User user) {


        this.authToken = generateAuthToken(user);
        this.refreshToken = generateRefreshToken(user);
    }

    // Constructor for creating token object with just refresh token -- for auth
    public AuthenticationToken (String refreshToken) throws SignatureException, MalformedJwtException, ExpiredJwtException, IncorrectClaimException {

        // Check the refresh token is as expected
        Jws<Claims> claims = Jwts.parser()
                .requireAudience(AUDIENCE)
                .requireIssuer(ISSUER)
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(refreshToken);

        // Get id from the claims
        long id = Integer.valueOf(claims.getBody().getSubject());
        // Get the user object from the datastore
        User user = ObjectifyService.ofy().load().type(User.class).id(id).now();
        // Get token type from the header
        String type = (String)claims.getHeader().get(TOKEN_TYPE);

        // START Validation
        // Check the token type is correct (refresh token)
        if (!type.equals(REFRESH_TYPE)) {
            // Throw the wrong token type exception if it's not a refresh token
            throw new IncorrectClaimException(claims.getHeader(), claims.getBody(), Errors.WRONG_TOKEN_TYPE.getMessage());
        }

        // Check the user exists
        if (user == null) {

            throw new IncorrectClaimException(claims.getHeader(), claims.getBody(), Errors.USER_DOESNT_EXIST.getMessage());
        }

        // END Validation

        // If no exception has been thrown then create new auth token
        authToken = generateAuthToken(user);
    }

    // MARK: Getters and Setters
    public String getAuthToken () {

        return authToken;
    }
    public String getRefreshToken () {

        return refreshToken;
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
                .setSubject(user.userId.toString())
                .setExpiration(expiryDate)
                .setHeaderParam(TOKEN_TYPE , REFRESH_TYPE)
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();

        return refreshToken;
    }

    // Function to get a new auth token for a given user
    private static String generateAuthToken (User user) {

        // get the current time
        Calendar c = Calendar.getInstance();
        Date expiryDate = c.getTime();
        // add the extra days to the expiry time
        c.add(Calendar.HOUR, AUTH_TOKEN_HOURS);  // number of days to add

        // create the token and store it
        String authToken = Jwts.builder()
                .setAudience(AUDIENCE)
                .setIssuer(ISSUER)
                .setSubject(user.userId.toString())
                .setExpiration(expiryDate)
                .setHeaderParam(TOKEN_TYPE , AUTH_TYPE)
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY).compact();

        return authToken;
    }

}
