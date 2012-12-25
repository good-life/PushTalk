//
//  APMessage.h
//  PushSDK
//
//  Created by LiDong on 12-9-16.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "APDispatch.h"
#import "APReceipt.h"

@interface APMessageDispatch : APDispatch {
    UInt8 _messageType;
    UInt32 _messageID;
    NSString *_content;
}

@property (nonatomic, assign, readonly) UInt8 messageType;
@property (nonatomic, assign, readonly) UInt32 messageID;
@property (nonatomic, retain, readonly) NSString *content;

@end

@interface APMessageReceipt : APReceipt {
    UInt8 _messageType;
    UInt32 _messageID;
}

- (id)initWithMessageDispatch:(APMessageDispatch *)dispatch;

@end


@interface APMessage : NSObject {
    UInt64 _messageID;
    UInt8 _messageType;
    UInt32 _fromUid;
    NSString *_title;
    NSString *_content;
    UInt64 _time;
}

@property (nonatomic, assign) UInt64 messageID;
@property (nonatomic, assign) UInt8 messageType;
@property (nonatomic, assign) UInt32 fromUid;
@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *content;
@property (nonatomic, assign) UInt64 time;

- (id)initWithMessageDispatch:(APMessageDispatch *)dispatch;

@end
