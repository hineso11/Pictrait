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
    
    let refreshControl = UIRefreshControl()
    
    // MARK: Variables
    var queriedPhotos = [Photo]() // array for datasource of photos
    var feedType: FeedType?
    var profileUser: User?
    var isRefreshing = false
    
    // MARK: Outlets
    @IBOutlet var noResultsView: UIView!
    var defaultBackground: UIView?
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Set the table to show the no results as default
        defaultBackground = collectionView?.backgroundView
        collectionView?.backgroundView = noResultsView
        
        // Add the refresh control
        self.collectionView?.alwaysBounceVertical = true
        refreshControl.addTarget(self, action: #selector(FeedCollectionViewController.refreshFeed), for: .valueChanged)
        collectionView?.addSubview(refreshControl)
        
        // Set size for different device sizes
        if let layout = collectionView?.collectionViewLayout as? UICollectionViewFlowLayout {
            layout.itemSize = CGSize(width: view.frame.width, height: view.frame.height * 0.7)
            layout.invalidateLayout()
        }
        // Refresh the feed to start with
        refreshFeed()
    }
    
    // MARK: Methods
    func refreshFeed () {
        
        // Only allow refresh of feed, if no operation is underway
        if (!isRefreshing) {
            
            isRefreshing = true
            
            switch feedType?.rawValue {
            case FeedType.Newsfeed.rawValue?:
                
                // Get the newsfeed
                PhotoFunctions.sharedInstance.getNewsfeed(callback: requestCallback(photos:error:))
                
            case FeedType.Profile.rawValue?:
                PhotoFunctions.sharedInstance.getProfile(user: profileUser!, callback: requestCallback(photos:error:))
            default:
                break
            }

        }
        
    }
    
    func requestCallback (photos: [Photo]?, error: AppError?) {
        
        DispatchQueue.main.async {
            self.refreshControl.endRefreshing()
            self.isRefreshing = false
        }
        
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
                
                // Update the background view if necessary
                if (self.queriedPhotos.count > 0) {
                    self.collectionView?.backgroundView = self.defaultBackground
                } else {
                    self.collectionView?.backgroundView = self.noResultsView
                    
                }
            }
        }

    }

    // MARK: Collection View Methods

    override func numberOfSections(in collectionView: UICollectionView) -> Int {
        
        if (queriedPhotos.count > 0) {
            return 1
        } else {
            return 0
        }
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
