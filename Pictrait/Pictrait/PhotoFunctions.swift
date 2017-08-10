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
    
    // Request Params
    private static let PHOTO_PARAM = "photo"
    
    private static let IMAGE_TYPE = "image/jpeg" // content type for upload
    
    // Response Params

    
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
}
