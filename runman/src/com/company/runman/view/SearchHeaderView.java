package com.company.runman.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.company.runman.R;

/**
 * Created by EdisonZhao on 14-8-26.
 * Email:zhaoliangyu@sobey.com
 */
public class SearchHeaderView extends LinearLayout {

    private Context mContext;

    public SearchHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.search_layout, this, true);
        mContext = context;
    }

}
