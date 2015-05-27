package com.company.runman.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.company.news.usershow.TrainingPlanUsershow;
import com.company.news.vo.TrainingPlanVO;
import com.company.runman.R;
import com.company.runman.utils.TimeUtils;
import com.company.runman.utils.TraceUtil;

import java.util.List;

/**
 * Created by EdisonZhao on 14-11-3.
 * Email:zhaoliangyu@sobey.com
 */
public class TrainingPlanAdapter extends DefaultAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int id;

    private Object lastItem;
    public TrainingPlanAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public TrainingPlanAdapter (int item, Context context,List data){
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
            convertView=LayoutInflater.from(context).inflate(R.layout.traininng_plan_item, null);
            HolderView h=new HolderView();
            h.start_day=(TextView)convertView.findViewById(R.id.start_day);
            h.place=(TextView)convertView.findViewById(R.id.place);
            h.run_times=(TextView)convertView.findViewById(R.id.run_times);
            h.start_to_end_time=(TextView)convertView.findViewById(R.id.start_to_end_time);
            h.price=(TextView)convertView.findViewById(R.id.price);
            h.status=(TextView)convertView.findViewById(R.id.status);

            TrainingPlanVO d=(TrainingPlanVO)this.mList.get(position);

            try{
                h.start_day.setText(TimeUtils.getDateString(d.getStart_time()));
                h.place.setText(d.getPlace());
                h.run_times.setText(d.getRun_times()+"");
                h.start_to_end_time.setText(TimeUtils.getHourAndMinuteByDate(d.getStart_time()) + "~" + TimeUtils.getHourAndMinuteByDate(d.getEnd_time()));
                h.price.setText("出价:"+d.getPrice());
                h.status.setText(TrainingPlanUsershow.changeByStatus(d.getStatus()));
            }catch (Exception ex){
                TraceUtil.traceThrowableLog( ex);
            }

            convertView.setTag(h);
        }
        return convertView;
    }

    class HolderView {
        private TextView start_day;
        private TextView place;
        private TextView run_times;
        private TextView start_to_end_time;
        private TextView price;
        private TextView status;

    }

}
