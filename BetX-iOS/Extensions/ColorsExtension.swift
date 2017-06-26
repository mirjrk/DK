//
//  ColorsExtension.swift
//  BetX
//
//  Created by Nemanja Ignjatovic on 3/31/17.
//  Copyright © 2017 Nemanja. All rights reserved.
//

import Foundation
import UIKit

/*
 UIColor extension for having customized UIColor objects
 */

extension UIColor {
    
    /// convenience init make short hand UIColor initialization
    convenience init(_ red: CGFloat, _ green: CGFloat, _ blue: CGFloat) {
        self.init(red: red / 255, green: green / 255, blue: blue / 255, alpha: 1)
    }
    
    public class var bitterLemon: UIColor {
        return UIColor(205, 219, 46)
    }
    
    public class var jumbo: UIColor {
        return UIColor(136, 136, 136)
    }
    
    public class var customWhite: UIColor {
        return UIColor(249, 249, 249)
    }
    
    public class var darkJungleGreen: UIColor {
        return UIColor(28, 28, 28)
    }
    
    public class var facebookBlue: UIColor {
        return UIColor(44, 72, 252)
    }
    
    public class var onyx: UIColor {
        return UIColor(15, 15, 15)
    }
    
    public class var mediumCandyAppleRed: UIColor {
        return UIColor(217, 0, 44)
    }
    
    public class var deepKoamaru: UIColor {
        return UIColor(25, 25, 120)
    }
    
    public class var fuelYellow: UIColor {
        return UIColor(241, 176, 48)
    }
    
    public class var kuCrimson: UIColor {
        return UIColor(245, 0, 12)
    }
    
    public class var leSalleGreen: UIColor {
        return UIColor(7, 129, 41)
    }
    
    public class var scarlett: UIColor {
        return UIColor(150, 8, 23)
    }
    
    public class var copperCanyon: UIColor {
        return UIColor(120, 60, 17)
    }
    
    public class var montana: UIColor {
        return UIColor(57, 57, 57)
    }
    
    public class var nero: UIColor {
        return UIColor(37, 37, 37)
    }
    
    public class var crimson: UIColor {
        return UIColor(207, 17, 43)
    }
    
    public class var mountainMist: UIColor {

        return UIColor(148, 148, 148)
    }
    
    public class var gallery: UIColor {

        return UIColor(239, 239, 239)
    }
    
    public class var customBlack: UIColor {
        
        return UIColor(35, 35, 37)
    }
    
    public class var lostRed: UIColor {
        
        return UIColor(233, 34, 36  )
    }
    
    public class var winGreen: UIColor {
        
        return UIColor(37, 165, 27)
    }
    
    public class var pendingYellow: UIColor {
        
        return UIColor(255, 194, 0)
    }
    
    public class var paidGreen: UIColor {
        
        return UIColor(23, 152, 100)
    }
    
    public class var silverChalice: UIColor {
        
        return UIColor(162, 162, 162)
    }
    
    public class var mineShaft: UIColor {
        
        return UIColor(37, 37, 37)
    }
    
    public class var alabaster: UIColor {
        
        return UIColor(250, 250, 250)
    }
    
    
    
}
