//
//  JPushViewController.h
//  PushTalk
//
//  Created by YuanQiang on 12-12-25.
//  Copyright (c) 2012年 YuanQiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JPushViewController : UIViewController <UIWebViewDelegate> {
    UIWebView *_webView;
    UIBarButtonItem *_leftButton;
    UIBarButtonItem *_rightButton;
    NSString *_info;
}

@end
