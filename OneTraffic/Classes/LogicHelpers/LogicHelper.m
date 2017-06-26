//
//  LogicHelper.m
//  OneTraffic
//
//  Created by Zesium on 12/28/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import "LogicHelper.h"
#import "UIHelper.h"
#import "Communication.h"
#import "HomeViewController.h"
#import "AppDelegate.h"
#import "SplashViewController.h"

@import AVFoundation;

@implementation LogicHelper

+(NSString *)getCurrentLanguage
{
    NSString * language = [[NSLocale preferredLanguages] objectAtIndex:0];
    NSDictionary* languageDic = [NSLocale componentsFromLocaleIdentifier:language];
    NSString* languageCode = [languageDic objectForKey:@"kCFLocaleLanguageCodeKey"];
    
    
    return languageCode;
}

+ (NSString *) createIdentifierForVendor
{
    if ([[UIDevice currentDevice] respondsToSelector:@selector(identifierForVendor)]) {
        return [[[UIDevice currentDevice] identifierForVendor] UUIDString];
    }
    return @"";
}

+ (NSString *) trimmedString:(NSString*)string {
    return [string stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

+(NSString *)setImageDependingOnId:(int)idEvent
{
    NSString *imageName;
    switch (idEvent) {
        case 0:
            imageName = @"Undefined";
            break;
        case 1:
            imageName = @"Accident";
            break;
        case 2:
            imageName = @"Attention";
            break;
        case 3:
            imageName = @"ClosedRoad";
            break;
        case 4:
            imageName = @"Police";
            break;
        case 5:
            imageName = @"RoadEvent";
            break;
        case 6:
            imageName = @"RoadWork";
            break;
        case 7:
            imageName = @"Traffic";
            break;
        case 8:
            imageName = @"Weather";
            break;
        case 99:
            imageName = @"turnLeft";
            break;
        case 100:
            imageName = @"Home";
            break;
        default:
            break;
    }
    
    return imageName;
}
+(NSString *)setTitleDependingOnId:(int)idEvent
{
    NSString *title;
    switch (idEvent) {
        case 0:
            title = NSLocalizedString(@"Undefined", nil);
            break;
        case 1:
            title = NSLocalizedString(@"Accident", nil);
            break;
        case 2:
            title = NSLocalizedString(@"Hazard", nil);;
            break;
        case 3:
            title = NSLocalizedString(@"ClosedRoad", nil);;
            break;
        case 4:
            title = NSLocalizedString(@"Police", nil);;
            break;
        case 5:
            title = NSLocalizedString(@"RoadEvent", nil);;
            break;
        case 6:
            title = NSLocalizedString(@"RoadWork", nil);;
            break;
        case 7:
            title = NSLocalizedString(@"Traffic", nil);;
            break;
        case 8:
            title = NSLocalizedString(@"Weather", nil);;
            break;
        case 99:
            title = @"Alternative Route";
            break;
        case 100:
            title = @"Destination";
            break;
        default:
            break;
    }
    
    return title;
}

+(NSString *)setImageDependingOnTypeOfIcon:(int)typeOfIcon
{
    NSString *imageName;
    switch (typeOfIcon) {
        case 0:
            imageName = @"DestinationHomeAnnotation";
            break;
        case 1:
            imageName = @"DestinationWorkAnnotation";
            break;
        case 2:
            imageName = @"DestinationSportsAnnotation";
            break;
        case 3:
            imageName = @"DestinationCultureAnnotation";
            break;
        case 4:
            imageName = @"DestinationSchoolAnnotation";
            break;
        case 5:
            imageName = @"DestinationTransportationAnnotation";
            break;
        case 6:
            imageName = @"DestinationCommercialAnnotation";
            break;
        case 7:
            imageName = @"DestinationParkingAnnotation";
            break;
        case 8:
            imageName = @"DestinationUndefinedAnnotation";
            break;
        case 9:
            imageName = @"DestinationFamilyAnnotation";
            break;
            
        case 10:
            imageName = @"DestinationFriendsAnnotation";
            break;
            
        default:
            break;
    }
    
    return imageName;
}



+(NSString *)setImageDependingOnTypeOfIcon:(int)typeOfIcon andColorType:(int)colorType
{
    NSString *imageName;
    NSString *color = [LogicHelper setColorDependingOnColorIdDestination:colorType];
    switch (typeOfIcon) {
        case 0:
            imageName = [NSString stringWithFormat:@"DestinationHomeAnnotation%@", color];
            break;
        case 1:
            imageName = [NSString stringWithFormat:@"DestinationWorkAnnotation%@", color];
            break;
        case 2:
            imageName = [NSString stringWithFormat:@"DestinationSportsAnnotation%@", color];
            break;
        case 3:
            imageName = [NSString stringWithFormat:@"DestinationCultureAnnotation%@", color];
            break;
        case 4:
            imageName = [NSString stringWithFormat:@"DestinationSchoolAnnotation%@", color];
            break;
        case 5:
            imageName = [NSString stringWithFormat:@"DestinationTransportationAnnotation%@", color];
            break;
        case 6:
            imageName = [NSString stringWithFormat:@"DestinationCommercialAnnotation%@", color];
            break;
        case 7:
            imageName = [NSString stringWithFormat:@"DestinationParkingAnnotation%@", color];
            break;
        case 8:
            imageName = [NSString stringWithFormat:@"DestinationUndefinedAnnotation%@", color];
            break;
        case 9:
            imageName = [NSString stringWithFormat:@"DestinationFamilyAnnotation%@", color];
            break;
        case 10:
            imageName = [NSString stringWithFormat:@"DestinationFriendsAnnotation%@", color];
            break;
            
        default:
            break;
    }
    
    return imageName;
    
}





+(NSString *)setLineUpImageDependingOnTypeOfIcon:(int)typeOfIcon andColorType:(int)colorType
{
    NSString *imageName;
    NSString *color = [LogicHelper setColorDependingOnColorIdDestination:colorType];
    switch (typeOfIcon) {
        case 0:
            imageName = [NSString stringWithFormat:@"HomePlaces%@", color];
            break;
        case 1:
            imageName = [NSString stringWithFormat:@"WorkPlaces%@", color];
            break;
        case 2:
            imageName = [NSString stringWithFormat:@"SportsPlaces%@", color];
            break;
        case 3:
            imageName = [NSString stringWithFormat:@"CulturePlaces%@", color];
            break;
        case 4:
            imageName = [NSString stringWithFormat:@"SchoolPlaces%@", color];
            break;
        case 5:
            imageName = [NSString stringWithFormat:@"TransportationPlaces%@", color];
            break;
        case 6:
            imageName = [NSString stringWithFormat:@"CommercialPlaces%@", color];
            break;
        case 7:
            imageName = [NSString stringWithFormat:@"ParkingPlaces%@", color];
            break;
        case 8:
            imageName = [NSString stringWithFormat:@"UndefinedPlaces%@", color];
            break;
            
        case 9:
            imageName = [NSString stringWithFormat:@"FamilyPlaces%@", color];
            break;
            
        case 10:
            imageName = [NSString stringWithFormat:@"FriendsPlaces%@", color];
            break;
            
        default:
            break;
    }
    
    return imageName;
}



+(NSMutableString*)createTopMenuStringFromDestination:(EventModel*)event{
    
    NSMutableString *string=[[NSMutableString alloc] init];
    
    if ([[[NSUserDefaults standardUserDefaults] objectForKey:@"metrics"]isEqualToString:@"km"]) {
        
        if (event.temporalDistance/60<1) {
            [string appendFormat:@"%@",[NSString stringWithFormat:@"%0.f sec", event.temporalDistance]];
        }
        else {
            [string appendFormat:@"%@",[NSString stringWithFormat:@"%0.f min", event.temporalDistance/60]];
        }
        
        
        if (event.distance/1000<1) {
            
            [string appendFormat:@" / %@",[NSString stringWithFormat:@"%0.f m", event.distance] ];
        }
        else {
            [string appendFormat:@" / %@",[NSString stringWithFormat:@"%0.f km", event.distance/1000]];
        }
        
    }
    else {
        
        
        if (event.temporalDistance/60<1) {
            [string appendFormat:@"%@",[NSString stringWithFormat:@"%0.f sec", event.temporalDistance]];
        }
        else {
            [string appendFormat:@"%@",[NSString stringWithFormat:@"%0.f min", event.temporalDistance/60]];
        }
        
        
        if (event.distance/1609<1) {
            [string appendFormat:@" / %@",[NSString stringWithFormat:@"%0.f yd", event.distance*1.09]];
        }
        else {
            [string appendFormat:@" / %@",[NSString stringWithFormat:@"%0.1f mi", event.distance*0.000621371]];
        }
        
    }
    
    [string appendString:[NSString stringWithFormat:@" - %@", event.destinationName]];
    return string;
}

+(NSMutableString*)createTopMenuStringFromDestinationWithoutDest:(EventModel*)event{
    
    NSMutableString *string=[[NSMutableString alloc] init];
    
    if ([[[NSUserDefaults standardUserDefaults] objectForKey:@"metrics"]isEqualToString:@"km"]) {
        
        if (event.temporalDistance/60<1) {
            [string appendFormat:@"%@",[NSString stringWithFormat:@"%0.f sec", event.temporalDistance]];
        }
        else {
            [string appendFormat:@"%@",[NSString stringWithFormat:@"%0.f min", event.temporalDistance/60]];
        }
        
        
        if (event.distance/1000<1) {
            
            [string appendFormat:@" / %@",[NSString stringWithFormat:@"%0.f m", event.distance] ];
        }
        else {
            [string appendFormat:@" / %@",[NSString stringWithFormat:@"%0.f km", event.distance/1000]];
        }
        
    }
    else {
        
        
        if (event.temporalDistance/60<1) {
            [string appendFormat:@"%@",[NSString stringWithFormat:@"%0.f sec", event.temporalDistance]];
        }
        else {
            [string appendFormat:@"%@",[NSString stringWithFormat:@"%0.f min", event.temporalDistance/60]];
        }
        
        
        if (event.distance/1609<1) {
            [string appendFormat:@" / %@",[NSString stringWithFormat:@"%0.f yd", event.distance*1.09]];
        }
        else {
            [string appendFormat:@" / %@",[NSString stringWithFormat:@"%0.1f mi", event.distance*0.000621371]];
        }
        
    }
    
    //    [string appendString:[NSString stringWithFormat:@" - %@", event.destinationName]];
    return string;
}


+(NSString *)getCountryCode
{
    CTTelephonyNetworkInfo *network_Info = [CTTelephonyNetworkInfo new];
    CTCarrier *carrier = network_Info.subscriberCellularProvider;
    NSString *path = [[NSBundle mainBundle] pathForResource:@"DiallingCodes" ofType:@"plist"];
    NSString *lowerCase = [carrier.isoCountryCode lowercaseString];
    NSDictionary *dict = [[NSDictionary alloc] initWithContentsOfFile:path];
    
    NSString *diallString = [dict objectForKey:lowerCase];
    
    if (diallString == (id)[NSNull null] || diallString == nil) {
        diallString = @"47";
    }
    
    return diallString;
}

+(NSArray *)getAllCountries
{
    NSMutableArray *countries = [[NSMutableArray alloc] init];
    NSData *data = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"countries" ofType:@"json"]];
    NSError *localError = nil;
    NSDictionary *parsedObject = [NSJSONSerialization JSONObjectWithData:data options:0 error:&localError];
    for (id something in parsedObject) {
        
        NSDictionary *temp = something;
        [countries addObject:temp];
    }
    if (localError != nil) {
        NSLog(@"%@", [localError userInfo]);
    }
    
    return countries;
}


+(UIImage *)scaleImage:(UIImage *)image
{
    float ratio = image.size.height/image.size.width;
    float imageHeight;
    float imageWidth;
    //Width is bigger than height
    if (ratio < 1) {
        ratio = image.size.width/image.size.height;
        //If width is smaller than 300px, there is no need for scaling
        if (image.size.width <= MAXIMAGE_SIZE) {
            imageWidth = image.size.width;
            imageHeight = image.size.height;
        }
        else {
            imageWidth = MAXIMAGE_SIZE;
            imageHeight = MAXIMAGE_SIZE/ratio;
        }
    }
    //Height is bigger than width
    else {
        //If height is smaller than 300px, there is no need for scaling
        if (image.size.height <= MAXIMAGE_SIZE) {
            imageHeight = image.size.height;
            imageWidth = image.size.width;
        }
        else {
            imageHeight = MAXIMAGE_SIZE;
            imageWidth = MAXIMAGE_SIZE/ratio;
        }
        
    }
    CGSize imageSize = CGSizeMake(imageWidth, imageHeight);
    UIGraphicsBeginImageContextWithOptions(imageSize, NO, 1.0);
    [image drawInRect:CGRectMake(0, 0, imageWidth, imageHeight)];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return newImage;
}


+(NSString *)getJsonFromDictionary:(NSDictionary *)dictionary
{
    NSError *error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dictionary
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    
    
    NSString *jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    return jsonString;
    
}

+(void)saveArrayToDocuments:(NSArray *)array
{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *filePath = [documentsDirectory stringByAppendingPathComponent:@"countryArray"];
    
    [array writeToFile:filePath atomically:YES];
}

+(NSArray *)readArrayFromPath
{
//    NSArray *myArray = [NSArray new];
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *filePath = [documentsDirectory stringByAppendingPathComponent:@"countryArray"];
    
    NSArray *myArray = [NSArray arrayWithContentsOfFile:filePath];
    
    return myArray;
}

+(NSString *)setColorDependingOnColorIdDestination:(NSInteger)colorId
{
    NSString *color;
    switch (colorId) {
        case 0:
            color = @"Red";
            break;
            
        case 1:
            color = @"DarkRed";
            break;
            
        case 2:
            color = @"Green";
            break;
            
        case 3:
            color = @"DarkGreen";
            break;
            
        case 4:
            color = @"Blue";
            break;
            
        case 5:
            color = @"DarkBlue";
            break;
            
        case 6:
            color = @"Orange";
            break;
            
        case 7:
            color = @"DarkOrange";
            break;
            
        default:
            break;
    }
    
    return color;
}

+(BOOL)isObjectEmpty:(id)checkObject
{
    if (checkObject != (id)[NSNull null] && checkObject != nil) {
        return NO;
    }
    
    return YES;
}


+(BOOL)array:(NSArray *)arr containsContactModel:(OTContactsModel *)contact
{
    BOOL containsObject = NO;
    for (OTContactsModel *contactTemp in arr) {
        if ([contact.contactId intValue] == [contactTemp.contactId intValue]) { //Should not return our profile or existing friend
            containsObject = YES;
            break;
        }
        else {
            containsObject = NO;
        }
        
    }
    return containsObject;
}
+(NSString *)createStringFromTimeStamp:(long)timestamp
{
    NSDate *dateDate = [NSDate dateWithTimeIntervalSince1970:timestamp];
    
    NSDateFormatter *dateformate=[[NSDateFormatter alloc]init];
    [dateformate setDateFormat:@"dd.MM.yyyy"];
    NSString *date = [dateformate stringFromDate:dateDate];
    return date;
}

+(BOOL)isValidEmail:(NSString *)checkString
{
    BOOL stricterFilter = NO;
    NSString *stricterFilterString = @"^[A-Z0-9a-z\\._%+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,4}$";
    NSString *laxString = @"^.+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2}[A-Za-z]*$";
    NSString *emailRegex = stricterFilter ? stricterFilterString : laxString;
    NSPredicate *emailTest = [NSPredicate predicateWithFormat:@"SELF MATCHES %@", emailRegex];
    return [emailTest evaluateWithObject:checkString];
}

+(NSAttributedString *)setAttributedStringDependingOnValue:(float)value andType:(NSString *)type
{
    NSAttributedString *attributed = [NSAttributedString new];
    if ([type isEqualToString:@"length"]) {
        if ([[[NSUserDefaults standardUserDefaults] objectForKey:@"metrics"]isEqualToString:@"km"]) {
            if (value/1000<1) {
                attributed = [UIHelper setAttributedString:[NSString stringWithFormat:@"%0.f", value] andType:@"m"];
            }
            else {
                attributed = [UIHelper setAttributedString:[NSString stringWithFormat:@"%0.f", value/1000] andType:@"km"];
            }
            
        }
        else {
            if (value/1609<1) {
                attributed = [UIHelper setAttributedString:[NSString stringWithFormat:@"%0.f", value*1.09] andType:@"yd"];
            }
            else {
                attributed = [UIHelper setAttributedString:[NSString stringWithFormat:@"%0.1f", value*0.000621371] andType:@"mi"];
            }
        }
    }
    else {
        if (value/60<1) {
            attributed = [UIHelper setAttributedString:[NSString stringWithFormat:@"%0.f", value] andType:@"sec"];
        }
        else {
            attributed = [UIHelper setAttributedString:[NSString stringWithFormat:@"%0.f", value/60] andType:@"min"];
        }
    }
    
    
    
    return attributed;
}






+(void)saveImageFromString:(NSString*)urlString withId:(NSString*)idString andName:(NSString *)name andSurname:(NSString *)surname {
    
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *getImagePath = [documentsDirectory stringByAppendingPathComponent:[NSString stringWithFormat:@"user%@.jpg",idString]];// here you jus need to pass image name that you entered when you stored it.
    
    NSData *imgData = [NSData dataWithContentsOfFile:getImagePath];
    
    // Store the data
    NSUserDefaults *defaults = [NSUserDefaults standardUserDefaults];
    
    
    NSMutableDictionary *usersDict;
    if([defaults objectForKey:@"userImageUrls"]){
        
        usersDict=[[NSMutableDictionary alloc] initWithDictionary:[defaults objectForKey:@"userImageUrls"]];
        
    }else{
        
        usersDict= [[NSMutableDictionary alloc] init];
        
    }
    
    
    
    if(urlString == nil || urlString == (id) [NSNull null]) { return; }
    
    
    
    if (imgData == nil){
        
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            
            
            
            dispatch_sync(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0),
                          ^{
                              if (urlString == nil || [urlString isEqualToString:@""] || urlString == (id)[NSNull null]) {
                                  
                                  
                                  NSString *Dir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
                                  
                                  NSString *pngPath = [NSString stringWithFormat:@"%@/user%@.jpg",Dir,idString];// this path if you want save reference path in sqlite
                                  NSData *data1 = [NSData dataWithData:UIImageJPEGRepresentation([UIHelper drawImageWithSnapshotWithFirstName:name andLastName:surname],0.9)];
                                  [data1 writeToFile:pngPath atomically:YES];
                                  
                                  
                                  [usersDict setObject:@"" forKey: idString];
                                  
                                  [defaults setObject: usersDict forKey: @"userImageUrls"];
                                  [defaults synchronize];
                                  
                                  
                              }
                              
                              else {
                                  UIImage *image = [[UIImage alloc] initWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:urlString]]];
                                  NSLog(@"size1%f,%f",image.size.width,image.size.height);
                                  
                                  CGRect rect = CGRectMake(0,0,50,50);
                                  UIGraphicsBeginImageContext( rect.size );
                                  [image drawInRect:rect];
                                  UIImage *picture1 = UIGraphicsGetImageFromCurrentImageContext();
                                  UIGraphicsEndImageContext();
                                  
                                  NSLog(@"sizepicture1 %f,%f",picture1.size.width,picture1.size.height);
                                  
                                  
                                  
                                  NSLog(@"picture1 size %f,%f",picture1.size.width,picture1.size.height);
                                  
                                  
                                  NSString *Dir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
                                  
                                  NSString *pngPath = [NSString stringWithFormat:@"%@/user%@.jpg",Dir,idString];// this path if you want save reference path in sqlite
                                  NSData *data1 = [NSData dataWithData:UIImageJPEGRepresentation(picture1,0.9)];
                                  [data1 writeToFile:pngPath atomically:YES];
                                  
                                  
                                  [usersDict setObject:urlString forKey: idString];
                                  [defaults setObject: usersDict forKey: @"userImageUrls"];
                                  [defaults synchronize];
                              }
                              
                          });
        });
        
        
    }else{
        
        
        if(![urlString isEqualToString:[usersDict objectForKey:idString]] ){
            
            dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
                
                
                
                dispatch_sync(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0),
                              ^{
                                  
                                  NSFileManager *fileManager = [NSFileManager defaultManager];
                                  
                                  NSError *error;
                                  BOOL success = [fileManager removeItemAtPath:getImagePath error:&error];
                                  
                                  if (success) {
                                      
                                      
                                      if (urlString == nil || [urlString isEqualToString:@""] || urlString == (id)[NSNull null]) {
                                          
                                          
                                          NSString *Dir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
                                          
                                          NSString *pngPath = [NSString stringWithFormat:@"%@/user%@.jpg",Dir,idString];// this path if you want save reference path in sqlite
                                          NSData *data1 = [NSData dataWithData:UIImageJPEGRepresentation([UIHelper drawImageWithSnapshotWithFirstName:name andLastName:surname],0.9)];
                                          [data1 writeToFile:pngPath atomically:YES];
                                          
                                          
                                          [usersDict setObject:@"" forKey: idString];
                                          [defaults setObject: usersDict forKey: @"userImageUrls"];
                                          [defaults synchronize];
                                          
                                          
                                      }
                                      
                                      else {
                                          UIImage *image = [[UIImage alloc] initWithData:[NSData dataWithContentsOfURL:[NSURL URLWithString:urlString]]];
                                          NSLog(@"size1%f,%f",image.size.width,image.size.height);
                                          
                                          CGRect rect = CGRectMake(0,0,50,50);
                                          UIGraphicsBeginImageContext( rect.size );
                                          [image drawInRect:rect];
                                          UIImage *picture1 = UIGraphicsGetImageFromCurrentImageContext();
                                          UIGraphicsEndImageContext();
                                          NSLog(@"sizepicture1 %f,%f",picture1.size.width,picture1.size.height);
                                          
                                          
                                          
                                          NSLog(@"picture1 size %f,%f",picture1.size.width,picture1.size.height);
                                          
                                          
                                          NSString *Dir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
                                          
                                          NSString *pngPath = [NSString stringWithFormat:@"%@/user%@.png",Dir,idString];// this path if you want save reference path in sqlite
                                          NSData *data1 = [NSData dataWithData:UIImageJPEGRepresentation(picture1,0.9)];
                                          [data1 writeToFile:pngPath atomically:YES];
                                          
                                          [usersDict setObject:urlString forKey: idString];
                                          [defaults setObject: usersDict forKey: @"userImageUrls"];
                                          [defaults synchronize];
                                      }
                                      
                                  }
                                  else
                                  {
                                      NSLog(@"Could not delete file -:%@ ",[error localizedDescription]);
                                  }
                                  
                                  
                                  
                                  
                              });
            });
            
            
            
        }
        
        
        
    }
    
    
    
    
    
}



+ (void)getSharingLocationImages
{
    
    dispatch_queue_t downloadImagesQueue = dispatch_queue_create("jsonQueue", NULL);
    dispatch_async(downloadImagesQueue, ^{
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT,0),^{
            [Communication getLocationSharingUsers:nil successBlock:^(NSDictionary *response) {
                if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
                    NSInteger myId = [[[NSUserDefaults standardUserDefaults] objectForKey:USER_ID] integerValue];
                    if ([response objectForKey:@"locationSharingGroups"] != (id)[NSNull null]) {
                        NSArray *lsGroups = [response objectForKey:@"locationSharingGroups"];
                        for (NSDictionary *dict in lsGroups) {
                            if ([dict objectForKey:@"usersInGroup"] != (id)[NSNull null]) {
                                NSArray *usersInGroup = [dict objectForKey:@"usersInGroup"];
                                for (NSDictionary *userDict in usersInGroup) {
                                    if ([[userDict objectForKey:@"userId"]integerValue] != myId) {
                                        [LogicHelper saveImageFromString:
                                         [NSString stringWithFormat:@"%@", [userDict objectForKey:@"pictureUrl"]] withId:[NSString stringWithFormat:@"%@", [userDict objectForKey:@"userId"]] andName:[NSString stringWithFormat:@"%@", [userDict objectForKey:@"name"]] andSurname:[NSString stringWithFormat:@"%@", [userDict objectForKey:@"surname"]]];
                                    }
                                }
                                
                            }
                        }
                        
                        
                    }
                    
                }
                
            } errorBlock:^(NSDictionary *error) {
                
            }];
        });
    });
    
}




+(NSString *)createStringFromDate:(NSDate *)date
{
    NSString *stringFromDate = @"";
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    
    NSDate *todayDate = [NSDate date];
    NSDate *createdDate = date;
    ;
    NSInteger daysDifference = [LogicHelper daysBetweenDate:createdDate andDate:todayDate];
    
    if (daysDifference == 0) {
        [formatter setDateFormat:@"HH:mm"];
        stringFromDate = [NSString stringWithFormat:@"%@ %@",NSLocalizedString(@"Today", nil) , [formatter stringFromDate:date]];
    }
    
    else if (daysDifference <= 7){
        if (daysDifference == 1) {
            [formatter setDateFormat:@"HH:mm"];
            stringFromDate = [NSString stringWithFormat:@"%@ %@",NSLocalizedString(@"Yesterday", ) , [formatter stringFromDate:date]];
        }
        else {
            [formatter setDateFormat:@"EEEE HH:mm"];
            stringFromDate = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
        }
        
    }
    else {
//        [formatter setDateFormat:@"dd.MM.yy HH:mm"];
        [formatter setDateFormat:@"dd.MM.yy"];
        stringFromDate = [NSString stringWithFormat:@"%@", [formatter stringFromDate:date]];
    }

    
    
    return stringFromDate;
}

+ (NSInteger)daysBetweenDate:(NSDate*)fromDateTime andDate:(NSDate*)toDateTime
{
    NSDate *fromDate;
    NSDate *toDate;
    
    NSCalendar *calendar = [NSCalendar currentCalendar];
    
    [calendar rangeOfUnit:NSCalendarUnitDay startDate:&fromDate
                 interval:NULL forDate:fromDateTime];
    [calendar rangeOfUnit:NSCalendarUnitDay startDate:&toDate
                 interval:NULL forDate:toDateTime];
    
    NSDateComponents *difference = [calendar components:NSCalendarUnitDay
                                               fromDate:fromDate toDate:toDate options:0];
    
    return [difference day];
}


+(void)playSoundNamed:(NSString *)name
{
    SystemSoundID audioEffect;
    NSString *path = [[NSBundle mainBundle] pathForResource:name ofType:@"wav"];
    
    if ([[NSFileManager defaultManager] fileExistsAtPath : path]) {
        NSURL *pathURL = [NSURL fileURLWithPath: path];
        AudioServicesCreateSystemSoundID((__bridge CFURLRef) pathURL, &audioEffect);
        AudioServicesPlaySystemSound(audioEffect);
    }
    else {
        NSLog(@"error, file not found: %@", path);
    }
}



+ (void)logoutUser {
    
    UINavigationController *navControllerTop = (UINavigationController *)[[[UIApplication sharedApplication] keyWindow] rootViewController];
    
    NSArray * controllerArray = [navControllerTop viewControllers];
    
    for (UIViewController *controller in controllerArray){
        if ([controller isKindOfClass:[HomeViewController class]]) {
            HomeViewController *controller1 = (HomeViewController *)controller;
            [controller1.locationManager stopUpdatingLocation];
            controller1.locationManager.delegate = nil;
            controller1.locationManager = nil;
            [controller1.timer invalidate];
            controller1.timer = nil;
        }
    }
    
    NSLog(@"%@", [[[NSUserDefaults standardUserDefaults] dictionaryRepresentation] allKeys]);
    
    AppDelegate *appDelegate = (AppDelegate *)[UIApplication sharedApplication].delegate;
    
    [appDelegate.window.rootViewController.view removeFromSuperview];
    
    SplashViewController *vc = [[SplashViewController alloc]init]; //was registration
    UINavigationController *navController= [[UINavigationController alloc] initWithRootViewController:vc];
    
    appDelegate.window.rootViewController = navController;
    [appDelegate.window makeKeyAndVisible];
    
    NSUserDefaults * defs = [NSUserDefaults standardUserDefaults];
    NSDictionary *dict = [defs dictionaryRepresentation];
    NSArray *array=@[@"metrics", @"token", @"userName", @"userStatus", @"userId", @"trackingMode", @"notFirstStart", @"traffic", @"timeFormat", @"locationSharing", @"alertSound", @"personalGraph"];
    
    for (id key in dict) {
        if([array containsObject:key]){
            if ([defs objectForKey:key] != (id)[NSNull null] && [defs objectForKey:key] != nil) {
                [defs removeObjectForKey:key];
            }
        }
    }
    [defs synchronize];
}


+ (NSDictionary *)makeDictionaryForFeatureWithCoords:(NSArray *)coordinates {
    NSMutableDictionary *feature = [NSMutableDictionary new];
    [feature setObject:@{} forKey:@"properties"];
    [feature setObject:@"Feature" forKey:@"type"];
    NSDictionary *geometry = @{@"coordinates":coordinates,
                               @"type":@"LineString"};
    [feature setObject:geometry forKey:@"geometry"];
    return feature;
}

+(NSArray *)makeLocalizableArrayOfKeys:(NSArray *)keys {
    NSMutableArray *localizables = [NSMutableArray new];
    
    for (NSString *key in keys) {
        [localizables addObject:NSLocalizedString(key, key)];
    }
    
    return localizables;
}


@end
