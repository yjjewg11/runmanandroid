package com.company.runman.view.sortlistview;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.company.runman.R;
import com.company.runman.activity.base.BaseActivity;
import com.company.runman.datacenter.model.MonitorStationEntity;
import com.company.runman.view.CommonListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortBaseActivity extends BaseActivity {
    private static final String TAG = "ContactsSortActivity";
    protected TextView extraTextView;
    public CommonListView commonListView;
    protected SortBaseAdapter adapter;
    protected ClearEditText mClearEditText;
    /**
     * 汉字转换成拼音的类
     */
    protected CharacterParser characterParser;
    protected List<MonitorStationEntity> sourceDataList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    protected PinyinComparator pinyinComparator;
    protected boolean isInit = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.sort_listview_layout);

        extraTextView = (TextView) findViewById(R.id.extraTextView);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        SideBar sideBar = (SideBar) findViewById(R.id.sidrbar);
        /**
         * 显示字母的TextView
         */
        TextView dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    commonListView.getListView().setSelection(position);
                }

            }
        });

        commonListView = (CommonListView) findViewById(R.id.commonListView);

        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    protected void setOnItemClickListener(OnItemClickListener itemClickListener) {
        commonListView.getListView().setOnItemClickListener(itemClickListener);
    }

    public void initData() {
//        Intent intent = getIntent();
//        String type = intent.getStringExtra(Constant.EXTRA_COMMON_KEY);
//        if (TextUtils.equals(Constant.CONTACTS_TIMER, type) && !isInit) {
//            adapter = new BaseListAdapter<MonitorStationEntity>(context);
//            new LastContactsTask(context).execute();
//        } else if (TextUtils.equals(Constant.CONTACTS_ALL, type) && !isInit) {
//            isInit = true;
//            adapter = new ContactsAdapter(context);
//            new LocalContactsTask(context).execute();
//        } else if (TextUtils.equals(SyncStateContract.Constant.CONTACTS_COLLECTION, type) && !isInit) {
//            adapter = new MonitorStationAdapter(context);
//            new CollectionContactsTask(context).execute();
//        }
//        setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
//                Intent intent = new Intent(context, ContactsDetailActivity.class);
//                intent.putExtra(Constant.EXTRA_COMMON_KEY, (MonitorStationEntity) adapter.getItem(position));
//                startActivity(intent);
//            }
//        });
//        commonListView.setListViewAdapter(adapter);
    }

    @Override
    public void setContentView() {

    }

    /**
     * 为ListView填充数据
     *
     * @param data 需要显示的填充数据
     * @param isDisplayAll 是否需要显示所有数据
     * @return 返回排序后得数据
     */
    protected List<MonitorStationEntity> filledData(List<MonitorStationEntity> data, boolean isDisplayAll) {
        List<MonitorStationEntity> mSortList = new ArrayList<MonitorStationEntity>();
        //将保存的定制列表取出来,如果为空,则显示所有
        for (MonitorStationEntity stationEntity : data) {
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(stationEntity.getStationName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                stationEntity.setSortLetters(sortString.toUpperCase());
            } else {
                stationEntity.setSortLetters("#");
            }
            if(isDisplayAll || stationEntity.isChecked()) {
                mSortList.add(stationEntity);
            }
        }
        return mSortList;
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 需要过滤的关键字符串
     */
    private void filterData(String filterStr) {
        if (sourceDataList == null || sourceDataList.isEmpty()) {
            return;
        }
        List<MonitorStationEntity> filterDateList = new ArrayList<MonitorStationEntity>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = sourceDataList;
        } else {
            filterDateList.clear();
            for (MonitorStationEntity MonitorStationEntity : sourceDataList) {
                String name = MonitorStationEntity.getStationName();
                if (name.toUpperCase().contains(filterStr.toUpperCase()) || characterParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toUpperCase())) {
                    filterDateList.add(MonitorStationEntity);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    /**
     * 按下键盘上返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {// 后退
            finish();
        } else {
            super.onKeyDown(keyCode, event);
        }
        return true;
    }
}