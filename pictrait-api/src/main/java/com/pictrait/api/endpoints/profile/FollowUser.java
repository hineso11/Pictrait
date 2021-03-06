package com.pictrait.api.endpoints.profile;

import com.googlecode.objectify.ObjectifyService;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.Follower;
import com.pictrait.api.datastore.User;
import com.pictrait.api.security.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oliver on 15/07/2017.
 */
@MultipartConfig
@WebServlet(name = "FollowUser", value = "/profile/follow")
public class FollowUser extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
        Long subjectId;
        // Attempt to get the photo id
        try {

            subjectId = Long.parseLong(request.getParameter(Constants.Parameters.SUBJECT_ID));
        } catch (NumberFormatException e) {
            // If the photo id was blank in params, set to null as exception handled in validation
            subjectId = null;
        }

        // Check the user id passes all necessary validation
        if (validateFields(response, user.getUserId(), subjectId)) {

            // Save the new follower relationship
            Follower follower = new Follower(user.getUserId(), subjectId);

            // Send the response
            response.getWriter().write(follower.toJson().toString());
            response.setContentType(Constants.JSON_TYPE);
        }
    }

    private boolean validateFields (HttpServletResponse response, Long followerId, Long subjectId) throws IOException {

        // Check for null fields
        if (subjectId == null || followerId == null) {

            Errors.NULL_FIELDS.sendError(response);
            return false;
        }

        // Check the user is not trying to follow themselves
        if (subjectId.equals(followerId)) {

            Errors.CANNOT_FOLLOW_SELF.sendError(response);
            return false;
        }

        // Check the user exists
        User user = ObjectifyService.ofy().load().type(User.class).id(subjectId).now();
        if (user == null) {

            Errors.USER_NOT_FOUND.sendError(response);
            return false;
        }

        // Check the user does not already follow this user
        // Attempt to find an existing follower relationship
        Follower follower = ObjectifyService.ofy().load().type(Follower.class)
                .filter(Constants.Follower.Datastore.FOLLOWER_ID, followerId)
                .filter(Constants.Follower.Datastore.SUBJECT_ID, subjectId)
                .first().now();
        if (follower != null) {

            Errors.ALREADY_FOLLOWING.sendError(response);
            return false;
        }

        return true;
    }
}
