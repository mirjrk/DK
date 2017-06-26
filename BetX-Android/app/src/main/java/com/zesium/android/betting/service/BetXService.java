package com.zesium.android.betting.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zesium.android.betting.model.BaseUserConfirmModel;
import com.zesium.android.betting.model.configuration.ApplicationUserConfiguration;
import com.zesium.android.betting.model.configuration.BettingConfiguration;
import com.zesium.android.betting.model.configuration.GeneralConfiguration;
import com.zesium.android.betting.model.configuration.WalletConfiguration;
import com.zesium.android.betting.model.forgot_password.ForgottenPasswordAnswerMobileModel;
import com.zesium.android.betting.model.forgot_password.ForgottenPasswordModel;
import com.zesium.android.betting.model.forgot_password.ForgottenPasswordQuestion;
import com.zesium.android.betting.model.payment.ActionBaseResultWithObjectResult;
import com.zesium.android.betting.model.payment.BankInfo;
import com.zesium.android.betting.model.payment.Banks;
import com.zesium.android.betting.model.payment.Countries;
import com.zesium.android.betting.model.payment.DepositCallModel;
import com.zesium.android.betting.model.payment.MoneyTransactionsRequest;
import com.zesium.android.betting.model.payment.PaymentProvidersResponse;
import com.zesium.android.betting.model.payment.WithdrawBody;
import com.zesium.android.betting.model.payment.WithdrawModel;
import com.zesium.android.betting.model.sports.MostPlayedLimitModel;
import com.zesium.android.betting.model.sports.Sport;
import com.zesium.android.betting.model.user.ActionResultBase;
import com.zesium.android.betting.model.user.BaseUserSecurityModel;
import com.zesium.android.betting.model.user.Country;
import com.zesium.android.betting.model.user.User;
import com.zesium.android.betting.model.user.UserDetails;
import com.zesium.android.betting.model.user.UserPasswordChangeModel;
import com.zesium.android.betting.model.user.UserSecurityResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * BetXService class defines all service requests that are used in BetX application.
 * Created by Ivan Panic on 12/17/2015.
 */
public interface BetXService {

    // User web services
    @FormUrlEncoded
    @POST("token")
    Call<User> loginUser(@Field("grant_type") String grantType,
                         @Field("username") String username,
                         @Field("password") String password,
                         @Field("client_id") String clientId,
                         @Field("terminal_type") String terminalType);

    @FormUrlEncoded
    @POST("token")
    Call<User> refreshToken(@Field("grant_type") String grantType,
                            @Field("refresh_token") String refresh_token,
                            @Field("client_id") String clientId,
                            @Field("terminal_type") String terminalType);

    @POST("userdetails")
    Call<UserDetails> getUserDetails(@Header("Authorization") String token,
                                     @Header("TerminalId") String terminalId);

    @FormUrlEncoded
    @POST("userfieldavailability")
    Call<Boolean> userfieldavailability(@Field("UserField") String UserField,
                                        @Field("Value") String Value);

    @GET("common/countries")
    Call<Country> getCountries();

    @GET("common/regions/{countryId}")
    Call<JsonArray> getRegions(@Path("countryId") String countryId);

    @GET("common/cities/region/{regionId}")
    Call<JsonArray> getCities(@Path("regionId") String regionId);

    @GET("common/securityquestions")
    Call<JsonArray> getSecurityQuestions(@Header("Accept-Language") String acceptLanguage);

    @FormUrlEncoded
    @POST("mobile/register")
    Call<ActionResultBase> registerUser(@Header("TerminalId") String terminalId,
                                        @Field("Zipcode") String Zipcode,
                                        @Field("UserPreferences") List<Integer> UserPreferences,
                                        @Field("AcceptTerms") boolean AcceptTerms,
                                        @Field("RecordingPersonalData") boolean RecordingPersonalData,
                                        @Field("FirstName") String FirstName,
                                        @Field("LastName") String LastName,
                                        @Field("Username") String Username,
                                        @Field("Birthdate") String Birthdate,
                                        @Field("Email") String Email,
                                        @Field("EmailConfirm") String EmailConfirm,
                                        @Field("MobileNumber") String MobileNumber,
                                        @Field("CityId") int CityId,
                                        @Field("RegionId") int RegionId,
                                        @Field("CountryId") int CountryId,
                                        @Field("Address") String Address,
                                        @Field("Password") String Password,
                                        @Field("PasswordConfirm") String PasswordConfirm,
                                        @Field("SecurityQuestionId") int SecurityQuestionId,
                                        @Field("SecurityAnswer") String SecurityAnswer);

    @POST("mobile/activationcode/resend")
    Call<Boolean> resendConfirmationCode(@Body String Username);

    @POST("securitysettings")
    @Headers({"Content-Type: application/json"})
    Call<UserSecurityResponse> changeSecurityQuestion(@Header("Authorization") String token,
                                                      @Header("TerminalId") String terminalId,
                                                      @Body BaseUserSecurityModel baseUserSecurityModel);

    @POST("register/confirmation")
    @Headers({"Content-Type: application/json"})
    Call<ActionResultBase> confirmRegistration(@Header("TerminalId") String terminalId,
                                               @Body BaseUserConfirmModel baseUserConfirmModel);

    @POST("userpassword")
    @Headers({"Content-Type: application/json"})
    Call<UserSecurityResponse> changeUserPassword(@Header("Authorization") String token,
                                                  @Header("TerminalId") String terminalId,
                                                  @Body UserPasswordChangeModel userPasswordChangeModel);

    @POST("forgottenpassword")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ForgottenPasswordQuestion> getSecurityQuestion(@Body ForgottenPasswordModel forgottenPasswordModel);

    // Sport web services
    @GET("api/config")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<GeneralConfiguration> getGeneralConfiguration(@Query("Accept-Language") String acceptLanguage);


    @GET("configuration")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<BettingConfiguration> getBettingConfiguration(@Header("Accept-Language") String acceptLanguage,
                                                             @Header("TerminalId") String terminaId);

    @POST("withdraw/configuration")
    Call<WalletConfiguration> getWalletConfiguration(@Header("Authorization") String token,
                                                     @Header("TerminalId") String terminalId,
                                                     @Header("Accept-Language") String acceptLanguage);

    @POST("deposit/providers")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<PaymentProvidersResponse> getDepositProviders(@Header("Authorization") String token,
                                                       @Header("TerminalId") String terminalId,
                                                       @Header("Accept-Language") String acceptLanguage);

    @POST("report/moneytransactions")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<ActionBaseResultWithObjectResult> getMoneyTransactions(@Header("Authorization") String token,
                                                                @Header("TerminalId") String terminalId,
                                                                @Header("Accept-Language") String acceptLanguage,
                                                                @Body MoneyTransactionsRequest moneyTransactionsRequest);

    @POST("offer/v2/mostplayed")
    Observable<List<Sport>> getBestOffer(@Header("Accept-Language") String acceptLanguage,
                                         @Body MostPlayedLimitModel mostPlayedLimitModel);

    @POST("offer/v2/sports/live")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Observable<List<Sport>> getSportForLiveBetting(@Header("Accept-Language") String acceptLanguage,
                                                   @Body MostPlayedLimitModel mostPlayedLimitModel);

    @GET("translation/{languageId}")
    Observable<JsonArray> getTranslation(@Path("languageId") String languageId);

    @POST("mobile/forgottenpassword/answer")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<JsonObject> sendAnswer(@Body ForgottenPasswordAnswerMobileModel forgottenPasswordAnswerMobileModel);

    @GET("/configuration/{terminalId}/application")
    Observable<ApplicationUserConfiguration> getConfiguration(@Header("Accepted-Language") String acceptedLanguage,
                                                              @Path("terminalId") String terminalId);

    @GET("api/common/banks")
    Observable<List<Banks>> getBanks(@Header("Accepted-Language") String acceptLanguage,
                                     @Header("TerminalId") String terminalId,
                                     @Header("Authorization") String authorization);

    @GET("api/common/countries")
    Observable<List<Countries>> getCountries(@Header("Accepted-Language") String acceptLanguage,
                                             @Header("TerminalId") String terminalId,
                                             @Header("Authorization") String authorization);

    @POST("user_api/bankinfo")
    Observable<BankInfo> getBankInfo(@Header("Accepted-Language") String acceptLanguage,
                                     @Header("TerminalId") String terminalId,
                                     @Header("Authorization") String authorization);

    @POST("withdraw")
    @Headers({"Content-Type: application/json;charset=UTF-8"})
    Call<WithdrawModel> withdrawMoney(@Header("Accepted-Language") String acceptLanguage,
                                      @Header("Authorization") String authorization,
                                      @Header("TerminalId") String terminalId,
                                      @Body WithdrawBody withdrawBody);

    @POST("deposit")
    @FormUrlEncoded
    Call<DepositCallModel> depositMoney(@Header("Accepted-Language") String acceptLanguage,
                                        @Header("Authorization") String authorization,
                                        @Header("TerminalId") String terminalId,
                                        @Field("ProviderId") int ProviderId,
                                        @Field("DepositAmount") double DepositAmount,
                                        @Field("SaveCardId") Boolean SaveCardId,
                                        @Field("SelectedBonusId") int SelectedBonusId);


    @POST("validatecode")
    @Headers({"Content-Type: application/json"})
    Call<ActionResultBase> sendReceivedSmsCode(@Header("TerminalId") String terminalId,
                                               @Body BaseUserConfirmModel baseUserConfirmModel);


    @POST("recoverypassword")
    @FormUrlEncoded
    Call<ActionResultBase> sendNewPassword(@Header("TerminalId") String terminalId,
                                           @Field("Password") String Password,
                                           @Field("PasswordConfirm") String PasswordConfirm,
                                           @Field("ActivationCode") String ActivationCode);
}

