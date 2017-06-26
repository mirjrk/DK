//
//  Constant.h
//  OneTraffic
//
//  Created by Stefan Andric on 12/21/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#ifndef Constant_h
#define Constant_h
#define STATUS_BAR [UIApplication sharedApplication].statusBarFrame.size.height

#define SCREEN_HEIGHT [UIApplication sharedApplication].keyWindow.frame.size.height
#define SCREEN_WIDTH [[UIScreen mainScreen] bounds].size.width

#define LEFT_MARGIN 20
#define SPACE_MARGIN 10

#define DEFAULT_URL @"https://app.onetraffic.com:8443"
#define MAP_URL @"https://app.onetraffic.com/mobile/mobile.json"
#define MAP_URL_DIM @"https://app.onetraffic.com/dim/mobile.json"
//#define MAP_URL_DIM @"https://onetraffic.blob.core.windows.net/mobile/style.json"
#define MAP_FINAL_URL @"http://onetraffic.westeurope.cloudapp.azure.com/maps/mobile/mobile.json"

#define TRAFFIC_URL @"http://trafficotlb.westeurope.cloudapp.azure.com:8080"
#define PROFILE_URL @"http://profileotlb.westeurope.cloudapp.azure.com:8080"
#define REALTIME_URL @"http://realtimetrafficotlb.westeurope.cloudapp.azure.com:8080"


#define UNIQUE_ID @"uniqueID"
#define USER_ID @"userId"
#define USER_NAME @"userName"
#define USER_IMAGE_URL @"userImageUrl"
#define USER_STATUS @"userStatus"
#define TOKEN @"token"
#define ERROR_MESSAGE @"errorMessage"

#define IS_EMPTY_STRING(str) (!(str) || ![(str) isKindOfClass:NSString.class] || [(str) length] == 0 || [(str) isEqualToString:@"(null)"] || [(str) isEqualToString:@"<null>"] || [(str) isKindOfClass:[NSNull class]])

#define NEON_GREEN [UIColor colorWithRed:(114.0/255.0) green:(255.0/255.0) blue:(135.0/255.0) alpha:1.0]
#define DARK_BLUE [UIColor colorWithRed:(0.0/255.0) green:(0.0/255.0) blue:(51.0/255.0) alpha:1.0]
#define DARK_BLUE_TRANSPARENT [UIColor colorWithRed:(0.0/255.0) green:(0.0/255.0) blue:(51.0/255.0) alpha:0.9]
#define SEPARATOR_COLOR [UIColor colorWithRed:(8.0/255.0) green:(26.0/255.0) blue:(148.0/255.0) alpha:1.0]
#define ROUTE_MAGENTA [UIColor colorWithRed:(255.0/255.0) green:(0.0/255.0) blue:(255.0/255.0) alpha:1.0]
#define ROUTE_BLUE [UIColor colorWithRed:(21.0/255.0) green:(140.0/255.0) blue:(255.0/255.0) alpha:1.0]

#define SYSTEM_VERSION_iOS [UIDevice currentDevice].systemVersion

#define kCountryName        @"name"
#define kCountryCallingCode @"dial_code"
#define kCountryCode        @"code"

#define MAXIMAGE_SIZE 300

#define IS_IPAD (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad)
#define IS_IPHONE (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
#define IS_RETINA ([[UIScreen mainScreen] scale] >= 2.0)

#define SCREEN_MAX_LENGTH (MAX(SCREEN_WIDTH, SCREEN_HEIGHT))
#define SCREEN_MIN_LENGTH (MIN(SCREEN_WIDTH, SCREEN_HEIGHT))

#define IS_IPHONE_4_OR_LESS (IS_IPHONE && SCREEN_MAX_LENGTH < 568.0)
#define IS_IPHONE_5 (IS_IPHONE && SCREEN_MAX_LENGTH == 568.0)
#define IS_IPHONE_6 (IS_IPHONE && SCREEN_MAX_LENGTH == 667.0)
#define IS_IPHONE_6P (IS_IPHONE && SCREEN_MAX_LENGTH == 736.0)
#define IS_IPAD_PRO (IS_IPAD && SCREEN_MAX_LENGTH == 1366.0)

#endif /* Constant_h */
