//
//  AddCommentViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 4/5/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "AddCommentViewController.h"
#import "Constant.h"
#import "Communication.h"
#import "UIHelper.h"

@interface AddCommentViewController ()

@end

@implementation AddCommentViewController


-(void)loadView
{
    self.view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view.backgroundColor = DARK_BLUE;
    [self setupView];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.commentTextView setDelegate:self];
    self.commentTextView.keyboardAppearance = UIKeyboardAppearanceDark;
    self.charactersLeftLabel.text = NSLocalizedString(@"", nil);
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)setupView
{
    self.backButton=[UIHelper createBackButton];
    [self.backButton addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:self.backButton];
    
    self.commentTextView = [[UITextView alloc] initWithFrame:CGRectMake(20, self.backButton.frame.origin.y+self.backButton.frame.size.height, SCREEN_WIDTH-40, SCREEN_HEIGHT/3)];
    self.commentTextView.backgroundColor = [UIColor whiteColor];
    [self.commentTextView setTextColor:NEON_GREEN];
    self.commentTextView.font= [UIFont systemFontOfSize:16];
    self.commentTextView.autocorrectionType = UITextAutocorrectionTypeNo;
    [[self.commentTextView layer] setBorderWidth:1.5f];
    [[self.commentTextView layer] setCornerRadius:5.0f];
    
    [self.view addSubview:self.commentTextView];
    
    self.charactersLeftLabel = [[UILabel alloc] initWithFrame:CGRectMake(self.commentTextView.frame.origin.x, self.commentTextView.frame.origin.y+self.commentTextView.frame.size.height, SCREEN_WIDTH-40, 30)];
    [self.charactersLeftLabel setTextColor:NEON_GREEN];
    [self.view addSubview:self.charactersLeftLabel];
    
    self.addButton = [UIButton buttonWithType:UIButtonTypeSystem];
    self.addButton.backgroundColor = NEON_GREEN;
    [self.addButton setTitleColor:DARK_BLUE forState:UIControlStateNormal];
    [self.addButton setTitle:NSLocalizedString(@"AddComment", nil) forState:UIControlStateNormal];
    [self.addButton addTarget:self action:@selector(addButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    [self.addButton setFrame:CGRectMake(SCREEN_WIDTH/2-75, self.charactersLeftLabel.frame.origin.y+self.charactersLeftLabel.frame.size.height, 150, 40)];
    self.addButton.clipsToBounds = YES;
    
    self.addButton.layer.cornerRadius = 15.0f;//half of the width
    self.addButton.layer.borderWidth=2.0f;
    [self.view addSubview:self.addButton];
    
    self.centerLabel = [UIHelper createLabelWithFrame:CGRectMake(SCREEN_WIDTH/2-(SCREEN_WIDTH-80)/2, self.backButton.frame.origin.y, SCREEN_WIDTH-80, 40) andText:@"Add new comment"];
    self.centerLabel.textColor = [UIColor whiteColor];
    self.centerLabel.font = [UIFont systemFontOfSize:20.0f];
    [self.view addSubview:self.centerLabel];
}

-(void)textViewDidChange:(UITextView *)textView
{
    __unused NSUInteger result = 255-self.commentTextView.text.length;
    self.charactersLeftLabel.text = NSLocalizedString(@"", nil);
    if (self.commentTextView.text.length<=255) {
        [self.addButton setEnabled:YES];
        [self.addButton setBackgroundColor:NEON_GREEN];
    }
    else {
        [self.addButton setEnabled:NO];
        [self.addButton setBackgroundColor:[UIColor grayColor]];
    }
}

-(void)addButtonTapped:(UIButton *)sender
{
    
    if(self.commentType == EventComment){
    
    if (self.commentTextView.text.length > 0) {
        NSDictionary *comment = @{@"eventId":@(self.eventId),
                                  @"typeOfUser":@(2),
                                  @"comment":self.commentTextView.text
                                  };
        
        [Communication addComment:comment succesBlock:^(NSDictionary *response) {
            
            if (self.delegate && [self.delegate respondsToSelector:@selector(addButtonPressed)]) {
                

                [self.delegate addButtonPressed];

            }

            [self dismissViewControllerAnimated:YES completion:nil];
            
        } errorBlock:^(NSDictionary *error) {
            
        }];
    }
    
    }else  if(self.commentType == ChannelComment){
        
        if (self.commentTextView.text.length > 0) {
            
            NSDictionary *comment = @{@"channelId":@(self.channelID),
                                      @"channelMessage":self.commentTextView.text
                                      };
            
            [Communication addChannelComment:comment succesBlock:^(NSDictionary *response) {
              
                if (self.delegate &&  [self.delegate  respondsToSelector:@selector(addButtonPressed)]){
                    
                    
                    [self.delegate addButtonPressed];
                    
                }
                
                [self dismissViewControllerAnimated:YES completion:nil];
                
            } errorBlock:^(NSDictionary *error) {
                
            }];
        }
        
        
        
    }
}

-(void)backAction
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

@end
