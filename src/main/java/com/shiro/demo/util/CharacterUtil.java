package com.shiro.demo.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 字符处理Util
 * Author TianYang
 * Date 2023/12/12 16:44
 */
public class CharacterUtil {

    public static String chineseToPinying(String chinese) throws BadHanyuPinyinOutputFormatCombination {
        char[] charArray = chinese.toCharArray();
        StringBuilder pinyin = new StringBuilder();
        HanyuPinyinOutputFormat fmt = new HanyuPinyinOutputFormat();
        fmt.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]")) {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, fmt);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    pinyin.append(pinyinArray[0]);
                }
            }
        }
        return pinyin.toString();
    }
}
