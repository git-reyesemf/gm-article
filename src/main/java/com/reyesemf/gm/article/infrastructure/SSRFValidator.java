package com.reyesemf.gm.article.infrastructure;

import java.util.function.Consumer;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

public class SSRFValidator implements Consumer<String> {
    
    private static final Pattern SSRF_PATTERN = compile(
            ".*(https?://|ftp://|file://|gopher://|dict://|smb://|nfs://|ldap://|ssh://|telnet://|" +
            "localhost|127\\.0\\.0\\.1|0\\.0\\.0\\.0|::1|0x7f000001|2130706433|" +
            "internal|intranet|admin|test|staging|dev|" +
            "169\\.254\\.|10\\.|192\\.168\\.|172\\.(1[6-9]|2[0-9]|3[0-1])\\.|" +
            "\\.\\.|%2e%2e|\\.\\.\\\\|\\.\\.%2f|\\.\\.%5c|" +
            "%2f|%5c|%00|%0a|%0d|" +
            "&#x2e;&#x2e;|&period;&period;|%252e%252e|" +
            "\\\\\\\\|///|\\\\|" +
            "javascript:|data:|vbscript:|about:|chrome:|" +
            "jar:|view-source:|resource:|moz-icon:|" +
            "\\$\\{|<%|<\\?|<script|<iframe|<object|<embed).*",
            CASE_INSENSITIVE
    );

    @Override
    public void accept(String value) {
        requireNonNull(value, "Value must be not null");
        if (SSRF_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("SSRF attempt on value: " + value);
        }
    }

}