package com.company.runman.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.company.news.vo.TrainingCourseVO;
import com.company.news.vo.UserVO;
import com.company.runman.R;
import com.company.runman.asynctask.AbstractDetailTrainingCourseAsyncTask;
import com.company.runman.asynctask.AbstractPullToRefreshListAsyncTask;
import com.company.runman.utils.IntentUtils;
import com.company.runman.utils.TraceUtil;
import com.company.runman.utils.VOUtils;

import java.util.List;

/**
 * Created by LMQ on 14-11-3.
 * Email:
 */
public class CoachListAdapter extends DefaultAdapter {
    private Context context;
    private LayoutInflater inflater;
    private int id;

    private Object lastItem;
    public CoachListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public CoachListAdapter(int item, Context context, List data){
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
            convertView=LayoutInflater.from(context).inflate(R.layout.coach_list_item_layout, null);
            HolderView h=new HolderView();
            h.name=(TextView)convertView.findViewById(R.id.name);
            h.sex=(TextView)convertView.findViewById(R.id.sex);

            h.age=(TextView)convertView.findViewById(R.id.age);
            h.city=(TextView)convertView.findViewById(R.id.city);


            UserVO d=(UserVO)this.mList.get(position);
            h.coach_course_list_btn=(Button)convertView.findViewById(R.id.coach_course_list_btn);
            h.coach_course_list_btn.setOnClickListener(new CourseOnClickListener(d.getId()));



            try{
                h.name.setText(d.getName());
                h.sex.setText(VOUtils.sexToString(d.getSex()));
                h.city.setText(VOUtils.objectToString(d.getCity()));
                h.age.setText(VOUtils.birthToString(d.getBirth()));
            }catch (Exception ex){
                TraceUtil.traceThrowableLog( ex);
            }
            convertView.setTag(h);
        }
        return convertView;
    }

    class HolderView {
        private TextView name;
        private TextView city;
        private TextView sex;
        private TextView age;
        private TextView trainer_star_level;
        private Button button_chat;
        private Button coach_course_list_btn;
    }


    protected class CourseOnClickListener implements View.OnClickListener {
        Long id;

        public CourseOnClickListener(Long id) {
            this.id = id;
        }

        @Override
        public void onClick(View view) {


            IntentUtils.startTrainingCourseListActivity(context, id);

//            new AbstractDetailTrainingCourseAsyncTask(context, id.toString()) {
//                public    void onPostExecute2(TrainingCourseVO vo){
//                    IntentUtils.startTrainingCourseDetailActivity(context, vo);
//                }
//            }.execute();
        }
    }




}
