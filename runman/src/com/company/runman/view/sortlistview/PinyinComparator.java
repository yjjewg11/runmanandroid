package com.company.runman.view.sortlistview;

import com.company.runman.datacenter.model.MonitorStationEntity;

import java.util.Comparator;

/**
 *
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<MonitorStationEntity> {

    public int compare(MonitorStationEntity o1, MonitorStationEntity o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
