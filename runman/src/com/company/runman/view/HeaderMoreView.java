package com.company.runman.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.company.runman.R;

/**
 * Created by EdisonZhao on 14/12/19.
 * Email:zhaoliangyu@sobey.com
 */
public class HeaderMoreView extends LinearLayout implements View.OnClickListener{

    private Context context;
    private PopupWindow popupWindow;
    private View monitorEdit;
    private View scanHistory;

    public HeaderMoreView(Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.header_more_popup_window_layout, this, true);
        findViewById(R.id.popup_window_home).setOnClickListener(this);
        monitorEdit = findViewById(R.id.popup_window_edit);
        scanHistory = findViewById(R.id.popup_window_history);
        scanHistory.setOnClickListener(this);
    }

    public void setParentWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    public View getMonitorEditView() {
        return monitorEdit;
    }

    public View getScanHistory() {
        return scanHistory;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.popup_window_home:
             //   UIControl.startMainActivity(context);
                break;
            case R.id.popup_window_history:
              //  UIControl.startCodeScanHistoryActivity(context);
        }
        if(popupWindow != null) {
            popupWindow.dismiss();
        }
    }
}
