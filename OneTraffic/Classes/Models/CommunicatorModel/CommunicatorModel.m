//
//  CommunicatorModel.m
//  OneTraffic
//
//  Created by Stefan Andric on 3/7/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "CommunicatorModel.h"
#import "LogicHelper.h"

@implementation CommunicatorModel

- (instancetype)initWithDictionary:(NSDictionary *)dict {
    self = [super init];
    if (self) {
        self.deletable = [[dict objectForKey:@"deletable"] boolValue];
        self.groupId = [dict objectForKey:@"groupId"];
        self.groupName = [dict objectForKey:@"groupName"];
        self.groupPictureUrl = [dict objectForKey:@"groupPictureUrl"];
        self.requestStatus = [dict objectForKey:@"requestStatus"];
        self.requestType = [dict objectForKey:@"requestType"];
        
        
        NSArray *firstLastName = [self.groupName componentsSeparatedByCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
        if (firstLastName.count > 0) {
            if (firstLastName.count == 2) {
                self.fromName = firstLastName[0];
                self.fromSurname = firstLastName[1];
            }
            else {
                self.fromName = firstLastName[0];
            }
        }
        
        float since = [[dict objectForKey:@"timestamp"] floatValue];
        NSDate *date = [NSDate dateWithTimeIntervalSince1970:since];
        self.timestamp = [LogicHelper createStringFromDate:date];
        
        self.unreadMessages = [dict objectForKey:@"unreadMessages"];
        
        switch ([self.requestType integerValue]) {
            case 1:
                self.finalTypeOfRequest = NSLocalizedString(@"FriendInvitation", nil);
                
                switch ([self.requestStatus integerValue]) {
                    case 1:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Request", nil);
                        break;
                        
                        case 10:
                        self.typeOfResponseToRequest = NSLocalizedString(@"SentRequest", nil);
                        break;
                        
                    case 11:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Active", nil);
                        break;
                        
                    case 12:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Declined", nil);
                        break;
                        
                    case 13:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Sent", nil);
                        break;
                        
                    case 14:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Active", nil);
                        break;
                        
                    case 15:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Declined", nil);
                        break;
                        
                    default:
                        break;
                }
                
                break;
                
            case 2:
                self.finalTypeOfRequest = NSLocalizedString(@"Message", nil);
                self.typeOfResponseToRequest = NSLocalizedString(@"Active", nil);
                break;
                
            case 3:
                self.finalTypeOfRequest = NSLocalizedString(@"LocationSharing", nil);
                
                
                switch ([self.requestStatus integerValue]) {
                    case 1:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Invited", nil);
                        break;
                        
                    case 2:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Active", nil);
                        break;
                        
                    case 3:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Leaved", nil);
                        break;
                        
                    case 4:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Leaved", nil);
                        break;
                        
                    case 5:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Pending", nil);
                        break;
                        
                    case 6:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Active", nil);
                        break;
                        
                    case 7:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Declined", nil);
                        break;
                        
                    case 8:
                        self.typeOfResponseToRequest = NSLocalizedString(@"Expired", nil);
                        break;
                        
                    default:
                        break;
                }
                break;
                
            default:
                break;
        }
        
    }
    
    return self;
}
@end
