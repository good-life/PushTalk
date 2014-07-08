//
//  AppDelegate.h
//  PushTalk
//
//  Created by YuanQiang on 12-12-25.
//  Copyright (c) 2012年 YuanQiang. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate> {
    UIWindow *_window;
    NSString *_deviceToken;
}

@property (nonatomic,strong) NSString *deviceToken;

@end
