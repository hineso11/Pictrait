//
//  User.swift
//  Pictrait
//
//  Created by Oliver Hines on 09/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation

class User {
    
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
    
}
