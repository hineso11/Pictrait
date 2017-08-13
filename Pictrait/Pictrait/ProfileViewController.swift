//
//  ProfileViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 12/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit

class ProfileViewController: UIViewController {

    // MARK: Variables
    var user: User?
    
    // MARK: Properties
    @IBOutlet weak var followersLabel: UILabel!
    @IBOutlet weak var followingLabel: UILabel!
    @IBOutlet weak var followButton: UIBarButtonItem!
    
    // MARK: Actions
    @IBAction func followButtonClicked(_ sender: Any) {
        
        // Check to see if the user is already following this person
        if (user?.isFollowing)! {
            // They are following user currently, unfollow them
            ProfileFunctions.sharedInstance.unfollowUser(user: user!, callback: {
                success, error in
                
            })
        } else {
            // They aren't following user currently, follow them
            ProfileFunctions.sharedInstance.followUser(user: user!, callback: {
                success, error in
                
            })
        }
        
        // Update the UI to deal with change
        updateUI()
    }
    
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Set up the UI according to the user object
        updateUI()
    }
    
    func updateUI () {
        
        navigationItem.title = user?.fullName
        navigationItem.prompt = "@".appending((user?.username)!)
        
        followersLabel.text = "\(user!.followers) Followers"
        followingLabel.text = "Following \(user!.following)"
        
        if (user?.isFollowing)! {
            
            followButton.title = "Following"
        } else {
            
            followButton.title = "Follow"
        }
    }
    
    // Intercept embed action
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if let vc = segue.destination as? FeedCollectionViewController,
            segue.identifier == "Embed" {
            
            // Set the feed type for the child vc
            vc.feedType = FeedType.Profile
            vc.profileUser = user
        }
    }

}
