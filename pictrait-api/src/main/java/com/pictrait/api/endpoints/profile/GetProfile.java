package com.pictrait.api.endpoints.profile;

import com.googlecode.objectify.ObjectifyService;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.Like;
import com.pictrait.api.datastore.User;
import com.pictrait.api.security.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oliver on 13/07/2017.
 */
@WebServlet(name = "GetProfile", value = "/profile/get")
public class GetProfile extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MARK: Security
        // Check whether the client ID has been supplied
        if (!Auth.checkClientID(request, response)) {
            // If there is a problem with client ID, end servlet as error sent
            return;
        }

        // Check that the auth token for the user is valid
        User user = Auth.checkAuthToken(request, response);
        if (user == null) {
            // If there is a problem with client ID, end servlet as error sent
            return;
        }
        // END: Security

        // MARK: Get Parameters
        Long userId;
        // Attempt to get the photo id
        try {

            userId = Long.parseLong(request.getParameter(Constants.Parameters.USER_ID));
        } catch (NumberFormatException e) {
            // If the photo id was blank in params, set to null as exception handled in validation
            userId = null;
        }
        // Get the username param if exists
        String username = request.getParameter(Constants.Parameters.USERNAME);

        // Check the user id passes all necessary validation
        if (validateFields(response, userId, username)) {

            User user1;
            if (userId != null) {

                // Get the user by their id
                user1 = ObjectifyService.ofy().load().type(User.class).id(userId).now();
                // Send the response
                response.getWriter().write(user1.toJson(user).toString());
            }
            if (username != null) {

                // Get the user by their username
                user1 = ObjectifyService.ofy().load().type(User.class)
                        .filter(Constants.User.Datastore.USERNAME, username)
                        .first().now();
                // Send the response
                response.getWriter().write(user1.toJson(user).toString());
            }


            response.setContentType(Constants.JSON_TYPE);
        }
    }

    private boolean validateFields (HttpServletResponse response, Long userId, String username) throws IOException {

        // Check for null fields, only username or id is required so one field can be null
        if (userId == null && (username == null || username.isEmpty())) {

            Errors.NULL_FIELDS.sendError(response);
            return false;
        }

        // If the request if using user id
        if (userId != null) {

            // Check the user exists
            User user = ObjectifyService.ofy().load().type(User.class).id(userId).now();
            if (user == null) {

                Errors.USER_NOT_FOUND.sendError(response);
                return false;
            }
        }
        // If the request is using username
        if (username != null) {

            // Check the user exists
            User user = ObjectifyService.ofy().load().type(User.class)
                    .filter(Constants.User.Datastore.USERNAME, username)
                    .first().now();
            if (user == null) {

                Errors.USER_NOT_FOUND.sendError(response);
                return false;
            }
        }


        return true;
    }
}
