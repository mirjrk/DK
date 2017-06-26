//
//  CommunicationRequest.swift
//  BetX
//
//  Created by Zesium on 5/19/17.
//  Copyright © 2017 Nemanja. All rights reserved.
//

import UIKit
import Alamofire


class CommunicationRequest:NSObject {
    
    /// URL
    var url:URL?
    
    /// Request-response method between a client and server
    var method: HTTPMethod?
    
    /// Request body parameters
    var parameters: [String:Any]?
    
    /// Type used to define how a set of parameters are applied to a URLRequest.
    var encoding: ParameterEncoding?
    
    /// Request header parameters
    var headers: HTTPHeaders?
    
    /// Success response
    var successBlock : ((Any) -> ())!
    
    /// Error response
    var failureBlock : ((ErrorResponse?) -> ())!
    
    ///Specific type of Request that manages an underlying URLSessionDataTask.
    var dataRequest: DataRequest?
    
    init(url:URL,method: HTTPMethod, parameters: [String:Any]?, encoding: ParameterEncoding, headers: HTTPHeaders?, successBlock : ((Any) -> ())!, failureBlock : ((ErrorResponse?) -> ())!){
        self.successBlock = successBlock
        self.failureBlock = failureBlock
        self.url = url
        self.method = method
        self.encoding = encoding
        self.parameters = parameters
        self.headers = headers
        
    }
    
    /**
    
    cancelRequest method purpose is to cancel current request
     
    */
    
    func cancelRequest(){
        
        if let unwrappedDataRequest = dataRequest {
            unwrappedDataRequest.cancel()
        }
    }
    
}
