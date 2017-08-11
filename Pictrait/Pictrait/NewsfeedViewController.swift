//
//  NewsfeedViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 11/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit

class NewsfeedViewController: UIViewController {

    // MARK: Properties
    @IBOutlet weak var containerView: UIView!
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()

    }

    // Intercept embed action
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if let vc = segue.destination as? FeedCollectionViewController,
            segue.identifier == "Embed" {
            
            // Set the feed type for the child vc
            vc.feedType = FeedType.Newsfeed
        }
    }
}
