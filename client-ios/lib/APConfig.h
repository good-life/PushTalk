/*
 *  APConfig.h
 *  APTalk
 *
 *  Created by LiDong on 11-3-26.
 *  Copyright 2011 HXHG. All rights reserved.
 *
 */

#ifndef __AP_CONFIG_H__
#define __AP_CONFIG_H__

#import <Foundation/Foundation.h>
#import "APUtilities.h"
#import "APService.h"


#define AP_SDK_VERSION @"1.2.5"

#define THIS_FILE [[NSString stringWithUTF8String:__FILE__] lastPathComponent]
#define THIS_METHOD NSStringFromSelector(_cmd)

#if 1

#define APLog(FORMAT, ...) printf("[%s] %s\n", APDescriptionForCurrentTime(), [[NSString stringWithFormat:FORMAT, ##__VA_ARGS__] UTF8String])
#define APLogTrace() APLog(@"[TRACING] FILE: %@ >> METHOD: %@ >> LINE: %d", THIS_FILE, THIS_METHOD, __LINE__)
#define APLogError(FORMAT, ...)

#else

#define APLog(...)
#define APLogTrace()
#define APLogError(FORMAT, ...)

#endif



#endif // __AP_CONFIG_H__
