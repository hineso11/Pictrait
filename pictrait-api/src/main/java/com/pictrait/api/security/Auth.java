package com.pictrait.api.security;

import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by oliver on 05/07/2017.
 */
public class Auth {

    public static boolean checkClientID (HttpServletRequest request, HttpServletResponse response) throws IOException {

        String clientID = request.getParameter(Constants.Parameters.CLIENT_ID);
        if (Arrays.asList(Constants.clientIDs).contains(clientID)) {


            return true;
        } else {

            response.sendError(Errors.CLIENT_ID_INCORRECT.getCode(), Errors.CLIENT_ID_INCORRECT.getMessage());
            return false;
        }
    }

    public static User checkAuthToken (HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Check that the auth token has been supplied
        String authToken = request.getParameter(Constants.Parameters.AUTH_TOKEN);
        if (authToken == null || authToken.isEmpty()) {

            response.sendError(Errors.NO_AUTH_TOKEN.getCode(), Errors.NO_AUTH_TOKEN.getMessage());
            return null;
        }

        AuthenticationToken token = new AuthenticationToken(authToken, AuthenticationToken.TokenType.AUTH_TOKEN,response);

        return token.getUser();
    }
}
