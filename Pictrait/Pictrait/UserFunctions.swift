//
//  UserFunctions.swift
//  Pictrait
//
//  Created by Oliver Hines on 02/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import Foundation

class UserFunctions {
    
    func login () {
        
        var request = APIRequest()
        request.makePostReqeust(urlEnding: "user/login", params: ["client_id":"clientID" as AnyObject, "username":"oliver.hines" as AnyObject, "password":"password" as AnyObject], callback: {array, error in
            
            
        })
    }
}

