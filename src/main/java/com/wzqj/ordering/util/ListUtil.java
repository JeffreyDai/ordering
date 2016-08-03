package com.wzqj.ordering.util;


import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by GY on 2015/12/11.
 */
public class ListUtil<T> {

    /**
     * sort
     */
    public void sort(List<T> list, final String method, final String sort) {
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                int ret = 0;
                try {
                    Method m1 = ((T) o1).getClass().getMethod(method, null);
                    Method m2 = ((T) o2).getClass().getMethod(method, null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}
