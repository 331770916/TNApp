package com.tpyzq.mobile.pangu.http.doConnect.self;

import java.util.ArrayList;
import java.util.List;

/**
 * **************************************************************
 * <p>
 * **************************************************************
 * Authors:huweidong on 2017/6/19 0019 15:12
 * Email：huwwds@gmail.com
 */

public class NetworkManager {

    private static List<String> stackList = new ArrayList<>();

    public static void addToStack(Object object) {
        if (!(object instanceof String))
            stackList.add(object.getClass().getName());
    }

    public static void removeFromStack(Object object){
        if (!(object instanceof String))
            stackList.remove(object.getClass().getName());
    }

    public static String getLatestActivityTag() {
        if (stackList.size() <= 0) return "";
        return stackList.get(stackList.size() - 1);
    }
}
