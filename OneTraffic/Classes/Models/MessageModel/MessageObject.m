//
//  MessageObject.m
//  OneTraffic
//
//  Created by Stefan Andric on 9/2/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "MessageObject.h"
#import "LogicHelper.h"

@implementation MessageObject
-(instancetype)initWithDictionary:(NSDictionary *)dict
{
    self = [super init];
    if (self) {
        self.messageId = @"";
        self.fromId = @"";
        self.message = @"";
        self.created = @"";
        
        if ([dict objectForKey:@"messageId"] != (id)[NSNull null]) {
            self.messageId = [dict objectForKey:@"messageId"];
        }
        
        if ([dict objectForKey:@"messageType"] != (id)[NSNull null]) {
            self.messageType = [dict objectForKey:@"messageType"];
        }
    
        if ([dict objectForKey:@"message"] != (id)[NSNull null]) {
            NSData *dataa = [[dict objectForKey:@"message"] dataUsingEncoding:NSUTF8StringEncoding];            
            
            self.message = [[NSString alloc] initWithData:dataa encoding:NSNonLossyASCIIStringEncoding];
        }
        
        if ([dict objectForKey:@"created"] != (id)[NSNull null]) {
            float since = [[dict objectForKey:@"created"] floatValue];
            NSDate *date = [NSDate dateWithTimeIntervalSince1970:since];
            
            self.created = [LogicHelper createStringFromDate:date];
        }
        
        if ([dict objectForKey:@"from"]) {
            NSDictionary *from = [dict objectForKey:@"from"];
            self.fromPictureUrl = [NSURL URLWithString:[from objectForKey:@"pictureUrl"]];
            self.fromName = [from objectForKey:@"name"];
            self.fromSurname = [from objectForKey:@"surname"];
            self.fromId = [from objectForKey:@"id"];
        }
        
       
        
        
        
    }
    
    return self;
}



- (instancetype)initWithDictionaryForRequest:(NSDictionary *)dict {
    self = [super init];
    if (self) {
        if ([dict objectForKey:@"messageId"] != (id)[NSNull null]) {
            self.messageId = [dict objectForKey:@"messageId"];
        }
        
        if ([dict objectForKey:@"messageType"] != (id)[NSNull null]) {
            self.messageType = [dict objectForKey:@"messageType"];
        }
        
        if ([dict objectForKey:@"from"] != (id)[NSNull null]) {
            NSDictionary *from = [dict objectForKey:@"from"];
            self.fromPictureUrl = [NSURL URLWithString:[from objectForKey:@"pictureUrl"]];
            self.fromName = [from objectForKey:@"name"];
            self.fromSurname = [from objectForKey:@"surname"];
            self.fromId = [from objectForKey:@"id"];
        }
        
        if ([dict objectForKey:@"groupId"] != (id)[NSNull null]) {
            self.groupId = [dict objectForKey:@"groupId"];
        }
        
        if ([dict objectForKey:@"created"] != (id)[NSNull null]) {
            float since = [[dict objectForKey:@"created"] floatValue];
            NSDate *date = [NSDate dateWithTimeIntervalSince1970:since];
            
            self.created = [LogicHelper createStringFromDate:date];
        }
        
        if ([dict objectForKey:@"usersInGroup"] != (id)[NSNull null]) {
            self.usersInGroup = [dict objectForKey:@"usersInGroup"];
        }
        
        
        switch ([self.messageType integerValue]) {
            case 1:
                self.typeToPresent = @"Location sharing request";
                break;
                
            case 2:
                self.typeToPresent = @"Accepted location sharing request";
                break;
                
            case 3:
                self.typeToPresent = @"Leaved location sharing";
                break;
                
            case 4:
                self.typeToPresent = @"Finished location sharing";
                break;
                
            case 5:
                self.typeToPresent = @"Sent location sharing";
                break;
                
            case 6:
                self.typeToPresent = @"Accepted location sharing request";
                break;
                
            case 7:
                self.typeToPresent = @"Declined location sharing";
                break;
                
            case 8:
                self.typeToPresent = @"Invitation expired";
                break;
                
            case 10:
                self.typeToPresent = @"Friend request";
                break;
                
            case 11:
                self.typeToPresent = @"Friend request accepted";
                break;
                
            case 12:
                self.typeToPresent = @"Friend request declined";
                break;
                
            case 13:
                self.typeToPresent = @"Friend request sent";
                break;
                
            case 14:
                self.typeToPresent = @"Friend request accepted";
                break;
                
            case 15:
                self.typeToPresent = @"Friend request declined";
                break;
                
            default:
                self.typeToPresent = @"Unknow request";
                break;
        }
        
        self.message = self.typeToPresent;
      
    }
    
    return self;
}

@end
