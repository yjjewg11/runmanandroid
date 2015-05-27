package com.company.runman.widget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.company.runman.utils.TimeUtils;

import java.util.Calendar;

/**
 */
public class DialogUtils {

    public static void alertErrMsg( Context mContext,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("错误");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //显示时间选择器
    public static void showTimeDialogBind( final EditText dateView) {
        final Context context = dateView.getContext();
        dateView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View actionView, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    String timeStr=dateView.getText().toString();
                    if(timeStr==null)timeStr=TimeUtils.getCurrentHourAndMinute();
                    final int hourOfDay=TimeUtils.getHourByTimeStr(timeStr);
                    final int minute= TimeUtils.getMinuteByTimeStr(timeStr);
                    /**
                     * 实例化一个TimePickerDialog的对象
                     * 第二个参数是一个TimePickerDialog.OnTimeSetListener匿名内部类，当用户选择好时间后点击done会调用里面的onTimeset方法
                     */
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener()
                    {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                        {
                            String hs=hourOfDay+"";
                            if(hs.length()<10)hs="0"+hs;
                            String ms=minute+"";
                            if(hs.length()<10)ms="0"+ms;
                            dateView.setText(hourOfDay + ":" + minute);
                        }
                    }, hourOfDay, minute, true);
                    timePickerDialog.show();
                }
                return true;
            }
        });
    }
        //显示时间选择器
    public static void showDateDialogBind(final EditText dateView) {
        final Context context = dateView.getContext();
        dateView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View actionView, MotionEvent event) {
                /**
                 * MotionEvent.ACTION_DOWN：在第一个点被按下时触发
                 MotionEvent.ACTION_UP:当屏幕上唯一的点被放开时触发
                 MotionEvent.ACTION_POINTER_DOWN:当屏幕上已经有一个点被按住，此时再按下其他点时触发。
                 MotionEvent.ACTION_POINTER_UP:当屏幕上有多个点被按住，松开其中一个点时触发（即非最后一个点被放开时）。
                 MotionEvent.ACTION_MOVE：当有点在屏幕上移动时触发。值得注意的是，由于它的灵敏度很高，而我们的手指又不可能完全静止（即使我们感觉不到移动，但其实我们的手指也在不停地抖动），所以实际的情况是，基本上只要有点在屏幕上，此事件就会一直不停地被触发。
                 */
                if (event.getAction() == MotionEvent.ACTION_UP) {//点击事件
                    String timeStr=dateView.getText().toString();
                    if(timeStr==null)timeStr=TimeUtils.getCurrentDateStr();
                     int year=TimeUtils.getYearByDateStr(timeStr);
                     int monthOfYear= TimeUtils.getMonthByDateStr(timeStr)-1;
                     int dayOfMonth=TimeUtils.getDayByDateStr(timeStr);
                    /**
                     * 实例化一个DatePickerDialog的对象
                     * 第二个参数是一个DatePickerDialog.OnDateSetListener匿名内部类，当用户选择好日期点击done会调用里面的onDateSet方法
                     */
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth)
                        {
                            dateView.setText( year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                        }
                    }, year, monthOfYear, dayOfMonth);

                    datePickerDialog.show();

                }
                return true;
            }
        });
    }
}
