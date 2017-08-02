//
//  Error.swift
//  Pictrait
//
//  Created by Oliver Hines on 02/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation

class AppError {
    
    // MARK: Variables
    private var statusCode: Int
    var errorType: ErrorType
    
    
    // MARK: Constructors
    init(statusCode: Int, errorReason: String) {
        
        // Assign vars from constructor
        self.statusCode = statusCode
        // Determine error from the reason given by request
        self.errorType = AppError.determineErrorType(errorReason: errorReason)
    }
    
    // MARK: Functions
    
    // Function used to determine error type from the given string of error
    private static func determineErrorType (errorReason: String) -> ErrorType {
        
    
        for error in iterateEnum(ErrorType) {
            
            if (errorReason == error.rawValue) {
                
                return error
            }
        }
        
        // If the error wasnt found in the list, then just return a general one
        return .GENERAL_ERROR
    }
    
    // Function to iterate through enum of errors
    private static func iterateEnum<T: Hashable>(_: T.Type) -> AnyIterator<T> {
        var i = 0
        return AnyIterator {
            let next = withUnsafePointer(to: &i) {
                $0.withMemoryRebound(to: T.self, capacity: 1) { $0.pointee }
            }
            if next.hashValue != i { return nil }
            i += 1
            return next
        }
    }
}

// Enums for all errors generated by the API
enum ErrorType: String {
    
    
    // List of all possible constants
    case GENERAL_ERROR = "Error, The request could not be completed"
    case CLIENT_ID_INCORRECT = "Client ID was not recognised"
    case USER_DOESNT_EXIST = "This user doesn't exist"
    case NULL_FIELDS = "Null fields were found"
    case USERNAME_LONG = "Username is too long"
    case USERNAME_SHORT = "Username is too short"
    case PASSWORD_LONG = "Password is too long"
    case PASSWORD_SHORT = "Password is too short"
    case EMAIL_LONG = "Email is too long"
    case NAME_LONG = "Name is too long"
    case EMAIL_FORMAT = "Email format is invalid"
    case USERNAME_FORMAT = "Username format is invalid"
    case EMAIL_IN_USE = "Email is already in use"
    case USERNAME_IN_USE = "Username is already in use"
    case TOKEN_INVALID = "This token is invalid"
    case TOKEN_EXPIRED = "This token has expired"
    case WRONG_TOKEN_TYPE = "Token is wrong type"
    case INCORRECT_COMBINATION = "Incorrect username and password combination"
    case NO_AUTH_TOKEN = "An auth token must be supplied"
    case FILE_NOT_JPEG = "The image provided must be a JPEG"
    case IMAGE_NOT_SQUARE = "The image must be a square"
    case IMAGE_NOT_UPLOADED = "The image could not be uploaded"
    case IMAGE_TOO_BIG = "The size of the image provided is too large"
    case PHOTO_DOESNT_EXIST = "The photo does not exist"
    case USER_NOT_FOUND = "The user requested does not exist"
    case ALREADY_LIKED = "The photo has already been liked by the user"
    case ALREADY_FOLLOWING = "The user is already following this user"
    case CANNOT_FOLLOW_SELF = "User cannot follow themselves"
    case NO_FOLLOWING_EXISTS = "User is not following this person"
    case NOT_FOLLOWING_ANYONE = "User does not follow anyone"
    
}

