package com.company.runman.activity.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.company.news.query.NSearchContion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/21.
 */
public class DefaultAdapter  extends BaseAdapter {
    protected List mList;
    protected ListView mListView;
    protected Context context;

    public NSearchContion getsSearchContion() {
        if(sSearchContion==null)sSearchContion=new NSearchContion();
        return sSearchContion;
    }

    public void setsSearchContion(NSearchContion sSearchContion) {
        this.sSearchContion = sSearchContion;
    }

    protected NSearchContion sSearchContion=new NSearchContion();
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    public DefaultAdapter(Context context) {
        this.context = context;
    }
    @Override
    public Object getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mList == null ? 0 : position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void setList(List list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public List getList() {
        return this.mList;
    }

    public void add(Object t) {
        if (mList == null) {
            mList = new ArrayList();
        }
        mList.add(t);
        notifyDataSetChanged();
    }

    public void addAll(List list) {
        if (mList == null) {
            mList = new ArrayList();
        }
        if (list != null) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List list, int position) {
        if (mList == null) {
            return;
        }
        mList.addAll(position, list);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if (mList != null) {
            mList.remove(position);
            notifyDataSetChanged();
        }
    }

    public void remove(Object t) {
        if (mList != null) {
            mList.remove(t);
            notifyDataSetChanged();
        }
    }

    public void removeAll() {
        if (mList != null) {
            mList.removeAll(mList);
            notifyDataSetChanged();
        }
    }

    public void setListView(ListView listView) {
        this.mListView = listView;
    }

    public ListView getListView() {
        return this.mListView;
    }
}
