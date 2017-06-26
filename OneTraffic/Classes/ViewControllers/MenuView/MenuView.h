//
//  MenuView.h
//  OneTraffic
//
//  Created by Stefan Andric on 1/13/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <UIKit/UIGestureRecognizerSubclass.h>
#import "Communication.h"


@protocol MenuDelegateA <NSObject>;
-(void)selectedIndex:(NSInteger)index ;
-(void)closeMenu ;

@end

@interface MenuView : UIView <UITableViewDataSource, UITableViewDelegate,UIGestureRecognizerDelegate>
{
    CGFloat _centerX;
    CGFloat firstX;
    CGFloat  firstY ;
    
}
@property (nonatomic,retain)UIView *clearView;
@property (nonatomic,retain)UIButton *closeButton;
@property (nonatomic,retain)UIView *coloredView;
@property (nonatomic,retain)UIImageView *userImageView;
@property (nonatomic,retain)UILabel *nameLabel;
@property (nonatomic,retain)NSArray *dataArray;
@property (nonatomic,retain)UITableView *menuTableView;
@property (nonatomic,retain) UIPanGestureRecognizer *panGestureRecognizer;
@property (nonatomic, weak) id <MenuDelegateA> delegate;

@end
