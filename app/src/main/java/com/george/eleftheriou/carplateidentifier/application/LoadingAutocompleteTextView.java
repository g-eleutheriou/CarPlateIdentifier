package com.george.eleftheriou.carplateidentifier.application;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.george.eleftheriou.carplateidentifier.Models.RegionModel;
import com.george.eleftheriou.carplateidentifier.R;
import com.george.eleftheriou.carplateidentifier.activities.DetailViewActivity;
import com.george.eleftheriou.carplateidentifier.helpers.JSONHelper;

public class LoadingAutocompleteTextView extends FrameLayout {

    public LoadingAutocompleteTextView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.loading_auto_complete_text_view_layout, this);

        final RegionAutoCompleteTextView textView = findViewById(R.id.text_view);
        final ProgressBar progressIndicator = findViewById(R.id.progress_indicator);

        textView.setProgressIndicator(progressIndicator);
        textView.setThreshold(0);
        textView.setAdapter(new RegionAutoCompleteAdapter(getContext()));
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final RegionModel model = (RegionModel) adapterView.getItemAtPosition(i);

                if (model != null) {
                    textView.setText(model.getCode());

                    final Intent intent = new Intent(context, DetailViewActivity.class);
                    intent.putExtra("regionJSON", JSONHelper.toJSON(model));

                    context.startActivity(intent);
                }
            }
        });
    }

}
