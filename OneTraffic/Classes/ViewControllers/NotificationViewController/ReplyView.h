//
//  ReplyView.h
//  OneTraffic
//
//  Created by Stefan Andric on 4/27/17.
//  Copyright © 2017 Zesium. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol ReplyDelegate <NSObject>

- (void)stringToSend:(NSString *)string;

@end

@interface ReplyView : UIView <UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>

@property (nonatomic, weak) id <ReplyDelegate> delegate;
@property (nonatomic, strong) NSArray *quickReplyMessages;

@end
