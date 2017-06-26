//
//  EventDetailsViewController.m
//  OneTraffic
//
//  Created by Stefan Andric on 4/1/16.
//  Copyright © 2016 Zesium. All rights reserved.
//

#import "EventDetailsViewController.h"
#import "Constant.h"
#import "EventTableViewCell.h"
#import "Communication.h"
#import "CommentModel.h"
#import "UIImageView+AFNetworking.h"
#import "UIHelper.h"
#import "LogicHelper.h"
#import "AddCommentViewController.h"
#import "CustomMGLPointAnnotation.h"
#import "MBProgressHUD.h"

@interface EventDetailsViewController ()
@property BOOL userIsGoingToComments;
@property (nonatomic, strong) UIButton *mapModeButton;
@property BOOL isMapButton;
@property BOOL isHelicopterPerspective;
@property BOOL trackingMode;
@end

@implementation EventDetailsViewController

-(void)loadView
{
    self.view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT)];
    self.view.backgroundColor = DARK_BLUE;
    self.automaticallyAdjustsScrollViewInsets = NO;
    [self createInterface];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.isHelicopterPerspective = YES;
    self.trackingMode = YES;
    self.headerLabel.text = [LogicHelper setTitleDependingOnId:self.event.cause];
    
    CustomMGLPointAnnotation *point = [[CustomMGLPointAnnotation alloc] init];
    point.coordinate = CLLocationCoordinate2DMake(self.event.location.lat, self.event.location.lon);
    point.title = [LogicHelper setTitleDependingOnId:self.event.cause];
    point.cause = self.event.cause;
    point.type = self.event.type;
    point.eventId = self.event.eventId;
    point.magnitude = self.event.magnitude;
    
    [self.mapView addAnnotation:point];
}

-(void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:YES];
    self.userIsGoingToComments = NO;
}

-(void)viewDidDisappear:(BOOL)animated
{
    if (self.userIsGoingToComments == NO) {
        [self.mapView removeFromSuperview];
        self.mapView = nil;
    }
    
    [super viewDidDisappear:YES];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)createInterface
{
    
    self.upperView = [UIHelper createViewWithFrame:CGRectMake(0, 64, SCREEN_WIDTH, 60)];
    UIView *neonGreenDownBorder = [[UIView alloc] initWithFrame:CGRectMake(0, self.upperView.frame.size.height-2, SCREEN_WIDTH, 2)];
    neonGreenDownBorder.backgroundColor = NEON_GREEN;
    [self.view addSubview:self.upperView];
    [self.upperView addSubview:neonGreenDownBorder];
    
    self.commentButton = [UIHelper createCustomButtonWithFrame:CGRectMake(20, 10, 40, 40) andTitle:nil];
    [self.commentButton setImage:[UIImage imageNamed:@"CommentIcon"] forState:UIControlStateNormal];
    [self.commentButton addTarget:self action:@selector(commentsButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    [self.upperView addSubview:self.commentButton];
    
    if (self.event.isDrivenThrough == YES) {
        [self setupEventInZoneLayout];
    }
    else {
        [self setupEventNotInZoneLayout];
    }
    
    
    NSString *styleUrl = MAP_FINAL_URL;
    NSURL *urlStyle = [NSURL URLWithString:styleUrl];
    self.mapView = [[MGLMapView alloc] initWithFrame:CGRectMake(0, 124, SCREEN_WIDTH, SCREEN_HEIGHT-124)styleURL:urlStyle];
    self.mapView.delegate = self;
    self.mapView.attributionButton.hidden=YES;
    self.mapView.logoView.hidden=YES;
    CLLocationCoordinate2D eventLocation = CLLocationCoordinate2DMake(self.event.location.lat, self.event.location.lon);
    
    [self.mapView setZoomLevel:14];
    [self.mapView setCenterCoordinate:eventLocation];
    self.mapView.userInteractionEnabled = YES;
    self.mapView.showsUserLocation = YES;
    self.mapView.tintColor = NEON_GREEN;
    
    UIPanGestureRecognizer* panRec = [[UIPanGestureRecognizer alloc] initWithTarget:self action:@selector(didDragMap:)];
    [panRec setDelegate:self];
    [self.mapView addGestureRecognizer:panRec];
    [self.view addSubview:self.mapView];
    
    
    self.mapModeButton= [UIButton buttonWithType:UIButtonTypeCustom];
    [self.mapModeButton setFrame:CGRectMake(10,self.mapView.frame.size.height-60 , 50, 50)];
    self.mapModeButton.layer.cornerRadius = 20;
    self.mapModeButton.clipsToBounds = YES;
    [self.mapModeButton setBackgroundColor:DARK_BLUE];
    [self.mapModeButton setImage:[UIImage imageNamed:@"mapPerspectiveIcon"]  forState:UIControlStateNormal];
    [self.mapModeButton addTarget:self action:@selector(mapModeButtonClicked:) forControlEvents:UIControlEventTouchUpInside];
    [self.mapView addSubview:self.mapModeButton];
    
}


-(void)addCommentButtonPressed:(UIButton *)sender
{
    
    AddCommentViewController *vc = [[AddCommentViewController alloc] init];
    vc.eventId = self.event.eventId;
    vc.commentType =EventComment;
    [self presentViewController:vc animated:YES completion:nil];
    
}

- (MGLAnnotationImage *)mapView:(MGLMapView *)mapView imageForAnnotation:(id <MGLAnnotation>)annotation {
    
    MGLAnnotationImage *annotationImage = [mapView dequeueReusableAnnotationImageWithIdentifier:@"marker"];
    if (!annotationImage) {
        UIImage *image = [UIImage imageNamed:[NSString stringWithFormat:@"%@Annotation",[LogicHelper setImageDependingOnId:self.event.cause]]];
        annotationImage = [MGLAnnotationImage annotationImageWithImage:image reuseIdentifier:@"marker"];
    }
    return annotationImage;
}

// Allow markers callouts to show when tapped
- (BOOL)mapView:(MGLMapView *)mapView annotationCanShowCallout:(id <MGLAnnotation>)annotation {
    return YES;
}

-(void)backAction{
    
    [self.navigationController popViewControllerAnimated:YES];
    
}


#pragma mark - CustomLayouts depending on event
-(void)setupEventNotInZoneLayout
{
    self.eventStartsInNumber = [UIHelper createLabelWithFrame:CGRectMake(CGRectGetMaxX(self.commentButton.frame), self.commentButton.frame.origin.y-3, (SCREEN_WIDTH-CGRectGetMaxX(self.commentButton.frame))/2, 20) andText:@"1 KM"];
    self.eventStartsInNumber.attributedText = [LogicHelper setAttributedStringDependingOnValue:self.event.distance andType:@"length"];
    self.eventStartsInNumber.font = [UIFont systemFontOfSize:22];
    [self.upperView addSubview:self.eventStartsInNumber];
    
    self.passEventInNumber = [UIHelper createLabelWithFrame:CGRectMake(CGRectGetMaxX(self.eventStartsInNumber.frame), self.eventStartsInNumber.frame.origin.y, self.eventStartsInNumber.frame.size.width, self.eventStartsInNumber.frame.size.height) andText:nil];
    NSMutableAttributedString *first = [UIHelper setAttributedString:@"4" andType:@"min/"];
    NSAttributedString *second = [UIHelper setAttributedString:@"1" andType:@"km"];
    [first appendAttributedString:second];
    
    [self.passEventInNumber setAttributedText: [LogicHelper setAttributedStringDependingOnValue:self.event.duration andType:@"duration"]];
    self.passEventInNumber.font = [UIFont systemFontOfSize:22];
    [self.upperView addSubview:self.passEventInNumber];
    
    self.eventStartsIn = [UIHelper createLabelWithFrame:CGRectMake(self.eventStartsInNumber.frame.origin.x, CGRectGetMaxY(self.eventStartsInNumber.frame), self.eventStartsInNumber.frame.size.width, self.eventStartsInNumber.frame.size.height) andText:@"Away"];
    self.eventStartsIn.textColor = [UIColor whiteColor];
    self.eventStartsIn.font = [UIFont systemFontOfSize:14];
    [self.upperView addSubview:self.eventStartsIn];
    
    
    self.passEventIn = [UIHelper createLabelWithFrame:CGRectMake(CGRectGetMaxX(self.eventStartsIn.frame), self.eventStartsIn.frame.origin.y, (SCREEN_WIDTH-CGRectGetMaxX(self.commentButton.frame))/2, 20) andText:@"Duration"];
    self.passEventIn.textColor = [UIColor whiteColor];
    self.passEventIn.font = [UIFont systemFontOfSize:14];
    [self.upperView addSubview:self.passEventIn];
}

-(void)setupEventInZoneLayout
{
    self.reportResolvedButton = [UIHelper createCustomButtonWithFrame:CGRectMake(SCREEN_WIDTH-60, self.commentButton.frame.origin.y, 40, 40) andTitle:nil];
    [self.reportResolvedButton setImage:[UIImage imageNamed:@"ResolveEventButton"] forState:UIControlStateNormal];
    [self.reportResolvedButton addTarget:self action:@selector(reportButtonTapped:) forControlEvents:UIControlEventTouchUpInside];
    [self.upperView addSubview:self.reportResolvedButton];
    
    
    self.eventStartsInNumber = [UIHelper createLabelWithFrame:CGRectMake(CGRectGetMaxX(self.commentButton.frame), self.commentButton.frame.origin.y-3, (SCREEN_WIDTH-CGRectGetMaxX(self.commentButton.frame)-self.reportResolvedButton.frame.size.width-20), 20) andText:@"1 KM"];
    self.eventStartsInNumber.attributedText = [LogicHelper setAttributedStringDependingOnValue:self.event.duration andType:@"duration"];
    self.eventStartsInNumber.font = [UIFont systemFontOfSize:22];
    [self.upperView addSubview:self.eventStartsInNumber];
    
    
    self.eventStartsIn = [UIHelper createLabelWithFrame:CGRectMake(self.eventStartsInNumber.frame.origin.x, CGRectGetMaxY(self.eventStartsInNumber.frame), self.eventStartsInNumber.frame.size.width, self.eventStartsInNumber.frame.size.height) andText:@"Remaining"];
    self.eventStartsIn.textColor = [UIColor whiteColor];
    self.eventStartsIn.font = [UIFont systemFontOfSize:14];
    [self.upperView addSubview:self.eventStartsIn];
    
}

-(void)commentsButtonTapped:(UIButton *)sender
{
    CommentsViewController *vc = [CommentsViewController new];
    vc.eventName = self.headerLabel.text;
    vc.eventId = self.event.eventId;
    self.userIsGoingToComments = YES;
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)reportButtonTapped:(UIButton *)sender {
    
    
    
    UIAlertController *confirmAlertController = [UIAlertController alertControllerWithTitle:NSLocalizedString(@"", nil) message:NSLocalizedString(@"ConfirmEventReport", nil) preferredStyle:UIAlertControllerStyleAlert];
    UIAlertAction *yesAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"Yes", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        NSDictionary *parameters = @{@"eventId":@(self.event.eventId)};
        MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
        hud.removeFromSuperViewOnHide = YES;
        [Communication resolveEventWithParameters:parameters successBlock:^(NSDictionary *response) {
            if ([[response objectForKey:@"error"] integerValue] == 0) {
                hud.mode = MBProgressHUDModeText;
                [hud setCenter:self.view.center];
                hud.labelText = NSLocalizedString(@"Done", nil);
                
                
                
                [hud hide:YES afterDelay:1];
                [self performSelector:@selector(removeScreen) withObject:nil afterDelay:1];
            }
            else {
                [hud hide:YES];
            }
            
        } errorBlock:^(NSDictionary *error) {
            [hud hide:YES];
        }];
    }];
    
    UIAlertAction *noAction = [UIAlertAction actionWithTitle:NSLocalizedString(@"No", nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        
    }];
    
    [confirmAlertController addAction:yesAction];
    [confirmAlertController addAction:noAction];
    
    [self presentViewController:confirmAlertController animated:YES completion:nil];
    
    
    
    
}

- (void)removeScreen {
    [self.navigationController popViewControllerAnimated:YES];
}



-(void)mapModeButtonClicked:(UIButton *)sender
{
    self.isMapButton = YES;
    if(self.trackingMode == YES){
        
        if (self.isHelicopterPerspective == YES) {
            MGLMapCamera *camera = [[self.mapView camera] copy];
            [camera setPitch:60.0];
            [self.mapView setCamera:camera animated:NO];
            self.isHelicopterPerspective = NO;
            [self.mapModeButton setImage:[UIImage imageNamed:@"mapPerspectiveIcon"]  forState:UIControlStateNormal];
            
            [self.mapView setUserLocationVerticalAlignment:MGLAnnotationVerticalAlignmentBottom animated:YES];
            
        }
        else {
            MGLMapCamera *camera = [[self.mapView camera] copy];
            [camera setPitch:0.0f];
            [self.mapView setCamera:camera animated:NO];
            [self.mapModeButton setImage:[UIImage imageNamed:@"mapPerspectiveIcon"]  forState:UIControlStateNormal];
            
            self.isHelicopterPerspective = YES;
            [self.mapView setUserLocationVerticalAlignment:MGLAnnotationVerticalAlignmentBottom animated:YES];
            
        }
    }
    else {
        
        self.trackingMode = YES;
        
        [self.mapModeButton setImage:[UIImage imageNamed:@"mapPerspectiveIcon"]  forState:UIControlStateNormal];
        
        
        if (self.isHelicopterPerspective == YES) {
            
            [self.mapView setUserLocationVerticalAlignment:MGLAnnotationVerticalAlignmentBottom animated:YES];
            
            self.isHelicopterPerspective = YES;
            
            
        }
        else {
            [self.mapView setUserLocationVerticalAlignment:MGLAnnotationVerticalAlignmentBottom animated:YES];
            self.isHelicopterPerspective = NO;
            
        }
        [self.mapView setCenterCoordinate:self.mapView.userLocation.coordinate animated:YES];
    }
    
}


- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    return YES;
}

- (void)didDragMap:(UIGestureRecognizer*)gestureRecognizer {
    if (gestureRecognizer.state == UIGestureRecognizerStateBegan){
        
        self.trackingMode = NO;
        self.mapView.userTrackingMode = MGLUserTrackingModeNone;
        [self.mapModeButton setImage:[UIImage imageNamed:@"mapLocationIcon"] forState:UIControlStateNormal];
    }
}


@end
