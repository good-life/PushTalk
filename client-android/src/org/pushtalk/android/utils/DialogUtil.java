package org.pushtalk.android.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogUtil {

    
    public static AlertDialog createInfoDialog(
            final Context context, int title, View info) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setView(info);
        dialog.setNegativeButton(android.R.string.cancel, null);
        return dialog.create();
    }
    
    public static AlertDialog createInfoDialog(
            final Context context, int title, CharSequence content) {
        TextView tv = new TextView(context);
        tv.setTextSize(18);
        tv.setAutoLinkMask(Linkify.WEB_URLS);
        tv.setText(content);
        tv.setClickable(false);
        tv.setPadding(15, 0, 0, 0);
        return createInfoDialog(context, title, tv);
    }
    
    public static AlertDialog createConfirmDialog(Context context, 
            DialogInterface.OnClickListener onClickListener, 
            CharSequence title, CharSequence content) {
        return createConfirmDialog(context, onClickListener, null, title, content);
    }

    public static AlertDialog createConfirmDialog(Context context, 
            DialogInterface.OnClickListener positiveClickListener, 
            DialogInterface.OnClickListener negativeClickListener, 
            CharSequence title, CharSequence content) {
        return createDialog(context, 
                positiveClickListener, negativeClickListener, 
                title, content, android.R.string.yes, android.R.string.no);
    }
    
    public static AlertDialog createDialog(Context context, 
            DialogInterface.OnClickListener positiveClickListener, 
            CharSequence title, CharSequence content, 
            int positiveButton) {
        return createDialog(context, positiveClickListener, null, 
                title, content, positiveButton, android.R.string.cancel);
    }
    
    public static AlertDialog createDialog(Context context, 
            DialogInterface.OnClickListener positiveClickListener, 
            DialogInterface.OnClickListener negativeClickListener, 
            CharSequence title, CharSequence content, 
            int positiveButton, int negativeButton) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        TextView tv = new TextView(context);
        tv.setTextSize(18);
        tv.setAutoLinkMask(Linkify.WEB_URLS);
        tv.setText(content);
        tv.setClickable(false);
        tv.setPadding(15, 0, 0, 0);
        dialog.setView(tv);
        dialog.setPositiveButton(positiveButton, positiveClickListener);
        dialog.setNegativeButton(negativeButton, negativeClickListener);
        return dialog.create();
    }
    
    public static void showToast(Context context, int prompt) {
        Toast.makeText(context, prompt, Toast.LENGTH_SHORT).show();
    }
    
    public static void showToast(Context context, String prompt) {
        Toast.makeText(context, prompt, Toast.LENGTH_SHORT).show();
    }
    
    public static void showDefineToast(Context context, int prompt, int bgres) {
    	String str = context.getString(prompt);
    	showDefineToast(context, str, bgres);
    }
    
    public static void showDefineToast(Context context, String prompt, int bgres) {
    	Toast toast = Toast.makeText(context, prompt, Toast.LENGTH_SHORT);
    	TextView textView = new TextView(context);
    	textView.setText(prompt);
    	textView.setTextSize(14);
    	textView.setTextColor(Color.BLACK);
    	toast.setGravity(Gravity.CENTER, 0, 0);
    	
    	LinearLayout linearLayout = new LinearLayout(context);  
    	linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    	linearLayout.setGravity(Gravity.CENTER);
    	LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
    			LinearLayout.LayoutParams.WRAP_CONTENT,textView.getHeight());
    	
    	linearLayout.setLayoutParams(lParams);
    	linearLayout.addView(textView);
    	textView.setGravity(Gravity.CENTER);
    	linearLayout.setBackgroundResource(bgres);
    	toast.setView(linearLayout);
    	toast.show();
    }

    

    
}
