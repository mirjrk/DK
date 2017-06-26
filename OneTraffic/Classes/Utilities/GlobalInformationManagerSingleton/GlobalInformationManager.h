//
//  GlobalInformationManager.h
//  OneTraffic
//
//  Created by Stefan Andric on 5/17/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GlobalInformationManager : NSObject
@property (nonatomic, strong) NSDictionary *personalGraph;
@property (nonatomic, strong) NSMutableArray *showedEventIds;
+ (GlobalInformationManager *)sharedInstance;
@end
