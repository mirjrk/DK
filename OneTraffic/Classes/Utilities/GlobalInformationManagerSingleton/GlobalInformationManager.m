//
//  GlobalInformationManager.m
//  OneTraffic
//
//  Created by Stefan Andric on 5/17/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import "GlobalInformationManager.h"

@implementation GlobalInformationManager
+ (GlobalInformationManager *)sharedInstance
{
    static dispatch_once_t predicate = 0;
    __strong static id sharedObject = nil;
    dispatch_once(&predicate, ^{
        sharedObject = [[self alloc] init];
    });
    return sharedObject;
}

- (id)init {
    if (self = [super init]) {
        self.personalGraph = [NSDictionary new];
        self.showedEventIds = [NSMutableArray new];
    }
    return self;
}
@end
