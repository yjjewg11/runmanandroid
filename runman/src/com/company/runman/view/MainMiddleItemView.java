package com.company.runman.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.company.runman.R;

/**
 * Created by star on 2014/6/4.
 */
public class MainMiddleItemView extends LinearLayout implements View.OnClickListener {

    public MainMiddleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainMiddleItemView);
        //获取自定义的属性值
        String text = typedArray.getString(R.styleable.MainMiddleItemView_text);
        Integer drawableId = typedArray.getResourceId(R.styleable.MainMiddleItemView_drawable, 0);
        //重新对界面进行调整赋值
        getUI(context, text, drawableId);
        typedArray.recycle();
    }

    private void getUI(Context context, String text, int drawableId) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.main_middle_item_layout, this, true);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
        TextView textView = (TextView) view.findViewById(R.id.text);
        imageView.setImageResource(drawableId);
        textView.setText(text);
    }

    @Override
    public void onClick(View view) {

    }
}
