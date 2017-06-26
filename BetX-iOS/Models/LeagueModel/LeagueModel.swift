//
//  MatchDetailsViewController
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//


import Foundation
import SwiftyJSON

public class LeagueModel: NSCoding {

  // MARK: Declaration for string constants to be used to decode and also serialize.
  private struct SerializationKeys {
    static let leagueTableHeader = "LeagueTableHeader"
    static let leagueName = "LeagueName"
    static let leagueId = "LeagueId"
    static let startDate = "StartDate"
    static let teamStatistics = "TeamStatistics"
    static let endDate = "EndDate"
    static let leagueForeignId = "LeagueForeignId"
  }

  // MARK: Properties
  public var leagueTableHeader: [String]?
  public var leagueName: String?
  public var leagueId: Int?
  public var startDate: String?
  public var teamStatistics: [TeamStatistics]?
  public var endDate: String?
  public var leagueForeignId: Int?

  // MARK: SwiftyJSON Initializers
  /// Initiates the instance based on the object.
  ///
  /// - parameter object: The object of either Dictionary or Array kind that was passed.
  /// - returns: An initialized instance of the class.
  public convenience init(object: Any) {
    self.init(json: JSON(object))
  }

  /// Initiates the instance based on the JSON that was passed.
  ///
  /// - parameter json: JSON object from SwiftyJSON.
  public required init(json: JSON) {
    if let items = json[SerializationKeys.leagueTableHeader].array { leagueTableHeader = items.map { $0.stringValue } }
    leagueName = json[SerializationKeys.leagueName].string
    leagueId = json[SerializationKeys.leagueId].int
    startDate = json[SerializationKeys.startDate].string
    if let items = json[SerializationKeys.teamStatistics].array { teamStatistics = items.map { TeamStatistics(json: $0) } }
    endDate = json[SerializationKeys.endDate].string
    leagueForeignId = json[SerializationKeys.leagueForeignId].int
  }

  /// Generates description of the object in the form of a NSDictionary.
  ///
  /// - returns: A Key value pair containing all valid values in the object.
  public func dictionaryRepresentation() -> [String: Any] {
    var dictionary: [String: Any] = [:]
    if let value = leagueTableHeader { dictionary[SerializationKeys.leagueTableHeader] = value }
    if let value = leagueName { dictionary[SerializationKeys.leagueName] = value }
    if let value = leagueId { dictionary[SerializationKeys.leagueId] = value }
    if let value = startDate { dictionary[SerializationKeys.startDate] = value }
    if let value = teamStatistics { dictionary[SerializationKeys.teamStatistics] = value.map { $0.dictionaryRepresentation() } }
    if let value = endDate { dictionary[SerializationKeys.endDate] = value }
    if let value = leagueForeignId { dictionary[SerializationKeys.leagueForeignId] = value }
    return dictionary
  }

  // MARK: NSCoding Protocol
  required public init(coder aDecoder: NSCoder) {
    self.leagueTableHeader = aDecoder.decodeObject(forKey: SerializationKeys.leagueTableHeader) as? [String]
    self.leagueName = aDecoder.decodeObject(forKey: SerializationKeys.leagueName) as? String
    self.leagueId = aDecoder.decodeObject(forKey: SerializationKeys.leagueId) as? Int
    self.startDate = aDecoder.decodeObject(forKey: SerializationKeys.startDate) as? String
    self.teamStatistics = aDecoder.decodeObject(forKey: SerializationKeys.teamStatistics) as? [TeamStatistics]
    self.endDate = aDecoder.decodeObject(forKey: SerializationKeys.endDate) as? String
    self.leagueForeignId = aDecoder.decodeObject(forKey: SerializationKeys.leagueForeignId) as? Int
  }

  public func encode(with aCoder: NSCoder) {
    aCoder.encode(leagueTableHeader, forKey: SerializationKeys.leagueTableHeader)
    aCoder.encode(leagueName, forKey: SerializationKeys.leagueName)
    aCoder.encode(leagueId, forKey: SerializationKeys.leagueId)
    aCoder.encode(startDate, forKey: SerializationKeys.startDate)
    aCoder.encode(teamStatistics, forKey: SerializationKeys.teamStatistics)
    aCoder.encode(endDate, forKey: SerializationKeys.endDate)
    aCoder.encode(leagueForeignId, forKey: SerializationKeys.leagueForeignId)
  }

}
