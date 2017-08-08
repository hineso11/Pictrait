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

        // Check the user id passes all necessary validation
        if (validateFields(response, userId)) {

            // Get the user by their id
            User user1 = ObjectifyService.ofy().load().type(User.class).id(userId).now();
            // Send the response
            response.getWriter().write(user1.toJson().toString());
            response.setContentType(Constants.JSON_TYPE);
        }
    }

    private boolean validateFields (HttpServletResponse response, Long userId) throws IOException {

        // Check for null fields
        if (userId == null) {

            Errors.NULL_FIELDS.sendError(response);
            return false;
        }

        // Check the user exists
        User user = ObjectifyService.ofy().load().type(User.class).id(userId).now();
        if (user == null) {

            Errors.USER_NOT_FOUND.sendError(response);
            return false;
        }

        return true;
    }
}
