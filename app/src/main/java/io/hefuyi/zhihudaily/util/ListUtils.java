package io.hefuyi.zhihudaily.util;

import java.util.List;

/**
 * Created by hefuyi on 16/8/4.
 */
public class ListUtils {

    public static final boolean isEmpty(List list) {
        return !(list != null && list.size() != 0);
    }
}
