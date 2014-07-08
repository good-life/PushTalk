//
//  APClient.h
//  PushSDK
//
//  Created by LiDong on 12-9-6.
//  Copyright (c) 2012年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <pthread.h>

@class APUserDefaults;
@class APDeviceToken;
@class APAttributes;

@interface APClient : NSObject {
    APUserDefaults *_userDefaults;
    NSString *_bundleVersion;
    NSString *_bundleIdentifier;
    NSString *_systemVersion;
    NSString *_IMEI; // fake
    UInt32 _uid;
    NSString *_password;
    APDeviceToken *_deviceToken;
    APAttributes *_attributes;
    NSTimeInterval _becomeActiveTime;
    BOOL _testMode;
    NSString *_applicationKey;
    NSString *_channel;
    pthread_mutex_t _mutex;
}

@property (nonatomic, copy, readonly) NSString *bundleVersion;
@property (nonatomic, copy, readonly) NSString *bundleIdentifier;
@property (nonatomic, copy, readonly) NSString *systemVersion;
@property (nonatomic, assign, readonly) BOOL testMode;
@property (nonatomic, copy, readonly) NSString *applicationKey;
@property (nonatomic, copy, readonly) NSString *channel;
@property (nonatomic, assign) UInt32 uid;
@property (nonatomic, copy) NSString *password;
@property (nonatomic, copy, readonly) NSString *IMEI;
@property (nonatomic, copy, readonly) APDeviceToken *deviceToken;
@property (nonatomic, copy, readonly) APAttributes *attributes;
@property (nonatomic, assign) NSTimeInterval becomeActiveTime;

- (id)initWithInfoDictionary:(NSDictionary *)infoDictionary;
- (void)setupWithPushConfig:(NSDictionary *)pushConfig;
- (BOOL)isRegistered;
- (void)setLatestDeviceToken:(NSString *)deviceToken;
- (void)setUploadedDeviceToken:(NSString *)deviceToken;
//- (void)setTags:(NSSet *)tags alias:(NSString *)alias;
- (void)addTags:(NSSet *)tags;
- (void)uploadDataIfNeeded;
- (BOOL)synchronize; // thread safe
- (void)lock;
- (BOOL)tryLock;
- (void)unlock;

@end

#define g_pClient [APClient sharedClientIfExists]
