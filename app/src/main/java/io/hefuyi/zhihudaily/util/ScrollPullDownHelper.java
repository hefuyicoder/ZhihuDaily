package io.hefuyi.zhihudaily.util;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by hefuyi on 16/8/8.
 */
public class ScrollPullDownHelper {

    private static final int PULLING_DOWN_TIME_MAX = 8;
    private static final int PULLING_DOWN_TIME_THRESHOLD = 6;

    private int lastScrollY;
    private final Queue<Boolean> lastestPullingDown;

    public ScrollPullDownHelper() {
        lastScrollY = 0;
        lastestPullingDown = new LinkedList<>();
    }

    //记录最近8次的滚动,如果最近的8次当中有6次是向下滚动,则判断为向下滚动
    public boolean onScrollChange(int scrollY) {
        boolean isPullingDownNow = scrollY < lastScrollY;
        lastestPullingDown.offer(isPullingDownNow);
        if (lastestPullingDown.size() > PULLING_DOWN_TIME_MAX) {
            lastestPullingDown.poll();
        }
        lastScrollY = scrollY;

        return getPullingDownTime() >= PULLING_DOWN_TIME_THRESHOLD;
    }

    private int getPullingDownTime() {
        int result = 0;
        for (Boolean isPullingDown : lastestPullingDown) {
            if (isPullingDown) {
                result++;
            }
        }
        return result;
    }
}
