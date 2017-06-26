//
//  CommentsViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 8/26/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "CommentsViewController.h"
#import "Communication.h"
#import "Constant.h"
#import "UIImageView+AFNetworking.h"
#import "UIHelper.h"

@interface CommentsViewController ()

@end

@implementation CommentsViewController

-(void)loadView
{
    self.view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view.backgroundColor = DARK_BLUE;
    self.automaticallyAdjustsScrollViewInsets = NO;
    
}

-(void)createInterface
{
    self.scrollView = [[TPKeyboardAvoidingScrollView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    [self.view insertSubview:self.scrollView belowSubview:self.headerView];
    
    self.eventCommentsTableView = [UIHelper createTableViewWithFrame:CGRectMake(0, CGRectGetMaxY(self.headerView.frame)+10, SCREEN_WIDTH, SCREEN_HEIGHT-CGRectGetMaxY(self.headerView.frame)-110)];
    self.eventCommentsTableView.backgroundColor = [UIColor clearColor];
    self.eventCommentsTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    [self.eventCommentsTableView registerNib:[UINib nibWithNibName:@"CommentTableViewCell" bundle:nil] forCellReuseIdentifier:@"commentsChatCell"];
    self.eventCommentsTableView.estimatedRowHeight = 300;
    self.eventCommentsTableView.rowHeight = UITableViewAutomaticDimension;
    
    
    UIView *downView = [UIView new];
    downView.frame = CGRectMake(0, CGRectGetMaxY(self.eventCommentsTableView.frame), SCREEN_WIDTH, 100);
    self.commentsTextView = [UIHelper createTextViewWithFrame:CGRectMake(10, 10, SCREEN_WIDTH-10-90, 80) andText:@"" andIsEditable:YES andIsScrollEnabled:NO];
    self.commentsTextView.backgroundColor = [UIColor whiteColor];
    [self.commentsTextView setTextColor:[UIColor blackColor]];
    self.commentsTextView.font= [UIFont systemFontOfSize:16];
    self.commentsTextView.autocorrectionType = UITextAutocorrectionTypeNo;
    self.commentsTextView.keyboardAppearance = UIKeyboardAppearanceDark;
    self.commentsTextView.layer.cornerRadius = 5.0f;
    self.commentsTextView.delegate = self;
    
    
    UIView *neonBorder = [UIView new];
    neonBorder.backgroundColor = NEON_GREEN;
    neonBorder.frame = CGRectMake(0, 0, SCREEN_WIDTH, 1);
    
    self.sendButton = [UIHelper createSystemButtonWithFrame:CGRectMake(SCREEN_WIDTH-80, downView.frame.size.height-40, 70, 30) andTitle:@"Send"];
    [self.sendButton setTitleColor:NEON_GREEN forState:UIControlStateNormal];
    [self.sendButton addTarget:self action:@selector(sendButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
    
    self.addComment = [[UILabel alloc] initWithFrame:CGRectMake(10, self.commentsTextView.frame.size.height-40, 200, 40)];
    self.addComment.text = NSLocalizedString(@"AddComment", nil);
    self.addComment.textColor = [UIColor whiteColor];
    self.addComment.font = [UIFont systemFontOfSize:12.0f];
    [self.commentsTextView addSubview:self.addComment];
    
    
    [self.scrollView addSubview:self.eventCommentsTableView];
    [self.scrollView addSubview:downView];
    [downView addSubview:self.commentsTextView];
    [downView addSubview:neonBorder];
    [downView addSubview:self.sendButton];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    [self createInterface];
    [self setTableViewDelegates];
    [self getNewComments];
    self.headerLabel.text = self.eventName;
    // Do any additional setup after loading the view.
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)getNewComments
{
    //Needs to be empty when reloading on approval
    [Communication getEventComments:self.eventId
                        succesBlock:^(NSDictionary *response) {
                            
                            if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
                                if ([response objectForKey:@"comments"] != (id)[NSNull null]) {
                                    self.commentsArray = [NSMutableArray new];
                                    NSArray *tempArray = [response objectForKey:@"comments"];
                                    for (NSDictionary *dict in tempArray) {
                                        CommentModel *model = [[CommentModel alloc] initWithDictionary:dict];
                                        [self.commentsArray addObject:model];
                                    }
                                    if (self.commentsArray.count < 1) {
                                        [UIHelper createTableBackgroundView:self.eventCommentsTableView andText:@"No comments for this event."];
                                    }
                                    [self.eventCommentsTableView reloadData];
                                }
                            }
                        } errorBlock:^(NSDictionary *error) {
                            
                        }];
}

-(void)setTableViewDelegates
{
    self.eventCommentsTableView.delegate = self;
    self.eventCommentsTableView.dataSource = self;
}



-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.commentsArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    CommentModel *model = self.commentsArray[indexPath.row];
    CommentTableViewCell *cell = (CommentTableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"commentsChatCell"];
    cell.leftUtilityButtons = [self leftButtons];
    cell.delegate = self;
    cell.usersName.textColor = NEON_GREEN;
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    cell.userImageView.layer.cornerRadius = 25;
    cell.userImageView.layer.masksToBounds = YES;
    if (model.pictureUrl != (id)[NSNull null]) {
        [cell.userImageView setImageWithURL:[NSURL URLWithString:model.pictureUrl]];
    }
    else {
        [cell.userImageView setImage:[UIHelper drawImageWithSnapshotWithFirstName:model.name andLastName:@""]];
    }
    cell.usersName.text = model.name;
    cell.commentText.text = model.comment;
    cell.likeLabel.text = [NSString stringWithFormat:@"Like (%@)", model.appropriate];
    cell.dislikeLabel.text = [NSString stringWithFormat:@"Dislike (%@)", model.inappropriate];
    cell.likeLabel.textColor = [UIColor whiteColor];
    cell.dislikeLabel.textColor = [UIColor whiteColor];
    cell.commentText.textColor = [UIColor whiteColor];
    
    
    cell.likeButton.tag = indexPath.row;
    [cell.likeButton addTarget:self action:@selector(likeButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    
    cell.dislikeButton.tag = indexPath.row;
    [cell.dislikeButton addTarget:self action:@selector(dislikeButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    
    
    if (model.alreadySetApproval == YES){
        [cell.likeButton setEnabled:NO];
        [cell.dislikeButton setEnabled:NO];
    }
    else {
        [cell.likeButton setEnabled:YES];
        [cell.dislikeButton setEnabled:YES];
    }
    
    cell.timeStampLabel.text = model.dateOfComment;
    
    return cell;
}


- (void)swipeableTableViewCell:(SWTableViewCell *)cell didTriggerLeftUtilityButtonWithIndex:(NSInteger)index {
    [self dislikeTapped:index];
}

- (NSArray *)leftButtons {
    NSMutableArray *leftButtons = [NSMutableArray new];
    [leftButtons sw_addUtilityButtonWithColor:[UIColor redColor] title:@"Report"];
    
    return leftButtons;
}

- (BOOL)swipeableTableViewCellShouldHideUtilityButtonsOnSwipe:(SWTableViewCell *)cell {
    return YES;
}

-(void)likeButtonTapped:(UIButton *)sender
{
    CommentModel *model = self.commentsArray[sender.tag];
    NSDictionary *settingApproval = @{@"commentId":model.commentId,
                                      @"typeOfUser":@2,
                                      @"approvalType":@1
                                      };
    [Communication setApprovalComment:settingApproval successBlock:^(NSDictionary *response) {
        NSLog(@"%@", response);
        [self getNewComments];
    } errorBlock:^(NSDictionary *error) {
        NSLog(@"%@", error);
    }];
    
}

-(void)dislikeButtonTapped:(UIButton *)sender
{
    CommentModel *model = self.commentsArray[sender.tag];
    
    
    NSDictionary *settingApproval = @{@"commentId":model.commentId,
                                      @"typeOfUser":@2,
                                      @"approvalType":@-1
                                      };
    
    
    [Communication setApprovalComment:settingApproval successBlock:^(NSDictionary *response) {
        NSLog(@"%@", response);
        
        [self getNewComments];
    } errorBlock:^(NSDictionary *error) {
        
        [self showSpinner:NO];
    }];
}

-(void)dislikeTapped:(NSInteger)tag
{
    CommentModel *model = self.commentsArray[tag];
    [self showSpinner:YES];
    NSDictionary *settingApproval = @{@"commentId":model.commentId,
                                      @"typeOfUser":@2,
                                      @"approvalType":@-1
                                      };
    [Communication setApprovalComment:settingApproval successBlock:^(NSDictionary *response) {
        NSLog(@"%@", response);
        [self showSpinner:NO];
        [self getNewComments];
    } errorBlock:^(NSDictionary *error) {
        [self showSpinner:NO];
        NSLog(@"%@", error);
    }];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
    if([text isEqualToString:@"\n"]) {
        [textView resignFirstResponder];
        return NO;
    }
    
    return YES;
}

-(BOOL)textViewShouldBeginEditing:(UITextView *)textView
{
    [self.addComment removeFromSuperview];
    return YES;
}

-(void)sendButtonPressed:(UIButton *)sender
{
    if (self.commentsTextView.text.length > 0) {
        
        
        NSString *textInView = self.commentsTextView.text;
        
        NSData *data = [textInView dataUsingEncoding:NSNonLossyASCIIStringEncoding];
        NSString *unicode = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
        NSDictionary *comment = @{@"eventId":@(self.eventId),
                                  @"typeOfUser":@(2),
                                  @"comment":unicode
                                  };
        
        [Communication addComment:comment succesBlock:^(NSDictionary *response) {
            if ([[response objectForKey:@"errorMessage"] integerValue] == 0) {
                
                
                self.commentsTextView.text = @"";
                [self getNewComments];
            }
            
        } errorBlock:^(NSDictionary *error) {
            
        }];
    }
    
    [self.commentsTextView resignFirstResponder];
    
}

@end
