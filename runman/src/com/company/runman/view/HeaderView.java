package com.company.runman.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.company.runman.R;

/**
 * Created by Edison on 2014/6/4.
 */
public class HeaderView extends RelativeLayout implements View.OnClickListener {
    private Context mContext;
    private View mBack, mMore;
    private TextView mTitle;

    public HeaderView(Context context) {
        super(context);
        getUI(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getUI(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        getUI(context);
    }

    private void getUI(Context context) {
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.header_layout, this, true);
        mTitle = (TextView) findViewById(R.id.headerTitle);
        mBack = findViewById(R.id.headerBack);
        mMore = findViewById(R.id.headerMore);
        mBack.setOnClickListener(this);
        mMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.headerBack:
                ((Activity)mContext).finish();
                break;
        }
    }

    /**
     * 设置顶部标题
     * @param title
     */
    public void setHeaderTitle(String title) {
        mTitle.setText(title);
    }
}
