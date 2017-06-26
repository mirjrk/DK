//
//  ThemeHelper.swift
//  BetX
//
//  Created by Zesium Mobile Ltd. on 9/1/16.
//  Copyright © 2016 Zesium Mobile Ltd. All rights reserved.
//



import UIKit
import Material



/// ThemeManager is responsible for UI elements appearance (textColor, backgroundColor, image...). It uses coresponding Theme enum value to set appearance on specific UI element and also saves choosen theme to user defaults.


public struct ThemeManager {
    
    static func currentTheme() -> Theme {
        
        if let storedTheme = BetXSettings.shared.themeType{
            return Theme(rawValue: storedTheme)!
        } else {
            return .light
        }
    }
    
    static func applyTheme(_ theme: Theme) {
        
        BetXSettings.shared.themeType = theme.rawValue
        
    }
}


/// Theme enum contains two cases: light and dark. Theme purpose is to set specific behavior of properties for choosen theme.

enum Theme: Int {
    case light, dark
    
    var backgroundColor: UIColor {
        switch self {
        case .light:
            return UIColor.white
        case .dark:
            return .onyx
        }
    }
    
    var activePlaceholderColor: UIColor {
        switch self {
        case .light:
            return .customBlack
        case .dark:
            return .customWhite
        }
    }
    
    
    var titleLabelColor: UIColor {
        switch self {
        case .light:
            return .customWhite
        case .dark:
            return .darkJungleGreen
        }
    }
    
    
    var textColor: UIColor {
        switch self {
        case .light:
            return UIColor.black
        case .dark:
            return UIColor.white
        }
    }
    
    var keyboardAppearance: UIKeyboardAppearance {
        switch self {
        case .light:
            return .light
        case .dark:
            return .dark
        }
    }
    
    
    var activityIndicatorStyle: UIActivityIndicatorViewStyle {
        switch self {
        case .light:
            return .gray
        case .dark:
            return .white
        }
    }
    
    var facebookColor: UIColor {
        switch self {
        case .light:
            return .facebookBlue
        case .dark:
            return UIColor.white
        }
    }
    
    var collectionLabelColor: UIColor {
        switch self {
        case .light:
            return .customWhite
        case .dark:
            return .montana
        }
    }
    
    
    var radioButtonColor: UIColor {
        switch self {
        case .light:
            return .gallery
        case .dark:
            return .nero
        }
    }
    
    var pagerSelectedColor: UIColor {
        switch self {
        case .light:
            return Color.blueGrey.darken4
        case .dark:
            return UIColor.white
        }
    }
    
    var pagerUnselectedColor: UIColor {
        switch self {
        case .light:
            return Color.blueGrey.base
        case .dark:
            return UIColor.gray
        }
    }
    
    
    
    var betxLogoImage: UIImage? {
        switch self {
        case .light:
            return UIImage(named: "img_logo_light")
        case .dark:
            return UIImage(named: "img_logo")
        }
    }
    
    var splashScreenBackgroundImage: UIImage? {
        switch self {
        case .light:
            return UIImage(named: "screen1_splash_light")
        case .dark:
            return UIImage(named: "screen1_splash_dark")
        }
    }
    
    
    var statusBarStyle: UIStatusBarStyle {
        switch self {
        case .light:
            return .default
        case .dark:
            return .lightContent
        }
    }
    
    
    var ticketLabels: UIColor? {
        switch self {
        case .light:
            return .black
        case .dark:
            return .white
        }
    }
    
    
    var ticketImageBackground: UIColor? {
        switch self {
        case .light:
            return .alabaster
        case .dark:
            return .mineShaft
        }
    }
    
    
}


