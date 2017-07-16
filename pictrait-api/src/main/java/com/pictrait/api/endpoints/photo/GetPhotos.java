package com.pictrait.api.endpoints.photo;

import com.googlecode.objectify.ObjectifyService;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.Follower;
import com.pictrait.api.datastore.Photo;
import com.pictrait.api.datastore.User;
import com.pictrait.api.security.Auth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oliver on 15/07/2017.
 */
@WebServlet(name = "GetPhotos", value = "/photo/feed")
public class GetPhotos extends HttpServlet {


    // Enum for the type of the request
    private enum FeedType {

        NEWSFEED(Constants.Photo.NEWSFEED),
        PROFILE(Constants.Photo.PROFILE);

        // MARK: Variables
        private String type;

        // MARK: Constructors
        FeedType(String type) {

            this.type = type;
        }
    }

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
        String feedType = request.getParameter(Constants.Parameters.FEED_TYPE);

        // Check that the field type is not blank
        if (feedType != null && !feedType.isEmpty()) {

            // Determine what type the request is
            if (feedType.equals(FeedType.NEWSFEED.type)) {

                getNewsfeed(response, user);
            }
            if (feedType.equals(FeedType.PROFILE.type)) {

                getProfile(request, response);
            }
        } else {

            response.sendError(Errors.NULL_FIELDS.getCode(), Errors.NULL_FIELDS.getMessage());
        }
    }

    // Procedure to return a newsfeed of photos to the user
    private void getNewsfeed (HttpServletResponse response, User user) throws IOException {

        // Get all the followings for the user
        List<Follower> following = ObjectifyService.ofy().load().type(Follower.class)
                .filter(Constants.Follower.Datastore.FOLLOWER_ID, user.getUserId())
                .list();

        // Get the subject ids for the entities
        List<Long> userIds = new ArrayList<Long>();
        for (Follower follower: following) {

            userIds.add(follower.getSubjectId());
        }

        // Get all photos for the user ids
        List<Photo> photos;
        if (userIds.size() > 0) {
            photos = ObjectifyService.ofy().load().type(Photo.class)
                    .filter(Constants.Photo.Datastore.USER_ID + " in", userIds)
                    .filter(Constants.Photo.Datastore.PHOTO_AVAILABLE, true)
                    .order("-" + Constants.Photo.Datastore.CREATED_AT) // order by newest
                    .limit(25).list();

            // Parse to JSON
            JSONArray photosArray = new JSONArray();
            for (Photo photo: photos) {

                photosArray.put(photo.toJson());
            }

            // Send the response in json
            JSONObject mainObject = new JSONObject();
            try {
                mainObject.put("photos", photosArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.setContentType(Constants.JSON_TYPE);
            response.getWriter().write(mainObject.toString());
        } else {

            response.sendError(Errors.NOT_FOLLOWING_ANYONE.getCode(), Errors.NOT_FOLLOWING_ANYONE.getMessage());
        }


    }

    // Procedure to return the photos associated with a profile to the user
    private void getProfile (HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Get the user id of the profile
        Long userId;
        // Attempt to get the photo id
        try {

            userId = Long.parseLong(request.getParameter(Constants.Parameters.USER_ID));
        } catch (NumberFormatException e) {
            // If the photo id was blank in params, set to null as exception handled in validation
            userId = null;
        }

        // Check that the user id is not null
        if (userId != null) {
            // User id supplied, check that the user exists
            User user = ObjectifyService.ofy().load().type(User.class).id(userId).now();
            // If the user does not exist, end servlet request, else carry on
            if (user == null) {

                response.sendError(Errors.USER_NOT_FOUND.getCode(), Errors.USER_NOT_FOUND.getMessage());
                return;
            }

            // Get all photos for a specific user
            List<Photo> photos = ObjectifyService.ofy().load().type(Photo.class)
                    .filter(Constants.Photo.Datastore.USER_ID, userId)
                    .filter(Constants.Photo.Datastore.PHOTO_AVAILABLE, true)
                    .order("-" + Constants.Photo.Datastore.CREATED_AT) // Order by newest first
                    .limit(25)
                    .list();

            // Parse all objects into json array
            JSONArray jsonArray = new JSONArray();
            for (Photo photo: photos) {

                jsonArray.put(photo.toJson());
            }
            JSONObject mainObject = new JSONObject();
            try {
                mainObject.put("photos", jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // Send the json response
            response.setContentType(Constants.JSON_TYPE);
            response.getWriter().write(mainObject.toString());

        } else {
            // User id not supplied, send error
            response.sendError(Errors.NULL_FIELDS.getCode(), Errors.NULL_FIELDS.getMessage());
        }
    }


}
