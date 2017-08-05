//
//  Auth.swift
//  Pictrait
//
//  Created by Oliver Hines on 05/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation

class Auth {
    
    // MARK: Constants
    // Parameters
    private static let USERNAME_PARAM = "username"
    private static let PASSWORD_PARAM = "password"
    
    // MARK: Methods
    
    // Method to log into the app using username and password
    func login (username: String, password: String) {
        
        let params = [Auth.USERNAME_PARAM: username as AnyObject, Auth.PASSWORD_PARAM: password as AnyObject]
        let request = APIRequest(parameters: params, urlEnding: "/user/login", shouldRefresh: true, callback: { response, error in
            
            print(error?.errorType)
            print(response)
        })
        request.doPost()
    }
    
    func logout () {
        
        // Logout code here
    }
    
    func reauth () {
        
    }
}
