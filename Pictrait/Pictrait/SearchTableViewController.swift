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
    
    // Intercept embed action
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        if let vc = segue.destination as? ProfileViewController,
            segue.identifier == "ShowProfile" {
            
            // Set the user object for the profile
            let cell = sender as! UITableViewCell
            vc.user = queriedUsers[(tableView.indexPath(for: cell)?.row)!]
        }
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
        
        cell.textLabel?.text = queriedUsers[indexPath.row].fullName
        cell.detailTextLabel?.text = queriedUsers[indexPath.row].username
        
        return cell
    }
    
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        self.performSegue(withIdentifier: "ShowProfile", sender: tableView.cellForRow(at: indexPath))
        tableView.deselectRow(at: indexPath, animated: false)
    }
}
