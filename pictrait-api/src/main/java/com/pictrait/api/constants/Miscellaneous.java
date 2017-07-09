package com.pictrait.api.constants;

/**
 * Created by oliver on 06/07/2017.
 */
public class Miscellaneous {

    public static boolean isDevelopmentServer () {

        if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
            // Check the System properties to determine if we are running on appengine or not
            // Google App Engine sets a few system properties that will reliably be present on a remote
            // instance.
            return false;
        } else {
           return true;
        }
    }
}
