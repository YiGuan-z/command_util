package com.cqsd.command.arguments;


import com.cqsd.command.result.Box;
import com.cqsd.command.result.Result;
import com.cqsd.command.util.StringMappingException;
import com.cqsd.command.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author caseycheng
 * @date 2023/3/22-23:34
 **/
public class CommandLineOption {
    /**
     * 存放控制
     */
    private final Map<String, Option> COMMAND_OPTION = new HashMap<>();
    /**
     * 存放结果
     */
    private final Map<String, String> result = new HashMap<>();
    /**
     * 存放帮助信息
     */
    private String helpMsg;
    /**
     * 存放参数解析完后的操作
     */
    private final List<Supplier<Void>> operate = new ArrayList<>();

    /**
     * 添加一个选项
     *
     * @param option
     */
    public CommandLineOption addOption(Option option) {
        Objects.requireNonNull(option);
        COMMAND_OPTION.put(option.flag(), option);
        return this;
    }

    /**
     * 添加一个选项集合
     *
     * @param collection
     */
    public CommandLineOption addOption(Collection<Option> collection) {
        Objects.requireNonNull(collection);
        for (Option option : collection) {
            COMMAND_OPTION.put(option.flag(), option);
        }
        return this;
    }

    /**
     * 添加一个选项集合
     *
     * @param options
     */
    public CommandLineOption addOption(Option... options) {
        Objects.requireNonNull(options);
        for (Option option : options) {
            COMMAND_OPTION.put(option.flag(), option);
        }
        return this;
    }

    private CommandLineOption() {

    }

    @NotNull
    @Contract(" -> new")
    public static CommandLineOption create() {
        return new CommandLineOption();
    }

    /**
     * 开始解析
     * 尽量捕获异常然后将异常栈屏蔽掉
     *
     * @param args
     */
    public Result<CommandLineOption> parse(String[] args) {
        //对参数进行解析
        final var parser = new ArgumentsParser(args);
        for (Map.Entry<String, Option> optionEntry : COMMAND_OPTION.entrySet()) {
            final var flag = optionEntry.getKey();
            final var option = optionEntry.getValue();
            final var arguments = parser.getArguments(flag);
            //判断是否为required
            if (option.required()) {
                //如果有值则直接put
                if (arguments.isOk()) {
                    result.put(flag, arguments.unwarp());
                } else {
                    final var msg = fmtHelpMsg();
                    return Result.err(String.format("\r\n-%s是必填参数\r\n%s", flag, msg));
                }
            } else {
                //如果没有返回值则加入默认值
                //如果有返回值则使用解析出的返回值
                if (arguments.isErr()) {
                    result.put(flag, option.defaultValue());
                } else {
                    result.put(flag, arguments.unwarp());
                }
            }
        }
        //对列表里方法运行
        if (this.operate.size() > 0) {
            for (Supplier<Void> supplier : this.operate) {
                supplier.get();
            }
        }
        return Result.ok(this);
    }

    public String fmtHelpMsg() {
        if (helpMsg == null) {
            final var builder = new StringBuilder();
            COMMAND_OPTION.forEach((k, v) -> {
                builder.append("选项:")
                        .append("\t")
                        .append('-')
                        .append(k)
                        .append(' ')
                        .append(v.optionHelp())
                        .append("\r\n");
            });
            return this.helpMsg = builder.toString();
        }
        return this.helpMsg;
    }

    /**
     * 返回解析后的参数
     * 该方法仅支持八大基本类型以及包装类型
     *
     * @param flag
     * @param type
     * @param <R>
     * @return
     */
    public <R> R getArguments(String flag, Class<R> type) throws StringMappingException {
        final var value = result.get(flag);
        return StringUtil.str_map(value, type);
    }

    public <R> R getArguments(Option option, Class<R> type) throws StringMappingException {
        final var argument = this.result.get(option.flag());
        return StringUtil.str_map(argument, type);
    }

    public CommandLineOption getArguments(String flag, Box<String> result) {
        Supplier<Void> bc = () -> {
            final var value = this.result.get(flag);
            result.setInstance(value);
            return null;
        };
        this.operate.add(bc);
        return this;
    }

}
