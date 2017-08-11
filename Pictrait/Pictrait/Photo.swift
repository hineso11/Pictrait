//
//  Photo.swift
//  Pictrait
//
//  Created by Oliver Hines on 11/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation

class Photo {
    
    // MARK: Variables
    var createdAt: Date
    var downloadUrl: URL
    var photoId: Int
    var userId: Int
    var likes: Int
    var userHasLiked: Bool
    
    // MARK: Constructors
    init(createdAt: Date, downloadUrl: URL, photoId: Int, userId: Int, likes: Int, userHasLiked: Bool) {
        
        self.createdAt = createdAt
        self.downloadUrl = downloadUrl
        self.photoId = photoId
        self.userId = userId
        self.likes = likes
        self.userHasLiked = userHasLiked
    }
}
