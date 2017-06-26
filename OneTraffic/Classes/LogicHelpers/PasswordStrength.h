//
//  PasswordStrength.h
//  OneTraffic
//
//  Created by Stefan Andric on 8/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface PasswordStrength : NSObject
@property (nonatomic, strong) NSString *password;
+(BOOL)passwordSatisfyAllConditions:(NSString *)password;
@end
