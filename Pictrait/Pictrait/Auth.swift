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
    private static let EMAIL_PARAM = "email"
    private static let FULL_NAME_PARAM = "full_name"
    
    // Json Parameters and persistent data store keys
    private static let AUTH_TOKEN = "auth_token"
    private static let REFRESH_TOKEN = "refresh_token"
    private static let USERNAME = "username"
    private static let PASSWORD = "password"
    private static let CURRENT_USER = "currentUser"
    
    // Persistent data params
    private static let IS_LOGGED_IN = "isLoggedIn"
    
    static let sharedInstance = Auth()
    
    // API Url Paths
    private static let LOGIN_PATH = "/user/login"
    private static let SIGN_UP_PATH = "/user/signup"
    private static let AUTH_PATH = "/user/auth"
    
    // MARK: Methods
    
    // Method to log into the app using username and password
    func login (username: String, password: String, callback: @escaping (Bool, AppError?) -> Void) {
        
        // Store the username and password for later use
        UserDefaults.standard.set(username, forKey: Auth.USERNAME)
        UserDefaults.standard.set(password, forKey: Auth.PASSWORD)
        
        let params = [Auth.USERNAME_PARAM: username as Any, Auth.PASSWORD_PARAM: password as Any]
        let request = APIRequest(parameters: params, urlEnding: Auth.LOGIN_PATH, shouldRefresh: true, method: .POST, callback: { response, error in
            
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
    
    // Method to sign up using username, password, email and full name
    func signUp (username: String, password: String, email: String, fullName: String, callback: @escaping (Bool, AppError?) -> Void) {
        
        let params = [Auth.USERNAME_PARAM: username as AnyObject, Auth.PASSWORD_PARAM: password as AnyObject,
                      Auth.EMAIL_PARAM: email as AnyObject, Auth.FULL_NAME_PARAM: fullName as AnyObject]
        let request = APIRequest(parameters: params, urlEnding: Auth.SIGN_UP_PATH, shouldRefresh: true, method: .POST, callback: {
            response, error in
            
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
    
    // Method to reauth using the refresh token
    func reauth (callback: @escaping (Bool) -> Void) {
        
        print("reauth")
        let refreshToken  = UserDefaults.standard.string(forKey: Auth.REFRESH_TOKEN)
        let params = [Auth.REFRESH_TOKEN: refreshToken as AnyObject]
        
        let request = APIRequest(parameters: params, urlEnding: Auth.AUTH_PATH, shouldRefresh: false, method: .POST, callback: {
            response, error in
            
            if (error == nil) {
                
                // Store the returned auth information and old refresh token
                let authToken = response?[Auth.AUTH_TOKEN] as! String
                self.storeAuth(authToken: authToken, refreshToken: refreshToken!)
                callback(true)
            } else {
                
                callback(false)
            }
        })
        request.doPost()
    }
    
    // Method to login again when refresh token has expired
    func relogin (callback: @escaping (Bool) -> Void) {
        
        let username = UserDefaults.standard.string(forKey: Auth.USERNAME)
        let password = UserDefaults.standard.string(forKey: Auth.PASSWORD)
        
        login(username: username!, password: password!, callback: {
            success, error in
            
            callback(success)
        })
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
    
    // Function to get the current auth token 
    func getAuthToken () -> String? {
        
        return UserDefaults.standard.string(forKey: Auth.AUTH_TOKEN)
    }
    
    // Function to get the logged in user's username
    func getUsername () -> String {
        
        return UserDefaults.standard.string(forKey: Auth.USERNAME)!
    }
}


