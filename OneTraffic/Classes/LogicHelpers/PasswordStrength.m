//
//  PasswordStrength.m
//  OneTraffic
//
//  Created by Stefan Andric on 8/12/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "PasswordStrength.h"

@implementation PasswordStrength


#pragma mark - count of characters

-(BOOL)passwordHaveNumber
{
    NSCharacterSet *numbers = [NSCharacterSet characterSetWithCharactersInString:@"0123456789"];
    if ([self.password rangeOfCharacterFromSet:numbers].location != NSNotFound) {
        return YES;
    }
    return NO;
}

-(BOOL)passwordHaveSpecialCharacter
{
   NSCharacterSet *characters = [NSCharacterSet characterSetWithCharactersInString:@")!@#$%^&*()"];
    if ([self.password rangeOfCharacterFromSet:characters].location != NSNotFound) {
        return YES;
    }
    return NO;
}

-(BOOL)passwordHaveUpperCaseCharacter
{
    NSCharacterSet *characters = [NSCharacterSet uppercaseLetterCharacterSet];
    if ([self.password rangeOfCharacterFromSet:characters].location != NSNotFound) {
        return YES;
    }
    return NO;
}

-(BOOL)passwordHaveLowerCaseCharacter
{
    NSCharacterSet *characters = [NSCharacterSet lowercaseLetterCharacterSet];
    if ([self.password rangeOfCharacterFromSet:characters].location != NSNotFound) {
        return YES;
    }
    return NO;
}

-(BOOL)passwordIsStrong
{
    if ([self passwordHaveNumber] == YES && [self passwordHaveLowerCaseCharacter] == YES && [self passwordHaveUpperCaseCharacter] == YES) {
        return YES;
    }
    return NO;
}

#pragma mark - public

+(BOOL)passwordSatisfyAllConditions:(NSString *)password
{
    PasswordStrength *strength = [PasswordStrength new];
    strength.password = password;
    return [strength passwordIsStrong];
}

@end
