package com.pictrait.api.datastore;

import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by oliver on 18/05/2017.
 */
public class OfyHelper implements ServletContextListener {

    public void contextInitialized(ServletContextEvent event) {
        // This will be invoked as part of a warmup request, or the first user request if no warmup
        // request.

        ObjectifyService.register(User.class);
        ObjectifyService.register(Photo.class);
        ObjectifyService.register(Like.class);
        ObjectifyService.register(Follower.class);
    }

    public void contextDestroyed(ServletContextEvent event) {
        // App Engine does not currently invoke this method.
    }
}
