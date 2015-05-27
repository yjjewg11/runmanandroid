package com.company.runman.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.company.news.usershow.TrainingPlanUsershow;
import com.company.news.vo.TrainingCourseVO;
import com.company.news.vo.TrainingPlanVO;
import com.company.runman.R;
import com.company.runman.utils.TimeUtils;
import com.company.runman.utils.TraceUtil;

import java.util.List;

/**
 * Created by LMQ on 14-11-3.
 * Email:
 */
public class MyTrainingCourseAdapter extends DefaultAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int id;

    private Object lastItem;
    public MyTrainingCourseAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public MyTrainingCourseAdapter(int item, Context context, List data){
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
            convertView=LayoutInflater.from(context).inflate(R.layout.my_traininng_course_item, null);
            HolderView h=new HolderView();
            h.title=(TextView)convertView.findViewById(R.id.title);
          //  h.exercise_mode=(TextView)convertView.findViewById(R.id.exercise_mode);

            h.price=(TextView)convertView.findViewById(R.id.price);
//            h.status=(TextView)convertView.findViewById(R.id.status);

            TrainingCourseVO d=(TrainingCourseVO)this.mList.get(position);

            try{
                h.title.setText(d.getTitle());
//                if(Integer.valueOf(2).equals(d.getExercise_mode())){
//                    h.exercise_mode.setText("马拉松");
//                }else{
//                    h.exercise_mode.setText("普通跑步");
//                }


                h.price.setText("价格:"+d.getPrice());

            }catch (Exception ex){
                TraceUtil.traceThrowableLog( ex);
            }

            convertView.setTag(h);
        }
        return convertView;
    }

    class HolderView {
        private TextView title;
      //  private TextView exercise_mode;
        private TextView price;
        private TextView status;

    }

}
