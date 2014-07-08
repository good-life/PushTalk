//
//  InfoDetailViewController.m
//  PushTalk
//
//  Created by yuanqiang on 13-3-5.
//  Copyright (c) 2013年 Jpush. All rights reserved.
//

#import "InfoDetailViewController.h"
#import "AppDelegate.h"

@implementation InfoDetailViewController

- (id)initWithInfo:(NSString *)info {
    self = [super init];
    
    if (self) {
        _info = info;
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.view setBackgroundColor:[UIColor blackColor]];
    
    _infoLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 40, 320, 100)];
    [_infoLabel setBackgroundColor:[UIColor clearColor]];
    [_infoLabel setTextColor:[UIColor colorWithRed:0.5 green:0.65 blue:0.75 alpha:1]];
    [_infoLabel setFont:[UIFont boldSystemFontOfSize:20]];
    [_infoLabel setTextAlignment:UITextAlignmentCenter];
    [_infoLabel setNumberOfLines:0];
    [_infoLabel setText:_info];
    [self.view addSubview:_infoLabel];
    
    _tokenLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 150, 320, 32*3)];
    [_tokenLabel setBackgroundColor:[UIColor clearColor]];
    [_tokenLabel setTextColor:[UIColor colorWithRed:0.5 green:0.7 blue:0.75 alpha:1]];
    [_tokenLabel setFont:[UIFont systemFontOfSize:14]];
    [_tokenLabel setTextAlignment:UITextAlignmentCenter];
    [_tokenLabel setNumberOfLines:3];
    
    AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
    NSString *deviceToken = [app deviceToken];
    
    [_tokenLabel setText:[NSString stringWithFormat:@"Device Token: %@", deviceToken]];
    [self.view addSubview:_tokenLabel]; 
    
    _udidLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 290, 320, 24)];
    [_udidLabel setBackgroundColor:[UIColor clearColor]];
    [_udidLabel setTextColor:[UIColor colorWithRed:0.5 green:0.7 blue:0.75 alpha:1]];
    [_udidLabel setFont:[UIFont systemFontOfSize:18]];
    [_udidLabel setTextAlignment:UITextAlignmentCenter];
    [_udidLabel setText:[NSString stringWithFormat:@"UDID: %@", [APService openUDID]]];
    [self.view addSubview:_udidLabel];
}




@end
