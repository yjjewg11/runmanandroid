package com.company.runman.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.company.runman.R;
import com.company.runman.datacenter.database.DBHelper;
import com.company.runman.datacenter.model.KeywordEntity;
import com.company.runman.utils.AbstractAsyncTask;

/**
 * Created by EdisonZhao on 14-11-3.
 * Email:zhaoliangyu@sobey.com
 */
public class KeywordAdapter extends BaseListAdapter<KeywordEntity> {

    private Context context;
    private EmptyDataCallBack emptyDataCallBack;

    public KeywordAdapter(Context context, EmptyDataCallBack emptyDataCallBack) {
        super(context);
        this.context = context;
        this.emptyDataCallBack = emptyDataCallBack;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if(convertView == null) {
            holderView = new HolderView();
            convertView = LayoutInflater.from(context).inflate(R.layout.keyword_item_layout, parent, false);
            holderView.keyword = (TextView) convertView.findViewById(R.id.keyword);
            holderView.deleteBtn = convertView.findViewById(R.id.deleteBtn);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        final KeywordEntity keywordEntity = mList.get(position);
        holderView.keyword.setText(keywordEntity.getKeyword());
        holderView.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteKeyword(keywordEntity).execute();
            }
        });
        convertView.setTag(holderView);
        return convertView;
    }

    class HolderView {
        private TextView keyword;
        private View deleteBtn;
    }

    class DeleteKeyword extends AbstractAsyncTask<KeywordEntity, Void, Boolean> {

        private KeywordEntity keywordEntity;

        public DeleteKeyword(KeywordEntity keywordEntity) {
            this.keywordEntity = keywordEntity;
        }

        @Override
        protected Boolean doInBackground(KeywordEntity... params) {
            DBHelper.getInstance().deleteKeywordFromDB(context, keywordEntity);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                mList.remove(keywordEntity);
                notifyDataSetChanged();
                if(mList.isEmpty()) {
                    emptyDataCallBack.emptyDataAction();
                }
            }
        }
    }
}
