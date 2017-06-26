//
//  LogicHelper.h
//  OneTraffic
//
//  Created by Zesium on 12/28/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "EventModel.h"
#import "Constant.h"
#import "OTContactsModel.h"

@import CoreTelephony;

@interface LogicHelper : NSObject
+(NSString *) getCurrentLanguage;
+ (NSString *) createIdentifierForVendor;
+ (NSString *) trimmedString:(NSString*)string;
+(NSString *)setImageDependingOnId:(int)idEvent;
+(NSString *)setTitleDependingOnId:(int)idEvent;
+(NSString *)setImageDependingOnTypeOfIcon:(int)typeOfIcon;
+(NSString *)setLineUpImageDependingOnTypeOfIcon:(int)typeOfIcon andColorType:(int)colorType;
+(NSMutableString*)createTopMenuStringFromDestination:(EventModel*)event;

+(NSString *)getCountryCode;
+(NSArray *)getAllCountries;
+(UIImage *)scaleImage:(UIImage *)image;
+(NSString *)getJsonFromDictionary:(NSDictionary *)dictionary;
+(void)saveArrayToDocuments:(NSArray *)array;
+(NSArray *)readArrayFromPath;
+(NSString *)setColorDependingOnColorIdDestination:(NSInteger)colorId;
+(BOOL)isObjectEmpty:(id)checkObject;
+(BOOL)array:(NSArray *)arr containsContactModel:(OTContactsModel *)contact;
+(NSString *)createStringFromTimeStamp:(long)timestamp;
+(NSString *)setImageDependingOnTypeOfIcon:(int)typeOfIcon andColorType:(int)colorType;
+(BOOL)isValidEmail:(NSString *)checkString;
+(NSAttributedString *)setAttributedStringDependingOnValue:(float)value andType:(NSString *)type;
+(void)saveImageFromString:(NSString*)urlString withId:(NSString*)idString andName:(NSString *)name andSurname:(NSString *)surname;
+ (void)getSharingLocationImages;
+(NSString *)createStringFromDate:(NSDate *)date;
+(NSMutableString*)createTopMenuStringFromDestinationWithoutDest:(EventModel*)event;
+(void)playSoundNamed:(NSString *)name;
+ (void)logoutUser;

+ (NSDictionary *)makeDictionaryForFeatureWithCoords:(NSArray *)coordinates;

+(NSArray *)makeLocalizableArrayOfKeys:(NSArray *)keys;
@end
