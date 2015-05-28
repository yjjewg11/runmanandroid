package com.company.runman.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import com.company.news.validate.CommonsValidate;
import com.company.news.vo.TimeScheduleRelationVO;
import com.company.runman.R;
import com.company.runman.utils.IntentUtils;
import com.company.runman.utils.Tool;
import com.company.runman.utils.TraceUtil;
import com.company.runman.widget.DialogUtils;

import java.util.List;

/**
 */
public class TimesScheduleRelationWeekShowItemAdapter extends DefaultAdapter {
    public static String TAG="TimesScheduleRelationWeekShowItemAdapter";
    private Context context;
    private LayoutInflater inflater;
    private int id;

    private Object lastItem;
    public TimesScheduleRelationWeekShowItemAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public TimesScheduleRelationWeekShowItemAdapter(int item, Context context, List data){
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

        HolderView holderView;
        //优化ListView
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.times_schedule_relation_week_show_item, null);
            HolderView h=new HolderView();
            h.textView_start_end_time=(EditText)convertView.findViewById(R.id.textView_start_end_time);

            TimeScheduleRelationVO d=(TimeScheduleRelationVO)this.mList.get(position);

            try{
                h.textView_start_end_time.setText(Tool.objectToString(d.getStart_time())+"-"+Tool.objectToString(d.getEnd_time()));
                if(d.getDays()!=null){
                    String[] tmpStrArr=d.getDays().split(",");
                    for(int i=0;i<tmpStrArr.length;i++){
                        String tmpStr=tmpStrArr[i];
                        if(CommonsValidate.isDecimal(tmpStr)){
                            switch (Integer.valueOf(tmpStr)){
                                case 1:
                                    convertView.findViewById(R.id.checkBox_week1).setOnClickListener(new WeekOnClick(d));
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week1)).setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week2)).setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week3)).setVisibility(View.VISIBLE);;
                                    break;
                                case 4:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week4)).setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week5)).setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week6)).setVisibility(View.VISIBLE);
                                    break;
                                case 7:
                                    ((CheckBox)convertView.findViewById(R.id.checkBox_week7)).setVisibility(View.VISIBLE);
                                    break;

                            }
                        }

                    }
                }

            }catch (Exception ex){
                TraceUtil.traceThrowableLog( ex);
            }

            convertView.setTag(h);
        }
        return convertView;
    }

    class HolderView {
        private EditText textView_start_end_time;


    }

    public class WeekOnClick implements AdapterView.OnClickListener{
        TimeScheduleRelationVO d;

        public WeekOnClick(TimeScheduleRelationVO d) {
            this.d = d;
        }

        @Override
        public void onClick(View view) {
            IntentUtils.startTrainingCourseDetailSubscribeActivity(context, d);
        }
    }

}
