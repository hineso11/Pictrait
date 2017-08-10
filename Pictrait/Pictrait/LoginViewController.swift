//
//  LoginViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 05/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit
import SwiftSpinner

class LoginViewController: UIViewController, UITextFieldDelegate {

    // MARK: Properties
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    
    // MARK: Actions
    @IBAction func loginButtonPressed(_ sender: Any) {
        
        passwordField.becomeFirstResponder()
        passwordField.resignFirstResponder()
        attemptLogin()
    }
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Setup views in controller
        usernameField.delegate = self
        passwordField.delegate = self
    }
    
    // MARK: Methods
    private func attemptLogin () {
        
        SwiftSpinner.show("Please wait...", animated: false)
        Auth.sharedInstance.login(username: usernameField.text!, password: passwordField.text!, callback: {
            success, error in
            
            SwiftSpinner.hide()
            
            if (success) {
                // If the login attempt has been successful
                // Show the main tab controller
                let tabController = self.storyboard?.instantiateViewController(withIdentifier: Constants.StoryboardId.MAIN_TAB_CONTROLLER.rawValue)
                self.present(tabController!, animated: true, completion: nil)
            } else {
                // If the login attempt has not been successful
                // Show the error
                error?.showError(vc: self)
            }
        })
    }
    
    // MARK: Text Field Methods
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        switch textField {
        case usernameField:
            passwordField.becomeFirstResponder()
        case passwordField:
            passwordField.resignFirstResponder()
            attemptLogin()
        default:
            break
        }
        return true
    }
    
}
