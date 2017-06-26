//
//  MatchDetailsViewController
//  BetX
//
//  Created by Zesium on 4/24/17.
//  Copyright © 2017 Zesium Mobile Ltd. All rights reserved.
//


import Alamofire



/// ErrorResponse - Communication error class. It parses received error data and sets error messages and code.
class ErrorResponse {
    
    // MARK: Declaration for string constants to be used to decode and also serialize.
    struct SerializationKeys {
        static let error = "error"
        static let message = "Message"
        static let errorDescription = "error_description"
    }
    
    // MARK: Types of errors.
    struct ErrorCodes {
        static let noInternet = -1
    }
    
    //MARK: Constants
    
    /// Error message key for invlaid grant.
    let kErroMsgInvalidGrant = "invalid_grant"
    
    //MARK: Variables
    
    ///The error message.
    var errorMessage: String = "Unknown error"
    
    ///The error description text.
    var errorDescription: String = "Unknown error"
    
    //The error code.
    var errorCode: NSInteger = 0
    
    
    /**
     Method that set error code and message when ther in no internet connection.
     
     - parameter error: NSError object
     */
    func setNoInternetConnection(){
        errorCode = ErrorCodes.noInternet
        errorMessage = LanguageManager.sharedInstance.translationFor(.PLEASE_CHECK_YOUR_INTERNET_CONNECTION, defaultValue: "Please check your internet connection")
    }
    
    /**
     Method that parses and sets error message
     
     - parameter error: NSError object
     - parameter errorResponse: Data received from Alamofire error response
     */
    func setError(_ error: NSError, errorResponse: DataResponse<Any>)
    {
        setError(error: error, errodCode: (errorResponse.response?.statusCode)!, errorData: errorResponse.data)
    }
    
    /**
     Method that parses and sets error message
     
     - parameter error: Error object
     - parameter errodCode: Error code
     - parameter errorData: Data received from Alamofire error response
     */
    func setError(error: NSError, errodCode: NSInteger, errorData: Data?)
    {
        switch errodCode{
        case 400,401, 403, 404, 409, 412:
            if let data = errorData {
                
                //try to parse received data
                let json = String(data: data, encoding: String.Encoding.utf8)
                let dict = convertToDictionary(text: json!)
                
                if let errorMessage = dict?[SerializationKeys.error] as? String {
                    if errorMessage == kErroMsgInvalidGrant {
                        self.errorMessage = LanguageManager.sharedInstance.translationFor(.UNKNOWN, defaultValue: "Wrong credentials, please try again.")
                    }else{
                        self.errorMessage = errorMessage
                    }
                }else if let errorMessage = dict?[SerializationKeys.message] as? String {
                    self.errorMessage = errorMessage
                }
                
                if let errorDescription = dict?[SerializationKeys.errorDescription] as? String {
                    self.errorDescription = errorDescription
                }
                
                self.errorCode = errodCode
            }
            return
        default:
            self.errorCode = error.code
            self.errorMessage = error.localizedDescription
        }
    
    }
    
    
    /**
     Method that converts string to dictionary
     
     - parameter text: String that should be converted

     - returns: Dictionary is string is succesfully converted, othervise it returns nil.
     */
    func convertToDictionary(text: String) -> [String: Any]? {
        if let data = text.data(using: .utf8) {
            do {
                return try JSONSerialization.jsonObject(with: data, options: []) as? [String: Any]
            } catch {
                print(error.localizedDescription)
            }
        }
        return nil
    }
    
    
}
