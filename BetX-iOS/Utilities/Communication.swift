//
//  Communication.swift
//  BetX
//
//  Created by Zesium Mobile Ltd. on 9/1/16.
//  Copyright © 2016 Zesium Mobile Ltd. All rights reserved.
//


/*
 
 Communication class contains all required methods which performs API calls, manage the underlying network calls and give back the server responses.
 
 */

import Alamofire
import Foundation
import SystemConfiguration



class Communication {
    
    //Shared Instance
    static let shared = Communication()
    
    
    // web services configuration
    
    private struct ServerURL {
        // Server api addresses
        static let BASE_SERVER = "betx.sk"
        static let OLD_BASE_SERVER = "bet-x.sk"
        
        static let sports = "https://\(BASE_SERVER)/sport_api/"
        static let userBalance = "https://\(BASE_SERVER)/notification_api/"
        static let userAuthorization = "https://authapisk.\(BASE_SERVER)/"
        static let tickets = "https://www.\(BASE_SERVER)/ticket_api/"
        static let terminal = "https://terminalapi_sk.\(BASE_SERVER)/"
        static let tableLeague = "http://sportapi.\(OLD_BASE_SERVER)/"
        static let user = "https://www.\(BASE_SERVER)/user_api/"
    }
    
    private struct APICalls {
        // Server api calls
        static let token = "token"
        static let register = "mobile/register"
        static let userAvailability = "userfieldavailability"
        static let countries = "common/countries"
        static let regions = "common/regions"
        static let cities = "common/cities"
        static let security = "common/securityquestions"
        static let forgotenPasswordAnswer = "mobile/forgottenpassword/answer"
        static let translation = "translation/%@"
        static let userDetails = "userdetails"
        static let forgottenPassword = "forgottenpassword"
        static let changeUserPassword = "userpassword"
        static let recoveryPassword = "recoverypassword"
        static let mostPlayed = "offer/v2/mostplayed"
        static let sports = "offer/v2/sports"
        static let sportsOffer = "offer/v2/sports/offer"
        static let matchOffer = "offer/v2/match/offers"
        static let validateCode = "validatecode"
        static let registerConfirmation = "register/confirmation"
        static let activationCodeResend = "mobile/activationcode/resend"
        static let ticketDetail = "v2/ticket"
        static let myTickets = "v2/tickets"
        static let leaguetable = "statistic/leaguetable/"
        static let cancel = "v2/cancel"
        static let securitySettings = "securitysettings"
        static let editUser = "edituser"
        
    }
    
    //MARK: Constants
    let kMaxRequestRetry: UInt = 4
    let reachabilityManager: NetworkReachabilityManager?
    
    //MARK: Variables
    var requestList: [CommunicationRequest] = []
    
    
    init(){
        reachabilityManager = Alamofire.NetworkReachabilityManager()
        listenForReachability()
    }
    
    
    //MARK: Web service methods
    
    
    /**
     Manage login web service.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "username" : String
     - "password" : String
     - "terminal_type": String
     - "grant_type": String
     - "client_id": String
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func loginWithParameters(_ parameters: [String:Any], successBlock : ((Any) -> ())!,
                             failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.token, relativeTo: URL(string: ServerURL.userAuthorization)){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: URLEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage registration web service.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "Zipcode":String
     - "AcceptTerms":String
     - "RecordingPersonalData":String
     - "FirstName":String
     - "LastName":String
     - "Username":String
     - "Birthdate":String
     - "Email":String
     - "EmailConfirm":String
     - "MobileNumber":String
     - "CityId": Int
     - "RegionId":Int
     - "CountryId":Int
     - "Address":String
     - "Password":String
     - "PasswordConfirm":String
     - "SecurityQuestionId":Int
     - "SecurityAnswer":String
     
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    
    func registerWithParameters(parameters: [String:AnyObject], successBlock : ((Any) -> ())!,
                                failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.register, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: URLEncoding.default, headers: ["TerminalId":"2000"], successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage registration confirmation web service.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "ActivationCode":String
     
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    
    
    func registerConfirmation(parameters: [String:String], successBlock : ((Any) -> ())!,
                              failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.registerConfirmation, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: URLEncoding.default, headers: ["TerminalId":"2000"], successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage activation code resend web service.
     
     - parameter string: require user's username.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    
    func activationCodeResend(string: String, successBlock : ((Any) -> ())!,
                              failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.activationCodeResend, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: [:], encoding: string, headers: ["Content-Type":"application/json"], successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage confirmation code validation web service.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "ActivationCode":String
     
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    
    func validateCode(_ parameters: [String:String], successBlock : ((Any) -> ())!,
                      failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.validateCode, relativeTo: URL(string: ServerURL.user)){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: URLEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage username availability web service.
     
     - parameter username: username entered by user.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func usernameAvailability(_ username: String, successBlock : ((Any) -> ())!,
                              failureBlock : ((ErrorResponse?) -> ())!){
        
        let usernameDict = ["Value":username]
        
        if let url = URL(string: APICalls.userAvailability, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: usernameDict, encoding: URLEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Get countries web service.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getCountries(_ successBlock : ((Any) -> ())!,
                      failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.countries, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Get regions web service.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    
    func getRegions(_ successBlock : ((Any) -> ())!,
                    failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.regions, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    
    /**
     Get cities web service.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    
    func getCities(_ successBlock : ((Any) -> ())!,
                   failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.cities, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Get security questions web service.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getSecurityQuestions(_ successBlock : ((Any) -> ())!,
                              failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.security, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage reset password web service.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "SecurityAnswer":String
     - "Username":String
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func resetPassword(_ parameters: [String:String], successBlock : ((Any) -> ())!,
                       failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.forgotenPasswordAnswer, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    /**
     Manage translations web service.
     
     - parameter language: language code; examples ("en","sk")
     
     - parameter saveRequest: boolean value to determine wheter request should be saved or not.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getTranslation(_ language: String, saveRequest: Bool, successBlock : ((Any) -> ())!,
                        failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: String(format: APICalls.translation, language), relativeTo: URL(string: ServerURL.terminal )){
            sendRequest(url: url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: nil, saveRequest: saveRequest, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage web service to fetch user details.
     
     - parameter headers: request header parameters for web service.
     
     ## headers structure
     
     - "Authorization":String
     - "TerminalId":String
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func userDetails(_ headers: [String:String], successBlock : ((Any) -> ())!,
                     failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.userDetails, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: nil, encoding: JSONEncoding.default, headers: headers, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage forgotten password web service.
     
     - parameter username: username entered by user.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func forgottenPassword(_ username: String, successBlock : ((Any) -> ())!,
                           failureBlock : ((ErrorResponse?) -> ())!){
        
        let forgotenPasswordParameters = ["value":"\(username)"]
        
        if let url = URL(string: APICalls.forgottenPassword, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: forgotenPasswordParameters, encoding: JSONEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    /**
     Manage web service for editing user information.
     
     - parameter headers: request header parameters for web service.
     
     ## headers structure
     
     - "Authorization":String
     - "TerminalId":String
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func editUserInfo(headers: [String: String]? = nil, params: [String: Any], successBlock : ((Any) -> ())!,
                      failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.editUser, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: params, encoding: URLEncoding.default, headers: headers, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    
    /**
     Manage web service for changing security question.
     
     - parameter headers: request header parameters for web service.
     
     ## headers structure
     
     - "Authorization":String
     - "TerminalId":String
     
     - parameter securityQuestionId: id: Int of security question.
     
     - parameter securityAnswer: answer: String - entered by user.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func changeSecurityQuestion(headers: [String: String]? = nil, securityQuestionId: Int, securityAnswer: String, successBlock : ((Any) -> ())!, failureBlock : ((ErrorResponse?) -> ())!){
        
        let securitySettingsParams = ["SecurityQuestionId":"\(securityQuestionId)", "SecurityAnswer":"\(securityAnswer)"]
        
        if let url = URL(string: APICalls.securitySettings, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: securitySettingsParams, encoding: URLEncoding.default, headers: headers, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    /**
     Manage web service for changing user password.
     
     - parameter headers: request header parameters for web service.
     
     ## headers structure
     
     - "Authorization":String
     - "TerminalId":String
     
     - parameter newPassword: String.
     
     - parameter oldPassword: String.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func changeUserPassword(headers: [String: String]? = nil, _ newPassword: String, oldPassword: String, successBlock : ((Any) -> ())!,failureBlock : ((ErrorResponse?) -> ())!){
        
        let forgotenPasswordParameters = ["NewPassword":"\(newPassword)", "OldPassword":"\(oldPassword)"]
        
        if let url = URL(string: APICalls.changeUserPassword, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: forgotenPasswordParameters, encoding: URLEncoding.default, headers: headers, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    /**
     Manage web service for sending new password.
     
     - parameter newPassword: String.
     
     - parameter passwordConfirm: String.
     
     - parameter activationCode: String.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func sendNewPassword(_ newPassword: String, passwordConfirm: String, activationCode: String, successBlock : ((Any) -> ())!,
                         failureBlock : ((ErrorResponse?) -> ())!){
        
        let forgotenPasswordParameters = ["Password":"\(newPassword)", "PasswordConfirm":"\(passwordConfirm)", "ActivationCode":"\(activationCode)"]
        
        if let url = URL(string: APICalls.recoveryPassword, relativeTo: URL(string: ServerURL.user )){
            sendRequest(url: url, method: .post, parameters: forgotenPasswordParameters, encoding: URLEncoding.default, headers:  ["TerminalId":"2000"], successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    
    /**
     Manage web service for getting most played matches.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "Limit":Int
     
     - parameter saveRequest: boolean value to determine wheter request should be saved or not (if true it will try to resend web service again if errors occure).
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    
    func mostPlayed(parameters: [String:Any], saveRequest: Bool, _ successBlock : ((Any) -> ())!,
                    failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.mostPlayed, relativeTo: URL(string: ServerURL.sports )){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: nil, saveRequest: saveRequest, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    /**
     Manage web service for getting sports.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "DateFrom": String
     - "DateTo": String
     - "OddsFilter":String
     - "SportIds":[Int]
     
     - parameter saveRequest: boolean value to determine wheter request should be saved or not (if true it will try to resend web service again if errors occure).
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getSports(_ parameters: [String:Any], saveRequest: Bool, successBlock : ((Any) -> ())!,
                   failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.sports, relativeTo: URL(string: ServerURL.sports )){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: nil, saveRequest: saveRequest, successBlock: successBlock, failureBlock: failureBlock)
        }
    }
    
    /**
     Manage web service for getting sport offers.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "SportIds": [Int]
     - "CategoryIds": [Int]
     - "LeagueIds": [Int]
     - "DateFrom": String
     - "DateTo": String
     - "Offset": Int
     - "Limit": Int
     - "OddsFilter": String]
     
     - parameter saveRequest: boolean value to determine wheter request should be saved or not (if true it will try to resend web service again if errors occure).
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getSportOffers(_ parameters: [String:Any], saveRequest: Bool, successBlock : ((Any) -> ())!,
                        failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.sportsOffer, relativeTo: URL(string: ServerURL.sports )){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: nil, saveRequest: saveRequest, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    
    /**
     Manage web service for getting match offers.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "MatchId": Int
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getMatchOffers(_ parameters: [String:Any], successBlock : ((Any) -> ())!,
                        failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.matchOffer, relativeTo: URL(string: ServerURL.sports )){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    /**
     Manage web service for getting table of league.
     
     - parameter id: String - id of league.
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getLeagueTableFor(_ id: String, successBlock : ((Any) -> ())!,
                           failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.leaguetable+id, relativeTo: URL(string: ServerURL.sports )){
            sendRequest(url: url, method: .get, parameters: nil, encoding: JSONEncoding.default, headers: nil, successBlock: successBlock, failureBlock: failureBlock)
        }
        
    }
    
    
    /**
     Manage web service for getting list of tickets.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "TimeFilterId":String
     - "Year":String
     - "GameId":String
     - "Username":String
     - "Offset":Int
     - "Limit":Int
     - "StatusId":String
     
     - parameter saveRequest: boolean value to determine wheter request should be saved or not (if true it will try to resend web service again if errors occure).
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getTicketList(_ parameters: [String:String], saveRequest:Bool, successBlock : ((Any) -> ())!,
                       failureBlock : ((ErrorResponse?) -> ())!){
        
        if let url = URL(string: APICalls.myTickets, relativeTo: URL(string: ServerURL.tickets )){
            if let accessToken = BetXSettings.shared.userData.accessToken, let terminalId = BetXSettings.shared.userData.terminalId {
                
                let headersCustom: HTTPHeaders = ["Authorization":accessToken, "TerminalId":terminalId]
                
                sendRequest(url: url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: headersCustom, saveRequest: saveRequest, successBlock: successBlock, failureBlock: failureBlock)
            }
            
        }
    }
    
    /**
     Manage web service for getting details of a single ticket.
     
     - parameter headers: request header parameters for web service.
     
     ## Headers structure
     
     - "Authorization":String
     - "TerminalId":String
     
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "EncryptedPin":String
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func getTicketDetails(headers : [String:Any], parameters: [String:Any], successBlock : ((Any) -> ())!, failureBlock : ((ErrorResponse?) -> ())!){
        
        
        if let url = URL(string: APICalls.ticketDetail, relativeTo: URL(string: ServerURL.tickets )){
            sendRequest(url: url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: headers as? HTTPHeaders, successBlock: successBlock, failureBlock: failureBlock)
            
        }
        
    }
    
    /**
     Manage web service for canceling a ticket.
     
     - parameter parameters: request body parameters for web service.
     
     ## Parameters structure
     
     - "EncryptedPin":String
     - "SystemCombinationsType":String
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     */
    
    func cancelTicket(parameters: [String:String], successBlock : ((Any) -> ())!, failureBlock : ((ErrorResponse?) -> ())!){
        if let url = URL(string: APICalls.cancel, relativeTo: URL(string: ServerURL.tickets)) {
            if let accessToken = BetXSettings.shared.userData.accessToken, let terminalId = BetXSettings.shared.userData.terminalId {
                
                let headersCustom: HTTPHeaders = ["Authorization":accessToken, "TerminalId":terminalId]
                sendRequest(url: url, method: .post, parameters: parameters, encoding: JSONEncoding.default, headers: headersCustom, successBlock: successBlock, failureBlock: failureBlock)
            }
            
        }
    }
    
    /**
     
     Shows network reachability status.
     
     - returns: Flag with information is network reachable.
     
     */
    
    func networkReachability() -> Bool {
        return (reachabilityManager?.isReachable)!
    }
    
    /**
     
     Cancelling all current requests.
     
     Loop through an requestList, cancel every request in it and remove request from list.
     
     */
    
    func cancelRequests(){
        for request in self.requestList {
            request.cancelRequest()
        }
        
        self.requestList.removeAll()
    }
    
    //MARK: local methods
    
    
    /**
     
     Observe when network connection change.
     
     */
    
    internal func listenForReachability() {
        self.reachabilityManager?.listener = { status in
            print("Network Status Changed: \(status)")
            switch status {
            case .notReachable, .unknown: break
            //Show error state
            case .reachable(_):
                let appDelegate = UIApplication.shared.delegate as! AppDelegate
                appDelegate.dismissNoInternetAlert()
                
                for request in self.requestList {
                    request.cancelRequest()
                    self.sendAlamofireRequest(request: request)
                }
            }
        }
        
        self.reachabilityManager?.startListening()
    }
    
    /**
     
     sendRequest method is responsible for sending request with given parameters.
     
     ## method tasks
     
     - Adding "Accept-Language" to headers
     - Createing CommunicationRequest instancewith provided params
     - Append request to rquestList if needed
     - Checks network reachability
     - sendAlamofireRequest
     
     - parameter url: URL
     
     - parameter method: request-response method between a client and server
     
     - parameter headers: request header parameters for web service.
     
     - parameter parameters: request body parameters for web service.
     
     - parameter encoding: Type used to define how a set of parameters are applied to a URLRequest.
     
     - parameter saveRequest: boolean value to determine wheter request should be saved or not (if true it will try to resend web service again if errors occure).
     
     - parameter successBlock: web service success response.
     
     - parameter failureBlock: web service error response.
     
     
     */
    
    internal func sendRequest(url:URL,method: HTTPMethod, parameters: [String:Any]?, encoding: ParameterEncoding, headers: HTTPHeaders?, saveRequest: Bool = false, successBlock : ((Any) -> ())!, failureBlock : ((ErrorResponse?) -> ())!){
        
        var headersWithLang = HTTPHeaders()
        if let unwrappedHeader = headers {
            headersWithLang = unwrappedHeader
        }
        
        
        if let userDefaultsLanguage = BetXSettings.shared.choosenLanguage {
            let language = LanguageManager.Language(rawValue: userDefaultsLanguage)!
            
            headersWithLang["Accept-Language"] = language.getWebServiceSuffix()
            
        }
        
        let request = CommunicationRequest(url: url, method: method, parameters: parameters, encoding: encoding, headers: headersWithLang, successBlock: successBlock, failureBlock: failureBlock)
        
        if saveRequest {
            requestList.append(request)
        }
        
        if !networkReachability() {
            let errorResponse = ErrorResponse()
            errorResponse.setNoInternetConnection()
            
            print("###Communication error occured - No Internet connection")
            
            failureBlock(errorResponse)
        }else{
            sendAlamofireRequest(request: request)
        }
        
    }
    
    /**
     
     Creates a DataRequest using the AlamofireManager to retrieve the contents of the specified url, method, parameters, encoding and headers.
     */
    
    internal func sendAlamofireRequest(request: CommunicationRequest){
        let dataRequest = Alamofire.request(request.url!, method: request.method!, parameters: request.parameters, encoding: request.encoding!, headers: request.headers)
        request.dataRequest = dataRequest
        
        
        retry(numberOfTimes: kMaxRequestRetry, request: request)
    }
    
    /**
     
     retry method purpose is to send request again for specific number of times if needed
     
     */
    
    internal func retry(numberOfTimes: UInt,request: CommunicationRequest){
        request.dataRequest?.validate(statusCode: 200..<300)
            .responseJSON {  response in
                
                switch response.result {
                    
                case .success:
                    if let JSON = response.result.value {
                        request.successBlock(JSON)
                    }
                    
                    self.requestList.remove(object: request)
                    
                case .failure(let error):
                    
                    let errorResponse = ErrorResponse()
                    errorResponse.setError(error: response.error! as NSError, errodCode: response.response != nil ? (response.response?.statusCode)! : 0, errorData: response.data)
                    
                    print("###Communication error occured - error code: \(errorResponse.errorCode)  message: \(errorResponse.errorMessage)  description: \(errorResponse.errorDescription)")
                    
                    if  errorResponse.errorCode != NSURLErrorCancelled{
                        // do we have retries left? if yes, call retry again
                        // if not, report error
                        if error._code == NSURLErrorTimedOut && numberOfTimes > 1 {
                            self.retry(numberOfTimes: numberOfTimes - 1, request: request)
                        } else {
                            self.requestList.remove(object: request)
                            
                            request.failureBlock(errorResponse)
                        }
                    }else{
                        self.requestList.remove(object: request)
                        
                    }
                }
                
        }
    }
    
}





