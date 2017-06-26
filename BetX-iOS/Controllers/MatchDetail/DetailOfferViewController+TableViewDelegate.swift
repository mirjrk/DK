//
//  DetailOfferViewController+TableViewDelegate.swift
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//

import UIKit



// MARK: UITableViewDelegate and UITableViewDataSource
extension DetailOfferViewController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        //if section is expanded, section has one row
        if sectionsExpandedState[section]{
            return 1
        }else{
            return 0
        }
    }
    
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: MatchDetailsConstants.Identifiers.detailOfferTableViewCell.rawValue, for: indexPath) as! DetailOfferTableViewCell
        
        //get offer group for given section
        let offerArray = groupedOffers?[indexPath.section]
        
        
        //assign data to the cell
        if let unwrappedOfferArray = offerArray{
            cell.offerList = unwrappedOfferArray
            cell.selectedMatchTemp = match
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        //default margin between odds
        let margin:CGFloat = 4
        
        //get offer group for given section
        let offerArray = groupedOffers?[indexPath.section]
        
        //calculate number of odd rows in cell
        
        var numberOfOddRows: CGFloat = 0
        
        //if group of offers contains one offer
        if offerArray?.count == 1 {
            let oddCount = (offerArray?[0].odds?.count)!
            
            //divide odd counts with max allowed number odds in row
            numberOfOddRows = CGFloat(oddCount / constants.ODDS_MAX_NUMBER_IN_ROW)
            
            if oddCount % constants.ODDS_MAX_NUMBER_IN_ROW != 0{
                numberOfOddRows += 1
            }
        }else{
            //if group of offers contains multiple offer
            //number of odd rows will be list count in that group
            numberOfOddRows = CGFloat((offerArray?.count)!)
        }
        
        
        return numberOfOddRows * constants.ODD_CELL_HEIGHT + margin*numberOfOddRows
        
    }
    
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        if let cnt = groupedOffers?.count{
            return cnt
        }else{
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return constants.TABLE_SECTION_HEIGHT
    }
    
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        
        let sectionHeaderView = tableView.dequeueReusableHeaderFooterView(withIdentifier: MatchDetailsConstants.Identifiers.detailOfferSectionHeaderView.rawValue) as! DetailOfferSectionHeaderView
        
        
        //assign data to section view
        sectionHeaderView.delegate = self
        sectionHeaderView.set(expanded: sectionsExpandedState[section])
        sectionHeaderView.customBackgroundView.backgroundColor = ThemeManager.currentTheme().backgroundColor
        
        let offer = groupedOffers?[section][0]
        sectionHeaderView.set(offerName: offer?.originDescription ?? "")
        
        return sectionHeaderView
    }
    
    
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        let view = UIView()
        view.backgroundColor = .mountainMist
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 1
    }
    
    
}



