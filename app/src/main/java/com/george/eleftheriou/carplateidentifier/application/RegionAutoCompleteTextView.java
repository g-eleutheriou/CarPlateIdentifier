package com.george.eleftheriou.carplateidentifier.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

@SuppressLint("AppCompatCustomView")
public class RegionAutoCompleteTextView extends AutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int INPUT_DELAY = 750;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            RegionAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    private ProgressBar progressIndicator = null;

    public RegionAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgressIndicator(ProgressBar progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (progressIndicator  != null) {
            progressIndicator.setVisibility(View.VISIBLE);
        }

        handler.removeMessages(MESSAGE_TEXT_CHANGED);
        handler.sendMessageDelayed(handler.obtainMessage(MESSAGE_TEXT_CHANGED, text), INPUT_DELAY);
    }

    @Override
    public void onFilterComplete(int count) {
        if (progressIndicator != null) {
            progressIndicator.setVisibility(View.GONE);
        }

        super.onFilterComplete(count);
    }
}
