package com.pictrait.api.endpoints.user;

import com.googlecode.objectify.ObjectifyService;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.User;
import com.pictrait.api.security.Auth;
import com.pictrait.api.security.AuthenticationToken;
import com.pictrait.api.security.Encrypter;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oliver on 06/07/2017.
 */

@WebServlet(name = "Login", value = "/user/login")
@MultipartConfig
public class Login extends HttpServlet {

    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MARK: Security
        // Check whether the client ID has been supplied
        if (!Auth.checkClientID(request, response)) {
            // If there is a problem with client ID, end servlet as error sent
            return;
        }
        // END: Security

        // MARK: Get parameters
        String username = request.getParameter(Constants.Parameters.USERNAME);
        String password = request.getParameter(Constants.Parameters.PASSWORD);
        // END: Get parameters

        if (validateFields(response, username, password)) {


            // Get the user
            User user = ObjectifyService.ofy().load().type(User.class).filter(Constants.User.Datastore.USERNAME, username).first().now();

            // Create a new auth token
            // Supply the user with a refresh and auth token
            AuthenticationToken token = new AuthenticationToken(user);


            // Send the response
            response.setContentType(Constants.JSON_TYPE);
            response.getWriter().write(token.toJson());
        }

    }

    private boolean validateFields (HttpServletResponse response, String username, String password) throws IOException {

        // MARK: Validate Fields
        // Check for null fields
        if (username == null || password == null  || username.isEmpty() || password.isEmpty()) {
            Errors.NULL_FIELDS.sendError(response);
            return false;
        }

        // Check whether the user exists
        User user = ObjectifyService.ofy().load().type(User.class).filter(Constants.User.Datastore.USERNAME, username).first().now();
        if (user == null) {
            // If user could not be found, send error
            Errors.INCORRECT_COMBINATION.sendError(response);
            return false;
        }
        // Check the password matches with the stored password
        if (!Encrypter.check(password, user.getHashedPassword())) {
            // if password doesn't match the stored hash with salt, send error
            Errors.INCORRECT_COMBINATION.sendError(response);
            return false;
        }

        // END: Validate Fields

        return true;
    }
}