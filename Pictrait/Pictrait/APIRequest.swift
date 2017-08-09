//
//  APIRequest.swift
//  Pictrait
//
//  Created by Oliver Hines on 02/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation
import SwiftHTTP

class APIRequest {
    
    // MARK: Constants
    private static let BASE_URL = "pictrait-app.appspot.com" // Base url of the API
    static let GENERAL_ERROR = "There was a problem making this request" // general message for unhandled error
    private static let CLIENT_ID_PARAM = "client_id"
    private static let CLIENT_ID_VAL = "clientID"
    private static let AUTH_TOKEN_PARAM = "auth_token"
    
    // HTTP Codes
    static let HTTP_OK = 200 // request ok
    private static let SECURITY_ERROR = 401 // security error in app
    private static let APP_ERROR = 501 // app error
    
    // MARK: Variables
    private var parameters: [String: Any]
    private var urlEnding: String
    private var operation: HTTP?
    private var callback: ([String: Any]?, AppError?) -> Void
    private var mainRequest: HTTPRequest?
    private var shouldRefresh: Bool
    private var method: HTTPVerb
    
    // MARK: Constructors
    init(parameters: [String: Any], urlEnding: String, shouldRefresh: Bool, method: HTTPVerb, callback: @escaping ([String: Any]?, AppError?) -> Void) {
        
        self.parameters = parameters
        self.urlEnding = urlEnding
        self.callback = callback
        self.shouldRefresh = shouldRefresh
        self.method = method
        
        // Add security to request 
        addSecurity()
    }
    
    // MARK: Methods
    
    // Method to add security data to the request
    private func addSecurity () {
        
        // Add the client ID
        parameters[APIRequest.CLIENT_ID_PARAM] = APIRequest.CLIENT_ID_VAL
        
        // Add the auth token 
        parameters[APIRequest.AUTH_TOKEN_PARAM] = Auth.sharedInstance.getAuthToken()
    }
    
    // Method to make a request to the API
    func doPost () {
        
        self.mainRequest = HTTPRequest(host: APIRequest.BASE_URL, path: urlEnding, parameters: parameters, callback: {
            response, error, code in
            
            self.mainCallback(response: response, error: error, code: code)
        })
        
        mainRequest?.doPost()
    }
    func doGet () {
        
        self.mainRequest = HTTPRequest(host: APIRequest.BASE_URL, path: urlEnding, parameters: parameters, callback: {
            response, error, code in
            
            self.mainCallback(response: response, error: error, code: code)
        })
        mainRequest?.doGet()
    }
    
    // Method to retry the original request 
    private func retryRequest () {
        
        addSecurity()
        switch method {
        case .POST:
            
            doPost()
        case .GET:
            
            doGet()
        default:
            break
        }
    }
    
    // Callback for the main request
    private func mainCallback (response: [String: Any]?, error: AppError?, code: Int?) {
        
        // Coordinate response based on the status code
        switch code {
        case APIRequest.HTTP_OK?:
            // If the request is ok, then just return response in callback
            callback(response, nil)
        case APIRequest.APP_ERROR?:
            // If the error is app related then just return error
            callback(nil, error)
        case APIRequest.SECURITY_ERROR?:
            // The error is security related, eg: the auth token or refresh token has expired
            handleSecurityError(error: error)
            
        default:
            callback(nil, error)
        }
    }
    
    // Method to handle security errors 
    private func handleSecurityError (error: AppError?) {
        
        switch error?.errorType.rawValue {
        case ErrorType.USER_DOESNT_EXIST.rawValue?:
            // The user currently logged in no longer exists
            // User must be logged out
            Auth.sharedInstance.logout()
        case ErrorType.AUTH_EXPIRED.rawValue?:
            print("auth token expired")
            // The auth token has expired, request a new one with the refresh token
            // Check that the request should refresh (ie is not a security request anyway)
            if (shouldRefresh) {
                // Attempt to get new auth token
                Auth.sharedInstance.reauth(callback: {
                    success in
                    
                    if (success) {
                        // Retry the original request
                        print("new auth token obtained")
                        self.retryRequest()
                    } else {
                        // Log the user out
                        print("error getting auth token")
                        Auth.sharedInstance.logout()
                    }
                })
                
            } else {
                // If the request is already a security request, log them out as theres a big problem
                Auth.sharedInstance.logout()
            }
        case ErrorType.REFRESH_EXPIRED.rawValue?:
            // The refresh token has expired, login again using the username and password
            print("refresh token expired")
            Auth.sharedInstance.relogin(callback: {
                success in
                
                if (success) {
                    print("new refresh token obtained")
                    self.retryRequest()
                } else {
                    print("error getting new refresh token")
                    Auth.sharedInstance.logout()
                }
            })
        default:
            // Any other type of security error results in the user being logged out
            Auth.sharedInstance.logout()
        }

    }
}
