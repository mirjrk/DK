//
//  CommunicatorModel.h
//  OneTraffic
//
//  Created by Stefan Andric on 3/7/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CommunicatorModel : NSObject
@property (nonatomic, strong) NSString *groupId;
@property (nonatomic, strong) NSString *groupName;
@property (nonatomic, strong) NSString *groupPictureUrl;
@property (nonatomic, strong) NSString *requestType;
@property (nonatomic, strong) NSString *requestStatus;
@property (nonatomic, strong) NSString *timestamp;
@property (nonatomic, strong) NSString *unreadMessages;

@property (nonatomic, strong) NSString *fromName;
@property (nonatomic, strong) NSString *fromSurname;

//Just to put logic of parsing in model, code will be sufficient on view.
@property (nonatomic, strong) NSString *finalTypeOfRequest;
@property (nonatomic, strong) NSString *typeOfResponseToRequest;


@property BOOL deletable;
//Parsing JSON into Object for easier manipulation of data
- (instancetype)initWithDictionary:(NSDictionary *)dict;
@end
