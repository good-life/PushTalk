//
//  AppDelegate.m
//  PushTalk
//
//  Created by YuanQiang on 12-12-25.
//  Copyright (c) 2012年 YuanQiang. All rights reserved.
//

#import "AppDelegate.h"
#import "APService.h"
#import "JPushViewController.h"


CGRect kScreenBounds;
CGRect kStatusBarFrame;
CGRect kNavigationBounds;
CGFloat kVCWrapperOffsetY;
CGRect kVCWrapperFrame;
CGRect kVCWrapperBounds;
CGRect kPageFrame;
CGRect kPageBounds;

UIColor *kClearColor;
UIColor *kBlackColor;
UIColor *kWhiteColor;
UIColor *kThemeColor;
UIColor *kNavigationColor;


@implementation AppDelegate

@synthesize deviceToken = _deviceToken;

//- (void)dealloc {
//    [_window release];
//    
//    RELEASE_SAFELY(kClearColor);
//    RELEASE_SAFELY(kBlackColor);
//    RELEASE_SAFELY(kWhiteColor);
//    RELEASE_SAFELY(kThemeColor);
//    RELEASE_SAFELY(kNavigationColor);
//    
//    [super dealloc];
//}

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    APLogTrace();
    kScreenBounds = [[UIScreen mainScreen] bounds];
    kStatusBarFrame = [[UIApplication sharedApplication] statusBarFrame];
    kNavigationBounds = CGRectMake(0, 0, kScreenBounds.size.width, 44);
    kVCWrapperOffsetY = kStatusBarFrame.origin.y + kStatusBarFrame.size.height;
    kVCWrapperFrame = CGRectMake(0, kVCWrapperOffsetY, kScreenWidth, kScreenHeight - kVCWrapperOffsetY);
    kVCWrapperBounds = CGRectMake(0, 0, kScreenBounds.size.width, kVCWrapperFrame.size.height);
    kPageFrame = CGRectMake(0, kVCWrapperOffsetY + kNavigationBarHeight, kScreenWidth, kVCWrapperFrame.size.height - kNavigationBarHeight);
    kPageBounds = CGRectMake(0, 0, kPageFrame.size.width, kPageFrame.size.height);
    
    kClearColor = [UIColor clearColor];
    kBlackColor = [UIColor blackColor];
    kWhiteColor = [UIColor whiteColor];
    kThemeColor = [[UIColor alloc] initWithRed:244.f/255.f green:240.f/255.f blue:231.f/255.f alpha:1];
    kNavigationColor = [[UIColor alloc] initWithRed:0.75 green:0.45 blue:0.35 alpha:1];
    
    _window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    [_window setBackgroundColor:[UIColor whiteColor]];
    
    JPushViewController *viewController = [[JPushViewController alloc] init];
    
    UINavigationController *navigationController = [[UINavigationController alloc] initWithRootViewController:viewController];
    
    [_window setRootViewController:navigationController];
    [_window makeKeyAndVisible];
    
    
    [APService registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge |
                                                   UIRemoteNotificationTypeSound |
                                                   UIRemoteNotificationTypeAlert)];
    [APService setupWithOption:launchOptions];
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    [application setApplicationIconBadgeNumber:0];
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    _deviceToken = [NSString stringWithFormat:@"%@",deviceToken];
    
    [APService registerDeviceToken:deviceToken];
}

- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *) error {
    NSLog(@"did Fail To Register For Remote Notifications With Error: %@", error);
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    [application setApplicationIconBadgeNumber:0];
    [APService handleRemoteNotification:userInfo];
}

@end
