//
//  ViewController.swift
//  Pictrait
//
//  Created by Oliver Hines on 02/08/2017.
//  Copyright © 2017 Pictrait. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        let funcs = UserFunctions()
        funcs.login()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

