package com.pictrait.api.constants;

/**
 * Created by oliver on 05/07/2017.
 */
public class Constants {

    public static final String[] clientIDs = {"clientID", "sfsffw34gtgwg121"};

    public class Parameters {

        public static final String CLIENT_ID = "client_id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String FULL_NAME = "full_name";
        public static final String EMAIL = "email";
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
            public static final String EMAIL = "email";
        }
    }
}
