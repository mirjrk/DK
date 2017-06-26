//
//  StatisticsViewController.swift
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//

import UIKit
import Material
import SwiftyJSON


/// StatisticsViewController - UIViewController with basic offers and match statistics. 

class StatisticsViewController:  BasicViewController, DetailOfferSectionHeaderDelegate {
    
    //MARK: Constants
    
    ///The object that contains all constants.
    let constants = MatchDetailsConstants()
    
    //MARK: UI outlet connections
    
    ///The statistics table view outlet.
    @IBOutlet weak var statisticsTableView: UITableView!
    
    //MARK: Variables
    
    ///The selected match.
    var match: Matches?
    
    ///The list of boolean values which indicates table section state - expanded or collapsed.
    var sectionsExpandedState: [Bool] = []
    
    //The header view of the table.
    var matchDetailTopView: MatchDetailTopView!
    
    ///The list of home team last matches.
    var homeLastMatches: [Matches]?
    
    ///The list of away team last matches.
    var awayLastMatches: [Matches]?
    
    ///The tteam statistics list which is used for generating league table.
    var teamStatisticArray: [TeamStatistics]?
    
    
    //MARK: UIViewController lifecycle methods
    
    open override func viewDidLoad() {
        super.viewDidLoad()
        
        //set theme color
        view.backgroundColor = ThemeManager.currentTheme().backgroundColor
        
        //generate section state array (by default all sections are colapsed)
        sectionsExpandedState = Array(repeating: false, count: constants.STATISTICS_TABLE_SECTION_COUNT)
        
        //setup statistics table view
        setupTableView()
        
        
        //hardcoded last matches arrays untils it is implemented on server side
        if let unwrappedMatch = match {
            homeLastMatches = [unwrappedMatch,unwrappedMatch,unwrappedMatch,unwrappedMatch,unwrappedMatch]
            awayLastMatches = []
        }
        
        
        //try to call web service for getting league table based on CommonLeagueId if exists otherwise base on LeagueId
        if let unwrappedLeagueId = match?.commonLeagueId {
            getLeagueTableFor(String(unwrappedLeagueId))
        }else if let unwrappedLeagueId = match?.leagueId {
            getLeagueTableFor(String(unwrappedLeagueId))
        }
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        //set selected color for the item title in PageTabBar
        self.pageTabBarItem.titleColor =  ThemeManager.currentTheme().pagerSelectedColor
        
        //assign match to MatchDetails view and trigger animation progress
        self.matchDetailTopView.match = match
        self.matchDetailTopView.drawProgressBars()
        
        
        self.statisticsTableView.reloadData()
        
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        //set unselected color for the item title in PageTabBar
        self.pageTabBarItem.titleColor =  ThemeManager.currentTheme().pagerUnselectedColor
        
    }
    
    //MARK: Init methods
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        //customize PageTabBar item
        preparePageTabBarItem()
    }
    
    init() {
        super.init(nibName: nil, bundle: nil)
        //customize PageTabBar item
        preparePageTabBarItem()
    }
    
    
    
    //MARK: UI action methods
    
    func contactUsPressed(_ sender: UIButton) {
        //push Contact Us view controller
        
        let contactVC = ContactViewController()
        navigationController?.pushViewController(contactVC, animated: true)
        
    }
    
    //MARK: SectionHeaderDelegate methods
    
    func didSelectSection(index:Int, bgView: UIView){
        //check if legaue table cannot be fetched due to internet connection problem
        if !sectionsExpandedState[index] && index ==  MatchDetailsConstants.StatisticsTableRowIndex.leagueTable.rawValue && self.teamStatisticArray == nil {
            self.statisticsTableView.reloadSections([index], with: .none)
            Commons.showToast(text: LanguageManager.sharedInstance.translationFor(.PLEASE_CHECK_YOUR_INTERNET_CONNECTION, defaultValue: "Please check your internet connection"))
            
            return
        }
        
        
        //change section state
        sectionsExpandedState[index] = !sectionsExpandedState[index]
        
        
        //update section statistics table view for given index
        
        bgView.backgroundColor = sectionsExpandedState[index] ? ThemeManager.currentTheme().radioButtonColor : ThemeManager.currentTheme().backgroundColor
        
        self.statisticsTableView.beginUpdates()
        
        if sectionsExpandedState[index] {
            self.statisticsTableView.insertRows(at: [IndexPath(row: 0, section: index)], with: .automatic)
        }else{
            self.statisticsTableView.deleteRows(at: [IndexPath(row: 0, section: index)], with: .automatic)
        }
        
        
        self.statisticsTableView.endUpdates()
    }
    
    //MARK: Local methods
    
    /**
     Customization of Match Details item in PageTabBar from 'Material' framework.
     */
    fileprivate func preparePageTabBarItem() {
        pageTabBarItem.title = LanguageManager.sharedInstance.translationFor(.STATISTICS, defaultValue: "Statistics")
        pageTabBarItem.titleColor =  ThemeManager.currentTheme().pagerUnselectedColor
        
    }
    
    
    /**
     Sets team form progress values in Match Details view.
     */
    func setDefaultData(){
        //using hardcoded values untils it's implemented on server side
        self.matchDetailTopView.setHomeTeamProgress(value: 94)
        self.matchDetailTopView.setAwayTeamProgress(value: 75)
    }
    
    
    /**
     Setup and customize offer table view.
     Create and assign header and footer table views.
     */
    func setupTableView () {
        
        //configure statistics table view
        self.statisticsTableView.allowsSelectionDuringEditing = false
        self.statisticsTableView.allowsMultipleSelectionDuringEditing = false
        self.statisticsTableView.delegate = self
        self.statisticsTableView.dataSource = self
        self.statisticsTableView.separatorStyle = .none
        self.statisticsTableView.backgroundColor = UIColor.clear
        
        //register views
        self.statisticsTableView.register(UINib(nibName: MatchDetailsConstants.Identifiers.detailOfferTableViewCell.rawValue, bundle: nil), forCellReuseIdentifier: MatchDetailsConstants.Identifiers.detailOfferTableViewCell.rawValue)
        
        self.statisticsTableView.register(UINib(nibName: MatchDetailsConstants.Identifiers.detailOfferSectionHeaderView.rawValue, bundle: nil), forHeaderFooterViewReuseIdentifier: MatchDetailsConstants.Identifiers.detailOfferSectionHeaderView.rawValue)
        
        self.statisticsTableView.register(UINib(nibName: MatchDetailsConstants.Identifiers.lastMatchesTableViewCell.rawValue, bundle: nil), forCellReuseIdentifier: MatchDetailsConstants.Identifiers.lastMatchesTableViewCell.rawValue)
        self.statisticsTableView.register(UINib(nibName: MatchDetailsConstants.Identifiers.headToHeadTableViewCell.rawValue, bundle: nil), forCellReuseIdentifier: MatchDetailsConstants.Identifiers.headToHeadTableViewCell.rawValue)
        self.statisticsTableView.register(UINib(nibName: MatchDetailsConstants.Identifiers.totalGoalsTableViewCell.rawValue, bundle: nil), forCellReuseIdentifier: MatchDetailsConstants.Identifiers.totalGoalsTableViewCell.rawValue)
        self.statisticsTableView.register(UINib(nibName: MatchDetailsConstants.Identifiers.leagueTableStatisticsTableViewCell.rawValue, bundle: nil), forCellReuseIdentifier: MatchDetailsConstants.Identifiers.leagueTableStatisticsTableViewCell.rawValue)
        
        //setup footer view
        let footerFrame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: constants.FOOTER_TABLE_VIEW_HEIGHT)
        let footerContainer = UIView(frame:footerFrame)
        
        let footerView = Bundle.main.loadNibNamed(MatchDetailsConstants.Identifiers.sportsBettingTableFooterView.rawValue, owner: nil, options: nil)?[0] as! SportsBettingTableFooterView
        footerView.frame = footerFrame
        
        //add contact us button target
        footerView.contactUsButton.addTarget(self, action: #selector(contactUsPressed), for: .touchUpInside)
        
        footerContainer.addSubview(footerView)
        
        self.statisticsTableView.tableFooterView = footerContainer
        
        
        //setup header view
        let headerFrame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.width * constants.HEADER_VIEW_ASPECT_RATION)
        self.matchDetailTopView = MatchDetailTopView(frame: headerFrame)
        
        
        self.statisticsTableView.tableHeaderView = matchDetailTopView
        
    }
    
    
    
    //MARK: Web API calls
    
    
    /**
     Calling web service for getting league table for given league id
     
     - parameter id: League unique identifier
     */
    func getLeagueTableFor(_ id: String) {
        
        
        Communication.shared.getLeagueTableFor(id, successBlock: { response in
            
            //parsing received data
            let json = JSON(response)
            let league = LeagueModel(json: json)
            
            //assign data
            if let teamStatisticArray = league.teamStatistics {
                self.teamStatisticArray = teamStatisticArray
            }else{
                self.teamStatisticArray = []
            }
            
        }, failureBlock: { error in
            //when error occurs show Toast view with error desription
            
            if let msg = error?.errorMessage{
                Commons.showToast(text: msg)
            }
            
        })
        
        
    }
    
}
