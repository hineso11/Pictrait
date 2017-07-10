package com.pictrait.api.endpoints.photo;

import com.googlecode.objectify.ObjectifyService;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.Like;
import com.pictrait.api.datastore.Photo;
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
 * Created by oliver on 10/07/2017.
 */
@WebServlet(name = "LikePhoto", value = "/photo/like")
@MultipartConfig
public class LikePhoto extends HttpServlet {
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
        Long photoId;
        // Attempt to get the photo id
        try {

            photoId = Long.parseLong(request.getParameter(Constants.Parameters.PHOTO_ID));
        } catch (NumberFormatException e) {
            // If the photo id was blank in params, set to null as exception handled in validation
            photoId = null;
        }

        if (validateFields(response, photoId, user)) {
            // If the validation has been successful, create a new like

            Like like = new Like(user, photoId);

            response.getWriter().write(like.toJson());
        }

    }

    private boolean validateFields (HttpServletResponse response, Long photoId, User user) throws IOException {

        // Check for null fields
        if (photoId == null) {

            response.sendError(Errors.NULL_FIELDS.getCode(), Errors.NULL_FIELDS.getMessage());
            return false;
        }

        // Check that a like doesn't already exist for this photo for this user
        // Attempt to find an existing record of like with user id and photo id
        Like existingLike = ObjectifyService.ofy().load().type(Like.class)
                .filter(Constants.Like.Datastore.PHOTO_ID, photoId)
                .filter(Constants.Like.Datastore.USER_ID, user.getUserId())
                .first().now();
        if (existingLike != null) {
            // If an existing like was found, send error
            response.sendError(Errors.ALREADY_LIKED.getCode(), Errors.ALREADY_LIKED.getMessage());
            return false;
        }

        // Check that the photo actually exists
        // Attempt to find the photo from photo id
        Photo photo = ObjectifyService.ofy().load().type(Photo.class).id(photoId).now();
        if (photo == null) {
            // If a photo was not found for this id, then send error
            response.sendError(Errors.PHOTO_DOESNT_EXIST.getCode(), Errors.PHOTO_DOESNT_EXIST.getMessage());
            return false;
        }

        return true;
    }
}
