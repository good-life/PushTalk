//
//  JPushViewController.h
//  JPushClient
//
//  Created by LiDong on 12-11-2.
//  Copyright (c) 2012年 LiDong. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface JPushViewController : UIViewController <UIWebViewDelegate> {
    UIWebView *_webView;
    UIBarButtonItem *_leftButton;
}

@end
