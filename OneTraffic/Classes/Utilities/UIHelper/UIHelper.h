//
//  UIHelper.h
//  OneTraffic
//
//  Created by Zesium on 12/21/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "Constant.h"

@interface UIHelper : NSObject

+(UITextField*)createTextFieldWithFrame:(CGRect)frame placeHolder:(NSString*)string  delegate:(id)delegate;
+(UITextView *)createTextViewWithFrame:(CGRect)frame andText:(NSString *)text andIsEditable:(BOOL)editable andIsScrollEnabled:(BOOL)scroll;
+(UIView *)createViewWithFrame:(CGRect)frame;
+(UIImageView *)createImageViewWithFrame:(CGRect)frame andImage:(UIImage *)image;
+(UIButton *)createSystemButtonWithFrame:(CGRect)frame andTitle:(NSString *)title;
+(UITableView *)createTableViewWithFrame:(CGRect)frame;
+(UILabel *)createLabelWithFrame:(CGRect)frame andText:(NSString *)text;
+(UILabel *)createTitleLabelWithFrame:(CGRect)frame andText:(NSString *)text ;
+(UIButton *)createBackButton ;
+(void)customizeTextField:(UITextField *)tf withPlaceholder:(NSString *)placeholder;
+(UIToolbar *)createToolbarWithDoneButtonAndView:(UIView *)view;
+ (CGRect)statusBarFrameViewRect:(UIView*)view;
+(UIAlertController *)showAlertControllerWithTitle:(NSString *)title andText:(NSString *)text;
+(UIImage*) drawText:(NSString*) text
             inImage:(UIImage*) image
             atPoint:(CGPoint) point;
+(UIImage *)drawImageWithSnapshotWithFirstName:(NSString *)name andLastName:(NSString *)lastName;

+(UIImage *)drawInitialsImageWithFirstName:(NSString *)name andLastName:(NSString *)lastName;
+(UIView *)createTableBackgroundView:(UITableView *)tableView andText:(NSString *)text;
+(UIButton *)createCustomButtonWithFrame:(CGRect)frame andTitle:(NSString *)title;
+(NSMutableAttributedString *)setAttributedString:(NSString *)normalString andType:(NSString *)type;
+ (void)showTutorialWithType:(NSString *)type onView:(UIView *)view;
+ (void)showTutorialWithTypeOfPlus:(NSString *)type onView:(UIView *)view;
+ (UIAlertController *)logoutAlert;

@end
