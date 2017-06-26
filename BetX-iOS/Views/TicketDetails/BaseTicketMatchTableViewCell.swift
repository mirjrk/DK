//
//  BaseTicketMatchTableViewCell.swift
//  BetX
//
//  Created by Zesium Mobile Ltd. on 5/4/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//

import UIKit

/// UITableViewCell that contains match information for all kind of sports. Since sports: basketball, hockey, tennis and soccer have different custom layout, cell is modified accordingly.


class BaseTicketMatchTableViewCell: UITableViewCell {
    
    //MARK: Constants
    
    /// The table footer view height constarint constant.
    let kMatchFooterHeightConstarint:CGFloat = 100
    /// The table default leading marging.
    let kDefaultLeadingMarging:CGFloat = 10
    /// The league name leading constraint constant.
    let kLeagueNameLeadingConstraint:CGFloat = 45
    
    //MARK: @IBOutlets
    
    @IBOutlet weak var offerNameLabel: UILabel!
    @IBOutlet weak var offerTypeOddLabel: UILabel!
    @IBOutlet weak var leagueNameLabel: UILabel!
    @IBOutlet weak var dateAndTimeLabel: UILabel!
    @IBOutlet weak var cellBackgroundView: UIView!
    @IBOutlet weak var matchHeaderView: UIView!
    @IBOutlet weak var matchFooterView: UIView!
    @IBOutlet weak var matchDetailsLabel: UILabel!
    @IBOutlet weak var teamHomeFooterLabel: UILabel!
    @IBOutlet weak var teamAwayFooterLabel: UILabel!
    @IBOutlet weak var triangleImageView: UIImageView!
    @IBOutlet weak var groupLabel: UILabel!
    @IBOutlet weak var matchTitleCustomView: MatchTitleCustomView!
    @IBOutlet weak var matchFooterViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var groupLabelTopConstraint: NSLayoutConstraint!
    @IBOutlet weak var groupLabelBottomConstraint: NSLayoutConstraint!
    @IBOutlet weak var leagueNameLeadingConstraint: NSLayoutConstraint!
    @IBOutlet weak var separatorView: UIView!
    @IBOutlet weak var liveBettingLabel: UILabel!
    
    //MARK: @IBOutlet collections
    
    @IBOutlet var matchPlaceholderStatusLabelCollection: [UILabel]!
    @IBOutlet var homeTeamStatusLabelCollection: [UILabel]!
    @IBOutlet var awayteamStatusLabelCollection: [UILabel]!
    
    /// Bet optional property with property observer.
    var bet: Bets? {
        didSet {
            setupViews()
            layoutSubviews()
            layoutIfNeeded()
        }
        
    }
    
    /// Ticket optional property.
    var ticket: Ticket?
    
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        customizeViews()
    }
    
    /**
     
     Customize view properties.
     
     */
    
    func customizeViews() {
        matchDetailsLabel.text = LanguageManager.sharedInstance.translationFor(.FINISHED, defaultValue: "Match has finished")
        
        triangleImageView.tintColor = ThemeManager.currentTheme().titleLabelColor
        
        _ = homeTeamStatusLabelCollection.map { $0.textColor = .white }
        _ = awayteamStatusLabelCollection.map { $0.textColor = .white }
        
        selectionStyle = .none
        
        separatorView.backgroundColor = ThemeManager.currentTheme().radioButtonColor
        
        matchDetailsLabel.textColor = .white
        teamHomeFooterLabel.textColor = .white
        teamAwayFooterLabel.textColor = .white
        
        groupLabel.backgroundColor = .mediumCandyAppleRed
        groupLabel.layer.cornerRadius = 3
        groupLabel.clipsToBounds = true
        groupLabel.textColor = .white
        
        liveBettingLabel.backgroundColor = .mediumCandyAppleRed
        liveBettingLabel.layer.cornerRadius = 3
        liveBettingLabel.clipsToBounds = true
        liveBettingLabel.textColor = .white
        
        
        matchHeaderView.backgroundColor = ThemeManager.currentTheme().titleLabelColor
        cellBackgroundView.backgroundColor = ThemeManager.currentTheme().backgroundColor
        matchTitleCustomView.backgroundColor = ThemeManager.currentTheme().titleLabelColor
        
        offerNameLabel.textColor = .darkGray
        
        offerTypeOddLabel.textColor = ThemeManager.currentTheme().textColor
        dateAndTimeLabel.textColor = .darkGray
        leagueNameLabel.textColor = .darkGray
        
    }
    
    /**
     
     Setup views constraint, properties and give them required behaviors.
     
     */
    
    func setupViews() {
        
        if let homeTeam = bet?.homeTeam {
            matchTitleCustomView!.teamHomeTitleLabel.text = homeTeam + " "
        }else{
            matchTitleCustomView!.teamHomeTitleLabel.text = ""
        }
        
        matchTitleCustomView!.removeConstraint(matchTitleCustomView!.alignCenterScoreLabelConstraint!)
        if let awayTeam = bet?.awayTeam {
            matchTitleCustomView.addConstraint(matchTitleCustomView!.alignCenterScoreLabelConstraint!)
            
            matchTitleCustomView!.teamAwayTitleLabel.text = " " + awayTeam
        }else{
            matchTitleCustomView!.teamAwayTitleLabel.text = ""
        }
        
        
        if let sportName = bet?.sportOrigName, let leagueTranslatedDescription = bet?.leagueTranslatedDescription {
            
            leagueNameLabel.text = "\(sportName), \(leagueTranslatedDescription)"
            
        }
        
        
        if let eventTime = bet?.eventTime {
            
            dateAndTimeLabel.text = LogicHelper.getFinalStringFromDate(date: LogicHelper.getDateFromString(stringDate: eventTime, expectedFormat: "yyyy-MM-dd'T'HH:mm:ss'Z'"), inFormat: "dd.MM.yyyy. HH:mm")
            
            
        }
        
        
        if let unwrappedTicket = ticket {
            if let group = bet?.group, !group.isEmpty, let ticketTypeOrigName = unwrappedTicket.ticketTypeOrigName, ticketTypeOrigName.isEqual("system") {
                groupLabel(show: true)
                groupLabel.text = group
            } else {
                groupLabel(show: false)
            }
        } else {
            groupLabel(show: false)
        }
        
        
        
        
        if let gameOrigName = bet?.gameOrigName, gameOrigName.isEqual("livebetting") {
            liveBettingLabel.isHidden = false
            leagueNameLeadingConstraint.constant = kLeagueNameLeadingConstraint
            liveBettingLabel.text = "LIVE"
        } else {
            liveBettingLabel.isHidden = true
            leagueNameLeadingConstraint.constant = kDefaultLeadingMarging
        }
        
        
        
        offerNameLabel.text = bet?.betTypeTranslatedName
        
        if let score = bet?.score, !score.isEmpty {
            matchTitleCustomView!.scoreTitleLabel.text = score
        } else {
            matchTitleCustomView!.scoreTitleLabel.text = "vs"
        }
        
        if let odd = bet?.odd {
            if let pick = bet?.pick {
                offerTypeOddLabel.text = "\(pick)  \(odd)"
            }else{
                offerTypeOddLabel.text = "\(odd)"
            }
        }
        
        
        teamHomeFooterLabel.text = bet?.homeTeam
        
        if let unwrappedAwayTeam = bet?.awayTeam {
            teamAwayFooterLabel.text = unwrappedAwayTeam
            
        } else {
            matchTitleCustomView!.scoreTitleLabel.text = bet?.score
        }
        
        
        
        if bet?.sportType == .soccer {
            matchFooterViewHeightConstraint.constant = kMatchFooterHeightConstarint
            
            matchFooterView.isHidden = false
            
            separatorView.isHidden = true
            
            matchFooterView.backgroundColor = .leSalleGreen
            
            setupSoccerFooterResultLabel(matchPlaceholderStatusLabelCollection)
            
            
        } else if bet?.sportType == .basketball {
            matchFooterViewHeightConstraint.constant = kMatchFooterHeightConstarint
            
            matchFooterView.isHidden = false
            
            separatorView.isHidden = true
            
            matchFooterView.backgroundColor = .scarlett
            
            setupBasketballFooterResultLabel(matchPlaceholderStatusLabelCollection)
            
            
        } else if bet?.sportType == .tennis {
            matchFooterViewHeightConstraint.constant = kMatchFooterHeightConstarint
            
            matchFooterView.isHidden = false
            
            separatorView.isHidden = true
            
            matchFooterView.backgroundColor = .copperCanyon
            
            setupTennisFooterResultLabel(matchPlaceholderStatusLabelCollection)
            
            
        } else if bet?.sportType == .hockey {
            
            matchFooterView.isHidden = false
            
            separatorView.isHidden = true
            
            matchFooterView.backgroundColor = .deepKoamaru
            
            setupHockeyFooterResultLabel(matchPlaceholderStatusLabelCollection)
            
        } else {
            matchFooterViewHeightConstraint.constant = 0
            separatorView.isHidden = false
            matchFooterView.isHidden = true
            
        }
        
        
        
    }
    
    /**
     
     Setup label collection constraints and manage wheter to show them.
     
     - parameter show: flag that determines showing groupLabel
     
     */
    
    func groupLabel(show: Bool){
        if show {
            let marginValue:CGFloat = 10
            groupLabel.isHidden = false
            groupLabelTopConstraint.constant = marginValue
            groupLabelBottomConstraint.constant = marginValue
        }else{
            let marginValue:CGFloat = -5
            groupLabel.isHidden = true
            groupLabelTopConstraint.constant = marginValue
            groupLabelBottomConstraint.constant = marginValue
        }
    }
    
    
    /**
     
     Add ic_corner image asset to attributed string text attachment and return attributed string.
     
     - returns: attributed string with image attachment
     
     */
    
    func cornerIconAttributedString()-> NSMutableAttributedString {
        let attributedString = NSMutableAttributedString(string: "")
        let attributedStringTextAttachment = NSTextAttachment()
        attributedStringTextAttachment.image = UIImage(named: "ic_corner")
        let attrStringWithImage = NSAttributedString(attachment: attributedStringTextAttachment)
        attributedString.append(attrStringWithImage)
        
        return attributedString
    }
    
    /**
     
     Modify cell footer view for sport: soccer. Every sport has different properties and behaviors.
     
     - parameter labelCollection: [UILabel]
     
     */
    
    func setupSoccerFooterResultLabel(_ labelCollection: [UILabel]) {
        
        // map through an Array of UILabels and set labels property for label with specific tag
        
        _ = labelCollection.map {
            
            $0.textColor = .white
            
            switch $0.tag {
            case 0:
                $0.text = ""
                $0.backgroundColor = .fuelYellow
            case 1:
                $0.text = ""
                $0.backgroundColor = .kuCrimson
            case 2:
                $0.text = ""
                $0.attributedText = cornerIconAttributedString()
            case 3:
                $0.text = "45'"
            case 4:
                $0.text = "90'"
            default: break
            }
            
            
        }
        
        
    }
    
    /**
     
     Modify cell footer view for sport: tennis. Every sport has different properties and behaviors.
     
     - parameter labelCollection: [UILabel]
     
     */
    
    func setupTennisFooterResultLabel(_ labelCollection: [UILabel]) {
        
        _ = labelCollection.map {
            
            // map through an Array of UILabels and set labels property for label with specific tag
            
            $0.textColor = .white
            
            switch $0.tag {
            case 0:
                $0.text = "1"
                $0.backgroundColor = .clear
            case 1:
                $0.text = "2"
                $0.backgroundColor = .clear
            case 2:
                $0.text = "3"
            case 3:
                $0.text = "4"
            case 4:
                $0.text = "5"
            default: break
            }
            
        }
    }
    
    
    /**
     
     Modify cell footer view for sport: basketball. Every sport has different properties and behaviors.
     
     - parameter labelCollection: [UILabel]
     
     */
    
    func setupBasketballFooterResultLabel(_ labelCollection: [UILabel]) {
        
        // map through an Array of UILabels and set labels property for label with specific tag
        
        _ = labelCollection.map {
            
            $0.textColor = .white
            
            
            switch $0.tag {
            case 0:
                $0.text = "1"
                $0.backgroundColor = .clear
            case 1:
                $0.text = "2"
                $0.backgroundColor = .clear
            case 2:
                $0.text = "3"
            case 3:
                $0.text = "4"
            case 4:
                $0.isHidden = true
                homeTeamStatusLabelCollection.last?.isHidden = true
                awayteamStatusLabelCollection.last?.isHidden = true
            default: break
            }
            
        }
    }
    
    
    /**
     
     Modify cell footer view for sport: hockey. Every sport has different properties and behaviors.
     
     - parameter labelCollection: [UILabel]
     
     */
    
    
    func setupHockeyFooterResultLabel(_ labelCollection: [UILabel]) {
        
        // map through an Array of UILabels and set labels property for label with specific tag
        
        _ = labelCollection.map {
            
            $0.textColor = .white
            
            switch $0.tag {
            case 0:
                $0.text = "1"
                $0.backgroundColor = .clear
            case 1:
                $0.text = "2"
                $0.backgroundColor = .clear
            case 2:
                $0.text = "3"
            case 3:
                $0.isHidden = true
                homeTeamStatusLabelCollection[3].isHidden = true
                awayteamStatusLabelCollection[3].isHidden = true
            case 4:
                $0.isHidden = true
                homeTeamStatusLabelCollection[4].isHidden = true
                awayteamStatusLabelCollection[4].isHidden = true
            default: break
            }
            
        }
    }
    
}
