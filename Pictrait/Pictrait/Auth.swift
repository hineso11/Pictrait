//
//  Auth.swift
//  Pictrait
//
//  Created by Oliver Hines on 05/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation
import UIKit

class Auth {
    
    // MARK: Constants
    // Parameters
    private static let USERNAME_PARAM = "username"
    private static let PASSWORD_PARAM = "password"
    
    // Json Parameters (also used in persistent data store)
    private static let AUTH_TOKEN = "auth_token"
    private static let REFRESH_TOKEN = "refresh_token"
    
    // Persistent data params
    private static let IS_LOGGED_IN = "isLoggedIn"
    
    static let sharedInstance = Auth()
    
    // MARK: Methods
    
    // Method to log into the app using username and password
    func login (username: String, password: String, callback: @escaping (Bool, AppError?) -> Void) {
        
        let params = [Auth.USERNAME_PARAM: username as AnyObject, Auth.PASSWORD_PARAM: password as AnyObject]
        let request = APIRequest(parameters: params, urlEnding: "/user/login", shouldRefresh: true, callback: { response, error in
            
            if (error != nil) {
                
                callback(false, error!)
            } else {
                
                // Store the returned auth information
                let authToken = response?[Auth.AUTH_TOKEN] as! String
                let refreshToken = response?[Auth.REFRESH_TOKEN] as! String
                self.storeAuth(authToken: authToken, refreshToken: refreshToken)
                
                // Trigger callback
                callback(true, nil)
            }
        })
        request.doPost()
    }
    
    // Method to store the auth information
    private func storeAuth (authToken: String, refreshToken: String) {
        
        // Store the auth token, refresh token, etc
        UserDefaults.standard.set(authToken, forKey: Auth.AUTH_TOKEN)
        UserDefaults.standard.set(refreshToken, forKey: Auth.REFRESH_TOKEN)
        UserDefaults.standard.set(true, forKey: Auth.IS_LOGGED_IN)
        
    }
    
    // Function to assertain whether a user is logged in
    func isLoggedIn () -> Bool {
        
        return UserDefaults.standard.bool(forKey: Auth.IS_LOGGED_IN)
    }

    // Method to log out the user from the system and redirect them to the login page
    func logout () {
        
        // Log them out in the system
        UserDefaults.standard.set(false, forKey: Auth.IS_LOGGED_IN)
        
        // Redirect to the login page
        let currentVC = UIApplication.shared.keyWindow?.rootViewController
        let loginVC = currentVC?.storyboard?.instantiateViewController(withIdentifier: Constants.StoryboardId.LOGIN_CONTROLLER.rawValue)
        UIApplication.shared.keyWindow?.rootViewController = loginVC
        
    }
}


