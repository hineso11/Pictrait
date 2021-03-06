package com.pictrait.api.endpoints.photo;

import com.pictrait.api.constants.Constants;
import com.pictrait.api.constants.Errors;
import com.pictrait.api.datastore.Photo;
import com.pictrait.api.datastore.User;
import com.pictrait.api.security.Auth;
import com.pictrait.api.storage.FileService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by oliver on 07/07/2017.
 */
@WebServlet(name = "Upload", value = "/photo/upload")
@MultipartConfig
public class Upload extends HttpServlet {
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

        // Get the file that has been uploaded
        Part filePart = request.getPart(Constants.Parameters.PHOTO);

        // Check the photo matches relevant criteria, passes validation
        if (validatePhoto(response, filePart)) {

            // First create the database entity
            Photo photo = new Photo(user);

            InputStream fileContent = filePart.getInputStream();
            FileService service = new FileService();
            String fileName = String.valueOf(photo.getPhotoId());


            // Try to upload the file to GCS
            boolean uploadSuccess = service.uploadFile(fileContent, fileName, Constants.Photo.FOLDER, Constants.Photo.FILE_TYPE);
            // If the photo was uploaded successfully, change photoAvailable boolean
            if (uploadSuccess) {
                // Make the photo available
                photo.setPhotoAvailable(true);

                // Send the response
                response.setContentType(Constants.JSON_TYPE);
                response.getWriter().write(photo.toJson(user).toString());
            } else {

                // Send error telling user, there's error in uploading file
                Errors.IMAGE_NOT_UPLOADED.sendError(response);
            }

        }


    }

    private boolean validatePhoto (HttpServletResponse response, Part filePart) throws IOException {

        // Check that the file has actually been provided
        if (filePart == null || filePart.getSize() == 0) {

            Errors.NULL_FIELDS.sendError(response);
            return false;
        }


        // Check that the size of the image uploaded is less than the max image size
        double size = filePart.getSize() / (double) 1000000; // size in MB
        if (size > Constants.Photo.MAX_PHOTO_SIZE) {

            Errors.IMAGE_TOO_BIG.sendError(response);
            return false;
        }

        // Check that the file provided is of the type required (JPEG)
        if (!filePart.getContentType().equals(Constants.Photo.IMAGE_TYPE)) {

            Errors.FILE_NOT_JPEG.sendError(response);
            return false;
        }

        // Check that the image is a square
        InputStream fileContent = filePart.getInputStream();
        BufferedImage image = ImageIO.read(fileContent);
        if (image.getWidth() != image.getHeight()) {

            Errors.IMAGE_NOT_SQUARE.sendError(response);
            return false;
        }

        return true;
    }
}
