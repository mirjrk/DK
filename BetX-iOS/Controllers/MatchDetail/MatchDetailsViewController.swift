//
//  MatchDetailsViewController
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//

import UIKit
import Material

/// MatchDetailsViewController - UIViewController that inheritence PageTabBarController from 'Material' third party framework (http://www.cosmicmind.com/material).
/// It's container controller that contains two children UIViewControllers: DetailOfferViewController and StatisticsViewController

class MatchDetailsViewController: PageTabBarController {
    
    
    //MARK: Variables
    
    /// The selected match.
    var match: Matches? {
        didSet{
            if let matchId = match?.id {
                //call web service for getting all macth offers
                getMatchOffers(matchId)
            }
        }
    }
    
    
    //MARK: UIViewController lifecycle methods
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        //set theme color
        view.backgroundColor = ThemeManager.currentTheme().backgroundColor
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        //set localized view controller title
        title = LanguageManager.sharedInstance.translationFor(.UNKNOWN, defaultValue: "Match details")
        
        //try to find parent reveal controller and disable gesture recognizers
        if let revealVC = navigationController?.viewControllers[0].revealViewController() {
            revealVC.panGestureRecognizer().isEnabled = false
            
            revealVC.tapGestureRecognizer().isEnabled = false
        }
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        //delete title
        title = ""
    }
    
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        // find DetailOfferViewController if it's initialized and set progress values
        // IMPORTANT: beacuse setting progress value have to have animation when user selects DetailsOffer screen, we are calling setDefaultProgressData() in viewDidAppear
        if viewControllers[0] is DetailOfferViewController{
            if let detailOfferViewController = viewControllers[0] as? DetailOfferViewController{
                detailOfferViewController.setDefaultProgressData()
            }
        }
        
    }
    
    //MARK: PageTabBarController methods
    
    open override func prepare() {
        super.prepare()
        
        delegate = self
        
        //customize PageTabBar
        preparePageTabBar()
    }
    
    //MARK: Local methods
    
    /**
     Customization of PageTabBar from 'Material' framework.
     */
    fileprivate func preparePageTabBar() {
        pageTabBarAlignment = .top
        pageTabBar.lineAlignment = .bottom
        pageTabBar.isLineAnimated = true
        pageTabBar.zPosition = 0
        
        pageTabBar.backgroundColor = ThemeManager.currentTheme().backgroundColor
        pageTabBar.dividerColor = .crimson
        pageTabBar.lineColor = .crimson
        
    }
    
    
    //MARK: Web API calls
    
    
    /**
     Calling web service for getting all match offers for given macth id
     
     - parameter matchId: Match unique identifier
     */
    func getMatchOffers(_ matchId: Int) {
        
        
        showActicityIndicator(true)
        
        let parameters: [String:AnyObject] = ["MatchId": matchId as AnyObject]
        
        
        Communication.shared.getMatchOffers(parameters, successBlock: { (response) in
            if let result = response as? [String: AnyObject] {
                //parsing received data
                if let receivedMatch = Matches(dictionary: result as NSDictionary) {
                    
                    //creating groups of offers (when we have offers with same type they should be grouped in one section)
                    let groupedOffers = OfferGroupManager.groupOffers(offerList: receivedMatch.offers!)
                    
                    
                    //assign data to children view controllers
                    if self.viewControllers[0] is DetailOfferViewController{
                        if let detailOfferViewController = self.viewControllers[0] as? DetailOfferViewController{
                            detailOfferViewController.match = receivedMatch
                            detailOfferViewController.groupedOffers = groupedOffers
                        }
                    }
                    
                    if self.viewControllers[1] is StatisticsViewController{
                        if let statisticsViewController = self.viewControllers[1] as? StatisticsViewController{
                            statisticsViewController.match = receivedMatch
                        }
                    }
                    
                    
                }
                
            }
            
            
            
            self.showActicityIndicator(false)
            
            
        }, failureBlock: {
            error in
            //when error occurs show Toast view with error desription and hide activity indicator
            
            if let msg = error?.errorMessage{
                Commons.showToast(text: msg)
            }
            
            self.showActicityIndicator(false)
        })
    }
    
}


// MARK: PageTabBarControllerDelegate

extension MatchDetailsViewController: PageTabBarControllerDelegate {
    @objc(pageTabBarControllerWithPageTabBarController:didTransitionTo:) func pageTabBarController(pageTabBarController: PageTabBarController, didTransitionTo viewController: UIViewController) {
        
        // find StatisticsViewController if it's initialized and set progress values
        // IMPORTANT: beacuse setting progress value have to have animation when user selects Statistics screen, we are calling setDefaultProgressData() in viewDidAppear
        
        if viewController is StatisticsViewController{
            if let statisticsViewController = viewControllers[1] as? StatisticsViewController{
                statisticsViewController.setDefaultData()
            }
        }
    }
}

