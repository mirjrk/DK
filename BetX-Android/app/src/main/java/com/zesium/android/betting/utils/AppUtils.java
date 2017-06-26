package com.zesium.android.betting.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.ui.main.MainActivity;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import static android.R.string.ok;

/**
 * AppUtils class that has all constants, static methods and fields that are used in multiple classes.
 * <p/>
 * Created by Ivan Panic on 12/24/2015.
 */
public class AppUtils {

    /**
     * This methods hides keyboard from phone screen
     */
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Check if entered email data is in valid format.
     *
     * @param target text entered for email
     * @return boolean value if email is valid.
     */
    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * Method createToastMessage is canceling previously created Toast message, if exists and
     * creates new one.
     *
     * @param text String value that represents text that will appear in Toast message.
     */
    public static void createToastMessage(Context context, Toast toast, String text) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Method for adjusting width and height of alert dialog.
     *
     * @param context     context
     * @param alertDialog alert dialog to adjust.
     */
    public static void adjustDialog(Context context, android.support.v7.app.AlertDialog alertDialog) {
        Window w = alertDialog.getWindow();
        int current_height = 0;
        int current_width = 0;
        if (w != null) {
            w.setGravity(Gravity.CENTER);
            current_width = w.getDecorView().getWidth();
            current_height = w.getDecorView().getHeight();
        }

        WindowManager.LayoutParams lp = w.getAttributes();

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int max_width = (int) (dm.widthPixels * 0.85);
        int max_height = (int) (dm.heightPixels * 0.85);

        if (current_width > max_width) {
            lp.width = max_width;
        }
        if (current_height > max_height) {
            lp.height = max_height;
        }

        w.setAttributes(lp);
    }

    /**
     * Method sendDetailMatchOfferBroadcast notifies other subscribers for selected/deselected bets.
     *
     * @param context  context value of application,
     * @param offerId  id of offer which is selected/deselected,
     * @param oddId    id of odd which is selected/deselected,
     * @param receiver name of the receiver which will be notified.
     */
    public static void sendDetailMatchOfferBroadcast(Context context, int offerId, int oddId, String receiver) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(AppConstants.BROADCAST_DETAIL_OFFER);
        intent.putExtra(AppConstants.BROADCAST_OFFER_ID, offerId);
        intent.putExtra(AppConstants.BROADCAST_ODD_ID, oddId);
        intent.putExtra(AppConstants.BROADCAST_TAB_TO_NOTIFY, receiver);
        context.sendBroadcast(intent);
    }

    /**
     * Method that notifies broadcast to update offer list.
     *
     * @param context context value of application,
     */
    public static void sendUpdateListBroadcast(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(AppConstants.BROADCAST_UPDATE_LIST);
        intent.putExtra(AppConstants.STATISTICS, true);
        context.sendBroadcast(intent);
    }

    /**
     * Method that notifies broadcast to show list of banks.
     *
     * @param context context value of application,
     */
    public static void showBankList(Context context, boolean stop) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction(AppConstants.BROADCAST_BANKS_LIST);
        intent.putExtra(AppConstants.BANK_LIST, stop);
        context.sendBroadcast(intent);
    }

    /**
     * Method for creating alert dialog that pop up when user click on button.
     *
     * @param message   text to be displayed in dialog
     * @param context   context value of application,
     * @param darkTheme if it's true darkTheme is active, if it's false dark theme is active
     */
    public static void showDialogWithMessage(final String message, Context context, boolean darkTheme, boolean isCancelable) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_with_message_withdraw, null);
        SFFontTextView tvMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);

        AlertDialog.Builder builder;
        if (darkTheme) {
            builder = new AlertDialog.Builder(context, R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new AlertDialog.Builder(context, R.style.WhiteBackgroundFilterDialog).setView(layout);
        }

        final AlertDialog alertDialog = builder.setPositiveButton(ok,
                (dialog, which) -> dialog.dismiss()).create();

        alertDialog.show();
        alertDialog.setCancelable(isCancelable);
        //setting width and height
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                    () -> AppUtils.adjustDialog(context, alertDialog));
        }
    }

    public static void showDialogUnauthorized(Context context) {

        String tokenExpired;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.TOKEN_EXPIRED)) {
            tokenExpired = BetXApplication.translationMap.get(TranslationConstants.TOKEN_EXPIRED);
        } else {
            tokenExpired = context.getString(R.string.token_expired);
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_with_message_withdraw, null);
        SFFontTextView tvMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(tokenExpired);

        AlertDialog.Builder builder;
        if (Preferences.getTheme(context)) {
            builder = new AlertDialog.Builder(context, R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new AlertDialog.Builder(context, R.style.WhiteBackgroundFilterDialog).setView(layout);
        }

        final AlertDialog alertDialog = builder.setPositiveButton(ok,
                (dialog, which) -> {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra(AppConstants.IS_TOKEN_EXPIRED, true);
                    context.startActivity(intent);
                }).create();

        alertDialog.show();
        alertDialog.setCancelable(true);
        //setting width and height
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                    () -> AppUtils.adjustDialog(context, alertDialog));
        }
    }

    /**
     * Method that selects/deselects checkbox by clicking on its parent layout
     */
    public static void selectCheckBox(LinearLayout linearLayout, CheckBox checkBox) {
        linearLayout.setOnClickListener(v -> {
            if (!checkBox.isChecked()) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
        });
    }
}
