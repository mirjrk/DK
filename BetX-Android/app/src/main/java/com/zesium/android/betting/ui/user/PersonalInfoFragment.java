package com.zesium.android.betting.ui.user;


import android.app.AlertDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.user.City;
import com.zesium.android.betting.model.user.Country;
import com.zesium.android.betting.model.user.Region;
import com.zesium.android.betting.model.user.Result;
import com.zesium.android.betting.model.user.UserDetails;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.model.util.DateAndTimeHelper;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.model.util.WSUtils;
import com.zesium.android.betting.utils.NetworkStateReceiver;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalInfoFragment extends Fragment implements View.OnClickListener, NetworkStateReceiver.NetworkStateReceiverListener {

    private static final String TAG = PersonalInfoFragment.class.getSimpleName();
    // Text Input Layouts that shows error messages
    private TextInputLayout tilStreetNameAndNumber;
    private TextInputLayout tilPostCode;
    private TextInputLayout tilCountry;
    private TextInputLayout tilEmail;
    private TextInputLayout tilRegion;
    private TextInputLayout tilCity;
    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPhoneNumber;

    // Edit text fields that contains values of inserted data
    private SFFontEditText etFirstName;
    private SFFontEditText etLastName;
    private SFFontEditText etStreetNameAndNumber;
    private SFFontEditText etPostCode;
    private SFFontEditText etCountry;
    private SFFontEditText etRegion;
    private SFFontEditText etCity;
    private SFFontEditText etUsername;
    private SFFontEditText etEmail;
    private SFFontEditText etPhoneNumber;
    private SFFontTextView tvDateOfBirth;
    private SFFontTextView tvDateOfBirthTitle;
    private SFFontTextView tvPersonalInformation;
    private SFFontTextView tvChangePersonalData;
    private SFFontTextView tvTermsAndConditions;
    private SFFontTextView tvAddress;
    private Button btnUpdate;
    private LinearLayout llProgressBar;
    private ScrollView svUserData;
    private UserDetails mUserDetails;
    private Toast mToast;
    private Map<String, Result> mCountriesMap = new HashMap<>();
    private Map<String, Region> mRegionsMap = new HashMap<>();
    private Map<String, City> mCitiesMap = new HashMap<>();

    private NetworkStateReceiver networkStateReceiver;
    private Call<Country> countriesRequest;
    private Call<JsonArray> regionsRequest;
    private Call<JsonArray> citiesRequest;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    public static PersonalInfoFragment newInstance(UserDetails userDetails) {

        PersonalInfoFragment fragment = new PersonalInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.USER_DATA, userDetails);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUserDetails = (UserDetails) getArguments().getSerializable(AppConstants.USER_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);

        initializePersonalData(view);
        fillFieldsWithValues();
        fillProfileData(mUserDetails);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkHelper.isNetworkAvailable(getActivity())) {
            createGetCountriesRequest(true);
        } else {
            if (networkStateReceiver == null) {
                networkStateReceiver = new NetworkStateReceiver();
                networkStateReceiver.addListener(this);
                getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
            }
            String checkInternet;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION)) {
                checkInternet = BetXApplication.translationMap.get(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION);
            } else {
                checkInternet = getActivity().getString(R.string.check_internet);
            }
            AppUtils.createToastMessage(getActivity(), mToast, checkInternet);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (networkStateReceiver != null) {
            getActivity().unregisterReceiver(networkStateReceiver);
            networkStateReceiver = null;
        }
    }

    /**
     * Method fillProfileData fills users fields with data that is retrieved from server.
     *
     * @param userDetails Details of user.
     */
    private void fillProfileData(UserDetails userDetails) {

        // Add not editable data
        if (userDetails.getUsername() != null) {
            etUsername.setText(userDetails.getUsername());
        }

        if (userDetails.getFirstName() != null) {
            etFirstName.setText(userDetails.getFirstName());
        }

        if (userDetails.getLastName() != null) {
            etLastName.setText(userDetails.getLastName());
        }

        if (userDetails.getEmail() != null) {
            etEmail.setText(userDetails.getEmail());
        }

        if (userDetails.getMobileNumber() != null) {
            etPhoneNumber.setText(userDetails.getMobileNumber());
        }

        // Add editable data
        if (userDetails.getBirthdate() != null) {
            String date = userDetails.getBirthdate();
            tvDateOfBirth.setText(date);
        }

        if (userDetails.getAddress() != null) {
            etStreetNameAndNumber.setText(userDetails.getAddress());
        }

        if (userDetails.getZipCode() != null) {
            etPostCode.setText(userDetails.getZipCode());
        }

        fillCityData(userDetails.getCityId());

        fillCountryField(userDetails.getCountryId());

        if (userDetails.getRegionId() != 0 && mRegionsMap.size() > 0 && etRegion.getText().length() <= 0) {
            etRegion.setText(mRegionsMap.get(String.valueOf(mUserDetails.getRegionId())).getName());
        }

        if (userDetails.getBirthdate() != null && !userDetails.getBirthdate().equals("")) {
            tvDateOfBirth.setText(parseTextToDate(userDetails.getBirthdate()));
        }

        llProgressBar.setVisibility(View.GONE);
        svUserData.setVisibility(View.VISIBLE);
    }

    /**
     * Method fillCityData fills users city data by id that is retrieved from server side.
     *
     * @param cityId id of city where user lives.
     */
    private void fillCityData(int cityId) {
        if (cityId != 0 && mCitiesMap.size() > 0 && etCity.getText().length() <= 0) {
            for (City city : mCitiesMap.values()) {
                if (city.getId() == mUserDetails.getCityId()) {
                    etCity.setText(city.getName());
                    break;
                }
            }
        }
    }

    /**
     * Method fillCountryField fills users country data by id that is retrieved from server side.
     *
     * @param countryId id of country where user lives.
     */
    private void fillCountryField(int countryId) {
        if (countryId != 0 && mCountriesMap.size() > 0 && etCountry.getText().length() <= 0) {
            for (Result country : mCountriesMap.values()) {
                if (country.getId() == mUserDetails.getCountryId()) {
                    etCountry.setText(country.getName());
                    creteRegionsRequest(country.getName(), true);
                    break;
                }
            }
        }
    }

    /**
     * Method initializePersonalData initializes fields for personal info screen.
     */
    private void initializePersonalData(View view) {
        // Initialize fields
        etEmail = (SFFontEditText) view.findViewById(R.id.et_email);
        etFirstName = (SFFontEditText) view.findViewById(R.id.et_first_name);
        etLastName = (SFFontEditText) view.findViewById(R.id.et_last_name);
        etStreetNameAndNumber = (SFFontEditText) view.findViewById(R.id.et_street);
        etCountry = (SFFontEditText) view.findViewById(R.id.et_country);
        etRegion = (SFFontEditText) view.findViewById(R.id.et_region);
        etCity = (SFFontEditText) view.findViewById(R.id.et_city);
        etPostCode = (SFFontEditText) view.findViewById(R.id.et_post_code);
        etPhoneNumber = (SFFontEditText) view.findViewById(R.id.et_phone_number);
        etUsername = (SFFontEditText) view.findViewById(R.id.et_username);
        tvDateOfBirth = (SFFontTextView) view.findViewById(R.id.tv_date_of_birth);
        tvDateOfBirthTitle = (SFFontTextView) view.findViewById(R.id.tv_date_of_birth_title);
        llProgressBar = (LinearLayout) view.findViewById(R.id.ll_progress_bar);
        svUserData = (ScrollView) view.findViewById(R.id.sv_user_data);
        tvPersonalInformation = (SFFontTextView) view.findViewById(R.id.tv_personal_information);
        tvChangePersonalData = (SFFontTextView) view.findViewById(R.id.tv_change_personal_data);
        tvTermsAndConditions = (SFFontTextView) view.findViewById(R.id.tv_terms_and_conditions);
        tvAddress = (SFFontTextView) view.findViewById(R.id.tv_address_title);

        ProgressBar pbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        pbLoading.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(getActivity(), R.color.primary_red),
                android.graphics.PorterDuff.Mode.SRC_IN);

        tilStreetNameAndNumber = (TextInputLayout) view.findViewById(R.id.til_street);
        tilCountry = (TextInputLayout) view.findViewById(R.id.til_country);
        tilRegion = (TextInputLayout) view.findViewById(R.id.til_region);
        tilCity = (TextInputLayout) view.findViewById(R.id.til_city);
        tilPostCode = (TextInputLayout) view.findViewById(R.id.til_post_code);
        tilEmail = (TextInputLayout) view.findViewById(R.id.til_email);

        tilPhoneNumber = (TextInputLayout) view.findViewById(R.id.til_phone_number);
        tilPostCode = (TextInputLayout) view.findViewById(R.id.til_post_code);
        tilFirstName = (TextInputLayout) view.findViewById(R.id.til_first_name);
        tilLastName = (TextInputLayout) view.findViewById(R.id.til_last_name);
        tilUsername = (TextInputLayout) view.findViewById(R.id.til_username);

        btnUpdate = (Button) view.findViewById(R.id.btn_update);

        etRegion.setOnClickListener(this);
        etCountry.setOnClickListener(this);
        etCity.setOnClickListener(this);

        llProgressBar.setVisibility(View.VISIBLE);
        svUserData.setVisibility(View.GONE);
    }

    private void fillFieldsWithValues() {
        StringBuilder email = new StringBuilder();
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_EMAIL_LABEL)) {
            email.append(BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_EMAIL_LABEL));
        } else {
            email.append(getActivity().getString(R.string.email));
        }
        email.append(": ");
        if (BetXApplication.translationMap.containsKey(TranslationConstants.EMAIL_EXAMPLE)) {
            email.append(BetXApplication.translationMap.get(TranslationConstants.EMAIL_EXAMPLE));
        } else {
            email.append(getActivity().getString(R.string.email_example));
        }
        tilEmail.setHint(email);

        String username;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.USERNAME)) {
            username = BetXApplication.translationMap.get(TranslationConstants.USERNAME);
        } else {
            username = getActivity().getString(R.string.username);
        }
        tilUsername.setHint(username);

        String phoneNumber;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PHONE_NUMBER)) {
            phoneNumber = BetXApplication.translationMap.get(TranslationConstants.PHONE_NUMBER);
        } else {
            phoneNumber = getActivity().getString(R.string.phone_number);
        }
        tilPhoneNumber.setHint(phoneNumber);

        String firstName;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.FIRST_NAME)) {
            firstName = BetXApplication.translationMap.get(TranslationConstants.FIRST_NAME);
        } else {
            firstName = getActivity().getString(R.string.first_name);
        }
        tilFirstName.setHint(firstName);

        String lastName;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LAST_NAME)) {
            lastName = BetXApplication.translationMap.get(TranslationConstants.LAST_NAME);
        } else {
            lastName = getActivity().getString(R.string.last_name);
        }
        tilLastName.setHint(lastName);

        String personalInformation;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PERSONAL_INFORMATION)) {
            personalInformation = BetXApplication.translationMap.get(TranslationConstants.PERSONAL_INFORMATION);
        } else {
            personalInformation = getActivity().getString(R.string.personal_information);
        }
        tvPersonalInformation.setText(personalInformation);

        String dateOfBirth;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_DATE_OF_BIRTH_LABEL)) {
            dateOfBirth = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_DATE_OF_BIRTH_LABEL);
        } else {
            dateOfBirth = getActivity().getString(R.string.date_of_birth);
        }
        tvDateOfBirthTitle.setText(dateOfBirth);

        String changePersonalData;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CHANGE_PERSONAL_DATA_CONTACT_CS)) {
            changePersonalData = BetXApplication.translationMap.get(TranslationConstants.CHANGE_PERSONAL_DATA_CONTACT_CS);
        } else {
            changePersonalData = getActivity().getString(R.string.change_personal_data_contact_cs);
        }
        tvChangePersonalData.setHint(changePersonalData);

        String country;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.COUNTRY)) {
            country = BetXApplication.translationMap.get(TranslationConstants.COUNTRY);
        } else {
            country = getActivity().getString(R.string.country);
        }
        tilCountry.setHint(country);

        String region;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.REGION)) {
            region = BetXApplication.translationMap.get(TranslationConstants.REGION);
        } else {
            region = getActivity().getString(R.string.region);
        }
        tilRegion.setHint(region);

        String city;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CITY)) {
            city = BetXApplication.translationMap.get(TranslationConstants.CITY);
        } else {
            city = getActivity().getString(R.string.city);
        }
        tilCity.setHint(city);

        String postCode;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ZIP_CODE)) {
            postCode = BetXApplication.translationMap.get(TranslationConstants.ZIP_CODE);
        } else {
            postCode = getActivity().getString(R.string.post_code);
        }
        tilPostCode.setHint(postCode);

        String termsAndConditions;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.TERMS_AND_CONDITIONS)) {
            termsAndConditions = BetXApplication.translationMap.get(TranslationConstants.TERMS_AND_CONDITIONS);
        } else {
            termsAndConditions = getActivity().getString(R.string.terms_and_conditions_personal_info);
        }
        tvTermsAndConditions.setText(termsAndConditions);

        String streetNameAndNumber;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_PLACEHOLDER)) {
            streetNameAndNumber = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_PLACEHOLDER);
        } else {
            streetNameAndNumber = getActivity().getString(R.string.street_name_and_number);
        }
        tilStreetNameAndNumber.setHint(streetNameAndNumber);

        String address;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_CAPTION)) {
            address = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_CAPTION);
        } else {
            address = getActivity().getString(R.string.address);
        }
        tvAddress.setText(address);

        String update;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.UPDATE)) {
            update = BetXApplication.translationMap.get(TranslationConstants.UPDATE);
        } else {
            update = getActivity().getString(R.string.update_profile);
        }
        btnUpdate.setText(update);
    }

    /**
     * Method parseTextToDate parses text from date text view to date object.
     *
     * @return Date object that represents selected data.
     */
    private String parseTextToDate(String dateFromServer) {
        String date;
        try {
            DateFormat targetFormat = new SimpleDateFormat(DateAndTimeHelper.RECEIVED_DATE_FORMAT_BEST_OFFER, Locale.getDefault());
            Date tempDate = targetFormat.parse(dateFromServer);

            SimpleDateFormat sdf = new SimpleDateFormat(DateAndTimeHelper.REGISTRATION_DATE, Locale.getDefault());
            date = sdf.format(tempDate);
        } catch (ParseException pe) {
            throw new IllegalArgumentException();
        }
        return date;
    }

    @Override
    public void onClick(View view) {
        String checkInternet;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION)) {
            checkInternet = BetXApplication.translationMap.get(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION);
        } else {
            checkInternet = getActivity().getString(R.string.check_internet);
        }
        switch (view.getId()) {
            case R.id.et_country:
                // tilSecurityQuestion.setError("");
                if (mCountriesMap.size() > 0) {
                    selectCountryDialog();
                } else {
                    if (NetworkHelper.isNetworkAvailable(getActivity())) {
                        createGetCountriesRequest(false);
                    } else {
                        AppUtils.createToastMessage(getActivity(), mToast, checkInternet);
                    }
                }
                break;
            case R.id.et_region:
                tilRegion.setError("");
                if (mRegionsMap.size() > 0) {
                    selectRegionDialog();
                } else {
                    if (NetworkHelper.isNetworkAvailable(getActivity())) {
                        Result country = mCountriesMap.get(etCountry.getText().toString());
                        if (country != null) {
                            creteRegionsRequest(country.getName(), false);
                        }
                    } else {
                        AppUtils.createToastMessage(getActivity(), mToast, checkInternet);
                    }
                }
                break;
            case R.id.et_city:
                tilCity.setError("");
                if (mCitiesMap.size() > 0) {
                    selectCityDialog();
                } else {
                    if (NetworkHelper.isNetworkAvailable(getActivity())) {
                        Region region = mRegionsMap.get(etRegion.getText().toString());
                        if (region != null) {
                            createCityRequest(region.getName(), false);
                        }
                    } else {
                        AppUtils.createToastMessage(getActivity(), mToast, checkInternet);
                    }
                }
                break;
        }
    }


    /**
     * Method that calls web service for citiesRequest in selected region and returns list of them.
     *
     * @param selectedRegion String value which represents previously selected region
     * @param isInitialCall  boolean value to mark if select city dialog should appear
     */
    private void createCityRequest(String selectedRegion, final boolean isInitialCall) {
        Region selectedRegionValue = mRegionsMap.get(selectedRegion);
        citiesRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getCities(String.valueOf(selectedRegionValue.getId()));
        citiesRequest.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray cities = response.body();
                if (mCitiesMap == null) {
                    mCitiesMap = new HashMap<>();
                } else {
                    mCitiesMap.clear();
                }
                if (cities != null) {
                    etCity.setEnabled(true);
                    for (JsonElement cityObj : cities.getAsJsonArray()) {
                        JsonObject cityData = cityObj.getAsJsonObject();
                        City city = WSUtils.parseCityData(cityData);
                        if (city != null) {
                            mCitiesMap.put(city.getName(), city);
                        }
                    }

                    if (mUserDetails != null) {
                        fillCityData(mUserDetails.getCityId());
                    }

                    if (!isInitialCall && mCitiesMap.size() > 0) {
                        selectCityDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AppLogger.error(TAG, "Exception occured with message: ", t);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getString(message));
                }
            }
        });
    }

    /**
     * Method that calls web service for regionsRequest in selected country and returns list of them.
     *
     * @param selectedCountry String value which represents previously selected country
     * @param isInitialCall   boolean value to mark if select region dialog should appear
     */
    private void creteRegionsRequest(String selectedCountry, final boolean isInitialCall) {
        Result selectedCountryResult = mCountriesMap.get(selectedCountry);
        regionsRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getRegions(String.valueOf(selectedCountryResult.getId()));
        regionsRequest.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray regions = response.body();
                if (mRegionsMap == null) {
                    mRegionsMap = new HashMap<>();
                } else {
                    mRegionsMap.clear();
                }
                if (regions != null) {
                    etRegion.setEnabled(true);
                    for (JsonElement regionObj : regions.getAsJsonArray()) {
                        JsonObject regionData = regionObj.getAsJsonObject();
                        Region region = WSUtils.parseRegionData(regionData);
                        if (region != null) {
                            mRegionsMap.put(region.getName(), region);
                        }
                    }

                    if (mUserDetails != null) {
                        if (etRegion.getText().length() <= 0 && mUserDetails != null && mUserDetails.getRegionId() != 0) {
                            for (Region region : mRegionsMap.values()) {
                                if (region.getId() == mUserDetails.getRegionId()) {
                                    etRegion.setText(region.getName());
                                    createCityRequest(region.getName(), true);
                                    break;
                                }
                            }
                        }
                    }
                    if (!isInitialCall && mRegionsMap.size() > 0) {
                        selectRegionDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying to get a list of the regions in the specific country", t);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getActivity().getString(message));
                }
            }
        });
    }

    /**
     * Method that calls web service for countriesRequest and returns list of them.
     *
     * @param isInitialCall boolean value to mark if select country dialog should appear
     */
    private void createGetCountriesRequest(final boolean isInitialCall) {
        countriesRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getCountries();
        countriesRequest.enqueue(new Callback<Country>() {

            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                Country country = response.body();
                if (country != null) {
                    if (country.getResult() != null) {
                        if (mCountriesMap == null) {
                            mCountriesMap = new HashMap<>();
                        }

                        for (Result result : country.getResult()) {
                            mCountriesMap.put(result.getName(), result);
                        }

                        if (mUserDetails != null) {
                            fillCountryField(mUserDetails.getCountryId());
                        }
                        /*if (etCountry.getText().length() <= 0 && mUserDetails != null && mUserDetails.getCountryId() != 0) {
                            etCountry.setText(mCountriesMap.get(String.valueOf(mUserDetails.getCountryId())).getName());
                        }*/

                        if (!isInitialCall && mCountriesMap.size() > 0) {
                            selectCountryDialog();
                        }
                    }
                } else {
                    AppUtils.createToastMessage(getActivity(), mToast, getString(R.string.error_on_server_side));
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying to get a list of countries", t);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getContext(), new Toast(getContext()), getActivity().getString(message));
                }

            }
        });
    }

    /**
     * Dialog for selecting city from citiesRequest list.
     */
    private void selectCityDialog() {
        if (mCitiesMap.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.choose_city));
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line);

            final ArrayList<String> filterList = new ArrayList<>();

            for (City city : mCitiesMap.values()) {
                filterList.add(city.getName());
            }
            arrayAdapter.addAll(filterList);
            builder.setAdapter(arrayAdapter, (dialog, which) -> {
                String selectedCity = filterList.get(which);
                etCity.setText(selectedCity);
                if (mCitiesMap.get(selectedCity).getCityAndZipCode() != null && !mCitiesMap.get(selectedCity).getCityAndZipCode().equals("")) {
                    String postCode = mCitiesMap.get(selectedCity).getCityAndZipCode().replace(" ", "");
                    etPostCode.setText(postCode);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Dialog for selecting country from countriesRequest list.
     */
    private void selectCountryDialog() {
        if (mCountriesMap.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.choose_country));
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line);

            final ArrayList<String> filterList = new ArrayList<>();

            for (Result result : mCountriesMap.values()) {
                filterList.add(result.getName());
            }

            arrayAdapter.addAll(filterList);
            builder.setAdapter(arrayAdapter, (dialog, which) -> {
                etRegion.setText("");
                etRegion.setEnabled(true);

                etCity.setText("");
                etCity.setEnabled(false);

                etPostCode.setText("");
                String selectedCountry = filterList.get(which);
                etCountry.setText(selectedCountry);

                if (NetworkHelper.isNetworkAvailable(getActivity())) {
                    creteRegionsRequest(selectedCountry, true);
                } else {
                    AppUtils.createToastMessage(getActivity(), mToast, getResources().getString(R.string.no_internet_connection_message));
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Dialog for selecting region from regionsRequest list.
     */
    private void selectRegionDialog() {
        if (mRegionsMap.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.choose_region));
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line);

            final ArrayList<String> filterList = new ArrayList<>();

            for (Region region : mRegionsMap.values()) {
                filterList.add(region.getName());
            }
            arrayAdapter.addAll(filterList);
            builder.setAdapter(arrayAdapter, (dialog, which) -> {
                etCity.setText("");
                etCity.setEnabled(false);
                etPostCode.setText("");
                String selectedRegion = filterList.get(which);
                etRegion.setText(selectedRegion);

                if (NetworkHelper.isNetworkAvailable(getActivity())) {
                    createCityRequest(selectedRegion, true);
                } else {
                    String checkInternet;
                    if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION)) {
                        checkInternet = BetXApplication.translationMap.get(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION);
                    } else {
                        checkInternet = getActivity().getString(R.string.check_internet);
                    }
                    AppUtils.createToastMessage(getActivity(), mToast, checkInternet);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void networkAvailable() {
        createGetCountriesRequest(true);
    }

    @Override
    public void networkUnavailable() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (regionsRequest != null) {
            regionsRequest.cancel();
        }
        if (citiesRequest != null) {
            citiesRequest.cancel();
        }
        if (countriesRequest != null) {
            countriesRequest.cancel();
        }
    }
}
