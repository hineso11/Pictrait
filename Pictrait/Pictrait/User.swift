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
    private var username: String
    private var userId: Int
    private var fullName: String
    private var followers: Int
    private var following: Int
    
    // MARK: Constructors 
    init(username: String, userId: Int, fullName: String, followers: Int, following: Int) {
        
        self.username = username
        self.userId = userId
        self.fullName = fullName
        self.followers = followers
        self.following = following
    }
    
    // MARK: Getters and Setters
    
    func getUsername () -> String {
        
        return username
    }
    func getFullName () -> String {
        
        return fullName
    }
}
