package com.cqsd.command.arguments;



import com.cqsd.command.result.Result;
import org.jetbrains.annotations.Contract;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author caseycheng
 * @date 2023/3/22-21:47
 **/
public class ArgumentsParser {
    private Map<String, String> arguments = null;

    public ArgumentsParser(String[] args) {
        final var it = Arrays.stream(args).iterator();
        String currentKey = null;
        String value = null;
        arguments = new HashMap<>(args.length / 2);
        while (it.hasNext()) {
            final var arg = it.next();

            if (arg.startsWith("-")) {
                currentKey = arg;
            }
            if (currentKey != null) {
                if (it.hasNext()) {
                    value = it.next();
                }
            }
            if (currentKey != null && value != null) {
                arguments.put(currentKey, value);
                currentKey = null;
                value = null;
            }
        }
    }

    /**
     * 传入一个参数标志用于查找对应参数值
     *
     * @param flag 没有 - 的参数标志
     * @return
     */
    @Contract("!null->!null")
    public Result<String> getArguments(String flag) {
        Objects.requireNonNull(flag);
        if (flag.startsWith("-")) {
            throw new InputFlagException("已自动添加-标志，请不要重复添加。");
        }
        final var value = arguments.get("-" + flag);
        if (value == null) {
            return Result.err("未找到变量");
        } else {
            return Result.ok(value.trim());
        }
    }

    protected Map<String, String> getArguments() {
        return this.arguments;
    }

}
