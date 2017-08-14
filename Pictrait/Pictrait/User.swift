//
//  User.swift
//  Pictrait
//
//  Created by Oliver Hines on 09/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation

class User {
    
    // MARK: Constants
    static let USERNAME_PARAM = "username"
    static let USER_ID_PARAM = "userId"
    static let FULL_NAME_PARAM = "fullName"
    static let FOLLOWERS_PARAM = "followers"
    static let FOLLOWING_PARAM = "following"
    static let IS_FOLLOWING_PARAM = "isFollowing"
    
    // MARK: Variables
    var username: String
    var userId: Int
    var fullName: String
    var followers: Int
    var following: Int
    var isFollowing: Bool
    
    // MARK: Constructors 
    init(username: String, userId: Int, fullName: String, followers: Int, following: Int, isFollowing: Bool) {
        
        self.username = username
        self.userId = userId
        self.fullName = fullName
        self.followers = followers
        self.following = following
        self.isFollowing = isFollowing
    }
    
    // MARK: Methods
    func toJson () -> [String: Any] {
        
        return [User.USERNAME_PARAM: username, User.USER_ID_PARAM: userId, User.FULL_NAME_PARAM: fullName,
                User.FOLLOWERS_PARAM: followers, User.FOLLOWING_PARAM: following, User.IS_FOLLOWING_PARAM: isFollowing]
    }
}
