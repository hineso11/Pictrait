//
//  MyProfileViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 14/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit

class MyProfileViewController: UIViewController {
    
    // MARK: Variables
    var user: User?
    
    // MARK: Properties
    @IBOutlet weak var followersLabel: UILabel!
    @IBOutlet weak var followingLabel: UILabel!
    @IBOutlet weak var containerView: UIView!
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        refreshProfile()
    }
    
    func refreshProfile () {
        
        // Get the profile of the current logged in user
        ProfileFunctions.sharedInstance.getUser(username: Auth.sharedInstance.getUsername(), callback: {
            user, error in
            
            // If the request was successful
            if (error == nil) {
                
                self.user = user
                // Set up the UI according to the user object
                DispatchQueue.main.async {
                    
                    self.updateUI()
                    
                    // Manually embed photos vc
                    
                    let vc = self.storyboard?.instantiateViewController(withIdentifier: Constants.StoryboardId.FEED_CONTROLLER.rawValue) as? FeedCollectionViewController
                    
                    
                    // Set the feed type for the child vc
                    vc?.feedType = FeedType.Profile
                    vc?.profileUser = user
                    
                    self.addChildViewController(vc!)
                    vc?.view.frame = CGRect(x: 0, y: 0, width: self.containerView.frame.size.width, height: self.containerView.frame.size.height)
                    self.containerView.addSubview((vc?.view)!)
                    vc?.didMove(toParentViewController: self)
                    
                }
                
            } else {
                
                DispatchQueue.main.async {
                    
                    error?.showError(vc: self)
                }
            }
            
        })

    }
    
    func updateUI () {
        
        navigationItem.title = user?.fullName
        navigationItem.prompt = "@".appending((user?.username)!)
        
        followersLabel.text = "\(user!.followers) Followers"
        followingLabel.text = "Following \(user!.following)"
        
    }

}
