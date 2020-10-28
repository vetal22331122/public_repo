package com.vetal22331122.webfluxproject.model.functions;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaScriptResolver {
    private final static String funcNameRegex = "(function\\s)(\\w+\\s*)\\(";

    private volatile ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

    public JavaScriptResolver() {
    }

    public synchronized Object resolveIntArgFunction(String input, int arg) throws ScriptException, NoSuchMethodException, IllegalArgumentException {
        engine.eval(input);
        Invocable invocable = (Invocable) engine;

        return invocable.invokeFunction(getFuncName(input), arg);
    }

    private static String getFuncName(String input) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile(funcNameRegex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(2).replaceAll("\\s", "");
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат ввода");
        }
    }
}
