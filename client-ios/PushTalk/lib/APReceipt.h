//
//  APReceipt.h
//  PushSDK
//
//  Created by LiDong on 12-9-16.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "APProtocol.h"

@class APDispatch;
@class APWritePacket;

@interface APReceipt : APNetElement {
    UInt32 _sessionID;
    UInt16 _statusCode;   // 状态码
    UInt32 _toUid;
}

@property (nonatomic, assign) UInt32 sessionID;
@property (nonatomic, assign) UInt16 statusCode;
@property (nonatomic, assign) UInt32 toUid;

- (id)initWithDispatch:(APDispatch *)dispatch;

@end
