package com.tpyzq.mobile.pangu.activity.market.quotation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhangwenbo on 2016/6/30.
 * 股票列表Intent传递的实体类
 */
public class StockListIntent implements Parcelable {

    private String [] filedNames;       //当前listview页面每个Item显示的属性
    private int comparePosition1;       //所要比较的 filedNames 数组中的1项
    private int comparePosition2;       //所要比较的 filedNames 数组中的2项
    private String title;               //标题
    private String head1;               //悬浮框内部数据1
    private String head2;               //悬浮框内部数据2
    private String head3;               //悬浮框内部数据3

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (filedNames == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(filedNames.length);
        }
        if (filedNames != null) {
            dest.writeStringArray(filedNames);
        }
        dest.writeInt(comparePosition1);
        dest.writeInt(comparePosition2);
        dest.writeString(title);
        dest.writeString(head1);
        dest.writeString(head2);
        dest.writeString(head3);
    }

    public static final Creator<StockListIntent> CREATOR  = new Creator<StockListIntent>() {
        @Override
        public StockListIntent createFromParcel(Parcel source) {
            StockListIntent bean = new StockListIntent();

            int length = source.readInt() ;
            String[] filedNames = null;
            if (length > 0) {
                filedNames = new String[length];
                source.readStringArray(filedNames);
            }

            bean.filedNames = filedNames;
            bean.comparePosition1 = source.readInt();
            bean.comparePosition2 = source.readInt();
            bean.title = source.readString();
            bean.head1 = source.readString();
            bean.head2 = source.readString();
            bean.head3 = source.readString();
            return bean;
        }

        @Override
        public StockListIntent[] newArray(int size) {
            return new StockListIntent[size];
        }
    };

    public void setComparePosition1(int comparePosition1) {
        this.comparePosition1 = comparePosition1;
    }

    public void setComparePosition2(int comparePosition2) {
        this.comparePosition2 = comparePosition2;
    }

    public void setFiledNames(String[] filedNames) {
        this.filedNames = filedNames;
    }


    public void setHead1(String head1) {
        this.head1 = head1;
    }

    public void setHead2(String head2) {
        this.head2 = head2;
    }

    public void setHead3(String head3) {
        this.head3 = head3;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getComparePosition1() {
        return comparePosition1;
    }

    public int getComparePosition2() {
        return comparePosition2;
    }


    public String getHead1() {
        return head1;
    }

    public String getHead2() {
        return head2;
    }

    public String getHead3() {
        return head3;
    }

    public String getTitle() {
        return title;
    }

    public String[] getFiledNames() {
        return filedNames;
    }

}
