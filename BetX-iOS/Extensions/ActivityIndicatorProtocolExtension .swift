//
//  ActivityIndicatorProtocolExtension .swift
//  BetX
//
//  Created by Zesium Mobile Ltd. on 6/23/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//

import Foundation
import NVActivityIndicatorView

/// ActivityIndicatorDelegate is protocol with showActivityIndicator method signature

protocol ActivityIndicatorDelegate {
    
    // showActivityIndicator - if onViewController is nil this func will find front vc of swreveal and attach activity indicator on it.
    
    func showActivityIndicator(_ show: Bool, onViewController: UIViewController?)
}

/// ActivityIndicatorDelegate protocol extension

extension ActivityIndicatorDelegate {
    
    /**
     Method purpose is to show or hide activity indicator.
     
     - parameter show: flag to determine to show or hide activity indicator.
     
     - parameter onViewController: optional UIViewController which by default is nil. If specified than activity indicator will be shown or hidden on that view controller, othervise activity indicator will be presented on currently presented view controller.
     
     */
    
    func showActivityIndicator(_ show: Bool, onViewController: UIViewController? = nil) {
        
        if let topController = UIApplication.topViewController() {
            
            if let swRevealViewController = topController as? SWRevealViewController {
                
                if let frontVC = swRevealViewController.frontViewController {
                    let indicatorView = NVActivityIndicatorView(frame: CGRect(x: topController.view.bounds.midX - 20, y: topController.view.bounds.midY - 20, width: 40, height: 40))
                    
                    indicatorView.type = .ballClipRotate
                    
                    indicatorView.color = .mediumCandyAppleRed
                    
                    indicatorView.tag = 40
                    
                    if let vc = onViewController {
                        if show {
                            vc.view.addSubview(indicatorView)
                            indicatorView.startAnimating()
                        } else {
                            indicatorView.stopAnimating()
                            vc.view.viewWithTag(40)?.removeFromSuperview()
                        }
                    } else {
                        if show {
                            frontVC.view.addSubview(indicatorView)
                            indicatorView.startAnimating()
                        } else {
                            indicatorView.stopAnimating()
                            frontVC.view.viewWithTag(40)?.removeFromSuperview()
                        }
                    }
                    
                    
                }
                
            }
            
        }
    }
    
    
    
}
