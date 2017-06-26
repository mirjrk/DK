//
//  MessageObject.h
//  OneTraffic
//
//  Created by Stefan Andric on 9/2/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MessageObject : NSObject

@property (nonatomic, strong) NSString *messageId;

@property (nonatomic, strong) NSString *message;
@property (nonatomic, strong) NSString *created;
@property (nonatomic, strong) NSString *messageType;

@property (nonatomic, strong) NSURL *fromPictureUrl;
@property (nonatomic, strong) NSString *fromName;
@property (nonatomic, strong) NSString *fromSurname;
@property (nonatomic, strong) NSString *fromId;

@property (nonatomic, strong) NSArray *usersInGroup;
@property (nonatomic, strong) NSString *userStatus;
@property (nonatomic, strong) NSString *groupId;

@property (nonatomic, strong) NSString *typeToPresent;

- (instancetype)initWithDictionary:(NSDictionary *)dict;
- (instancetype)initWithDictionaryForRequest:(NSDictionary *)dict;

@end
