//
//  NewsfeedCollectionViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 11/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit



class FeedCollectionViewController: UICollectionViewController {

    // MARK: Constants
    private static let REUSE_IDENTIFIER = "PhotoCell"
    
    // MARK: Variables
    var queriedPhotos = [Photo]() // array for datasource of photos
    var feedType: FeedType?
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        refreshFeed()
    }
    
    // MARK: Methods
    func refreshFeed () {
        
        switch feedType?.rawValue {
        case FeedType.Newsfeed.rawValue?:
            
            // Get the newsfeed
            PhotoFunctions.sharedInstance.getNewsfeed(callback: {
                photos, error in
                if (error == nil) {
                    // There has been no error, update data
                    
                    // Empty all old data
                    self.queriedPhotos.removeAll()
                    // Add new photos
                    for photo in photos! {
                        self.queriedPhotos.append(photo)
                    }
                    // Reload data
                    DispatchQueue.main.async {
                        
                        
                        self.collectionView?.reloadData()
                    }
                } else {
                    
                    print(error?.errorType.rawValue)
                }
            })
            
        case FeedType.Profile.rawValue?:
            break
        default:
            break
        }
    }
    

    // MARK: Collection View Methods

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        
        return 1
    }


    override func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        
        return queriedPhotos.count
    }

    override func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: FeedCollectionViewController.REUSE_IDENTIFIER, for: indexPath) as! PhotoCollectionViewCell
        
        cell.setupUI(feedType: .Newsfeed, photo: queriedPhotos[indexPath.row])
    
        // Configure the cell
        
        return cell
    }
   
    
    
}

enum FeedType: Int {
    case Newsfeed = 0
    case Profile = 1
}
