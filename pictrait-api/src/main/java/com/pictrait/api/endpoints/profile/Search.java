package com.pictrait.api.endpoints.profile;

import com.googlecode.objectify.ObjectifyService;
import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
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
import java.util.List;

/**
 * Created by oliver on 12/07/2017.
 */
@WebServlet(name = "Search", value = "/profile/search")
public class Search extends HttpServlet {

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
        String searchString = request.getParameter(Constants.Parameters.SEARCH_STRING);

        // If the given parameters pass validation, execute query
        if (validateFields(response, searchString)) {

            // Find all entities that start with the given substring
            List<User> users = ObjectifyService.ofy().load().type(User.class)
                    .filter(Constants.User.Datastore.FULL_NAME + " >=", searchString)
                    .filter(Constants.User.Datastore.FULL_NAME + " <", searchString + "\ufffd")
                    .limit(20) // limit the number of results returned
                    .list();

            JSONArray usersArray = new JSONArray();
            for (User user1: users) {

                usersArray.put(user1.toJson());
            }

            // Send the response in json
            JSONObject mainObject = new JSONObject();
            try {
                mainObject.put("users", usersArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            response.setContentType(Constants.JSON_TYPE);
            response.getWriter().write(mainObject.toString());
        }
    }

    private boolean validateFields (HttpServletResponse response, String searchString) throws IOException {

        // MARK: Validate Fields
        // For each field that can be changed by user, check if its blank or not
        // and edit it if it has been provided

        // If the username is provided
        if (searchString == null || searchString.isEmpty()) {

            response.sendError(Errors.NULL_FIELDS.getCode(), Errors.NULL_FIELDS.getMessage());
            return false;
        }

        return true;
    }
}
