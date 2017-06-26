//
//  CommentsViewController.h
//  OneTraffic
//
//  Created by Stefan Andric on 8/26/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BasicViewController.h"
#import "CommentModel.h"
#import "EventTableViewCell.h"
#import "CommentTableViewCell.h"
#import "TPKeyboardAvoidingScrollView.h"

@interface CommentsViewController : BasicViewController <UITableViewDelegate, UITableViewDataSource, UITextViewDelegate, SWTableViewCellDelegate>

@property (nonatomic, strong) TPKeyboardAvoidingScrollView *scrollView;
@property int eventId;
@property NSMutableArray *commentsArray;
@property UITableView *eventCommentsTableView;
@property NSString *eventName;
@property UITextView *commentsTextView;
@property UIButton *sendButton;
@property UILabel *addComment;
@end
