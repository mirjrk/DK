//
//  RadioButton.swift
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//

import UIKit


/// RadioButton - Custom button with two labels. RadioButton for showing odds.
class RadioButton: UIButton {
    
    //MARK: Constants
    
    ///Label font size
    let LABEL_FONT_SIZE: CGFloat = 15
    
    
    //MARK: Variables

    ///Additional tag
    var buttonCustomTag = 0

    //MARK: UI outlet connections
    
    @IBOutlet weak var leftLabel: GTMFadeTruncatingLabel!
    @IBOutlet weak var rightLabel: UILabel!
    
    /// Custom view from the XIB file
    var view: UIView!
    
    //MARK: Init methods

    override init(frame: CGRect) {
        // call super.init(frame:)
        super.init(frame: frame)
        
        // Setup view from .xib file
        xibSetup()
    }
    
    required init?(coder aDecoder: NSCoder) {
        // call super.init(coder:)
        super.init(coder: aDecoder)
        
        //Setup view from .xib file
        xibSetup()
    }
    
    
    //MARK: Local methods
    
    
    /**
        Load view from Nib and setup view and subviews.
        View customization.
     */
    func xibSetup() {
        view = loadViewFromNib()
        
        // use bounds not frame or it'll be offset
        view.frame = bounds
        
        // Make the view stretch with containing view
        view.autoresizingMask = [UIViewAutoresizing.flexibleWidth, UIViewAutoresizing.flexibleHeight]
        // Adding custom subview on top of our view
        addSubview(view)
        
        
        //View customization
        leftLabel.backgroundColor = UIColor.clear
        leftLabel.textColor = .jumbo
        leftLabel.font = UIFont.systemFont(ofSize: LABEL_FONT_SIZE)
        leftLabel.lineBreakMode = .byClipping
        leftLabel.truncateMode = GTMFadeTruncatingTail
        
        rightLabel.backgroundColor = UIColor.clear
        rightLabel.font = UIFont.systemFont(ofSize: LABEL_FONT_SIZE)
        rightLabel.textColor = .jumbo
    }
    
    
    /**
        Load view from Nib.
     
        - Returns: Loaded view
     */
    func loadViewFromNib() -> UIView {
        
        let bundle = Bundle(for: type(of: self))
        let nib = UINib(nibName: "RadioButton", bundle: bundle)
        let view = nib.instantiate(withOwner: self, options: nil)[0] as! UIView
        

        
        return view
    }
    
    /**
      Setup labels when RadioButton change selected state
     */
    func toggleButon() {
        
        backgroundColor = isSelected ? .bitterLemon : ThemeManager.currentTheme().radioButtonColor
        
        leftLabel.textColor = isSelected ? .customBlack : .jumbo
        leftLabel.font = isSelected ? UIFont.boldSystemFont(ofSize: LABEL_FONT_SIZE) : UIFont.systemFont(ofSize: LABEL_FONT_SIZE)
        
        rightLabel.textColor = isSelected ? .customBlack : .jumbo
        rightLabel.font = isSelected ? UIFont.boldSystemFont(ofSize: LABEL_FONT_SIZE) : UIFont.systemFont(ofSize: LABEL_FONT_SIZE)
        
    }
    
    //MARK: UI actions
    
    override var isSelected: Bool {
        
        didSet {
            toggleButon()
        }
    }
    
   
    
    
    
    
}
