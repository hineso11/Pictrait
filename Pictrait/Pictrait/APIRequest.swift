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
    
    // Base url of the API
    static let BASE_URL = "https://pictrait-app.appspot.com/"
    // HTTP Request OK code
    static let HTTP_OK = 200
    
    // Wrapper to make post request
    func makePostReqeust (urlEnding: String, params: [String: AnyObject], callback: @escaping ([String: Any]?, AppError?) -> Void) {

        // Try to create and start operation
        do {
            // operation uses BASE_URL + function provided ending and params
            let operation = try HTTP.POST(APIRequest.BASE_URL + urlEnding, parameters: params)
            // start the operation
            doOperation(operation: operation, callback: <#T##(Response) -> Void#>)
        } catch {
            // If an error was found creating or starting request
            // Trigger callback
            
            // Create error object with generic error
            callback(nil, AppError(statusCode: 000, errorReason: "Could not contact server at this time"))
        }
    }
    
    private func doOperation (operation: HTTP, callback: @escaping (Response) -> Void) {
        
        operation.start { response in
            
            // If success code found
            if (response.statusCode == APIRequest.HTTP_OK) {
                
                // Read in the response from raw data
                var jsonArray: [String: Any]?
                do {
                    // to json
                    jsonArray = try JSONSerialization.jsonObject(with: response.data) as? [String: Any]
                    
                    // return data to callback
                    callback(jsonArray, nil)
                } catch {
                    
                    // error trying to parse json, send general error
                    callback(nil, AppError(statusCode: response.statusCode!, errorReason: "There was an error completing this request"))
                }
            } else {
                
                
                // Create the error object using error from response
                let error = AppError(statusCode: response.statusCode!, errorReason: self.findError(data: response.data))
                callback(nil, error)
            }

        }
    }
    
    private func findError (data: Data) -> String {

        // Get string
        let htmlString = String(bytes: data, encoding: .ascii)
        
        // Scrape html for error
        if let match = htmlString?.range(of: "(?<=<h1>Error: )[^</h1>]+", options: .regularExpression) {
            
            return (htmlString?.substring(with: match))!
        } else {
            
            // Give a default error if none could be found
            return "There was an error completing this request"
        }
        
    }
    
}
