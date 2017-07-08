package com.pictrait.api.storage;

import com.google.appengine.tools.cloudstorage.*;
import com.pictrait.api.constants.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

/**
 * Created by oliver on 08/07/2017.
 */
public class FileService {


    // MARK: Constants
    // Specify the params to create connection with GCS service
    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());

    // To determine how big chunks read in in copy are
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;

    // MARK: Methods
    // Function to upload a new file to GCS
    public boolean uploadFile (InputStream inputStream, String fileName, String folder, String fileType) {

        GcsFileOptions instance = GcsFileOptions.getDefaultInstance();
        GcsFilename name = new GcsFilename(Constants.BUCKET_NAME, folder + "/" + fileName + "." + fileType);
        GcsOutputChannel outputChannel;

        try {

            outputChannel = gcsService.createOrReplace(name, instance);
            copy(inputStream, Channels.newOutputStream(outputChannel));
            return true;
        } catch (IOException e) {

            return false;
        }
    }

    // Function to transfer data from input to output stream then finally close both streams
    private void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    }
}
