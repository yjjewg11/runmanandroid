package com.company.runman.activity.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.company.runman.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础adapter
 *
 * @param <T>
 * @author
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected ListView mListView;
    protected LayoutInflater mInflater;
    protected Resources mResources;
    private Toast toast;
    private View toastView;
    private boolean isShown = false;

    public BaseListAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mResources = context.getResources();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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

    public void setList(List<T> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return this.mList;
    }

    public void add(T t) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        mList.add(t);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        if (mList == null) {
            mList = new ArrayList<T>();
        }
        if (list != null) {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addAll(List<T> list, int position) {
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

    public void remove(T t) {
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

    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg) || !isShown) {
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        if (null == toastView) {
            toastView = View.inflate(mContext, R.layout.toast_view, null);
        }
        ((TextView) toastView).setText(msg);
        toast.setGravity(Gravity.CENTER, toast.getXOffset() / 2,
                toast.getYOffset() / 2);
        toast.setView(toastView);
        toast.show();
    }

    public final String getStringAfterCheck(int resId, Object... formatArgs) {
        if(formatArgs != null) {
            for(int i=0;i<formatArgs.length;i++) {
                Object object = formatArgs[i];
                if(object == null || "null".equals(object)) {
                    formatArgs[i] = "";
                }
            }
        }
        return mContext.getResources().getString(resId, formatArgs);
    }

    public interface EmptyDataCallBack {
        public void emptyDataAction();
    }

    public interface DataChangeCallBack {
        public void changeAction(boolean isChange);
    }
}
