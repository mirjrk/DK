//
//  DetailOfferViewController
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//

import UIKit
import Material



/// DetailsOfferViewController - UIViewController with basic match statistics and list of all match offers

class DetailOfferViewController: BasicViewController, DetailOfferSectionHeaderDelegate {
    
    //MARK: Constants
    
    ///The object that contains all constants.
    let constants = MatchDetailsConstants()
    
    //MARK: UI outlet connections
    
    ///The offer table view outlet.
    @IBOutlet weak var offerTableView: UITableView!
    
    //MARK: Variables
    
    ///The selected match.
    var match: Matches?
    
    ///The list of boolean values which indicates table section state - expanded or collapsed.
    var sectionsExpandedState: [Bool] = []
    
    //The header view of the table.
    var matchDetailTopView: MatchDetailTopView!
    
    ///The list of grouped offers.
    var groupedOffers: [[Offers]]? {
        didSet {
            
            guard let unwrappedGroupedOffers = groupedOffers else { return }
            
            //generate section state array depending on numbers of sections (grouped offers)
            if unwrappedGroupedOffers.count > constants.DEFAULT_EXPANDED_ROW_COUNT {
                self.sectionsExpandedState = Array(repeating: true, count: constants.DEFAULT_EXPANDED_ROW_COUNT) + Array(repeating: false, count: unwrappedGroupedOffers.count - constants.DEFAULT_EXPANDED_ROW_COUNT)
                
            }else{
                self.sectionsExpandedState = Array(repeating: true, count: (groupedOffers!.count))
            }
            
            self.offerTableView.reloadData()
        }
        
    }
    
    //MARK: UIViewController lifecycle methods
    
    open override func viewDidLoad() {
        super.viewDidLoad()
        
        //set theme color
        view.backgroundColor = ThemeManager.currentTheme().backgroundColor
        
        //setup offer table view
        setupTableView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        //set selected color for the item title in PageTabBar
        pageTabBarItem.titleColor =  ThemeManager.currentTheme().pagerSelectedColor
        
        offerTableView.reloadData()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        //assign match to MatchDetails view and trigger animation progress
        self.matchDetailTopView.match = match
        self.matchDetailTopView.drawProgressBars()
        
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        //set unselected color for the item title in PageTabBar
        pageTabBarItem.titleColor =  ThemeManager.currentTheme().pagerUnselectedColor
        
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
    
    
    
    
    
    //MARK: SectionHeaderDelegate methods
    
    func didSelectSection(index:Int, bgView: UIView){
        
        //change section state
        sectionsExpandedState[index] = !sectionsExpandedState[index]
        
        //update section offer table view for given index
        self.offerTableView.beginUpdates()
        
        if sectionsExpandedState[index] {
            self.offerTableView.insertRows(at: [IndexPath(row: 0, section: index)], with: .automatic)
        }else{
            self.offerTableView.deleteRows(at: [IndexPath(row: 0, section: index)], with: .automatic)
        }
        
        
        self.offerTableView.endUpdates()
    }
    
    
    
    //MARK: UI action methods
    
    func contactUsPressed(_ sender: UIButton) {
        //push Contact Us view controller
        let contactVC = ContactViewController()
        navigationController?.pushViewController(contactVC, animated: true)
    }
    
    //MARK: Local methods
    
    /**
     Customization of Match Details item in PageTabBar from 'Material' framework.
     */
    fileprivate func preparePageTabBarItem() {
        pageTabBarItem.title = LanguageManager.sharedInstance.translationFor(.UNKNOWN, defaultValue: "Detail offer")
        pageTabBarItem.titleColor =  ThemeManager.currentTheme().pagerUnselectedColor
    }
    
    
    /**
     Sets team form progress values in Match Details view.
     */
    func setDefaultProgressData(){
        //using hardcoded values untils it's implemented on server side
        self.matchDetailTopView.setHomeTeamProgress(value: 94)
        self.matchDetailTopView.setAwayTeamProgress(value: 75)
    }
    
    
    /**
     Setup and customize offer table view.
     Create and assign header and footer table views.
     */
    func setupTableView () {
        
        //configure offer table view
        self.offerTableView.allowsSelectionDuringEditing = false
        self.offerTableView.allowsMultipleSelectionDuringEditing = false
        self.offerTableView.delegate = self
        self.offerTableView.dataSource = self
        self.offerTableView.separatorStyle = .none
        self.offerTableView.backgroundColor = .clear
        
        
        //register views
        self.offerTableView.register(UINib(nibName: MatchDetailsConstants.Identifiers.detailOfferTableViewCell.rawValue, bundle: nil), forCellReuseIdentifier: MatchDetailsConstants.Identifiers.detailOfferTableViewCell.rawValue)
        
        self.offerTableView.register(UINib(nibName: MatchDetailsConstants.Identifiers.detailOfferSectionHeaderView.rawValue, bundle: nil), forHeaderFooterViewReuseIdentifier: MatchDetailsConstants.Identifiers.detailOfferSectionHeaderView.rawValue)
        
        
        //setup footer view
        let footerFrame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: constants.FOOTER_TABLE_VIEW_HEIGHT)
        let footerContainer = UIView(frame:footerFrame)
        
        let footerView = Bundle.main.loadNibNamed(MatchDetailsConstants.Identifiers.sportsBettingTableFooterView.rawValue, owner: nil, options: nil)?[0] as! SportsBettingTableFooterView
        footerView.frame = footerFrame
        
        //add contact us button target
        footerView.contactUsButton.addTarget(self, action: #selector(contactUsPressed), for: .touchUpInside)
        
        footerContainer.addSubview(footerView)
        
        self.offerTableView.tableFooterView = footerContainer
        
        
        //setup header view
        let headerFrame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.width * constants.HEADER_VIEW_ASPECT_RATION)
        self.matchDetailTopView = MatchDetailTopView(frame: headerFrame)
        
        self.offerTableView.tableHeaderView = matchDetailTopView
        
    }
    
}
