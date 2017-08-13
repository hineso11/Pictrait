//
//  UploadViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 09/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit
import SwiftSpinner

class UploadViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    // MARK: Constants
    let imagePicker = UIImagePickerController()
    
    // MARK: Variables
    var selectedImage: UIImage?
    
    // MARK: Properties
    @IBOutlet weak var photoButton: UIButton!
    @IBOutlet weak var uploadButton: UIButton!
    
    // MARK: Actions
    @IBAction func photoButtonClicked(_ sender: Any) {
        
        present(imagePicker, animated: true, completion: nil)
    }
    
    @IBAction func uploadButtonClicked(_ sender: Any) {
        
        SwiftSpinner.show("Please wait...", animated: false)
        
        // Attempt to upload the photo to API
        PhotoFunctions.sharedInstance.uploadPhoto(photo: selectedImage!, callback: {
            success, error in
            
            SwiftSpinner.hide()
            
            if (error == nil) {
                // The request was successful, reset UI elements for future uploads
                
                DispatchQueue.main.async {
                    
                    self.photoButton.setBackgroundImage(#imageLiteral(resourceName: "DefaultImage"), for: .normal)
                    self.photoButton.setTitle("Click to choose photo", for: .normal)
                    self.uploadButton.isHidden = true
                }
                
                // Show an alert to inform upload has been successful
                let alert = UIAlertController(title: "Upload Successful", message: "", preferredStyle: .alert)
                alert.addAction(UIAlertAction(title: "Ok", style: .default, handler: nil))
                self.present(alert, animated: true, completion: nil)
                
                
            } else {
                // the request was not successful, display error
                error?.showError(vc: self)
            }
        })
    }
    
    // MARK: View Controller Functions
    override func viewDidLoad() {
        super.viewDidLoad()

        imagePicker.delegate = self
        imagePicker.allowsEditing = true
        
    }
    
    // MARK: Image Picker Functions
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : AnyObject]) {
        if let pickedImage = info[UIImagePickerControllerEditedImage] as? UIImage {
            // set the image preview
            photoButton.setBackgroundImage(pickedImage, for: .normal)
            // remove the old title
            photoButton.setTitle("", for: .normal)
            // retain image
            selectedImage = pickedImage
            // show the upload button
            uploadButton.isHidden = false
        }
        
        dismiss(animated: true, completion: nil)
    }

}
