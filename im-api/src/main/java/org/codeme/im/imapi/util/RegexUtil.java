package org.codeme.im.imapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RegexUtil
 *
 * @author walker lee
 * @date 2020/2/9
 */
public class RegexUtil {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^((13[0-9])|(15[^4])|(18[0-9])|(17[0-9])|(147))\\d{8}$");

    public static boolean isMobileNO(String mobile) {
        Matcher m = MOBILE_PATTERN.matcher(mobile);
        return m.matches();
    }


}
