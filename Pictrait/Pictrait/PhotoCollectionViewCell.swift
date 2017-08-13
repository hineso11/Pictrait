//
//  PhotoCollectionViewCell.swift
//  Pictrait
//
//  Created by Oliver Hines on 11/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit
import SDWebImage

class PhotoCollectionViewCell: UICollectionViewCell {
    
    // MARK: Variables
    private var feedType: FeedType?
    private var photo: Photo?
    
    // MARK: Properties
    @IBOutlet weak var usernameLabel: UILabel!
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var likesLabel: UILabel!
    @IBOutlet weak var likeButton: UIButton!
    
    // MARK: Actions
    @IBAction func likeButtonClicked(_ sender: Any) {
        
        PhotoFunctions.sharedInstance.likePhoto(photo: photo!, callback: {
            success, error in
            
        })
        // Set the UI up for the update
        setupUI(feedType: feedType!, photo: photo!)
    }
    
    // MARK: Methods
    
    func setupUI (feedType: FeedType, photo: Photo) {
        
        self.feedType = feedType
        self.photo = photo
        
        // Set username label
        if (feedType == .Newsfeed) {
            
            usernameLabel.text = String(photo.username)
        } else {
            
            usernameLabel.isHidden = true
        }
        
        // Set the likes label up
        switch photo.likes {
        case 0:
            likesLabel.text = "No Likes"
        case 1:
            likesLabel.text = "1 Like"
        default:
            likesLabel.text = String(photo.likes).appending(" Likes")
        }
        
        // Alter the like button if necessary
        if (photo.userHasLiked) {
            likeButton.isEnabled = false
            likeButton.setTitle("Liked", for: .normal)
        } else {
            
            likeButton.isEnabled = true
            likeButton.setTitle("Like", for: .normal)
        }
        
        // Start loading the image
        imageView.sd_setShowActivityIndicatorView(true)
        imageView.sd_setImage(with: photo.downloadUrl, placeholderImage: #imageLiteral(resourceName: "DefaultImage"))
        
    }
}
