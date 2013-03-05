//
//  InfoDetailViewController.h
//  PushTalk
//
//  Created by yuanqiang on 13-3-5.
//  Copyright (c) 2013年 Jpush. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface InfoDetailViewController : UIViewController {
    UILabel *_infoLabel;
    UILabel *_tokenLabel;
    UILabel *_udidLabel;
    NSString *_info;
}

-(id)initWithInfo:(NSString *)info;

@end
