package com.cqsd.command.util;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

/**
 * @author caseycheng
 * @date 2022/12/10-09:49
 **/
//@SuppressWarnings("ALL")
final public class StringUtil {
    /**
     * 将字符验证速度调整到相同以防止通过验证速度上的差异而猜出密码
     *
     * @param p1
     * @param p2
     * @return
     */
    public static boolean safe_eq(final @NotNull String p1, final @NotNull String p2) {
        //这句用来拖时间的操作，如果两段密码长度都不对，那就一定不对，后续的验证都用来拖延时间。
        var ret = p1.length() == p2.length();
        for (var i = 0; i < p1.length(); i++) {
            ret = ret && (p1.charAt(i) == p2.charAt(i));
        }
        return ret;
    }

    /**
     * 干掉字符串开始中的get或set还有is
     *
     * @param str
     * @return
     */
    @Contract("!null->_;null->null")
    public static String remove_get_set(String str) {
        if (hasLength(str)) {
            if (str.startsWith("set") || str.startsWith("get")) {
                return str.substring(3);
            }
            if (str.startsWith("is")) {
                return str.substring(2);
            }
        }
        return str;
    }

    /**
     * 如果有值为true，无值为false
     *
     * @param str
     * @return
     */
    @Contract(value = "!null->true;null->false",pure = true)
    public static boolean hasLength(String str) {
        return str != null && str.length() != 0;
    }
    @Contract(value = "!null,_->_;null,_->fail",pure = true)
    public static void hasLength(String content, Supplier<Throwable> throwableSupplier) throws Throwable {
        if (!hasLength(content)) {
            throw throwableSupplier.get();
        }
    }

    /**
     * 将String处理为以下类型
     * 所有基本类型
     *
     * @param str
     * @param type
     * @param <R>
     * @return
     */
    @Contract("null, _ -> null")
    @SuppressWarnings("unchecked")
    public static <R> R str_map(String str, Class<R> type) {
        R result;
        if (str == null) {
            result = null;
        } else if (type.equals(String.class)) {
            result = (R) str;
        } else if (type.equals(Integer.class) || type.equals(int.class)) {
            result = (R) Integer.valueOf(str);
        } else if (type.equals(Short.class) || type.equals(short.class)) {
            result = (R) Short.valueOf(str);
        } else if (type.equals(Long.class) || type.equals(long.class)) {
            result = (R) Long.valueOf(str);
        } else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            result = (R) Boolean.valueOf(str);
        } else if (type.equals(Double.class) || type.equals(double.class)) {
            result = (R) Double.valueOf(str);
        } else if (type.equals(Float.class) || type.equals(float.class)) {
            result = (R) Float.valueOf(str);
        } else if (type.equals(Byte.class) || type.equals(byte.class)) {
            result = (R) Byte.valueOf(str);
        } else if (type.equals(Character.class) || type.equals(char.class)) {
            result = (R) Character.valueOf(str.charAt(0));
        } else if (type.equals(Byte[].class) || type.equals(byte[].class)) {
            result = (R) str.getBytes(StandardCharsets.UTF_8);
        } else if (type.equals(Character[].class) || type.equals(char[].class)) {
            result = (R) str.toCharArray();
        } else {
            throw new StringMappingException(type.getSimpleName() + "不支持处理");
        }
        return result;
    }

}
