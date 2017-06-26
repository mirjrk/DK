//
//  TicketDetailsFooterView.swift
//  BetX
//
//  Created by Zesium Mobile Ltd. on 5/5/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//

import UIKit

/// protocol TicketDetailsFooterViewDelegate contains cancelButtonTapped() method signature

protocol TicketDetailsFooterViewDelegate {
    /// Ability to cancel ticket.
    func cancelButtonTapped()
}

/*
 
 TicketDetailsFooterView is a table footer view. It contains ticket information as total odds, posible win, ticke status.
 
 */

class TicketDetailsFooterView: UIView {
    
    //MARK: Constants
    
    /// specific ticket cancelation time
    let ticketCancelationTime = "00:00:00"
    
    // TicketType enum contain two cases: system and basic
    enum TicketType: String {
        case system = "system"
        case basic = "basic"
    }
    
    //MARK: @IBOutlets
    
    @IBOutlet weak var backgroundView: UIView!
    @IBOutlet weak var totalOddsPlaceholderLabel: UILabel!
    @IBOutlet weak var eventualWinPlaceholderLabel: UILabel!
    @IBOutlet weak var ticketStatusPlaceholderLabel: UILabel!
    @IBOutlet weak var ticketTypePlaceholderLabel: UILabel!
    @IBOutlet weak var totalOddsLabel: UILabel!
    @IBOutlet weak var eventualWinLabel: UILabel!
    @IBOutlet weak var ticketStatusLabel: UILabel!
    @IBOutlet weak var ticketTypeLabel: UILabel!
    @IBOutlet weak var stakePlaceholderLabel: UILabel!
    @IBOutlet weak var paidPlaceholderLabel: UILabel!
    @IBOutlet weak var stakeLabel: UILabel!
    @IBOutlet weak var paidLabel: UILabel!
    @IBOutlet weak var datePlaceholderLabel: UILabel!
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var timePlaceholderLabel: UILabel!
    @IBOutlet weak var timeLabel: UILabel!
    @IBOutlet weak var systemViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet weak var cancelAndLogoStackView: UIStackView!
    @IBOutlet weak var cancelButton: UIButton!
    @IBOutlet weak var betxLogoImageView: UIImageView!
    @IBOutlet weak var betxRightsLabel: UILabel!
    
    //MARK: @IBOutlet collections
    
    @IBOutlet var triangleImageViews: [UIImageView]!
    @IBOutlet var lineSeparatorViews: [UIView]!
    @IBOutlet var circleImageViews: [UIImageView]!
    
    /// TicketDetailsFooterViewDelegate
    var delegate: TicketDetailsFooterViewDelegate?
    
    /// Ticket optional property with property observer.
    var ticket: TicketDetailModel? {
        
        didSet {
            
            setupViews()
            
        }
        
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        customizeViews()
        
    }
    
    /**
     
     Setup view properties and give them required behaviors.
     
     */
    
    func setupViews() {
        
        guard let ticketDetail = ticket?.result?.ticket else { return }
        
        
        if ticketDetail.cancellationTime == ticketCancelationTime || ticketDetail.cancellationTime == nil {
            cancelButton.isHidden = true
        }
        else {
            cancelButton.isHidden = false
        }
        
        if let oddsTotal = ticketDetail.oddsTotal {
            totalOddsLabel.text = String(oddsTotal)
        }
        
        if let winTotalPossibleAmount = ticketDetail.winTotalPossibleAmount {
            eventualWinLabel.text = Commons.getPriceFormat(price: winTotalPossibleAmount)
            
        }
        
        
        if let betAmount = ticketDetail.betAmount {
            stakeLabel.text = Commons.getPriceFormat(price: betAmount)
            
        }
        
        
        if let payoutAmount = ticketDetail.winTotalPossibleAmount {
            paidLabel.text = Commons.getPriceFormat(price: Float(payoutAmount))
            
        }
        
        timeLabel.text = ticketDetail.time
        
        dateLabel.text = ticketDetail.date
        
        ticketStatusLabel.text = ticketDetail.statusTranslatedName
        
        if let ticketSystems = ticket?.result?.systems, let ticketTypeName = ticket?.result?.ticket?.ticketTypeOrigName, ticketTypeName == TicketType.system.rawValue {
            
            /// initial ticketTypeLabelSystemtext
            var ticketTypeLabelSystemtext = ""
            
            /// initial index value
            var index = 0
            
            // mapping through ticketSystems
            
            _ = ticketSystems.map {
                
                // chained if let statements
                
                if let unwrappedTicketTypeTranslatedName = ticketDetail.ticketTypeTranslatedName,
                    let unwrappedTypeFrom = $0.typeFrom,
                    let unwrappedTypeTo = $0.typeTo {
                    
                    ticketTypeLabelSystemtext.append("\(unwrappedTicketTypeTranslatedName) \(unwrappedTypeFrom)/\(unwrappedTypeTo)")
                }
                
                
                
                
                if index < ticketSystems.count - 1 {
                    ticketTypeLabelSystemtext.append("\n")
                }
                
                index += 1 }
            
            ticketTypeLabel.text = ticketTypeLabelSystemtext
            
            
        } else {
            ticketTypeLabel.text = ticketDetail.ticketTypeTranslatedName
            
        }
        
    }
    
    
    
    /**
     
     Customize view properties and behaviors.
     
     */
    
    func customizeViews() {
        _ = lineSeparatorViews.map { $0.backgroundColor = ThemeManager.currentTheme().radioButtonColor }
        
        totalOddsPlaceholderLabel.textColor = ThemeManager.currentTheme().textColor
        eventualWinPlaceholderLabel.textColor = ThemeManager.currentTheme().textColor
        ticketStatusPlaceholderLabel.textColor = ThemeManager.currentTheme().textColor
        ticketTypePlaceholderLabel.textColor = ThemeManager.currentTheme().textColor
        totalOddsLabel.textColor = ThemeManager.currentTheme().textColor
        eventualWinLabel.textColor = ThemeManager.currentTheme().textColor
        ticketStatusLabel.textColor = ThemeManager.currentTheme().textColor
        ticketTypeLabel.textColor = ThemeManager.currentTheme().textColor
        stakePlaceholderLabel.textColor = ThemeManager.currentTheme().textColor
        stakeLabel.textColor = ThemeManager.currentTheme().textColor
        paidPlaceholderLabel.textColor = ThemeManager.currentTheme().textColor
        paidLabel.textColor = ThemeManager.currentTheme().textColor
        datePlaceholderLabel.textColor = .gray
        dateLabel.textColor = ThemeManager.currentTheme().textColor
        timePlaceholderLabel.textColor = .gray
        timeLabel.textColor = ThemeManager.currentTheme().textColor
        betxRightsLabel.textColor = ThemeManager.currentTheme().textColor
        
        betxLogoImageView.image = ThemeManager.currentTheme().betxLogoImage
        
        backgroundColor = ThemeManager.currentTheme().backgroundColor
        
        backgroundView.backgroundColor = ThemeManager.currentTheme().titleLabelColor
        
        _ = triangleImageViews.map { $0.tintColor = ThemeManager.currentTheme().titleLabelColor }
        
        _ = circleImageViews.map { $0.tintColor = ThemeManager.currentTheme().backgroundColor }
        
        
        
        
        betxRightsLabel.text = LanguageManager.sharedInstance.translationFor(.ALL_RIGHT_RESERVED, defaultValue: "All rights reserved")
        totalOddsPlaceholderLabel.text = LanguageManager.sharedInstance.translationFor(.TOTAL_ODD, defaultValue: "Total odds")
        eventualWinPlaceholderLabel.text = LanguageManager.sharedInstance.translationFor(.LB_POSSIBLE_WIN, defaultValue: "Possible win")
        ticketStatusPlaceholderLabel.text = LanguageManager.sharedInstance.translationFor(.TICKET_STATUS, defaultValue: "Ticket status")
        ticketTypePlaceholderLabel.text = LanguageManager.sharedInstance.translationFor(.TICKET_TYPE, defaultValue: "Ticket type")
        
        stakePlaceholderLabel.text = LanguageManager.sharedInstance.translationFor(.STAKE, defaultValue: "Stake")
        paidPlaceholderLabel.text = LanguageManager.sharedInstance.translationFor(.PAID, defaultValue: "PAID")
        
        datePlaceholderLabel.text = LanguageManager.sharedInstance.translationFor(.DATE, defaultValue: "Date")
        timePlaceholderLabel.text = LanguageManager.sharedInstance.translationFor(.TIME, defaultValue: "Time")
        
        cancelButton.setTitleColor(.crimson, for: .normal)
        cancelButton.setTitle(LanguageManager.sharedInstance.translationFor(.CANCEL_TICKET, defaultValue: "Cancel ticket"), for: .normal)
        
        cancelButton.addTarget(self, action:#selector(cancelTapped), for: .touchUpInside)
    }
    
    /**
     
     Call cancelButtonTapped method on TicketDetailsFooterViewDelegate
     
     */
    
    func cancelTapped() {
        delegate?.cancelButtonTapped()
    }
    
    
}
