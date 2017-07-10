package com.pictrait.api.endpoints.user;

import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.User;
import com.pictrait.api.security.Auth;
import com.pictrait.api.security.AuthenticationToken;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pictrait.api.constants.Constants.User.*;

/**
 * Created by oliver on 05/07/2017.
 */
@WebServlet(name = "SignUp", value = "/user/signup")
@MultipartConfig
public class SignUp extends HttpServlet {
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
        String fullName = request.getParameter(Constants.Parameters.FULL_NAME);
        String email = request.getParameter(Constants.Parameters.EMAIL);
        // END: Get parameters

        // Check the data passes all validation
        if (validateFields(response, username, password, fullName, email)) {

            // Sign up the new user
            User user = new User(username, password, fullName, email);

            // Supply the user with a refresh and auth token
            AuthenticationToken token = new AuthenticationToken(user);

            // Send the response
            response.getWriter().write(token.toJson());
        }

    }

    private boolean validateFields (HttpServletResponse response, String username, String password, String fullName, String email) throws IOException {

        // MARK: Validate Fields
        // Check for null fields
        if (username == null || password == null || fullName == null || email == null ||
                username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
            response.sendError(Errors.NULL_FIELDS.getCode(), Errors.NULL_FIELDS.getMessage());
            return false;
        }
        // Check the fields are the correct length
        if (username.length() > MAX_USERNAME_LENGTH) {
            response.sendError(Errors.USERNAME_LONG.getCode(), Errors.USERNAME_LONG.getMessage());
            return false;
        }
        if (username.length() < MIN_USERNAME_LENGTH) {
            response.sendError(Errors.USERNAME_SHORT.getCode(), Errors.USERNAME_SHORT.getMessage());
            return false;
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            response.sendError(Errors.PASSWORD_LONG.getCode(), Errors.PASSWORD_LONG.getMessage());
            return false;
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            response.sendError(Errors.PASSWORD_SHORT.getCode(), Errors.PASSWORD_SHORT.getMessage());
            return false;
        }
        if (email.length() > MAX_EMAIL_LENGTH) {
            response.sendError(Errors.EMAIL_LONG.getCode(), Errors.EMAIL_LONG.getMessage());
            return false;
        }
        if (fullName.length() > MAX_NAME_LENGTH) {
            response.sendError(Errors.NAME_LONG.getCode(), Errors.NAME_LONG.getMessage());
            return false;
        }
        // Check that the email is the correct format
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        if (!m.matches()) {
            response.sendError(Errors.EMAIL_FORMAT.getCode(), Errors.EMAIL_FORMAT.getMessage());
            return false;
        }
        // Check the username uses only accepted characters
        if (!username.matches("^[a-zA-Z0-9._-]{3,}$")) {
            response.sendError(Errors.USERNAME_FORMAT.getCode(), Errors.USERNAME_FORMAT.getMessage());
            return false;
        }

        // Check the username is unique
        if (!User.usernameIsUnique(username)) {

            response.sendError(Errors.USERNAME_IN_USE.getCode(), Errors.USERNAME_IN_USE.getMessage());
            return false;
        }
        // Check the email is unique
        if (!User.emailIsUnique(email)) {

            response.sendError(Errors.EMAIL_IN_USE.getCode(), Errors.EMAIL_IN_USE.getMessage());
            return false;
        }
        // END: Validate Fields

        // Return true if no problems with validation
        return true;
    }

}
