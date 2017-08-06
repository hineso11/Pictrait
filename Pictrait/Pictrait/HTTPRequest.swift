//
//  HTTPRequest.swift
//  Pictrait
//
//  Created by Oliver Hines on 05/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation
import SwiftHTTP

class HTTPRequest {
    
    // MARK: Constants
    
    private static let SCHEME = "https"
    
    // MARK: Variables
    private var parameters: [String: Any]?
    private var callback: ([String: Any]?, AppError?, Int) -> Void
    private var operation: HTTP?
    private var host: String
    private var path: String
    
    // MARK: Constructors
    init (host: String, path: String, parameters: [String: Any]?, callback: @escaping ([String: Any]?, AppError?, Int) -> Void) {
        
        self.parameters = parameters
        self.callback = callback
        self.host = host
        self.path = path
    }
    
    // MARK: Public methods used by API functions classes to connect to server
    func doPost () {
        
        // Create the request, params and url etc
        do {
            
            operation = try HTTP.POST(constructUrl(items: nil), parameters: parameters)
            // Send request to central method
            makeRequest()
        } catch {
            
            // Create a general error
            let error = AppError(statusCode: 000, errorReason: APIRequest.GENERAL_ERROR)
            // Throw an error in creating the request
            callback(nil, error, 000)
        }
    }
    
    func doGet () {
        
        // Create the request, params and url etc
        do {
            
            var urlParams = [URLQueryItem]()
            // Add the parameters to the url
            for (name, value) in parameters! {
                
                 urlParams.append(URLQueryItem(name: name, value: value as? String))
            }
            
            operation = try HTTP.GET(constructUrl(items: urlParams))
            // Send request to central method
            makeRequest()
        } catch {
            
            // Create a general error
            let error = AppError(statusCode: 000, errorReason: APIRequest.GENERAL_ERROR)
            // Throw an error in creating the request
            callback(nil, error, 000)
        }

    }
    
    // MARK: Private methods used to make a request
    
    // Method to construct the url with escaped params etc if necessary
    private func constructUrl (items: [URLQueryItem]?) -> String {
        
        var urlConstructor = URLComponents()
        urlConstructor.scheme = HTTPRequest.SCHEME
        urlConstructor.host = host
        urlConstructor.path = path
        urlConstructor.queryItems = items
        
        return (urlConstructor.url?.absoluteString)!
    }
    
    // Method used by get and post to make the request
    private func makeRequest () {
        
        // Finish making the request
        operation?.start { response in
            
            // Callback code
            // If there is no error
            if (response.statusCode == APIRequest.HTTP_OK) {
                // Send data with no error to calling function
                do {
                    // to json
                    let jsonArray = try JSONSerialization.jsonObject(with: response.data) as? [String: Any]
                    self.callback(jsonArray, nil, response.statusCode!)
                } catch {
                    // error trying to parse json, send general error
                    self.callback(nil, AppError(statusCode: 000, errorReason: APIRequest.GENERAL_ERROR), 000)
                }
            } else {
                // Handle the error
                self.handleError(response: response)
            }
        }
    }
    
    // Method used to handle the error
    private func handleError (response: Response) {
        
        // Find out what the error was specifically
        let errorString = findError(data: response.data)
        let error = AppError(statusCode: response.statusCode!, errorReason: errorString)
        
        callback(nil, error, response.statusCode!)
    }
    
    // Function to find the error in a response
    private func findError (data: Data) -> String {
        
        // Get string
        let htmlString = String(bytes: data, encoding: .ascii)
        
        // Scrape html for error
        if let match = htmlString?.range(of: "(?<=<h1>Error: )[^</h1>]+", options: .regularExpression) {
            
            return (htmlString?.substring(with: match))!
        } else {
            
            // Give a default error if none could be found
            return APIRequest.GENERAL_ERROR
        }
        
    }

}

