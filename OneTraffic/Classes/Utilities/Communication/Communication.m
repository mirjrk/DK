//
//  Communication.m
//  OneTraffic
//
//  Created by Stefan Andric on 12/22/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import "Communication.h"
#import <UIKit/UIKit.h>
#import "AFNetworking.h"
#import "Constant.h"

@implementation Communication

+ (void)signUpWithParameters:(NSDictionary *)parameters
             successfulBlock:(void (^)(NSDictionary *response))completionHandler
                  errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/addNewUser", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"JSON: %@", responseObject);
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            
            errorHandler(serializedData);
        }
        else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}

+ (void)sendSMSVerificationCodeWithParameters:(NSDictionary *)parameters
                              successfulBlock:(void (^)(NSDictionary *response))completionHandler
                                   errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/checkSmsCode", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"JSON: %@", responseObject);
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}

+ (void)resendSMSVerificationCodeWithParameters:(NSDictionary *)parameters
                                successfulBlock:(void (^)(NSDictionary *response))completionHandler
                                     errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/resendSmsCode", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSLog(@"JSON: %@", responseObject);
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}

+ (void)getSettingsWithParameters:(NSDictionary *)parameters
                  successfulBlock:(void (^)(NSDictionary *response))completionHandler
                       errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    [manager GET:[NSString stringWithFormat:@"%@/users/updateSettings", PROFILE_URL] parameters:parameters progress:nil success:^
     (NSURLSessionTask *task, id responseObject) {
         NSLog(@"JSON: %@", responseObject);
         completionHandler(responseObject);
     } failure:^(NSURLSessionTask *operation, NSError *error) {
         NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
         
         if (errorData) {
             
             NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
             errorHandler(serializedData);
             
         }else {
             NSDictionary *noData = @{@"noData": @"No data!"};
             errorHandler(noData);
         }
         
         
     }];
    
}

+ (void)updateSettingsWithParameters:(NSDictionary *)parameters
                     successfulBlock:(void (^)(NSDictionary *response))completionHandler
                          errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    [manager GET:[NSString stringWithFormat:@"%@/users/updateSettings", DEFAULT_URL] parameters:parameters progress:nil success:^
     (NSURLSessionTask *task, id responseObject) {
         NSLog(@"JSON: %@", responseObject);
         completionHandler(responseObject);
     } failure:^(NSURLSessionTask *operation, NSError *error) {
         NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
         if (errorData) {
             
             NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
             errorHandler(serializedData);
             
         }else {
             NSDictionary *noData = @{@"noData": @"No data!"};
             errorHandler(noData);
         }
     }];
    
}


+ (void)getProfileWithParameters:(NSDictionary *)parameters
                 successfulBlock:(void (^)(NSDictionary *response))completionHandler
                      errorBlock:(void (^)(NSDictionary *error))errorHandler {
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializerWithReadingOptions:NSJSONReadingAllowFragments];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", [[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/getProfile", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }
        else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
    
}

+ (void)pingServerWithParameters:(NSDictionary *)parameters
                 successfulBlock:(void (^)(NSDictionary *response))completionHandler
                      errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    manager.requestSerializer.timeoutInterval = 7.0f;
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializerWithReadingOptions:NSJSONReadingAllowFragments];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", [[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/ping", TRAFFIC_URL]
       parameters:parameters progress:nil success:^(NSURLSessionTask *task, id responseObject) {
           completionHandler(responseObject);
           
       } failure:^(NSURLSessionTask *operation, NSError *error) {
           NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
           if (errorData!=nil) {
               NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
               errorHandler(serializedData);
               
           }else{
               NSDictionary *serializedData= @{@"error":@"error"};
               
               errorHandler(serializedData);
               
               
           }
           
       }];
}
+ (void)pingServerInDestinationModeWithParameters:(NSDictionary *)parameters
                                  successfulBlock:(void (^)(NSDictionary *response))completionHandler
                                       errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer.timeoutInterval = 7.0f;
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializerWithReadingOptions:NSJSONReadingAllowFragments];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", [[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/ping", TRAFFIC_URL]
       parameters:parameters progress:nil success:^(NSURLSessionTask *task, id responseObject) {
           completionHandler(responseObject);
           
       } failure:^(NSURLSessionTask *operation, NSError *error) {
           NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
           if (errorData!=nil) {
               NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
               errorHandler(serializedData);
               
           }else{
               NSDictionary *serializedData= @{@"error":@"error"};
               
               errorHandler(serializedData);
               
               
           }
           
       }];
}

+ (void)updateProfileWithProfileImage:(NSData *)profileImage
                        andParameters:(NSDictionary *)parameters
                              andName:(NSString *)imageName
                isProfileImageChanged:(BOOL)isImageOfProfileChanged
                      successfulBlock:(void (^)(NSDictionary *response))completionHandler
                           errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", [[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/updateProfile", PROFILE_URL] parameters:parameters constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        if (isImageOfProfileChanged == YES) {
            [formData appendPartWithFileData:profileImage name:@"file" fileName:[NSString stringWithFormat:@"%@.jpg", imageName] mimeType:@"image/jpeg"];
        }
    } progress:nil
          success:^(NSURLSessionDataTask *task, id responseObject) {
              completionHandler(responseObject);
          } failure:^(NSURLSessionDataTask *task, NSError *error) {
              NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
              if (errorData) {
                  
                  NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
                  errorHandler(serializedData);
                  
              }else {
                  NSDictionary *noData = @{@"noData": @"No data!"};
                  errorHandler(noData);
              }
          }];
    
}

+ (void)setDestinationWithParameters:(NSDictionary *)parameters
                     successfulBlock:(void (^)(NSDictionary *response))completionHandler
                          errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];

    [manager POST:[NSString stringWithFormat:@"%@/user/destinations", DEFAULT_URL]
       parameters:parameters progress:nil success:^(NSURLSessionTask *task, id responseObject) {
           completionHandler(responseObject);
       } failure:^(NSURLSessionTask *operation, NSError *error) {
           NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
           if (errorData) {
               
               NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
               errorHandler(serializedData);
               
           }else {
               NSDictionary *noData = @{@"noData": @"No data!"};
               errorHandler(noData);
           }       }];
}

+ (void)addDestinationWithParameters:(NSDictionary *)parameters
                     successfulBlock:(void (^)(NSDictionary *response))completionHandler
                          errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@",[[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/addDestination", TRAFFIC_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        
        completionHandler(responseObject);
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
            
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
    
}
+ (void)removeDestinationWithParameters:(NSDictionary *)parameters
                        successfulBlock:(void (^)(NSDictionary *response))completionHandler
                             errorBlock:(void (^)(NSDictionary *error))errorHandler{
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@",[[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/deleteDestination", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        
        completionHandler(responseObject);
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
            
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
    
    
}
+ (void)editDestinationWithParameters:(NSDictionary *)parameters
                      successfulBlock:(void (^)(NSDictionary *response))completionHandler
                           errorBlock:(void (^)(NSDictionary *error))errorHandler{
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@",[[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/editDestination", TRAFFIC_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        
        completionHandler(responseObject);
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        
        if (errorData) {
            
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
            
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
    
    
    
}

+ (void)getDestinationListWithParameters:(NSDictionary *)parameters
                         successfulBlock:(void (^)(NSDictionary *response))completionHandler
                              errorBlock:(void (^)(NSDictionary *error))errorHandler{
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@",[[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/getDestinations", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        
        completionHandler(responseObject);
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        
        if (errorData) {
            
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
            
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}

+ (void)getRouteToDestinationListWithParameters:(NSDictionary *)parameters
                                successfulBlock:(void (^)(NSDictionary *response))completionHandler
                                     errorBlock:(void (^)(NSDictionary *error))errorHandler{
    
    
    
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@",[[NSUserDefaults standardUserDefaults] objectForKey:TOKEN]] forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/users/getRouteToDestination", TRAFFIC_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        
        completionHandler(responseObject);
        
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
            
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
    
    
    
    
}

+ (void)addEventWithParameters:(NSDictionary *)parameters
               successfulBlock:(void (^)(NSDictionary *response))completionHandler
                    errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token] forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/addEvent", REALTIME_URL] parameters:parameters progress:nil success:^(NSURLSessionTask *task, id responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionTask *operation, NSError *error) {
        errorHandler(@{});
    }];
}


+(void)syncWithOtContacts:(NSDictionary *)parameters
          successfulBlock:(void (^)(NSDictionary *response))completionHandler
               errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager POST:[NSString stringWithFormat:@"%@/users/syncWithOTContacts", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * ask, id responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}

+(void)requestForLocationSharing:(NSDictionary *)parameters
                 successfulBlock:(void (^)(NSDictionary *response))completionHandler
                      errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Accept"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/sendLocationSharingChanges", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * task, id responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * task, NSError * error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}

+(void)getNewMessages:(void (^)(NSDictionary *response))completionHandler
           errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
 
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/getNewMessages", PROFILE_URL] parameters:nil progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}

+(void)getEventComments:(int)eventId
            succesBlock:(void (^)(NSDictionary *response))completionHandler
             errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    NSDictionary *dict = @{@"eventId":@(eventId),
                           @"typeOfUser":@2};
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/getEventComments", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}


+(void)addComment:(NSDictionary *)dict
      succesBlock:(void (^)(NSDictionary *response))completionHandler
       errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/addEventComments", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
    
}

+(void)setApprovalComment:(NSDictionary *)dict
             successBlock:(void (^)(NSDictionary *response))completionHandler
               errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/setApprovalOnEventComments", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}


+(void)logInWithParameters:(NSDictionary *)parameters
              successBlock:(void (^)(NSDictionary *response))completionHandler
                errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/login", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}



+(void)inviteFriends:(NSDictionary *)parameters
        successBlock:(void (^)(NSDictionary *response))completionHandler
          errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    
    [manager POST:[NSString stringWithFormat:@"%@/users/inviteFriends", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        completionHandler(responseObject);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}

+(void)friendRequestResponse:(NSDictionary *)parameters
                successBlock:(void (^)(NSDictionary *response))completionHandler
                  errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    
    [manager POST:[NSString stringWithFormat:@"%@/users/invitationResponse", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        completionHandler(responseObject);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}

+(void)friendRequestResponseLSH:(NSDictionary *)parameters
                   successBlock:(void (^)(NSDictionary *response))completionHandler
                     errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    
    [manager POST:[NSString stringWithFormat:@"%@/users/invitationResponseLSH", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        
        completionHandler(responseObject);
        
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}

+(void)getFriends:(void (^)(NSDictionary *response))completionHandler
       errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/getFriends", PROFILE_URL] parameters:nil progress:nil success:^
     (NSURLSessionTask *task, id responseObject) {
         
         NSLog(@"JSON: %@", responseObject);
         
         completionHandler(responseObject);
         
     } failure:^(NSURLSessionTask *operation, NSError *error) {
         NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
         if (errorData) {
             
             NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
             errorHandler(serializedData);
             
         }else {
             NSDictionary *noData = @{@"noData": @"No data!"};
             errorHandler(noData);
         }     }];}


+(void)getChannels:(void (^)(NSDictionary *response))completionHandler
        errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/getChannels", DEFAULT_URL] parameters:nil progress:nil success:^
     (NSURLSessionTask *task, id responseObject) {
         
         NSLog(@"JSON: %@", responseObject);
         
         completionHandler(responseObject);
         
     } failure:^(NSURLSessionTask *operation, NSError *error) {
         NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
         if (errorData) {
             
             NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
             errorHandler(serializedData);
             
         }else {
             NSDictionary *noData = @{@"noData": @"No data!"};
             errorHandler(noData);
         }     }];
}
+(void)getChannelMessages:(NSDictionary*)parameters successBlock:(void (^)(NSDictionary *response))completionHandler
               errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/getChannelMessages", DEFAULT_URL] parameters:parameters progress:nil success:^
     (NSURLSessionTask *task, id responseObject) {
         
         NSLog(@"JSON: %@", responseObject);
         
         completionHandler(responseObject);
         
     } failure:^(NSURLSessionTask *operation, NSError *error) {
         NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
         if (errorData) {
             
             NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
             errorHandler(serializedData);
             
         }else {
             NSDictionary *noData = @{@"noData": @"No data!"};
             errorHandler(noData);
         }     }];
}

+(void)addChannelComment:(NSDictionary *)dict
             succesBlock:(void (^)(NSDictionary *response))completionHandler
              errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/addChannelMessage", DEFAULT_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask *task, id responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask *task, NSError *error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
    
}

+(void)getAddress:(NSDictionary *)dict
     successBlock:(void (^)(NSDictionary *response))completionHandler
       errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/getAddressBook",DEFAULT_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
    
}

+(void)getCoordinates:(NSDictionary *)dict
         successBlock:(void (^)(NSDictionary *response))completionHandler
           errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/addresses/getCoordinates",DEFAULT_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
    
}

+(void)changeNameOfGroup:(NSDictionary *)dict
            successBlock:(void (^)(NSDictionary *response))completionHandler
              errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/changeGroupName", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}

+ (void)changePassword:(NSDictionary *)dict
          successBlock:(void (^) (NSDictionary *response))completionHandler
            errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/changePassword", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}


+(void)getAllAddresses:(NSDictionary *)parameters
          successBlock:(void (^) (NSDictionary *response))completionHandler
            errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/getAddressBook", DEFAULT_URL] parameters:nil progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}


+(void)getProChannels:(NSDictionary *)parameters
         successBlock:(void (^)(NSDictionary *response))successHandler
           errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager POST:[NSString stringWithFormat:@"%@/channels/getProChannels", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        successHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}


+(void)getAllMessages:(void (^)(NSDictionary *response))completionHandler
           errorBlock:(void (^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/getAllMessages", DEFAULT_URL] parameters:nil progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
}




+ (void)getSingleChannel:(NSDictionary *)dict
            successBlock:(void (^) (NSDictionary *response))completionHandler
              errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/channels/getProChannelPages", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}


+ (void)deleteNotificationWithParameters:(NSDictionary *)dict
                            successBlock:(void (^) (NSDictionary *response))completionHandler
                              errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/deleteMessage", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}


+ (void)subscribeToChannelWithParameters:(NSDictionary *)dict
                            successBlock:(void (^) (NSDictionary *response))completionHandler
                              errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/subscribeOnProChannel", DEFAULT_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}

+ (void)unsubscribeChannelWithParameters:(NSDictionary *)dict
                            successBlock:(void (^) (NSDictionary *response))completionHandler
                              errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/unsubscribeFromProChannel", DEFAULT_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}


+ (void)findOTContactWithParameters:(NSDictionary *)dict
                       successBlock:(void (^) (NSDictionary *response))completionHandler
                         errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/findOTContact", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}


+(void)getAddressByCoordinates:(NSDictionary *)dict
                  successBlock:(void (^) (NSDictionary *response))completionHandler
                    errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    
    [manager POST:[NSString stringWithFormat:@"%@/addresses/getAddress", PROFILE_URL] parameters:dict progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}


+(void)getPersonalGraphWithSuccessBlock:(void (^) (NSDictionary *response))completionHandler
                             errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    
    NSString *userId = [[NSUserDefaults standardUserDefaults] objectForKey:USER_ID];
    
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager POST:[NSString stringWithFormat:@"%@/users/getPersonalGraph", PROFILE_URL] parameters:@{@"userId":userId} progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
    }];
    
}

+(void)getUserMessages:(NSDictionary *)parameters
      withSuccessBlock:(void (^) (NSDictionary *response))completionHandler
            errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/getUserMessagesByGroupId", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}


+(void)sendMessage:(NSDictionary *)parameters
  withSuccessBlock:(void (^) (NSDictionary *response))completionHandler
        errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/sendMessage", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}

+(void)forgetPassword:(NSDictionary *)parameters
     withSuccessBlock:(void (^) (NSDictionary *response))completionHandler
           errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/forgetPassword", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}

+(void)cleanHistory:(NSDictionary *)parameters
   withSuccessBlock:(void (^) (NSDictionary *response))completionHandler
         errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/clearHistory", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}


+(void)getAddressNumbers:(NSDictionary *)parameters
            successBlock:(void (^) (NSDictionary *response))completionHandler
              errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/getAddressNumbers", DEFAULT_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}


+(void)getLocationSharingUsers:(NSDictionary *)parameters
                  successBlock:(void (^) (NSDictionary *response))completionHandler
                    errorBlock:(void (^) (NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/getInitialState", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        completionHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
}



+ (void)getAddressesForAutocompleteWithParameters:(NSDictionary *)parameters
                                     successBlock:(void(^)(NSDictionary *response))successHandler
                                       errorBlock:(void(^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/v1/traffic/getAddresses", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        successHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}


+ (void)resolveEventWithParameters:(NSDictionary *)parameters
                      successBlock:(void(^)(NSDictionary *response))successHandler
                        errorBlock:(void(^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/resolveEvent", DEFAULT_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        successHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}


+ (void)getCommunicatorList:(NSDictionary *)parameters
               successBlock:(void(^)(NSDictionary *response))successHandler
                 errorBlock:(void(^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/getCommunicator", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        successHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}


+ (void)getCommunicatorMessages:(NSDictionary *)parameters
                   successBlock:(void(^)(NSDictionary *response))successHandler
                     errorBlock:(void(^)(NSDictionary *error))errorHandler
{
    NSString *token = [[NSUserDefaults standardUserDefaults]objectForKey:TOKEN];
    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFJSONResponseSerializer serializer];
    [manager.requestSerializer setValue:[NSString stringWithFormat:@"Bearer %@", token]forHTTPHeaderField:@"Authorization"];
    [manager.requestSerializer setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
    
    [manager POST:[NSString stringWithFormat:@"%@/users/getCommunicatorMessages", PROFILE_URL] parameters:parameters progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        successHandler(responseObject);
    } failure:^(NSURLSessionDataTask * _Nullable task, NSError * _Nonnull error) {
        NSData *errorData = error.userInfo[AFNetworkingOperationFailingURLResponseDataErrorKey];
        if (errorData) {
            NSDictionary *serializedData = [NSJSONSerialization JSONObjectWithData: errorData options:kNilOptions error:nil];
            errorHandler(serializedData);
        }else {
            NSDictionary *noData = @{@"noData": @"No data!"};
            errorHandler(noData);
        }
        
    }];
    
}

@end
