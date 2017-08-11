//
//  PhotoView.swift
//  Pictrait
//
//  Created by Oliver Hines on 11/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit
import SDWebImage

class PhotoView: UIView {

    // MARK: Variables
    private var photo: Photo?
    
    // MARK: Properties
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var likesLabel: UILabel!
    @IBOutlet weak var likeButton: UIButton!
    
    // MARK: Actions
    @IBAction func likeButtonClicked(_ sender: Any) {
        
        
    }

    // MARK: Methods
    
    // Method for setting the photo object so it can be loaded
    func setPhoto (photo: Photo) {
        
        self.photo = photo
        
        // Change all the UI based on photo object
        
        // Change username label
        usernameLabel.text = String(photo.userId)
        
        // Change likes label
        switch photo.likes {
        case 0:
            likesLabel.text = "No Likes"
        case 1:
            likesLabel.text = "1 Like"
        default:
            likesLabel.text = String(photo.likes).appending(" Likes")
        }
        
        // Change like button if user has liked already
        if (photo.userHasLiked) {
            
            likeButton.setTitle("Liked", for: .normal)
            likeButton.isEnabled = false
        }
    
        imageView.sd_setShowActivityIndicatorView(true)
        imageView.sd_setIndicatorStyle(.gray)
        imageView.sd_setImage(with: photo.downloadUrl, completed: nil)
    }
}
