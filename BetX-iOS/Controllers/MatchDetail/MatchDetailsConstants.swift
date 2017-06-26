//
//  MatchDetailsConstants.swift
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//


import Foundation

/// MatchDetailsConstants - Class that contains constants which is used in MatchDetail controllers

class MatchDetailsConstants: NSObject{
    
    //MARK: Enums

    
    /**
     Table view identifiers
     
     - detailOfferTableViewCell: A DetailOffer cell identifier.
     - detailOfferSectionHeaderView: A section view identifier.
     - sportsBettingTableFooterView: A table footer identifier.
     - lastMatchesTableViewCell: A LastMatches cell identifier.
     - headToHeadTableViewCell: A HeadToHead cell identifier.
     - totalGoalsTableViewCell: A TotalGoals cell identifier.
     - leagueTableStatisticsTableViewCell: A LeagueTable cell identifier.

     */
    
    enum Identifiers: String {
        
        case detailOfferTableViewCell = "DetailOfferTableViewCell"
        case detailOfferSectionHeaderView = "DetailOfferSectionHeaderView"
        case sportsBettingTableFooterView = "SportsBettingTableFooterView"
        case lastMatchesTableViewCell = "LastMatchesTableViewCell"
        case headToHeadTableViewCell = "HeadToHeadTableViewCell"
        case totalGoalsTableViewCell = "TotalGoalsTableViewCell"
        case leagueTableStatisticsTableViewCell = "LeagueTableStatisticsTableViewCell"
        
    }

    /**
     Statistics table view row indexes
     
     - basicOffer: A basic offers row index.
     - lastMatches: A last matches row index.
     - headToHead: A head to head row index.
     - totalGoals: A total goals row index.
     - leagueTable: A league table row index.
     */
    enum StatisticsTableRowIndex: Int {
        case basicOffer = 0
        case lastMatches = 1
        case headToHead = 2
        case totalGoals = 3
        case leagueTable = 4
    }
    
    
    //MARK: Constants
    
    /// The aspect ratio of header view (basic statistics view).
    let HEADER_VIEW_ASPECT_RATION: CGFloat = 2/5
    
    ///The height of odd cells.
    let ODD_CELL_HEIGHT: CGFloat = 35
    
    ///The height of table sections.
    let TABLE_SECTION_HEIGHT: CGFloat = 60
    
    ///The maximum number of odds in single row.
    let ODDS_MAX_NUMBER_IN_ROW: Int = 3
    
    ///The number of rows which should be expanded at the start.
    let DEFAULT_EXPANDED_ROW_COUNT: Int = 5
    
    ///The number of sections in Statistics table view.
    let STATISTICS_TABLE_SECTION_COUNT: Int = 5
    
    ///The height of footer view.
    let FOOTER_TABLE_VIEW_HEIGHT: CGFloat = 80

   
}

