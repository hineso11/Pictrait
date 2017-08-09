//
//  SettingsTableViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 08/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit

class SettingsTableViewController: UITableViewController {

    // MARK: Constants
    private static let LOGOUT_PATH = IndexPath(row: 0, section: 0)
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }

    // MARK: Table View Methods
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        switch indexPath {
        case SettingsTableViewController.LOGOUT_PATH:
            
            Auth.sharedInstance.logout()
        default:
            break
        }
    }
}
