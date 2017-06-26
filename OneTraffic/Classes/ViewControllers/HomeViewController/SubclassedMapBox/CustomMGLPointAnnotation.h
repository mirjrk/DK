//
//  CustomMGLPointAnnotation.h
//  OneTraffic
//
//  Created by Stefan Andric on 2/3/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <Mapbox/Mapbox.h>

@interface CustomMGLPointAnnotation : MGLPointAnnotation

@property int type;
@property int cause;
@property (nonatomic,retain)NSString *sponsoredImageUrl;
@property int typeOfIcon;
@property int eventId;
@property int magnitude;
@property (nonatomic,retain) NSString *userId;
@property int typeOfColor;
@property float passEventIn;
@property float eventStartsIn;
@property BOOL isDrivenThrough;
@end
