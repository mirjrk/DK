//
//  AddCommentViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 4/5/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol AddComment <NSObject>

-(void)addButtonPressed;

@end

typedef enum {
    
    EventComment,
    ChannelComment
    
} CommentType;

@interface AddCommentViewController : UIViewController <UITextViewDelegate>

@property (nonatomic, assign) NSInteger eventId;
@property (nonatomic, assign) NSInteger channelID;
@property (nonatomic, strong) UITextView *commentTextView;
@property (nonatomic, strong) UILabel *charactersLeftLabel;
@property (nonatomic, strong) UITextField *commentsTextField;
@property (nonatomic, strong) UIButton *addButton;
@property (nonatomic, weak)   id <AddComment> delegate;
@property (nonatomic, strong) UIButton *backButton;
@property (nonatomic, strong) UILabel *centerLabel;
@property (nonatomic, assign) CommentType commentType;

@end
