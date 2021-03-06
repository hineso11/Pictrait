package com.pictrait.api.constants;

/**
 * Created by oliver on 05/07/2017.
 */
public class Constants {

    // TODO Change to production client ids
    public static final String[] clientIDs = {"clientID", "sfsffw34gtgwg121"};
    public static final String BUCKET_NAME = "pictrait-app.appspot.com";
    public static final String BASE_FILE_URL = "https://storage.googleapis.com/";
    public static final String JSON_TYPE = "text/json";
    public static final String ERROR_REASON  = "errorReason";

    public class Parameters {

        public static final String CLIENT_ID = "client_id";

        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String FULL_NAME = "full_name";
        public static final String EMAIL = "email";

        public static final String AUTH_TOKEN = "auth_token";
        public static final String REFRESH_TOKEN = "refresh_token";

        public static final String PHOTO = "photo";
        public static final String PHOTO_ID = "photo_id";

        public static final String SEARCH_STRING = "search_string";

        public static final String USER_ID = "user_id";

        public static final String SUBJECT_ID = "subject_id";

        public static final String FEED_TYPE = "feed_type";
    }

    public class User {

        public static final int MAX_USERNAME_LENGTH = 15;
        public static final int MIN_USERNAME_LENGTH = 7;
        public static final int MAX_PASSWORD_LENGTH = 15;
        public static final int MIN_PASSWORD_LENGTH = 7;
        public static final int MAX_EMAIL_LENGTH = 254;
        public static final int MAX_NAME_LENGTH = 25;

        public static final String FOLLOWER_COUNT = "followers";
        public static final String FOLLOWING_COUNT = "following";
        public static final String IS_FOLLOWING = "isFollowing";

        public class Datastore {

            public static final String USER_ID = "userId";
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
        public static final String LIKES_COUNT = "likes";
        public static final String HAS_LIKED = "userHasLiked";

        public static final String NEWSFEED = "newsfeed";
        public static final String PROFILE ="profile";

        public class Datastore {

            public static final String PHOTO_ID = "photoId";
            public static final String USER_ID = "userId";
            public static final String PHOTO_AVAILABLE = "photoAvailable";
            public static final String CREATED_AT = "createdAt";
        }
    }

    public class Like {

        public class Datastore {

            public static final String PHOTO_ID = "photoId";
            public static final String USER_ID = "userId";
        }
    }

    public class Follower {

        public class Datastore {

            public static final String FOLLOWER_ID = "followerId";
            public static final String SUBJECT_ID = "subjectId";
        }
    }

    public class AuthenticationToken {

        public static final String AUTH_TOKEN = "auth_token";
        public static final String REFRESH_TOKEN = "refresh_token";
    }
}
