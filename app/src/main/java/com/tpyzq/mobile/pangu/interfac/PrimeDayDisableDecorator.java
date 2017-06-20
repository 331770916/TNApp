package com.tpyzq.mobile.pangu.interfac;

import android.support.annotation.NonNull;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.List;

/**
 * Created by zhangwenbo on 2016/8/13.
 */
public class PrimeDayDisableDecorator implements DayViewDecorator {

    private List<CalendarDay> mCalendarDays;

    public PrimeDayDisableDecorator(@NonNull List<CalendarDay> calendarDays) {
        mCalendarDays = calendarDays;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return !mCalendarDays.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
    }
}
