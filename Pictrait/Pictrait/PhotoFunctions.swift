//
//  PhotoFunctions.swift
//  Pictrait
//
//  Created by Oliver Hines on 09/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation
import UIKit
import SwiftHTTP

class PhotoFunctions {
    
    // MARK: Constants
    static let sharedInstance = PhotoFunctions()
    
    // Request paths
    private static let UPLOAD_PATH = "/photo/upload"
    private static let NEWSFEED_PATH = "/photo/feed"
    
    // Request Params
    private static let PHOTO_PARAM = "photo"
    private static let FEED_TYPE_PARAM = "feed_type"
    
    private static let NEWSFEED_TYPE = "newsfeed"
    
    private static let IMAGE_TYPE = "image/jpeg" // content type for upload
    
    // Response Params
    private static let PHOTOS_ARRAY = "photos"
    private static let CREATED_AT = "createdAt"
    private static let DOWNLOAD_URL = "download_url"
    private static let PHOTO_ID = "photoId"
    private static let USERNAME = "username"
    private static let LIKES = "likes"
    private static let USER_HAS_LIKED = "userHasLiked"
    
    // MARK: Methods
    func uploadPhoto (photo: UIImage, callback: @escaping (Bool, AppError?) -> Void) {
        
        let upload = Upload(data: UIImageJPEGRepresentation(photo, 1)!, fileName: PhotoFunctions.PHOTO_PARAM, mimeType: PhotoFunctions.IMAGE_TYPE)
        let params = [PhotoFunctions.PHOTO_PARAM: upload]
        let request = APIRequest(parameters: params, urlEnding: PhotoFunctions.UPLOAD_PATH, shouldRefresh: true, method: .POST, callback: {
            response, error in
            
            if (error == nil) {
                
                callback(true, nil)
            } else {
                
                callback(false, error)
            }
        })
        request.doPost()
    }
    
    func getNewsfeed (callback: @escaping ([Photo]?, AppError?) -> Void) {
        
        let params = [PhotoFunctions.FEED_TYPE_PARAM: PhotoFunctions.NEWSFEED_TYPE]
        let request = APIRequest(parameters: params, urlEnding: PhotoFunctions.NEWSFEED_PATH, shouldRefresh: true, method: .GET, callback: {
            response, error in
            
            if (error == nil) {
                // There has been no error, parse response to photo objects
                
                let photosArray = response?[PhotoFunctions.PHOTOS_ARRAY] as! [Any]
                var photoObjArray = [Photo]()
                for photo in photosArray {
                    
                    let photoDictionary = photo as! [String: Any]
                    // parse timestamp to date type
                    let formatter = DateFormatter()
                    formatter.dateFormat = "E MMM d HH:mm:ss zzz yyyy"
                    let createdAt = formatter.date(from: photoDictionary[PhotoFunctions.CREATED_AT] as! String)
                    let downloadUrl = URL(string: photoDictionary[PhotoFunctions.DOWNLOAD_URL] as! String)
                    let photoId = photoDictionary[PhotoFunctions.PHOTO_ID] as! Int
                    let username = photoDictionary[PhotoFunctions.USERNAME] as! String
                    let likes = photoDictionary[PhotoFunctions.LIKES] as! Int
                    let userHasLiked = photoDictionary[PhotoFunctions.USER_HAS_LIKED] as! Bool
                    
                    let photoObject = Photo(createdAt: createdAt!, downloadUrl: downloadUrl!, photoId: photoId, username: username, likes: likes, userHasLiked: userHasLiked)
                    photoObjArray.append(photoObject)
                }

                callback(photoObjArray, nil)
            } else {
                // There was an error, display it to the user
                callback(nil, error)
            }
        })
        request.doGet()
    }
}
