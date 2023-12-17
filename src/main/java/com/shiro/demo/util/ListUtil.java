package com.shiro.demo.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * List工具类
 * Author TianYang
 * Date 2023/12/14 14:39
 */
public class ListUtil {

    /**
     * List转化为Map
     * @param list
     * @param keyExtractor
     * @return
     * @param <T>
     * @param <K>
     */
    public static <T, K> Map<K, T> convertListToMap(List<T> list, Function<T, K> keyExtractor) {
        return list.stream()
                .collect(Collectors.toMap(keyExtractor, item -> item));
    }
}
