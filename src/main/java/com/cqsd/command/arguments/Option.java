package com.cqsd.command.arguments;

import com.cqsd.command.result.Box;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * @author caseycheng
 * @date 2023/3/23-18:06
 **/
public final class Option {
    private final String flag;
    private final String optionHelp;
    private final boolean required;
    private final String defaultValue;
    private Box<String> box;

    public Option(@NotNull String flag, @Nls String optionHelp, boolean required, @NonNls String defaultValue) {
        if (flag.startsWith("-")) throw new InputFlagException("选项不能以-开头");
        this.flag = flag;
        this.optionHelp = optionHelp;
        this.required = required;
        this.defaultValue = defaultValue;
    }


    /**
     * 创建一个必填的option
     *
     * @param flag         选项标志
     * @param optionHelp   选项帮助
     * @param defaultValue 默认值
     * @return {@code Option}
     */
    @NotNull
    @Contract("_, _, _ -> new")
    public static Option newOption(String flag, String optionHelp, String defaultValue) {
        return new Option(flag, optionHelp, false, defaultValue);
    }

    /**
     * 创建一个选填的option
     *
     * @param flag       选项标志
     * @param optionHelp 选项帮助
     * @return {@code Option}
     */
    @NotNull
    @Contract("_, _ -> new")
    public static Option newOption(String flag, String optionHelp) {
        return new Option(flag, optionHelp, true, null);
    }

    public String flag() {
        return flag;
    }

    public String optionHelp() {
        return optionHelp;
    }

    public boolean required() {
        return required;
    }

    public String defaultValue() {
        return defaultValue;
    }

    /**
     * 用于存放解析结果
     *
     * @return
     */
    public Box<String> box() {
        return box == null ? box = Box.newEmptyBox() : box;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Option) obj;
        return Objects.equals(this.flag, that.flag) &&
                Objects.equals(this.optionHelp, that.optionHelp) &&
                this.required == that.required &&
                Objects.equals(this.defaultValue, that.defaultValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flag, optionHelp, required, defaultValue);
    }

    @Override
    public String toString() {
        return "Option[" +
                "flag=" + flag + ", " +
                "optionHelp=" + optionHelp + ", " +
                "required=" + required + ", " +
                "defaultValue=" + defaultValue + ']';
    }

}
