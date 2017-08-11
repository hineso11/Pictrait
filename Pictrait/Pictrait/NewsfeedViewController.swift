//
//  NewsfeedViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 11/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit

class NewsfeedViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        PhotoFunctions.sharedInstance.getNewsfeed(callback: {
            photos, error in
            
            
        })
    }

}
