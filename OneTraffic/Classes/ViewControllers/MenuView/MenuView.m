//
//  MenuView.m
//  OneTraffic
//
//  Created by Stefan Andric on 1/13/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "MenuView.h"
#import "Constant.h"
#import "UIHelper.h"
#import "UIImageView+AFNetworking.h"

#define SLIDE_TIMING .25



@implementation MenuView

- (id)initWithFrame:(CGRect)frame{
    
    self = [super initWithFrame:frame];
    if (self) {
        
        
        self.backgroundColor= [UIColor clearColor];
        [self createInterface];
        
        
    }
    return self;
}

-(void) createInterface
{
    
    self.clearView= [[UIView alloc] initWithFrame:CGRectMake(0, 0, 50, SCREEN_HEIGHT)];
    self.clearView.backgroundColor= [UIColor clearColor];
    [self addSubview:self.clearView];
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(close)];
    [self.clearView addGestureRecognizer:tapGestureRecognizer];

    
    

    
    self.coloredView= [[UIView alloc] initWithFrame:CGRectMake(50, 0, SCREEN_WIDTH-50, SCREEN_HEIGHT)];
    self.coloredView.backgroundColor= DARK_BLUE_TRANSPARENT;
    [self addSubview:self.coloredView];

    
    self.userImageView = [[UIImageView alloc] initWithFrame:CGRectMake (15,  25 , 50, 50)];
        self.userImageView.layer.cornerRadius = self.userImageView.frame.size.width / 2;
        self.userImageView.clipsToBounds = YES;

    
    [ self.coloredView addSubview:self.userImageView ];
    
    
    self.closeButton = [[UIButton alloc] initWithFrame:CGRectMake (SCREEN_WIDTH-50-55,  20 , 40, 40)];
    [self.closeButton setImage:[UIImage imageNamed:@"close.png"] forState:UIControlStateNormal];
    
    [self.closeButton addTarget:self action:@selector(close) forControlEvents:UIControlEventTouchUpInside];
    
    [ self.coloredView addSubview:self.closeButton ];
    
    NSDictionary *parameters = @{TOKEN:[[NSUserDefaults standardUserDefaults]objectForKey:TOKEN]};
    [Communication getProfileWithParameters:parameters successfulBlock:^(NSDictionary *response) {
        NSDictionary *profileDict = [response objectForKey:@"profile"];
        
        if ([profileDict objectForKey:@"pictureUrl"] != nil && [profileDict objectForKey:@"pictureUrl"] != [NSNull null]) {
            
            [self.userImageView setImageWithURL:[NSURL URLWithString:[profileDict objectForKey:@"pictureUrl"]] placeholderImage:[UIImage imageNamed:@"photo_icon"]];
        }
    } errorBlock:^(NSDictionary *error) {
        
    }];



    self.nameLabel= [[UILabel alloc] initWithFrame:CGRectMake(15, 80, 200, 20)];
    [self.nameLabel setText:[NSString stringWithFormat: @"%@",[[NSUserDefaults standardUserDefaults] objectForKey:USER_NAME]]];
    [self.nameLabel setTextColor:[UIColor whiteColor]];
    [self.coloredView addSubview:self.nameLabel];

    self.dataArray= [[NSArray alloc] initWithObjects:@"Profile",@"Settings",@"Terms Of Use",@"Help",@"Feedback",@"Pause Positioning",@"LOG OUT", nil];
    
    self.menuTableView = [[UITableView alloc]initWithFrame:CGRectMake(0, 100, self.frame.size.width,  7*60)];
    self.menuTableView.delegate = self;
    self.menuTableView.dataSource = self;
    self.menuTableView.backgroundColor=[UIColor clearColor];
    self.menuTableView.bounces=NO;
    [self.coloredView addSubview:self.menuTableView];
    
    
    UIPanGestureRecognizer *panGestureRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(move:)];
    [self addGestureRecognizer:panGestureRecognizer];
    
    
    
}

-(void)move:(id)sender {
    [self.superview bringSubviewToFront:[(UIPanGestureRecognizer*)sender view]];
    CGPoint translatedPoint = [(UIPanGestureRecognizer*)sender translationInView:self.superview];
    
    if ([(UIPanGestureRecognizer*)sender state] == UIGestureRecognizerStateBegan) {

        firstX = [[sender view] center].x;
        firstY = [[sender view] center].y;
        NSLog(@"firstX  %f",firstX);

    }
    
    translatedPoint = CGPointMake(firstX+translatedPoint.x, firstY);
    NSLog(@"translatedPoint  %f",translatedPoint.x);

    if(translatedPoint.x > [[sender view] frame].size.width/2 ){
    
    [[sender view] setCenter:translatedPoint];
    
    }
    if ([(UIPanGestureRecognizer*)sender state] == UIGestureRecognizerStateEnded) {
        CGFloat velocityX = ([(UIPanGestureRecognizer*)sender velocityInView:self.superview].x);
        NSLog(@"ENDED");
        CGFloat finalX = translatedPoint.x + velocityX;
        CGFloat finalY = firstY;// translatedPoint.y + (.35*[(UIPanGestureRecognizer*)sender velocityInView:self.view].y);

        NSLog(@"finalX  %f",finalX);

        NSLog(@"self.superview.frame.size.width  %f",self.superview.frame.size.width);

            if (finalX <= self.superview.frame.size.width/2+ self.frame.size.width/2 ){
                finalX =  self.frame.size.width/2;
                NSLog(@"maenj");

            } else if (finalX >  self.superview.frame.size.width/2 + self.frame.size.width/2) {
                finalX =  self.superview.frame.size.width + self.frame.size.width/2;
                NSLog(@"vece");

            }
        
        
        
        CGFloat animationDuration = 0.3;
        
        NSLog(@"the duration is: %f", animationDuration);
        
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationDuration:animationDuration];
        [UIView setAnimationCurve:UIViewAnimationCurveEaseOut];
        [UIView setAnimationDelegate:self];
        
        
        if (finalX >  self.superview.frame.size.width/2+ self.frame.size.width/2 ) {
            
            [UIView setAnimationDidStopSelector:@selector(close)];

        }
        
        [[sender view] setCenter:CGPointMake(finalX, finalY)];
        [UIView commitAnimations];
    }
}



-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArray.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
   
    
    UITableViewCell *cell = (UITableViewCell *)[tableView dequeueReusableCellWithIdentifier:@"menuCell"];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"menuCell"];
        cell.backgroundColor=[UIColor clearColor];
        cell.selectionStyle= UITableViewCellSelectionStyleNone;
    }
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@",[self.dataArray objectAtIndex:indexPath.row]];
    cell.textLabel.textColor=NEON_GREEN;
    cell.textLabel.textAlignment=NSTextAlignmentLeft;
    
    return cell;
    
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"%ld", (long)indexPath.row); // you can see selected row number in your console;
    
    if (self.delegate && [self.delegate respondsToSelector:@selector(selectedIndex:)]) {
    
        [self.delegate selectedIndex:indexPath.row];
    
    }

}


- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    return 60;
}


-(void)close {
    
    
    NSLog(@"close");
    if (self.delegate && [self.delegate respondsToSelector:@selector(closeMenu)]) {
        
        [self.delegate closeMenu];
        
    }
}
@end
