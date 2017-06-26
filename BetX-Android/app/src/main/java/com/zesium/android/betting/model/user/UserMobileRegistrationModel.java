package com.zesium.android.betting.model.user;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 6/30/2016.
 */
public class UserMobileRegistrationModel {
    private String Zipcode;
    private List<Integer> UserPreferences;
    private boolean AcceptTerms;
    private boolean RecordingPersonalData;
    private String FirstName;
    private String LastName;
    private String Username;
    private String Birthdate;
    private String Email;
    private String EmailConfirm;
    private String MobileNumber;
    private int CityId;
    private int RegionId;
    private int CountryId;
    private String Address;
    private String Password;
    private String PasswordConfirm;
    private int SecurityQuestionId;
    private String SecurityAnswer;

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public List<Integer> getUserPreferences() {
        return UserPreferences;
    }

    public void setUserPreferences(List<Integer> userPreferences) {
        UserPreferences = userPreferences;
    }

    public boolean isAcceptTerms() {
        return AcceptTerms;
    }

    public void setAcceptTerms(boolean acceptTerms) {
        AcceptTerms = acceptTerms;
    }

    public boolean isRecordingPersonalData() {
        return RecordingPersonalData;
    }

    public void setRecordingPersonalData(boolean recordingPersonalData) {
        RecordingPersonalData = recordingPersonalData;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getBirthdate() {
        return Birthdate;
    }

    public void setBirthdate(String birthdate) {
        Birthdate = birthdate;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmailConfirm() {
        return EmailConfirm;
    }

    public void setEmailConfirm(String emailConfirm) {
        EmailConfirm = emailConfirm;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getRegionId() {
        return RegionId;
    }

    public void setRegionId(int regionId) {
        RegionId = regionId;
    }

    public int getCountryId() {
        return CountryId;
    }

    public void setCountryId(int countryId) {
        CountryId = countryId;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPasswordConfirm() {
        return PasswordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        PasswordConfirm = passwordConfirm;
    }

    public int getSecurityQuestionId() {
        return SecurityQuestionId;
    }

    public void setSecurityQuestionId(int securityQuestionId) {
        SecurityQuestionId = securityQuestionId;
    }

    public String getSecurityAnswer() {
        return SecurityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        SecurityAnswer = securityAnswer;
    }
}
