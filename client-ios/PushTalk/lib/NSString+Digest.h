//
//  NSString+Digest.h
//  APTalk
//
//  Created by LiDong on 11-4-1.
//  Copyright 2011 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface NSString (Digest);

- (NSString *)AP_MD5;
- (NSString *)AP_UUID;
- (NSString *)AP_SHA256;

@end
