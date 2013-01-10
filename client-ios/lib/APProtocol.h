//
//  APProtocol.h
//  PushSDK
//
//  Created by LiDong on 12-9-6.
//  Copyright (c) 2012年 HXHG. All rights reserved.
//

#import <Foundation/Foundation.h>


#define PT_REGISTRATION             0           //注册
#define PT_LOGIN                    1           //登录
#define PT_HEARTBEAT                2           //心跳
#define PT_MESSAGE                  3           //消息
#define PT_RECEIPT                  4           //消息回执
#define PT_LOGOUT                   5           //注销
#define PT_SET_ATTRIBUTES           10          //设置标签和别名
#define PT_UPLOAD_DEVICE_TOKEN      13          //device token 上报


typedef struct __PROTOCOL_HEADER {
    UInt8 version;
    UInt16 protocolType;
    UInt16 rid;
} PROTOCOL_HEADER;

////////////////////////////////////////////////////////////////////

@class APWritePacket;
@class APReadPacket;

@interface APNetElement : NSObject {
    UInt8 _version;         // 命令的版本号
    UInt16 _protocolType;   // 命令字，version>128时，占2个字节，否则为1个字节
    UInt16 _rid;            // 请求流水号
}

@property (nonatomic, assign) UInt8 version;
@property (nonatomic, assign) UInt16 protocolType;
@property (nonatomic, assign) UInt16 rid;

- (id)initWithHeader:(const PROTOCOL_HEADER *)header;
- (BOOL)encodeToPacket:(APWritePacket *)writePacket;
- (void)decodeBodyFromPacket:(APReadPacket *)readPacket;

@end

