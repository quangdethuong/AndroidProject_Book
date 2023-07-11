/*
 * Copyright © 2018-present, MNK Group. All rights reserved.
 */

package com.codingstuff.shoeapp;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Class that stores the API keys used by the Stripe library and the URL to the app's server.
 */

public class Utils {

    public static  int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void showSingleButtonAlert(Context context, String title, String message){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.button_ok), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showSingleButtonAlertWithEditableButton(Context context, String title, String message, String buttonText){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonText, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static void showSingleButtonAlertWithoutTitle(Context context, String message){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(context.getResources().getString(R.string.button_ok), null);
        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public static AlertDialog getSingleButtonAlertWithoutTitle(Context context, String message, String buttonText){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
//        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonText, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCancelable(false);
        alertDialog.show();
        return alertDialog;
    }


    public static AlertDialog showDoubleButtonAlert(Context context, String title, String message, String positiveText, String negativeText){
        AlertDialog.Builder builder =new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, null);
        builder.setNegativeButton(negativeText, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }


    public static String formatPrice(double total){
        BigDecimal bd = new BigDecimal(( total - Math.floor( total )) * 100 );
        bd = bd.setScale(4, RoundingMode.HALF_DOWN);

        if (bd.intValue() / 10 != 0 && bd.intValue() % 10 != 0){
            return new DecimalFormat("0.00").format(total);
        } else {
            return new DecimalFormat("0.0").format(total);
        }
    }

    public static String formatError(String error){
        String string = error.replace("[", "");
        string = string.replace("]", "");
        string = string.replace("\"", "");
        return string;
    }

}
