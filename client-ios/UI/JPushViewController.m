//
//  JPushViewController.m
//  PushTalk
//
//  Created by YuanQiang on 12-12-25.
//  Copyright (c) 2012年 YuanQiang. All rights reserved.
//

#import "JPushViewController.h"
#import "APService.h"
#import "APClient.h"
#import "NSString+Digest.h"
#import "APMessage.h"
#import "SBJson.h"
#import "InfoDetailViewController.h"

@interface APService (Privates)
+ (APService *)sharedService;
- (void)addTags:(NSSet *)tags;
- (BOOL)isRegistered;
@end



@interface JPushViewController ()

@end

@implementation JPushViewController

#define HTTP_URL_PREFIX @"http://111.13.48.109:10010/"

- (id)init {
    self = [super init];
    if (self) {
        NSNotificationCenter *defaultCenter = [NSNotificationCenter defaultCenter];
        
        [defaultCenter addObserver:self selector:@selector(networkDidSetup:) name:kAPNetworkDidSetupNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networkDidClose:) name:kAPNetworkDidCloseNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networkDidRegister:) name:kAPNetworkDidRegisterNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networkDidLogin:) name:kAPNetworkDidLoginNotification object:nil];
        [defaultCenter addObserver:self selector:@selector(networkDidReceiveMessage:) name:kAPNetworkDidReceiveMessageNotification object:nil];
    }
    return self;
}

- (void)loadWebView {
    APLogTrace();
    if (nil == _webView ) {
        APLogTrace();
        
        _leftButton = [[UIBarButtonItem alloc] initWithTitle:@"返回" style:UIBarButtonItemStyleDone target:self action:@selector(goBack)];
        _rightButton = [[UIBarButtonItem alloc] initWithTitle:@"信息" style:UIBarButtonItemStyleBordered target:self action:@selector(infoDetail)];
        [_leftButton setEnabled:NO];
        self.navigationItem.leftBarButtonItem = _leftButton;
        self.navigationItem.rightBarButtonItem = _rightButton;
        
        UIView *view = [self view];
        [self setTitle:@"推聊"];
        
        _webView = [[UIWebView alloc] initWithFrame:[view bounds]];
        [_webView setAutoresizingMask:(UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight)];
        [_webView setDelegate:self];
        
        NSString *udid = [APService openUDID];
        NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"%@main?udid=%@", HTTP_URL_PREFIX, udid]];
        
        [_webView loadRequest:[NSURLRequest requestWithURL:url cachePolicy:NSURLRequestReloadIgnoringCacheData timeoutInterval:30]];
        
        [view addSubview:_webView];
        APLogTrace();
    }
}

- (void)goBack {
    [_webView goBack];
}

- (void)infoDetail {
    InfoDetailViewController *viewController = [[InfoDetailViewController alloc] initWithInfo:_info];
    [[self navigationController] pushViewController:viewController animated:YES];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    [self loadWebView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark -

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSMutableURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType {
    NSString *absoluteString = [[request URL] absoluteString];
    
    NSRange range = [absoluteString rangeOfString:@"udid="];
    
    NSMutableString *tempUrl = [[NSMutableString alloc] initWithString:absoluteString];
    NSString *udid = [APService openUDID];
    
    if (range.location == NSNotFound) {
        NSArray *comps = [absoluteString componentsSeparatedByString:@"?"];
        if ([comps count] >= 2) {
            [tempUrl appendFormat:@"&udid=%@",udid];
        } else {
            [tempUrl appendFormat:@"?udid=%@",udid];
        }
        
        NSURL *url = [NSURL URLWithString:tempUrl];
        [request setURL:url];
        [webView loadRequest:request];
        
        return NO;
    }
    
    NSString *chattingPrefix = [HTTP_URL_PREFIX stringByAppendingString:@"chatting"];
    NSString *channelEnterPrefix = [HTTP_URL_PREFIX stringByAppendingString:@"channel/enter"];
    NSString *channelExitPrefix = [HTTP_URL_PREFIX stringByAppendingString:@"channel/exit"];
    
    if ([absoluteString hasPrefix:channelEnterPrefix] || [absoluteString hasPrefix:channelExitPrefix] || [absoluteString hasPrefix:chattingPrefix]) {
        NSThread *thread = [[NSThread alloc] initWithTarget:self selector:@selector(resetAliasAndTags) object:nil];
        [thread start];
    }
    
    return YES;
}

- (void)webViewDidStartLoad:(UIWebView *)webView {
}

- (void)webViewDidFinishLoad:(UIWebView *)webView {
    NSString *title = [webView stringByEvaluatingJavaScriptFromString:@"document.title"];
    [self setTitle:title];
    
    if ([webView canGoBack]) {
        [_leftButton setEnabled:YES];
    } else {
        [_leftButton setEnabled:NO];
    }
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error {
}

#pragma -

#pragma mark -

- (void)networkDidSetup:(NSNotification *)notification {
    APLog(@"%s", __PRETTY_FUNCTION__);
    _info = @"已连接";
}

- (void)networkDidClose:(NSNotification *)notification {
    APLog(@"%s", __PRETTY_FUNCTION__);
    _info = @"未连接。。。";
}

- (void)networkDidRegister:(NSNotification *)notification {
    APLog(@"%s", __PRETTY_FUNCTION__);
    _info = @"已注册";
    [self loadWebView];
}

- (void)networkDidLogin:(NSNotification *)notification {
    APLog(@"%s", __PRETTY_FUNCTION__);
    _info = @"已登录";
    [self loadWebView];
}

- (void)networkDidReceiveMessage:(NSNotification *)notification {
    APLog(@"%s", __PRETTY_FUNCTION__);
    
    NSDictionary *message = [notification userInfo];
    NSMutableDictionary *receiveDic = [[NSMutableDictionary alloc] initWithCapacity:4];
    NSString *title = [message valueForKey:@"title"];
    NSString *content = [message valueForKey:@"content"];
    NSDictionary *extrasDic = [message valueForKey:@"extras"];
    
    [receiveDic setValue:title forKey:@"title"];
    [receiveDic setValue:content forKey:@"message"];
    [receiveDic setValue:extrasDic forKey:@"extras"];
    
    NSString *msg = [receiveDic JSONRepresentation];
    
    [_webView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"receivedMessage('%@');",msg]];
}


- (void)resetAliasAndTags {
    @autoreleasepool {
    
        NSString *URLString = [NSString stringWithFormat:@"%@%@", HTTP_URL_PREFIX, @"api/user"];
        URLString = [URLString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:URLString] cachePolicy:NSURLRequestReloadIgnoringLocalCacheData timeoutInterval:20];
        
        NSMutableString *result = [[NSMutableString alloc] init];
        [result appendFormat:@"udid=%@",[APService openUDID]];
        
        NSData *data = [result dataUsingEncoding:NSUTF8StringEncoding];
        
        [request setHTTPBody:data];
        [request setHTTPMethod:@"POST"];
        
        NSHTTPURLResponse *response = nil;
        NSData *responseData = nil;
        NSError *error = nil;
        
        
        responseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&response error:&error];
        
        if (200 == [response statusCode]) {
            NSString *receiveString = [[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding];
            NSDictionary *dic = [receiveString JSONValue];
            NSString *username = [dic objectForKey:@"username"];
            NSArray *channels = [dic objectForKey:@"channels"];
            ////
            [APService setTags:[NSSet setWithArray:channels]
                         alias:username
              callbackSelector:@selector(tagsAliasCallback)
                        object:self];
        }
    
    }
}
- (void)tagsAliasCallback:(int)iResCode tags:(NSSet*)tags alias:(NSString*)alias {
}

@end
