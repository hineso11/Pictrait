//
//  ProfileFunctions.swift
//  Pictrait
//
//  Created by Oliver Hines on 09/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation

class ProfileFunctions {
    
    // MARK: Constants
    static let sharedInstance = ProfileFunctions()
    
    // Request paths
    private static let SEARCH_PATH = "/profile/search"
    private static let FOLLOW_PATH = "/profile/follow"
    private static let UNFOLLOW_PATH = "/profile/unfollow"
    
    // Request Params
    private static let SEARCH_PARAM = "search_string"
    private static let SUBJECT_ID = "subject_id"
    
    // Response Params
    private static let USERS_ARRAY = "users"
    private static let USERNAME = "username"
    private static let USER_ID = "userId"
    private static let FULL_NAME = "fullName"
    private static let FOLLOWERS = "followers"
    private static let FOLLOWING = "following"
    private static let IS_FOLLOWING = "isFollowing"
    
    // MARK: Methods
    
    func followUser (user: User, callback: @escaping (Bool, AppError?) -> Void) {
        
        user.followers = user.followers + 1
        user.isFollowing = true
        
        let params = [ProfileFunctions.SUBJECT_ID: user.userId as Any]
        let request = APIRequest(parameters: params, urlEnding: ProfileFunctions.FOLLOW_PATH, shouldRefresh: true, method: .POST, callback: {
            response, error in
            
            if (error == nil) {
                // There has been no error with request, continue
                callback(true, nil)
            } else {
                
                callback(false, error)
            }
        })
        request.doPost()
    }
    
    func unfollowUser (user: User, callback: @escaping (Bool, AppError?) -> Void) {
        
        user.followers = user.followers - 1
        user.isFollowing = false
        
        let params = [ProfileFunctions.SUBJECT_ID: user.userId as Any]
        let request = APIRequest(parameters: params, urlEnding: ProfileFunctions.UNFOLLOW_PATH, shouldRefresh: true, method: .POST, callback: {
            response, error in
            
            if (error == nil) {
                // There has been no error with request, continue
                callback(true, nil)
            } else {
                
                print(error?.errorType.rawValue)
                callback(false, error)
            }
        })
        request.doPost()
    }
    
    func searchUsers (searchString: String, callback: @escaping ([User]?, AppError?) -> Void) {
        
        let params = [ProfileFunctions.SEARCH_PARAM: searchString as AnyObject]
        let request = APIRequest(parameters: params, urlEnding: ProfileFunctions.SEARCH_PATH, shouldRefresh: true, method: .GET, callback: {
            response, error in
            
            if (error == nil) {
                // There has been no error with the request, continue
                
                let usersArray = response?[ProfileFunctions.USERS_ARRAY] as! [Any]
                var userObjArray = [User]()
                for user in usersArray {
                    
                    let userDictionary = user as! [String: Any]
                    let username = userDictionary[ProfileFunctions.USERNAME] as! String
                    let userId = userDictionary[ProfileFunctions.USER_ID] as! Int
                    let fullName = userDictionary[ProfileFunctions.FULL_NAME] as! String
                    let followers = userDictionary[ProfileFunctions.FOLLOWERS] as! Int
                    let following = userDictionary[ProfileFunctions.FOLLOWING] as! Int
                    let isFollowing = userDictionary[ProfileFunctions.IS_FOLLOWING] as! Bool
                    
                    let userObject = User(username: username, userId: userId, fullName: fullName, followers: followers, following: following, isFollowing: isFollowing)
                    userObjArray.append(userObject)
                }
                
                
                callback(userObjArray, nil)
            } else {
                // There was an error completing the request
                // No possible user error, so just return empty results
                callback(nil, error)
            }
        })
        request.doGet()
    }
}
