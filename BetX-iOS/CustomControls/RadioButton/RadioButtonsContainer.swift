//
//  RadioButtonsContainer.swift
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//


import Foundation
import UIKit

/// RadioButtonContainerDelegate. Delegate optionally implements didSelectButton that receives selected button.
@objc protocol RadioButtonContainerDelegate {
    /**
     This function is called when a button is selected. If 'shouldLetDeSelect' is true, and a button is deselected, this function
     is called with a nil.
     
     */
    @objc optional func didSelectButton(_ aButton: UIButton?)
}

/// RadioButtonsContainer - Container class that presents and handles array of buttons.

class RadioButtonsContainer : NSObject
{
    fileprivate var buttonsArray = [UIButton]()
    fileprivate weak var currentSelectedButton:UIButton? = nil
    weak var delegate : RadioButtonContainerDelegate? = nil
    /**
     Set whether a selected radio button can be deselected or not. Default value is false.
     */
    var shouldLetDeSelect = false
    /**
     Variadic parameter init that accepts UIButtons.
     
     - parameter buttons: Buttons that should behave as Radio Buttons
     */
    init(buttons: UIButton...) {
        super.init()
        for aButton in buttons {
            aButton.addTarget(self, action: #selector(self.pressed(_:)), for: UIControlEvents.touchUpInside)
        }
        self.buttonsArray = buttons
    }
    /**
     Add a UIButton to Controller
     
     - parameter button: Add the button to controller.
     */
    func addButton(_ aButton: UIButton) {
        buttonsArray.append(aButton)
        aButton.addTarget(self, action: #selector(self.pressed(_:)), for: UIControlEvents.touchUpInside)
    }
    /**
     Remove a UIButton from controller.
     
     - parameter button: Button to be removed from controller.
     */
    func removeButton(_ aButton: UIButton) {
        let iteration = 0
        var iteratingButton: UIButton? = nil
        
        for iteration in 0...buttonsArray.count {
            
            //        for ; iteration<buttonsArray.count; iteration += 1 {
            iteratingButton = buttonsArray[iteration]
            if(iteratingButton == aButton) {
                break
            } else {
                iteratingButton = nil
            }
        }
        if(iteratingButton != nil) {
            buttonsArray.remove(at: iteration)
            iteratingButton!.removeTarget(self, action: #selector(self.pressed(_:)), for: UIControlEvents.touchUpInside)
            if currentSelectedButton == iteratingButton {
                currentSelectedButton = nil
            }
        }
    }
    /**
     Set an array of UIButons to behave as controller.
     
     - parameter buttonArray: Array of buttons
     */
    func setButtonsArray(_ aButtonsArray: [UIButton]) {
        for aButton in aButtonsArray {
            aButton.addTarget(self, action: #selector(self.pressed(_:)), for: UIControlEvents.touchUpInside)
        }
        buttonsArray = aButtonsArray
    }
    
    func pressed(_ sender: UIButton) {
        if(sender.isSelected) {
            if shouldLetDeSelect {
                sender.isSelected = false
                currentSelectedButton = nil
            }
        } else {
            for aButton in buttonsArray {
                aButton.isSelected = false
            }
            sender.isSelected = true
            currentSelectedButton = sender
        }
        delegate?.didSelectButton?(currentSelectedButton)
    }
    /**
     Get the currently selected button.
     
     - returns: Currenlty selected button.
     */
    func selectedButton() -> UIButton? {
        return currentSelectedButton
    }
}
