package com.github.not.n0w.weblab3Bot.utils;

public class EscapeMarkdown {
    public static String escapeMarkdown(String markdown) {
        if (markdown == null) return null;
        String regex = "([\\\\_\\{\\}\\[\\]\\(\\)#\\+\\-\\.\\!\\|>~=])";
        return markdown.replaceAll(regex, "\\\\$1");
    }
}
