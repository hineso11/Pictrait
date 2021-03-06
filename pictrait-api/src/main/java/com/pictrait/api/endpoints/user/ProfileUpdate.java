package com.pictrait.api.endpoints.user;

import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pictrait.api.constants.Constants.User.*;

/**
 * Created by oliver on 07/07/2017.
 */
@WebServlet(name = "ProfileUpdate", value = "/user/update")
@MultipartConfig
public class ProfileUpdate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MARK: Security
        // Check whether the client ID has been supplied
        if (!com.pictrait.api.security.Auth.checkClientID(request, response)) {
            // If there is a problem with client ID, end servlet as error sent
            return;
        }

        // Check that the auth token for the user is valid
        User user = com.pictrait.api.security.Auth.checkAuthToken(request, response);
        if (user == null) {
            // If there is a problem with client ID, end servlet as error sent
            return;
        }
        // END: Security

        // MARK: Get parameters
        String username = request.getParameter(Constants.Parameters.USERNAME);
        String fullName = request.getParameter(Constants.Parameters.FULL_NAME);
        String email = request.getParameter(Constants.Parameters.EMAIL);

        if (validateFields(response, username, fullName, email)) {

            // If the fields supplied have passed all necessary validation, update all the fields
            user.changeUsername(username);
            user.changeEmail(email);
            user.changeFullName(fullName);


            // Send the response
            response.setContentType(Constants.JSON_TYPE);
            response.getWriter().write(user.toJson(user).toString());
        }

    }

    private boolean validateFields (HttpServletResponse response, String username, String fullName, String email) throws IOException {

        // MARK: Validate Fields
        // For each field that can be changed by user, check if its blank or not
        // and edit it if it has been provided

        // If the username is provided
        if (username != null && !username.isEmpty()) {

            // Check length of username
            if (username.length() > MAX_USERNAME_LENGTH) {
                Errors.USERNAME_LONG.sendError(response);
                return false;
            }
            if (username.length() < MIN_USERNAME_LENGTH) {
                Errors.USERNAME_SHORT.sendError(response);
                return false;
            }

            // Check the username uses only accepted characters
            if (!username.matches("^[a-zA-Z0-9._-]{3,}$")) {
                Errors.USERNAME_FORMAT.sendError(response);
                return false;
            }

            // Check the username is unique
            if (!User.usernameIsUnique(username)) {

                Errors.USERNAME_IN_USE.sendError(response);
                return false;
            }

        }

        // If the full name is provided
        if (fullName != null && !fullName.isEmpty()) {

            // Check the length of the name
            if (fullName.length() > MAX_NAME_LENGTH) {
                Errors.NAME_LONG.sendError(response);
                return false;
            }

        }

        // If the email is provided
        if (email != null && !fullName.isEmpty()) {

            // Check the length of the email
            if (email.length() > MAX_EMAIL_LENGTH) {
                Errors.EMAIL_LONG.sendError(response);
                return false;
            }
            // Check that the email is the correct format
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email);
            if (!m.matches()) {
                Errors.EMAIL_FORMAT.sendError(response);
                return false;
            }
            // Check the email is unique
            if (!User.emailIsUnique(email)) {

                Errors.EMAIL_IN_USE.sendError(response);
                return false;
            }

        }
        // END: Validate Fields

        return true;
    }
}
