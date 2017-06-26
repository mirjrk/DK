package com.zesium.android.betting.ui.user;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.user.BaseUserSecurityModel;
import com.zesium.android.betting.model.user.Question;
import com.zesium.android.betting.model.user.UserSecurityResponse;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SFFontTextView;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.model.util.WSUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecurityQuestionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = SecurityQuestionFragment.class.getSimpleName();
    private int mSecurityQuestionId;
    private String mSecurityQuestionAnswer;
    private TextInputLayout tilSecurityAnswer;
    private TextInputLayout tilSecurityQuestion;
    private SFFontEditText etSecurityQuestion;
    private SFFontEditText etSecurityAnswer;
    private Button btnChange;
    private ProgressDialog mProgressDialog;
    private final Map<String, Question> mSecurityQuestions = new HashMap<>();
    private Toast mToast;
    private String choseSecurityQuestionTxt;
    private Call<JsonArray> securityQuestionsRequest;
    private Call<UserSecurityResponse> changeSecurityQuestionRequest;

    public SecurityQuestionFragment() {
        // Required empty public constructor
    }

    public static SecurityQuestionFragment newInstance(int securityQuestionId, String securityAnswer) {

        SecurityQuestionFragment fragment = new SecurityQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(AppConstants.SECURITY_QUESTION_ID, securityQuestionId);
        args.putString(AppConstants.SECURITY_QUESTION_ANSWER, securityAnswer);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSecurityQuestionId = getArguments().getInt(AppConstants.SECURITY_QUESTION_ID);
            mSecurityQuestionAnswer = getArguments().getString(AppConstants.SECURITY_QUESTION_ANSWER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_security_question, container, false);

        initialiseFields(view);

        setTranslationsForOtherStringValues();

        fillFieldsWithData();

        createGetSecurityQuestionsRequest(true);

        btnChange.setOnClickListener(this);
        etSecurityQuestion.setOnClickListener(this);

        return view;
    }

    private void initialiseFields(View view) {
        etSecurityQuestion = (SFFontEditText) view.findViewById(R.id.et_security_question);
        etSecurityAnswer = (SFFontEditText) view.findViewById(R.id.et_security_answer);

        tilSecurityAnswer = (TextInputLayout) view.findViewById(R.id.til_security_answer);
        tilSecurityQuestion = (TextInputLayout) view.findViewById(R.id.til_security_question);

        btnChange = (Button) view.findViewById(R.id.btn_change);
    }

    private void fillFieldsWithData() {
        String secQuestion;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SECURITY_QUESTION)) {
            secQuestion = BetXApplication.translationMap.get(TranslationConstants.SECURITY_QUESTION);
        } else {
            secQuestion = getActivity().getString(R.string.security_question);
        }
        tilSecurityQuestion.setHint(secQuestion);

        String secAnswer;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_SECURITY_ANSWER_LABEL)) {
            secAnswer = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_SECURITY_ANSWER_LABEL);
        } else {
            secAnswer = getActivity().getString(R.string.security_answer);
        }
        tilSecurityAnswer.setHint(secAnswer);

        String change;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CHANGE)) {
            change = BetXApplication.translationMap.get(TranslationConstants.CHANGE);
        } else {
            change = getActivity().getString(R.string.change);
        }
        btnChange.setText(change);
    }

    /**
     * Method that calls web service for security securityQuestionsRequest and returns list of them.
     *
     * @param isInitialCall boolean value to mark if select question dialog should appear
     */
    private void createGetSecurityQuestionsRequest(final boolean isInitialCall) {

        securityQuestionsRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getSecurityQuestions(Preferences.getLanguage(getActivity()));
        securityQuestionsRequest.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray questions = response.body();
                if (questions != null) {
                    for (JsonElement questionObj : questions.getAsJsonArray()) {
                        JsonObject questionData = questionObj.getAsJsonObject();
                        Question question = WSUtils.parseQuestionData(questionData);
                        if (question != null) {
                            mSecurityQuestions.put(question.getQuestion(), question);
                        }
                    }

                    fillSecurityQuestion();
                    etSecurityAnswer.setText(mSecurityQuestionAnswer);

                    if (!isInitialCall && mSecurityQuestions.size() > 0) {
                        selectSecurityQuestionDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getContext(), new Toast(getContext()), getActivity().getString(message));
                }

            }
        });
    }

    /**
     * Method fillSecurityQuestion fills users security question data by id that is retrieved from server side.
     *
     */
    private void fillSecurityQuestion() {
        if (mSecurityQuestions.size() > 0 && etSecurityQuestion.getText().length() <= 0) {
            for (Question questions : mSecurityQuestions.values()) {
                if (questions.getId() == mSecurityQuestionId) {
                    etSecurityQuestion.setText(questions.getQuestion());
                    break;
                }
            }
        }
    }

    /**
     * Dialog for selecting security question from list.
     */
    private void selectSecurityQuestionDialog() {
        SFFontTextView tvTitle = new SFFontTextView(getActivity());

        int padding_edges_in_dp = 16;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_edges_in_dp * scale + 0.5f);

        tvTitle.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tvTitle.setText(choseSecurityQuestionTxt);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCustomTitle(tvTitle);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1);
        final ArrayList<String> filterList = new ArrayList<>();

        for (Question question : mSecurityQuestions.values()) {
            filterList.add(question.getQuestion());
        }
        arrayAdapter.addAll(filterList);
        builder.setAdapter(arrayAdapter, (dialog, which) -> {
            String selectedQuestion = filterList.get(which);
            if (!selectedQuestion.equals(etSecurityQuestion.getText().toString())) {
                etSecurityAnswer.setText("");
            }
            etSecurityQuestion.setText(selectedQuestion);
            etSecurityAnswer.setEnabled(true);
            etSecurityAnswer.setFocusable(true);
            etSecurityAnswer.setFocusableInTouchMode(true);

        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Method that calls web service for security securityQuestionsRequest and returns list of them.
     */
    private void createChangeSecurityQuestionRequest() {

        final BaseUserSecurityModel baseUserSecurityModel = new BaseUserSecurityModel();
        baseUserSecurityModel.setSecurityAnswer(etSecurityAnswer.getText().toString());

        Question question = mSecurityQuestions.get(etSecurityQuestion.getText().toString());
        baseUserSecurityModel.setSecurityQuestionId(question.getId());

        changeSecurityQuestionRequest = NetworkHelper.getBetXService(AppConstants.USER_API).changeSecurityQuestion(Preferences.getUserToken(getActivity()), Preferences.getTerminalId(getActivity()), baseUserSecurityModel);
        changeSecurityQuestionRequest.enqueue(new Callback<UserSecurityResponse>() {

            @Override
            public void onResponse(Call<UserSecurityResponse> call, Response<UserSecurityResponse> response) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    cancelProgressDialog();
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //refreshTokenRequest(getApp());

                        AppUtils.showDialogUnauthorized(getActivity());
                    } else {
                        UserSecurityResponse responseFromServer = response.body();
                        if (responseFromServer != null) {
                            if (responseFromServer.isSuccess()) {
                                String message;
                                if (BetXApplication.translationMap.containsKey(TranslationConstants.SECURITY_INFO_SUCESFULLY_CHANGED)) {
                                    message = BetXApplication.translationMap.get(TranslationConstants.SECURITY_INFO_SUCESFULLY_CHANGED);
                                } else {
                                    message = getActivity().getString(R.string.security_data_changed);
                                }
                                AppUtils.createToastMessage(getActivity(), mToast, message);
                                mSecurityQuestionAnswer = baseUserSecurityModel.getSecurityAnswer();
                                mSecurityQuestionId = baseUserSecurityModel.getSecurityQuestionId();
                            } else {
                                AppUtils.createToastMessage(getActivity(), mToast, "Problem occurred during updating your security settings. Please try again!!");
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserSecurityResponse> call, Throwable t) {
                AppLogger.error(TAG, "Exception occured with message: ", t);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getString(message));
                }
            }
        });

        // Show progress dialog
        mProgressDialog = new ProgressDialog(getActivity());
        String message;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT)) {
            message = BetXApplication.translationMap.get(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT);
        } else {
            message = getString(R.string.title_activity_registration);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_security_question:
                tilSecurityQuestion.setError("");
                if (mSecurityQuestions.size() > 0) {
                    selectSecurityQuestionDialog();
                } else {
                    if (NetworkHelper.isNetworkAvailable(getActivity())) {
                        createGetSecurityQuestionsRequest(false);
                    } else {
                        AppUtils.createToastMessage(getActivity(), mToast, getResources().getString(R.string.check_internet));
                    }
                }
                break;
            case R.id.btn_change:
                if (NetworkHelper.isNetworkAvailable(getActivity())) {
                    verifyFields();
                } else {
                    AppUtils.createToastMessage(getActivity(), mToast, getResources().getString(R.string.check_internet));
                }
                break;
        }
    }

    private void verifyFields() {
        int currentQuestionId = 0;
        for (Question question : mSecurityQuestions.values()) {
            if (etSecurityQuestion.getText().toString().equals(question.getQuestion())) {
                currentQuestionId = question.getId();
                break;
            }
        }

        if (etSecurityAnswer.getText().toString().isEmpty()) {
            String securityAnswerError;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_FORM_SECURITY_ANSWER_PLACEHOLDER)) {
                securityAnswerError = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_FORM_SECURITY_ANSWER_PLACEHOLDER);
            } else {
                securityAnswerError = getString(R.string.error_security_answer);
            }
            tilSecurityAnswer.setError(securityAnswerError);
        } else {
            tilSecurityAnswer.setError("");
            if (!etSecurityAnswer.getText().toString().equals(mSecurityQuestionAnswer)
                    || mSecurityQuestionId != currentQuestionId) {
                createChangeSecurityQuestionRequest();
            }
        }
    }

    private void cancelProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Method that translates strings depending on chosen language.
     * If string's translation is not available string will get it's value from strings.xml file.
     */
    private void setTranslationsForOtherStringValues() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SECURITY_QUESTION)) {
            choseSecurityQuestionTxt = BetXApplication.translationMap.get(TranslationConstants.SECURITY_QUESTION);
        } else {
            choseSecurityQuestionTxt = getResources().getString(R.string.change_security_question);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (securityQuestionsRequest != null) {
            securityQuestionsRequest.cancel();
        }
        if (changeSecurityQuestionRequest != null) {
            changeSecurityQuestionRequest.cancel();
        }

    }


}
