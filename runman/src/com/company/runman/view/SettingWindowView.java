package com.company.runman.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.company.runman.R;

/**
 * Created by Edison on 2014/6/4.
 */
public class SettingWindowView extends LinearLayout implements View.OnClickListener {

    private Context mContext;
    private View mAccountItem;
    private View mCollectionItem;
    private View mSettingItem;
    private View mFeedbackItem;

    public SettingWindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getUI(context);
    }

    private void getUI(Context context) {
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.setting_layout, this, true);
        mAccountItem = findViewById(R.id.setting_window_account_item);
        mAccountItem.setOnClickListener(this);
        mCollectionItem = findViewById(R.id.setting_window_collection_item);
        mCollectionItem.setOnClickListener(this);
        mSettingItem = findViewById(R.id.setting_window_setting_item);
        mSettingItem.setOnClickListener(this);
        mFeedbackItem = findViewById(R.id.setting_window_feedback_item);
        mFeedbackItem.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}
