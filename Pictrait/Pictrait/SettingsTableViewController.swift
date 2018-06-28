//
//  SettingsTableViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 08/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit

class SettingsTableViewController: UITableViewController, UITextFieldDelegate {

    // MARK: Constants
    private static let LOGOUT_PATH = IndexPath(row: 0, section: 1)
    
    // MARK: Variables
    var user: User?
    var fieldsUpdated = false
    
    // MARK: Properties
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var fullNameField: UITextField!
    
    // MARK: Actions
    @IBAction func logoutButtonClicked(_ sender: Any) {
        
        Auth.sharedInstance.logout()
    }
    
    @IBAction func updateProfile(_ sender: Any) {
        
        if (user != nil && fieldsUpdated) {
            
            var username: String?
            var fullName: String?
            
            // If username has been changed, set it for update
            if (usernameField.text != user?.username) {
                
                username = usernameField.text
            }
            // If full name has been changed, set it for update
            if (fullNameField.text != user?.fullName) {
                
                fullName = fullNameField.text
            }
            
            Auth.sharedInstance.updateProfile(username: username, fullName: fullName, callback: {
                user, error in
                
                if (error == nil) {
                    // No error updating user profile
                    self.user = user // update current user object
                    self.fieldsUpdated = false // set to false to prepare for next update
                    
                    DispatchQueue.main.async {
                        // update the necessary elements of the UI
                        self.updateUI()
                        
                        // Show a message to indicate successful update
                        let alert = UIAlertController(title: "Success", message: "Your profile was updated", preferredStyle: UIAlertControllerStyle.alert)
                        alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
                        self.present(alert, animated: true, completion: nil)
                    }
                } else {
                    
                    DispatchQueue.main.async {
                        
                        error?.showError(vc: self)
                    }
                }
                
            })
        } else {
            
            // Show an error message
            let alert = UIAlertController(title: "Error", message: "There's nothing to update", preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "Ok", style: UIAlertActionStyle.default, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
    }
    
    // Detects when the text field has been updated
    @IBAction func textFieldEdited(_ sender: Any) {
        
        fieldsUpdated = true
    }
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Set UI elements up
        usernameField.delegate = self
        fullNameField.delegate = self
        
        // Get the current user
        getCurrentUser()
    }
    
    // MARK: Methods
    func getCurrentUser () {
        
        ProfileFunctions.sharedInstance.getUser(username: Auth.sharedInstance.getUsername(), callback: {
            user, error in
            
            if (error == nil) {
                // No error, update user information
                self.user = user
                DispatchQueue.main.async {
                    
                    self.updateUI()
                }
            } else {
                
                error?.showError(vc: self)
            }
        })
    }
    
    func updateUI () {
        
        usernameField.text = user?.username
        fullNameField.text = user?.fullName
    }
    // MARK: Text Field Methods
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        textField.resignFirstResponder()
        
        return true
    }
}
