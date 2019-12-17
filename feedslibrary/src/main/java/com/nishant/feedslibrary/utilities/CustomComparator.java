package com.nishant.feedslibrary.utilities;

import com.nishant.feedslibrary.model.Posts;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public class CustomComparator implements Comparator<Posts> {
    private Integer mode, which;

    public CustomComparator(Integer mode, Integer which) {
        this.mode = mode;
        this.which = which;
    }

    @Override
    public int compare(Posts o1, Posts o2) {
        if(mode >= 0){
            switch (which){
                case 1:
                    return (int)(o1.getEvent_date()-o2.getEvent_date());
                case 2:
                    return (int)(o1.getViews()-o2.getViews());
                case 3:
                    return (int)(o1.getLikes()-o2.getLikes());
                case 4:
                    return (int)(o1.getShares()-o2.getShares());
                case 5:
                    return (int)(o1.getEvent_name().compareTo(o2.getEvent_name()));
            }
        } else {
            switch (which){
                case 1:
                    return (int)(o2.getEvent_date()-o1.getEvent_date());
                case 2:
                    return (int)(o2.getViews()-o1.getViews());
                case 3:
                    return (int)(o2.getLikes()-o1.getLikes());
                case 4:
                    return (int)(o2.getShares()-o1.getShares());
                case 5:
                    return (int)(o2.getEvent_name().compareTo(o1.getEvent_name()));
            }
        }
        return 0;
    }

    @Override
    public Comparator<Posts> reversed() {
        return null;
    }

    @Override
    public Comparator<Posts> thenComparing(Comparator<? super Posts> other) {
        return null;
    }

    @Override
    public <U> Comparator<Posts> thenComparing(Function<? super Posts, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return null;
    }

    @Override
    public <U extends Comparable<? super U>> Comparator<Posts> thenComparing(Function<? super Posts, ? extends U> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<Posts> thenComparingInt(ToIntFunction<? super Posts> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<Posts> thenComparingLong(ToLongFunction<? super Posts> keyExtractor) {
        return null;
    }

    @Override
    public Comparator<Posts> thenComparingDouble(ToDoubleFunction<? super Posts> keyExtractor) {
        return null;
    }
}
