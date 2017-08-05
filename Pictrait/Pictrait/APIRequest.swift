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
    
    // MARK: Constructors
    init(parameters: [String: Any], urlEnding: String, shouldRefresh: Bool, callback: @escaping ([String: Any]?, AppError?) -> Void) {
        
        self.parameters = parameters
        self.urlEnding = urlEnding
        self.callback = callback
        
        // Add security to request 
        addSecurity()
    }
    
    // MARK: Methods
    
    // Method to add security data to the request
    private func addSecurity () {
        
        // Add the client ID
        parameters[APIRequest.CLIENT_ID_PARAM] = APIRequest.CLIENT_ID_VAL
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
            print("Security error")
            callback(nil, error)
        default:
            callback(nil, error)
        }
    }
}
