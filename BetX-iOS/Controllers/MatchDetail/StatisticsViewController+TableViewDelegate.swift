//
//  StatisticsViewController+TableViewDelegate.swift
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Nemanja. All rights reserved.
//

import UIKit

// MARK: UITableViewDelegate and UITableViewDataSource

extension StatisticsViewController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //first section (Basic offer) always expanded
        if sectionsExpandedState[section] || section == 0{
            return 1
        }else{
            return 0
        }
    }
    
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        switch indexPath.section {
        case MatchDetailsConstants.StatisticsTableRowIndex.basicOffer.rawValue:
            //Basic offers
            
            let cell = tableView.dequeueReusableCell(withIdentifier: MatchDetailsConstants.Identifiers.detailOfferTableViewCell.rawValue, for: indexPath) as! DetailOfferTableViewCell
            
            
            guard let offer = match?.offers?[0], let oddList = offer.odds  else { return UITableViewCell() }
            
            
            //assign data and reload data
            
            cell.oddList = oddList
            cell.selectedMatchTemp = match
            
            cell.reloadData()
            
            return cell
        case MatchDetailsConstants.StatisticsTableRowIndex.lastMatches.rawValue:
            //Last matches
            
            let cell = tableView.dequeueReusableCell(withIdentifier: MatchDetailsConstants.Identifiers.lastMatchesTableViewCell.rawValue, for: indexPath) as! LastMatchesTableViewCell
            
            
            //assign data
            cell.homeLastMatches = homeLastMatches
            cell.awayLastMatches = awayLastMatches
            
            if let unwrappedMatch = match, cell.match == nil  {
                cell.match = unwrappedMatch
            }
            
            return cell
        case MatchDetailsConstants.StatisticsTableRowIndex.headToHead.rawValue:
            //Head To Head
            
            let cell = tableView.dequeueReusableCell(withIdentifier: MatchDetailsConstants.Identifiers.headToHeadTableViewCell.rawValue, for: indexPath) as! HeadToHeadTableViewCell
            
            //disable selection
            cell.selectionStyle = .none
            
            
            //assign data
            if let unwrappedMatch = match{
                cell.match = unwrappedMatch
            }
            
            cell.headToHeadMatches = homeLastMatches
            
            
            return cell
        case MatchDetailsConstants.StatisticsTableRowIndex.totalGoals.rawValue:
            //Total goals
            
            let cell = tableView.dequeueReusableCell(withIdentifier: MatchDetailsConstants.Identifiers.totalGoalsTableViewCell.rawValue, for: indexPath) as! TotalGoalsTableViewCell
            
            //disable selection
            cell.selectionStyle = .none
            
            //assign data and reload data
            if let unwrappedMatch = match, cell.match == nil {
                cell.match = unwrappedMatch
            }
            
            cell.reloadData()
            
            return cell
        case MatchDetailsConstants.StatisticsTableRowIndex.leagueTable.rawValue:
            //League table
            let cell = tableView.dequeueReusableCell(withIdentifier: MatchDetailsConstants.Identifiers.leagueTableStatisticsTableViewCell.rawValue, for: indexPath) as! LeagueTableStatisticsTableViewCell
            
            //disable selection
            cell.selectionStyle = .none
            
            //assign data
            if let unwrappedTeamStatisticArray = teamStatisticArray {
                cell.teamStatisticArray = unwrappedTeamStatisticArray
            }
            
            
            return cell
            
            
        default:
            return UITableViewCell(frame: .zero)
            
        }
        
        
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        
        switch indexPath.section {
        
        
        case MatchDetailsConstants.StatisticsTableRowIndex.basicOffer.rawValue:
            //Basic offers
            
            //default margin between odds
            let margin:CGFloat = 4
            
            guard let oddsArray = match?.basicOffer?.odds else { return 0 }
            
            //calculate number of odd rows in cell
            
            var numberOfRows: CGFloat = CGFloat(Int(oddsArray.count / constants.ODDS_MAX_NUMBER_IN_ROW))
            
            if oddsArray.count % constants.ODDS_MAX_NUMBER_IN_ROW != 0{
                numberOfRows += 1
            }
            
            return numberOfRows * constants.ODD_CELL_HEIGHT + margin*numberOfRows
     
        
        case MatchDetailsConstants.StatisticsTableRowIndex.lastMatches.rawValue:
            //Last matches
            
            //calculate cell height
            
            
            let numberOfRows: CGFloat = 6 // 5 matches plus header row
            let rowHeight: CGFloat = 30
            let segmentioHeight: CGFloat = 50
            let margin: CGFloat = 20
            
            return rowHeight*numberOfRows + segmentioHeight + margin
      
        
        case MatchDetailsConstants.StatisticsTableRowIndex.headToHead.rawValue:
            //Head to head
            
            //calculate cell height
            
            let numberOfRows: CGFloat = 6 // 5 matches plus header row
            let topViewHeight: CGFloat = 20
            let rowHeight: CGFloat = 30
            let margin: CGFloat = 30
            
            var oddHeight: CGFloat = 0
            if let unwrappedMatch = match, let _ = Commons.findDrawNoBetOffer(match: unwrappedMatch) {
                oddHeight = 70
            }
            
            
            return topViewHeight + rowHeight*numberOfRows + oddHeight + margin
            
        case MatchDetailsConstants.StatisticsTableRowIndex.totalGoals.rawValue:
            //Total goals
            
            //calculate cell height
            
            if let unwrappedMatch = match{
                let offers:[Offers] = Commons.findTotalGoals(match: unwrappedMatch)
                
                if !offers.isEmpty{
                    let segmentioAndLabelHeight: CGFloat = 96
                    let chartViewHeight: CGFloat = 100
                    let rowHeight: CGFloat = 35
                    let margin: CGFloat = 35
                    
                    return rowHeight*CGFloat(offers.count + 1) + segmentioAndLabelHeight + chartViewHeight + margin
                }
            }
      
        
        case MatchDetailsConstants.StatisticsTableRowIndex.leagueTable.rawValue:
            //League table
            
            //calculate cell height
            
            if let unwrappedTeamStatisticArray = teamStatisticArray, !unwrappedTeamStatisticArray.isEmpty {
                let rowHeight = 44 // team count + 1 for header view
                let margin: CGFloat = 10
                
                return CGFloat((unwrappedTeamStatisticArray.count + 1) * rowHeight) + margin
            }
       
        
        default:
            break
        }
     
        
        return 0
    }
    
    
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return constants.STATISTICS_TABLE_SECTION_COUNT
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        
        //for basic offers
        if section == 0{
            let margin:CGFloat = 6
            
            return margin
        }
        
        return constants.TABLE_SECTION_HEIGHT
    }
    
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        //for basic offers
        if section == 0{
            return UIView(frame: .zero)
        }
        
        let sectionHeaderView = tableView.dequeueReusableHeaderFooterView(withIdentifier: MatchDetailsConstants.Identifiers.detailOfferSectionHeaderView.rawValue) as! DetailOfferSectionHeaderView
        
        
        sectionHeaderView.delegate = self
        sectionHeaderView.section = section
        
        
        
        
        //set section title
        var sectionStr = ""
        
        let lastMatchesIndex = 1, headToHeadIndex = 2, totalGoalsIndex = 3, leagueTableIndex = 4
        switch section {
        case lastMatchesIndex:
            sectionStr = LanguageManager.sharedInstance.translationFor(.UNKNOWN, defaultValue: "Last matches")
        case headToHeadIndex:
            sectionStr = LanguageManager.sharedInstance.translationFor(.UNKNOWN, defaultValue: "Head to Head")
        case totalGoalsIndex:
            sectionStr = LanguageManager.sharedInstance.translationFor(.TOTAL_GOALS, defaultValue: "Total goals")
        case leagueTableIndex:
            sectionStr = LanguageManager.sharedInstance.translationFor(.UNKNOWN, defaultValue: "League table")
        default:
            break
        }
        
        sectionHeaderView.offerNameLabel.text = sectionStr
        
        //set section state
        sectionHeaderView.set(expanded: sectionsExpandedState[section])
        if sectionsExpandedState[section]  {
            sectionHeaderView.customBackgroundView.backgroundColor = ThemeManager.currentTheme().backgroundColor
        } else {
            sectionHeaderView.customBackgroundView.backgroundColor = ThemeManager.currentTheme().radioButtonColor
            
        }
        
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



