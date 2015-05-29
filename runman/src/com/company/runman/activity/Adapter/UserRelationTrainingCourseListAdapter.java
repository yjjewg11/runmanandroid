package com.company.runman.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.company.news.vo.TrainingCourseVO;
import com.company.news.vo.UserRelationTrainingCourseVO;
import com.company.runman.R;
import com.company.runman.utils.TimeUtils;
import com.company.runman.utils.Tool;
import com.company.runman.utils.TraceUtil;
import com.company.runman.utils.VOUtils;

import java.util.List;

/**
 * Created by LMQ on 14-11-3.
 * Email:
 */
public class UserRelationTrainingCourseListAdapter extends DefaultAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int id;

    private Object lastItem;

    public UserRelationTrainingCourseListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public UserRelationTrainingCourseListAdapter(int item, Context context, List data){
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
            convertView=LayoutInflater.from(context).inflate(R.layout.user_relation_training_course_list_item_layout, null);
            HolderView h=new HolderView();
            h.course_title=(TextView)convertView.findViewById(R.id.course_title);
            h.course_price=(TextView)convertView.findViewById(R.id.course_price);
            h.course_time_length=(TextView)convertView.findViewById(R.id.course_time_length);
            h.course_difficulty_degree=(TextView)convertView.findViewById(R.id.course_difficulty_degree);
            h.course_place=(TextView)convertView.findViewById(R.id.course_place);
            h.status=(TextView)convertView.findViewById(R.id.status);
            h.course_time=(TextView)convertView.findViewById(R.id.course_time);
            UserRelationTrainingCourseVO vo=(UserRelationTrainingCourseVO)this.mList.get(position);
            try{
                h.course_title.setText(Tool.objectToString(vo.getCourse_title()));
                h.course_price.setText("价格："+Tool.objectToString(vo.getCourse_price()));
                h.course_time_length.setText("时长："+Tool.objectToString(vo.getCourse_time_length()));
                h.course_difficulty_degree.setText("难度："+Tool.objectToString(vo.getCourse_difficulty_degree()));
                h.course_place.setText("地点："+Tool.objectToString(vo.getCourse_place()));
                h.course_time.setText(TimeUtils.getTimeString(vo.getCourse_time()));
                h.status.setText(VOUtils.userRelCourseStatusToString(vo.getStatus()));

            }catch (Exception ex){
                TraceUtil.traceThrowableLog( ex);
            }

            convertView.setTag(h);
        }
        return convertView;
    }

    class HolderView {

        private TextView course_title;
        private TextView course_time_length;
        private TextView course_price;
        private TextView course_difficulty_degree;
        private TextView course_place;
        private TextView status;
        private TextView course_time;
    }

}
