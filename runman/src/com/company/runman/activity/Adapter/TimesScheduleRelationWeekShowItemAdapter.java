package com.company.runman.activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.company.news.validate.CommonsValidate;
import com.company.news.vo.TimeScheduleRelationVO;
import com.company.news.vo.TrainingCourseVO;
import com.company.runman.R;
import com.company.runman.activity.TrainingCourseDetailSubscribeActivity;
import com.company.runman.utils.*;
import com.company.runman.widget.DialogUtils;
import com.google.zxing.common.StringUtils;

import java.util.Calendar;
import java.util.List;

/**
 */
public class TimesScheduleRelationWeekShowItemAdapter extends DefaultAdapter {
    public static String TAG="TimesScheduleRelationWeekShowItemAdapter";
    private Context context;
    private LayoutInflater inflater;
    private int id;
    private Calendar week1Cal;
     private TrainingCourseVO vo;

    public Calendar getWeek1Cal() {
        return week1Cal;
    }

    public void setWeek1Cal(Calendar week1Cal) {
        this.week1Cal = week1Cal;
    }

    private Object lastItem;
    public TimesScheduleRelationWeekShowItemAdapter(Context context,TrainingCourseVO vo) {
        super(context);
        this.context = context;
        this.vo=vo;
    }
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView h;
        if (convertView == null) {
            h = new HolderView();
            convertView=LayoutInflater.from(context).inflate(R.layout.times_schedule_relation_week_show_item, null);
            h.textView_start_end_time=(TextView)convertView.findViewById(R.id.textView_start_end_time);
            convertView.setTag(h);
        } else {
            h = (HolderView) convertView.getTag();
        };
            TimeScheduleRelationVO d=(TimeScheduleRelationVO)this.mList.get(position);

            try{
                h.textView_start_end_time.setText(Tool.objectToString(d.getStart_time())+"-"+Tool.objectToString(d.getEnd_time()));
                if(d.getDays()!=null){
                    String[] tmpStrArr=d.getDays().split(",");
                    for(int i=0;i<tmpStrArr.length;i++){
                        String tmpStr=tmpStrArr[i];
                        View tmpView=null;
                        if(CommonsValidate.isDecimal(tmpStr)){
                            Integer tmpWeekNumber=Integer.valueOf(tmpStr);
                            switch (tmpWeekNumber){
                                case 1:
                                    tmpView=convertView.findViewById(R.id.Button_week1);
                                    tmpView.setOnClickListener(new WeekOnClick(d,tmpWeekNumber));
                                    tmpView.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    tmpView=convertView.findViewById(R.id.Button_week2);
                                    tmpView.setOnClickListener(new WeekOnClick(d,tmpWeekNumber));
                                    tmpView.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    tmpView=convertView.findViewById(R.id.Button_week3);
                                    tmpView.setOnClickListener(new WeekOnClick(d,tmpWeekNumber));
                                    tmpView.setVisibility(View.VISIBLE);
                                    break;
                                case 4:
                                    tmpView=convertView.findViewById(R.id.Button_week4);
                                    tmpView.setOnClickListener(new WeekOnClick(d,tmpWeekNumber));
                                    tmpView.setVisibility(View.VISIBLE);
                                    break;
                                case 5:
                                    tmpView=convertView.findViewById(R.id.Button_week5);
                                    tmpView.setOnClickListener(new WeekOnClick(d,tmpWeekNumber));
                                    tmpView.setVisibility(View.VISIBLE);
                                    break;
                                case 6:
                                    tmpView=convertView.findViewById(R.id.Button_week6);
                                    tmpView.setOnClickListener(new WeekOnClick(d,tmpWeekNumber));
                                    tmpView.setVisibility(View.VISIBLE);
                                    break;
                                case 7:

                                    tmpView=convertView.findViewById(R.id.Button_week7);
                                    tmpView.setOnClickListener(new WeekOnClick(d,tmpWeekNumber));
                                    tmpView.setVisibility(View.VISIBLE);
                                    break;

                            }
                        }

                    }
                }

            }catch (Exception ex){
                TraceUtil.traceThrowableLog( ex);
                DialogUtils.alertErrMsg(context,TAG+ ex.getMessage());
            }

            convertView.setTag(h);

        return convertView;
    }

    class HolderView {
        private TextView textView_start_end_time;


    }

    public class WeekOnClick implements AdapterView.OnClickListener{
        TimeScheduleRelationVO d;
        Integer weekNumber;
        public WeekOnClick(TimeScheduleRelationVO d,Integer weekNumber) {
            this.d = d;
            this.weekNumber=weekNumber;
        }

        @Override
        public void onClick(View view) {
//            IntentUtils.startTrainingCourseDetailSubscribeActivity(context, d);
            week1Cal.add(Calendar.DAY_OF_MONTH, weekNumber-1);
            if(d.getStart_time()==null){
                Toast.makeText(context,"数据异常，开始时间是空！", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] strArr=d.getStart_time().split(":");
            week1Cal.set(Calendar.HOUR_OF_DAY,Integer.valueOf(strArr[0]));
            if(strArr.length>1){
                week1Cal.set(Calendar.MINUTE,Integer.valueOf(strArr[1]));
            }else{
                week1Cal.set(Calendar.MINUTE,Integer.valueOf(0));
            }
            if(strArr.length>2){
                week1Cal.set(Calendar.SECOND,Integer.valueOf(strArr[2]));
            }else{
                week1Cal.set(Calendar.SECOND,Integer.valueOf(0));
            }
            Intent intent = new Intent(context, TrainingCourseDetailSubscribeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.ResponseData.DATA, null);
                bundle.putSerializable(Constant.ResponseData.Subscribe_Date, week1Cal.getTime() );
                bundle.putSerializable(Constant.ResponseData.TrainingCourseVO,vo);
                bundle.putSerializable(Constant.ResponseData.TimeScheduleRelationVO, d );
            bundle.putSerializable("mode", "create" );
                intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

}
