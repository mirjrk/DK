//
//  UIHelper.m
//  OneTraffic
//
//  Created by Zesium on 12/21/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import "UIHelper.h"
#import "TutorialView.h"
#import "LogicHelper.h"

@import UIKit;

@implementation UIHelper

+(UITextField*)createTextFieldWithFrame:(CGRect)frame placeHolder:(NSString*)string  delegate:(id)delegate{
    
    UITextField *tf = [[UITextField alloc]initWithFrame:frame];
    tf.borderStyle = UITextBorderStyleNone;
    
    tf.delegate=delegate;
    tf.placeholder=string;
    tf.autocorrectionType = UITextAutocorrectionTypeNo;
    tf.autocapitalizationType=NO;
    tf.textAlignment = NSTextAlignmentCenter;
    tf.font=[UIFont systemFontOfSize:18];
    tf.textColor=[UIColor blackColor];
    
    return tf;
}

+(UITextView *)createTextViewWithFrame:(CGRect)frame andText:(NSString *)text andIsEditable:(BOOL)editable andIsScrollEnabled:(BOOL)scroll
{
    UITextView *textView = [[UITextView alloc]initWithFrame:frame];
    [textView setText:text];
    textView.editable = editable;
    textView.scrollEnabled = scroll;
    
    return textView;
}

+(UILabel *)createTitleLabelWithFrame:(CGRect)frame andText:(NSString *)text {
    UILabel *label = [[UILabel alloc]initWithFrame:frame];
    label.textColor=[UIColor whiteColor];
    label.font= [UIFont systemFontOfSize:22];
    [label setText:text];
    label.textAlignment=NSTextAlignmentCenter;
    
    return label;
}

+(UIButton *)createBackButton {
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom ];
    [button setImage:[UIImage imageNamed:@"back.png" ] forState:UIControlStateNormal];
    [button setFrame: CGRectMake(0, 30, 50, 50)];
    
    return button;
}


+(UIView *)createViewWithFrame:(CGRect)frame
{
    UIView *view = [[UIView alloc]initWithFrame:frame];
    
    return view;
}

+(UIImageView *)createImageViewWithFrame:(CGRect)frame andImage:(UIImage *)image
{
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:frame];
    [imageView setImage:image];
    return imageView;
}

+(UIButton *)createSystemButtonWithFrame:(CGRect)frame andTitle:(NSString *)title
{
    UIButton *button = [UIButton buttonWithType:UIButtonTypeSystem];
    [button setFrame:frame];
    [button setTitle:title forState:UIControlStateNormal];
    
    return button;
}

+(UIButton *)createCustomButtonWithFrame:(CGRect)frame andTitle:(NSString *)title
{
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setFrame:frame];
    [button setTitle:title forState:UIControlStateNormal];
    
    return button;
}



+(UIAlertController *)showAlertControllerWithTitle:(NSString *)title andText:(NSString *)text
{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:text preferredStyle:UIAlertControllerStyleAlert];
    [alert addAction:[UIAlertAction actionWithTitle:NSLocalizedString(@"Ok", nil) style:UIAlertActionStyleDefault handler:nil]];
    return alert;
}


+(UITableView *)createTableViewWithFrame:(CGRect)frame
{
    UITableView *tableView = [[UITableView alloc]init];
    [tableView setFrame:frame];
    return tableView;
}

+(NSString *)getCurrentLanguage
{
    NSString *language = [[NSLocale preferredLanguages] objectAtIndex:0];
    NSDictionary *languageDict = [NSLocale componentsFromLocaleIdentifier:language];
    NSString *languageCode = [languageDict objectForKey:@"kCFLocaleLanguageCodeKey"];
    
    return languageCode;
}

+(UILabel *)createLabelWithFrame:(CGRect)frame andText:(NSString *)text
{
    UILabel *label = [[UILabel alloc]init];
    [label setFrame:frame];
    label.text = text;
    label.textAlignment = NSTextAlignmentCenter;
    return label;
}

+(void)customizeTextField:(UITextField *)tf withPlaceholder:(NSString *)placeholder
{
    tf.layer.cornerRadius = tf.frame.size.height/2;
    tf.layer.masksToBounds = YES;
    tf.layer.borderColor = NEON_GREEN.CGColor;
    tf.layer.borderWidth = 1.5f;
    UIColor *color = [UIColor whiteColor];
    tf.attributedPlaceholder = [[NSAttributedString alloc] initWithString:placeholder attributes:@{NSForegroundColorAttributeName: color}];
    [tf setTextColor:[UIColor whiteColor]];
}

+(UIToolbar *)createToolbarWithDoneButtonAndView:(UIView *)view
{
    UIToolbar* numberToolbar = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, view.frame.size.width, 50)];
    
    
    //            numberToolbar.barStyle = UIBarStyleDefault;
    numberToolbar.translucent = NO;
    numberToolbar.barTintColor = NEON_GREEN;
    [numberToolbar sizeToFit];
    
    
    return numberToolbar;
}
+ (CGRect)statusBarFrameViewRect:(UIView*)view
{
    CGRect statusBarFrame = [[UIApplication sharedApplication] statusBarFrame];
    
    CGRect statusBarWindowRect = [view.window convertRect:statusBarFrame fromWindow: nil];
    
    CGRect statusBarViewRect = [view convertRect:statusBarWindowRect fromView: nil];
    
    return statusBarViewRect;
}


+(UIImage*) drawText:(NSString*) text
             inImage:(UIImage*) image
             atPoint:(CGPoint) point
{
    
    UIGraphicsBeginImageContext(image.size);
    [image drawInRect:CGRectMake(0,0,image.size.width,image.size.height)];
    CGRect rect = CGRectMake(point.x, point.y, image.size.width, image.size.height);
    [[UIColor whiteColor] set];
    [text drawInRect:rect withAttributes: @{
                                            NSFontAttributeName: [UIFont fontWithName:@"HelveticaNeue-Bold" size:25],
                                            NSForegroundColorAttributeName: DARK_BLUE
                                            }];
    //    [text drawInRect:CGRectIntegral(rect) withFont:font];
    UIImage *newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return newImage;
}

+(UIImage *)drawInitialsImageWithFirstName:(NSString *)name andLastName:(NSString *)lastName
{
    NSString *firstLetterName = [name substringToIndex:1];
    NSString *firstLetterSurname = [lastName substringToIndex:1];
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
    view.backgroundColor = NEON_GREEN;
    UILabel *lbl = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
    lbl.textColor = DARK_BLUE;
    lbl.text = [NSString stringWithFormat:@"%@%@", firstLetterName, firstLetterSurname];
    lbl.textAlignment = NSTextAlignmentCenter;
    lbl.font = [UIFont systemFontOfSize:25.0f];
    [view addSubview:lbl];
    
    UIGraphicsBeginImageContextWithOptions(view.bounds.size, view.opaque, 0.0);
    [view.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    NSLog(@"FINISHED");
    return image;
    
}

+(UIImage *)drawImageWithSnapshotWithFirstName:(NSString *)name andLastName:(NSString *)lastName
{
    NSString *firstLetterName = @"";
    NSString *firstLetterSurname = @"";
    
    if (name.length > 0 && name != (id)[NSNull null]) {
        firstLetterName = [name substringToIndex:1];
    }
    
    if (lastName != (id)[NSNull null] && lastName.length > 0) {
        firstLetterSurname = [lastName substringToIndex:1];
    }
    
    
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
    view.backgroundColor = NEON_GREEN;
    UILabel *lbl = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 50, 50)];
    lbl.textColor = DARK_BLUE;
    lbl.text = [NSString stringWithFormat:@"%@%@", firstLetterName, firstLetterSurname];
    lbl.textAlignment = NSTextAlignmentCenter;
    lbl.font = [UIFont systemFontOfSize:25.0f];
    [view addSubview:lbl];
    
    
    
    UIGraphicsBeginImageContextWithOptions(view.bounds.size, NO, 0.0f);
    //    [view drawViewHierarchyInRect:view.bounds afterScreenUpdates:YES];
    [view.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage * snapshotImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return snapshotImage;
}


+(UIView *)createTableBackgroundView:(UITableView *)tableView andText:(NSString *)text
{
    UIView *backgroundView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, tableView.frame.size.height)];
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, backgroundView.frame.size.width, backgroundView.frame.size.height)];
    label.textAlignment = NSTextAlignmentCenter;
    label.textColor = [UIColor whiteColor];
    label.text = text;
    label.numberOfLines = 0;
    [backgroundView addSubview:label];
    
    return backgroundView;
}


+(NSMutableAttributedString *)setAttributedString:(NSString *)normalString andType:(NSString *)type
{
    NSDictionary *attributes = @{NSForegroundColorAttributeName: NEON_GREEN};
    NSString *typeForAttribute = [NSString stringWithFormat:@" %@", [type uppercaseString]];
    NSMutableAttributedString *string = [[NSMutableAttributedString alloc] initWithString:normalString attributes:attributes];
    NSDictionary *attributesWhite = @{NSForegroundColorAttributeName: [UIColor whiteColor]};
    NSAttributedString *typeAtt = [[NSAttributedString alloc] initWithString:typeForAttribute attributes:attributesWhite];
    
    [string appendAttributedString:typeAtt];
    
    return string;
}


+ (void)showTutorialWithType:(NSString *)type onView:(UIView *)view {
    NSString *tutorial = type;
    if ([[NSUserDefaults standardUserDefaults] boolForKey:tutorial] == NO) {
        TutorialView *viewT = [[TutorialView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT) andType:tutorial];
        viewT.layer.zPosition = 1;
        [view addSubview:viewT];
    }
}

+ (void)showTutorialWithTypeOfPlus:(NSString *)type onView:(UIView *)view {
    NSString *tutorial = type;
    if ([[NSUserDefaults standardUserDefaults] boolForKey:tutorial] == NO) {
        TutorialView *viewT = [[TutorialView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, view.frame.size.height-80) andType:tutorial];
        viewT.layer.zPosition = 1;
        [view addSubview:viewT];
    }
}

+ (UIAlertController *)logoutAlert {
    UIAlertController *errorController = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"Error", nil) message:NSLocalizedString(@"SeesionNotValid", nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *okAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Ok", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [LogicHelper logoutUser];
    }];
    
    [errorController addAction:okAction];
    
    return errorController;
}

@end
