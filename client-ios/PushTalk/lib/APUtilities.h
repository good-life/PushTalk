//
//  APUtilities.h
//  PushSDK
//
//  Created by LiDong on 12-9-6.
//  Copyright (c) 2012年 HXHG. All rights reserved.
//

#ifndef __UTILITIES_H__
#define __UTILITIES_H__


#import <Foundation/Foundation.h>


extern NSString * const NSEmptyString;
extern NSString * const kAPNetworkDidSetupNotification;
extern NSString * const kAPNetworkDidCloseNotification;
extern NSString * const kAPNetworkDidRegisterNotification;
extern NSString * const kAPNetworkDidLoginNotification;
extern NSString * const kAPNetworkDidLogoutNotification;
extern NSString * const kAPNetworkDidReceiveMessageNotification;


extern NSString *APHomeDirectory(void);
extern NSString *APHomeDirectoryForBundleIdentier(NSString *bundleIdentifier);
extern NSString *CTCarrierName(void);
extern UInt32 APCarrierTypeFromCarrierName(NSString *carrierName);
extern UInt32 APNetworkTypeFromCarrierType(const UInt32 carrierType);
extern UInt32 APNetworkTypeFromCarrierName(NSString *carrierName);
extern void APAlert(NSString *title, NSString *message);
extern NSTimeInterval APGetCurrentTimestamp(void);
extern const char *APDescriptionForCurrentTime(void);
extern NSString *APFakeIMEI(void);


#define NSBundleIdentifierKey (NSString *)kCFBundleIdentifierKey
#define NSBundleVersionKey (NSString *)kCFBundleVersionKey


#define	UIColorFromRGB(r, g, b) [UIColor colorWithRed:(r&0xFF)/255.0 green:(g&0xFF)/255.0 blue:(b&0xFF)/255.0 alpha:1.0f]
#define	UIColorFromRGBA(r, g, b, a) [UIColor colorWithRed:(r&0xFF)/255.0 green:(g&0xFF)/255.0 blue:(b&0xFF)/255.0 alpha:(a&0xFF)/255.0]

#define RETAIN_SAFELY(receiver, assigner) do { id _assigner = assigner; if (_assigner != receiver) { [receiver release]; receiver = [_assigner retain]; } } while (0)
#define COPY_SAFELY(receiver, assigner) do { id _assigner = assigner; if (_assigner != receiver) { [receiver release]; receiver = [_assigner copy]; } } while (0)
#define RELEASE_SAFELY(obj) do { [obj release]; obj = nil; } while (0)
#define UIVIEW_RELEASE_SAFELY(view) do { [view removeFromSuperview]; [view release]; view = nil; } while(0)

#define STOP_TIMER(timer) do { [timer invalidate]; timer = nil; } while(0)

#define LS(key) NSLocalizedString(key, nil)

#define STRETCHABLE_IMAGE(imageName, w, h) [[UIImage imageNamed:imageName] stretchableImageWithLeftCapWidth:w topCapHeight:h]


#endif // __UTILITIES_H__