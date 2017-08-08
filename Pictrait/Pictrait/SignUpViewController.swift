//
//  SignUpViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 08/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit
import SwiftSpinner

class SignUpViewController: UIViewController, UITextFieldDelegate {

    // MARK: Properties
    @IBOutlet weak var usernameField: UITextField!
    @IBOutlet weak var passwordField: UITextField!
    @IBOutlet weak var emailField: UITextField!
    @IBOutlet weak var fullNameField: UITextField!
    
    // MARK: Actions
    @IBAction func signUpButtonPressed(_ sender: Any) {
        
        // Close text fields
        fullNameField.becomeFirstResponder()
        fullNameField.resignFirstResponder()
        attemptSignUp()
    }
    
    // MARK: View Controller Methods
    override func viewDidLoad() {
        super.viewDidLoad()

        // Setup views in controller
        usernameField.delegate = self
        passwordField.delegate = self
        emailField.delegate = self
        fullNameField.delegate = self
    }
    
    // MARK: Methods
    private func attemptSignUp () {
        
        SwiftSpinner.show("Please wait...", animated: false)
        Auth.sharedInstance.signUp(username: usernameField.text!, password: passwordField.text!,
                                   email: emailField.text!, fullName: fullNameField.text!, callback: {
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
                error?.showError(error: error, vc: self)
            }
        })

    }
    
    // MARK: Text Field Methods
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        switch textField {
        case usernameField:
            passwordField.becomeFirstResponder()
        case passwordField:
            emailField.becomeFirstResponder()
        case emailField:
            fullNameField.becomeFirstResponder()
        case fullNameField:
            fullNameField.resignFirstResponder()
            attemptSignUp()
        default:
            break
        }
        
        return true
    }

}
