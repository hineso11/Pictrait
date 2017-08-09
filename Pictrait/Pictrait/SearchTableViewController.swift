//
//  SearchTableViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 08/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit

class SearchTableViewController: UITableViewController, UISearchBarDelegate {

    // MARK: Variables
    private var isSearching = false
    private var queriedUsers = [User]()
    
    // MARK: Properties
    var searchBar: UISearchBar!
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Set up the searchbar 
        searchBar = UISearchBar()
        searchBar.sizeToFit()
        searchBar.delegate = self
        navigationItem.titleView = searchBar
        
        self.tableView.register(UITableViewCell.self, forCellReuseIdentifier: "SearchCell")
    }
    
    // MARK: Search Bar Methods
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        
        // If no request is currently underway, then start one
        if (!isSearching) {
            searchBar.resignFirstResponder()
            isSearching = true
            self.queriedUsers.removeAll()
            self.tableView.reloadData()
            ProfileFunctions.sharedInstance.searchUsers(searchString: searchBar.text!, callback: {
                users, error in
                
                DispatchQueue.main.async {
                    
                    // change this var to enable other searches to take place
                    self.isSearching = false
                    
                    if (error == nil) {
                        
                        self.queriedUsers = users!
                        self.tableView.reloadData()
                    } 

                }
            })
        }
        
    }

    // MARK: Table View Methods
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return queriedUsers.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        
        let cell = UITableViewCell(style: .subtitle, reuseIdentifier: "SearchCell")
        
        cell.textLabel?.text = queriedUsers[indexPath.row].getFullName()
        cell.detailTextLabel?.text = queriedUsers[indexPath.row].getUsername()
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        self.tableView.deselectRow(at: indexPath, animated: true)
    }
}
