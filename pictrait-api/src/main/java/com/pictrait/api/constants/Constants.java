package com.pictrait.api.constants;

/**
 * Created by oliver on 05/07/2017.
 */
public class Constants {

    public static final String[] clientIDs = {"clientID", "sfsffw34gtgwg121"};
    public static final String BUCKET_NAME = "pictrait-app.appspot.com";

    public class Parameters {

        public static final String CLIENT_ID = "client_id";

        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String FULL_NAME = "full_name";
        public static final String EMAIL = "email";

        public static final String AUTH_TOKEN = "auth_token";
        public static final String REFRESH_TOKEN = "refresh_token";

        public static final String PHOTO = "photo";
    }

    public class User {

        public static final int MAX_USERNAME_LENGTH = 15;
        public static final int MIN_USERNAME_LENGTH = 7;
        public static final int MAX_PASSWORD_LENGTH = 15;
        public static final int MIN_PASSWORD_LENGTH = 7;
        public static final int MAX_EMAIL_LENGTH = 254;
        public static final int MAX_NAME_LENGTH = 25;

        public class Datastore {

            public static final String USERNAME = "username";
            public static final String FULL_NAME = "fullName";
            public static final String EMAIL = "email";
        }
    }

    public class Photo {

        public static final double MAX_PHOTO_SIZE = 2.0; // Megabytes
        public static final String IMAGE_TYPE = "image/jpeg";
        public static final String FILE_TYPE = "jpg";
        public static final String FOLDER = "photos";

        public static final int PHOTO_ACCESS_DAYS = 1;

        public static final String DOWNLOAD_URL = "download_url";

        public class Datastore {

            public static final String PHOTO_ID = "photoId";
        }
    }

    public class AuthenticationToken {

        public static final String AUTH_TOKEN = "auth_token";
        public static final String REFRESH_TOKEN = "refresh_token";
    }
}
