package com.pictrait.api.endpoints.user;

/**
 * Created by oliver on 06/07/2017.
 */

import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
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

/**
 * Created by oliver on 06/07/2017.
 */

@WebServlet(name = "Auth", value = "/user/auth")
@MultipartConfig
public class Auth extends HttpServlet {

    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // MARK: Security
        // Check whether the client ID has been supplied
        if (!com.pictrait.api.security.Auth.checkClientID(request, response)) {
            // If there is a problem with client ID, end servlet as error sent
            return;
        }
        // END: Security

        // MARK: Get parameters
        String refreshToken = request.getParameter(Constants.Parameters.REFRESH_TOKEN);

        // Check validation
        if (validateFields(response, refreshToken)) {

            // Attempt to get the new auth token object
            AuthenticationToken token = new AuthenticationToken(refreshToken, AuthenticationToken.TokenType.REFRESH_TOKEN, response);

            // Send the response
            response.setContentType(Constants.JSON_TYPE);
            response.getWriter().write(token.toJson());
        }
    }

    private boolean validateFields (HttpServletResponse response, String refreshToken) throws IOException {

        // MARK: Validate Fields
        // Check for null fields
        if (refreshToken == null || refreshToken.isEmpty()) {
            response.sendError(Errors.NULL_FIELDS.getCode(), Errors.NULL_FIELDS.getMessage());
            return false;
        }
        // END: Validate Fields

        return true;
    }

}