//
//  Communication.h
//  OneTraffic
//
//  Created by Stefan Andric on 12/22/15.
//  Copyright © 2015 Zesium. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Communication : NSObject

+ (void)signUpWithParameters:(NSDictionary *)parameters
             successfulBlock:(void (^)(NSDictionary *response))completionHandler
                  errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)sendSMSVerificationCodeWithParameters:(NSDictionary *)parameters
                              successfulBlock:(void (^)(NSDictionary *response))completionHandler
                                   errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)resendSMSVerificationCodeWithParameters:(NSDictionary *)parameters

                                successfulBlock:(void (^)(NSDictionary *response))completionHandler
                                     errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)getSettingsWithParameters:(NSDictionary *)parameters
                  successfulBlock:(void (^)(NSDictionary *response))completionHandler
                       errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)updateSettingsWithParameters:(NSDictionary *)parameters
                     successfulBlock:(void (^)(NSDictionary *response))completionHandler
                          errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)getProfileWithParameters:(NSDictionary *)parameters
                 successfulBlock:(void (^)(NSDictionary *response))completionHandler
                      errorBlock:(void (^)(NSDictionary *error))errorHandler;


+ (void)pingServerWithParameters:(NSDictionary *)parameters
                 successfulBlock:(void (^)(NSDictionary *response))completionHandler
                      errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)pingServerInDestinationModeWithParameters:(NSDictionary *)parameters
                                  successfulBlock:(void (^)(NSDictionary *response))completionHandler
                                       errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)updateProfileWithProfileImage:(NSData *)profileImage
                        andParameters:(NSDictionary *)parameters
                              andName:(NSString *)imageName
                isProfileImageChanged:(BOOL)isImageOfProfileChanged
                      successfulBlock:(void (^)(NSDictionary *response))completionHandler
                           errorBlock:(void (^)(NSDictionary *error))errorHandler;


+ (void)setDestinationWithParameters:(NSDictionary *)parameters
                     successfulBlock:(void (^)(NSDictionary *response))completionHandler
                          errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)addDestinationWithParameters:(NSDictionary *)parameters
                     successfulBlock:(void (^)(NSDictionary *response))completionHandler
                          errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)editDestinationWithParameters:(NSDictionary *)parameters
                      successfulBlock:(void (^)(NSDictionary *response))completionHandler
                           errorBlock:(void (^)(NSDictionary *error))errorHandler;


+ (void)removeDestinationWithParameters:(NSDictionary *)parameters
                        successfulBlock:(void (^)(NSDictionary *response))completionHandler
                             errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)getDestinationListWithParameters:(NSDictionary *)parameters
                         successfulBlock:(void (^)(NSDictionary *response))completionHandler
                              errorBlock:(void (^)(NSDictionary *error))errorHandler;


+ (void)getRouteToDestinationListWithParameters:(NSDictionary *)parameters
                                successfulBlock:(void (^)(NSDictionary *response))completionHandler
                                     errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)addEventWithParameters:(NSDictionary *)parameters
               successfulBlock:(void (^)(NSDictionary *response))completionHandler
                    errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)syncWithOtContacts:(NSDictionary *)parameters
          successfulBlock:(void (^)(NSDictionary *response))completionHandler
               errorBlock:(void (^)(NSDictionary *error))errorHandler;


+(void)requestForLocationSharing:(NSDictionary *)parameters
                 successfulBlock:(void (^)(NSDictionary *response))completionHandler
                      errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)getNewMessages:(void (^)(NSDictionary *response))completionHandler
           errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)getEventComments:(int)eventId
            succesBlock:(void (^)(NSDictionary *response))completionHandler
             errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)addComment:(NSDictionary *)dict
      succesBlock:(void (^)(NSDictionary *response))completionHandler
       errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)setApprovalComment:(NSDictionary *)dict
             successBlock:(void (^)(NSDictionary *response))completionHandler
               errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)logInWithParameters:(NSDictionary *)parameters
              successBlock:(void (^)(NSDictionary *response))completionHandler
                errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)getFriends:(void (^)(NSDictionary *response))completionHandler
       errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)friendRequestResponse:(NSDictionary *)parameters
                successBlock:(void (^)(NSDictionary *response))completionHandler
                  errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)inviteFriends:(NSDictionary *)parameters
        successBlock:(void (^)(NSDictionary *response))completionHandler
          errorBlock:(void (^)(NSDictionary *error))errorHandler;


+(void)getChannels:(void (^)(NSDictionary *response))completionHandler
        errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)getChannelMessages:(NSDictionary*)parameters successBlock:(void (^)(NSDictionary *response))completionHandler
               errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)addChannelComment:(NSDictionary *)dict
             succesBlock:(void (^)(NSDictionary *response))completionHandler
              errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)getAddress:(NSDictionary *)dict
     successBlock:(void (^)(NSDictionary *response))completionHandler
       errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)getCoordinates:(NSDictionary *)dict
         successBlock:(void (^)(NSDictionary *response))completionHandler
           errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)changeNameOfGroup:(NSDictionary *)dict
            successBlock:(void (^)(NSDictionary *response))completionHandler
              errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)changePassword:(NSDictionary *)dict
          successBlock:(void (^) (NSDictionary *response))completionHandler
            errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)getAllAddresses:(NSDictionary *)parameters
          successBlock:(void (^) (NSDictionary *response))completionHandler
            errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)getProChannels:(NSDictionary *)parameters
         successBlock:(void (^)(NSDictionary *response))successHandler
           errorBlock:(void (^)(NSDictionary *error))errorHandler;

+(void)getAllMessages:(void (^)(NSDictionary *response))completionHandler
           errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)getSingleChannel:(NSDictionary *)dict
            successBlock:(void (^) (NSDictionary *response))completionHandler
              errorBlock:(void (^) (NSDictionary *error))errorHandler;

+ (void)deleteNotificationWithParameters:(NSDictionary *)dict
                            successBlock:(void (^) (NSDictionary *response))completionHandler
                              errorBlock:(void (^) (NSDictionary *error))errorHandler;

+ (void)subscribeToChannelWithParameters:(NSDictionary *)dict
                            successBlock:(void (^) (NSDictionary *response))completionHandler
                              errorBlock:(void (^) (NSDictionary *error))errorHandler;

+ (void)unsubscribeChannelWithParameters:(NSDictionary *)dict
                            successBlock:(void (^) (NSDictionary *response))completionHandler
                              errorBlock:(void (^) (NSDictionary *error))errorHandler;

+ (void)findOTContactWithParameters:(NSDictionary *)dict
                       successBlock:(void (^) (NSDictionary *response))completionHandler
                         errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)getAddressByCoordinates:(NSDictionary *)dict
                  successBlock:(void (^) (NSDictionary *response))completionHandler
                    errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)getPersonalGraphWithSuccessBlock:(void (^) (NSDictionary *response))completionHandler
                             errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)getUserMessages:(NSDictionary *)parameters
      withSuccessBlock:(void (^) (NSDictionary *response))completionHandler
            errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)sendMessage:(NSDictionary *)parameters
  withSuccessBlock:(void (^) (NSDictionary *response))completionHandler
        errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)forgetPassword:(NSDictionary *)parameters
     withSuccessBlock:(void (^) (NSDictionary *response))completionHandler
           errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)cleanHistory:(NSDictionary *)parameters
   withSuccessBlock:(void (^) (NSDictionary *response))completionHandler
         errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)getAddressNumbers:(NSDictionary *)parameters
            successBlock:(void (^) (NSDictionary *response))completionHandler
              errorBlock:(void (^) (NSDictionary *error))errorHandler;

+(void)getLocationSharingUsers:(NSDictionary *)parameters
                  successBlock:(void (^) (NSDictionary *response))completionHandler
                    errorBlock:(void (^) (NSDictionary *error))errorHandler;

+ (void)getAddressesForAutocompleteWithParameters:(NSDictionary *)parameters
                                     successBlock:(void(^)(NSDictionary *response))successHandler
                                       errorBlock:(void(^)(NSDictionary *error))errorHandler;

+ (void)resolveEventWithParameters:(NSDictionary *)parameters
                      successBlock:(void(^)(NSDictionary *response))successHandler
                        errorBlock:(void(^)(NSDictionary *error))errorHandler;

+ (void)friendRequestResponseLSH:(NSDictionary *)parameters
                   successBlock:(void (^)(NSDictionary *response))completionHandler
                     errorBlock:(void (^)(NSDictionary *error))errorHandler;

+ (void)getCommunicatorList:(NSDictionary *)parameters
               successBlock:(void(^)(NSDictionary *response))successHandler
                 errorBlock:(void(^)(NSDictionary *error))errorHandler;

+ (void)getCommunicatorMessages:(NSDictionary *)parameters
                   successBlock:(void(^)(NSDictionary *response))successHandler
                     errorBlock:(void(^)(NSDictionary *error))errorHandler;

@end
