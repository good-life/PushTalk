//
//  APService.h
//  APService
//
//  Created by LiDong on 12-8-15.
//  Copyright (c) 2012年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>


extern NSString * const kAPNetworkDidSetupNotification;          // 建立连接
extern NSString * const kAPNetworkDidCloseNotification;          // 关闭连接
extern NSString * const kAPNetworkDidRegisterNotification;       // 注册成功
extern NSString * const kAPNetworkDidLoginNotification;          // 登录成功
extern NSString * const kAPNetworkDidReceiveMessageNotification; // 收到消息(非APNS)


@interface APService : NSObject

// 以下四个接口是必须调用的
+ (void)setupWithOption:(NSDictionary *)launchingOption;      // 初始化
+ (void)registerForRemoteNotificationTypes:(int)types;        // 注册APNS类型
+ (void)registerDeviceToken:(NSData *)deviceToken;            // 向服务器上报Device Token
+ (void)handleRemoteNotification:(NSDictionary *)remoteInfo;  // 处理收到的APNS消息，向服务器上报收到APNS消息

// 下面的接口是可选的
+ (void)setTags:(NSSet *)tags alias:(NSString *)alias; // 设置标签和别名（若参数为nil，则忽略；若是空对象，则清空；详情请参考文档：http://docs.jpush.cn/pages/viewpage.action?pageId=557241）
+ (NSString *)openUDID; // UDID

@end
