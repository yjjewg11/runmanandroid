package com.company.runman.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.company.news.validate.CommonsValidate;
import com.company.news.vo.TimeScheduleRelationVO;
import com.company.news.vo.TrainingCourseVO;
import com.company.runman.R;
import com.company.runman.utils.TimeUtils;
import com.company.runman.utils.Tool;
import com.company.runman.utils.TraceUtil;
import com.company.runman.utils.Utility;
import com.company.runman.widget.DialogUtils;
import org.apache.commons.lang.Validate;

import java.util.List;

/**
 */
public class TimesScheduleRelationWeekEditItemAdapter extends DefaultAdapter {
    public static String TAG="TimesScheduleRelationWeekEditItemAdapter";
    private Context context;
    private LayoutInflater inflater;
    private int id;

    private Object lastItem;
    public TimesScheduleRelationWeekEditItemAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public TimesScheduleRelationWeekEditItemAdapter(int item, Context context, List data){
        super(context);
        this.context = context;
        this.id=item;
        inflater=LayoutInflater.from(context);
        this.mList=data;
    }
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView h;
        if (convertView == null) {
            convertView=LayoutInflater.from(context).inflate(R.layout.times_schedule_relation_week_edit_item, null);

            h = new HolderView();
            h.start_time=(EditText)convertView.findViewById(R.id.start_time);
            h.end_time=(EditText)convertView.findViewById(R.id.end_time);
            DialogUtils.showTimeDialogBind( h.start_time);
            DialogUtils.showTimeDialogBind(  h.end_time);
            convertView.setTag(h);
        } else {
            h = (HolderView) convertView.getTag();
        };
        TimeScheduleRelationVO d=(TimeScheduleRelationVO)this.mList.get(position);
            try{
                h.start_time.setText(Tool.objectToString(d.getStart_time()));
                h.end_time.setText(Tool.objectToString(d.getEnd_time()));
                if(d.getDays()!=null){
                    String[] tmpStrArr=d.getDays().split(",");
                    for(int i=0;i<tmpStrArr.length;i++){
                        String tmpStr=tmpStrArr[i];
                        if(CommonsValidate.isDecimal(tmpStr)){
                            switch (Integer.valueOf(tmpStr)){
                                case 1:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week1)).setChecked(true);
                                    break;
                                case 2:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week2)).setChecked(true);
                                    break;
                                case 3:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week3)).setChecked(true);
                                    break;
                                case 4:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week4)).setChecked(true);
                                    break;
                                case 5:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week5)).setChecked(true);
                                    break;
                                case 6:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week6)).setChecked(true);
                                    break;
                                case 7:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week7)).setChecked(true);
                                    break;

                            }
                        }

                    }
                }

            }catch (Exception ex){
                TraceUtil.traceThrowableLog( ex);
            }

            convertView.setTag(h);

        return convertView;
    }

    class HolderView {
        private EditText start_time;
        private EditText end_time;


    }

}
