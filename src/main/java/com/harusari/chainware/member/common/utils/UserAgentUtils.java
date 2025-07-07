package com.harusari.chainware.member.common.utils;

import ua_parser.Parser;
import ua_parser.Client;

public class UserAgentUtils {

    private static final Parser UA_PARSER = new Parser();

    public static String parseBrowser(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        Client client = UA_PARSER.parse(userAgent);
        String browserName = client.userAgent.family; // ex) Chrome
        String major = client.userAgent.major;        // ex) 115
        String minor = client.userAgent.minor;        // ex) 0

        StringBuilder result = new StringBuilder(browserName);

        if (major != null) {
            result.append(" ").append(major);
            if (minor != null) {
                result.append(".").append(minor);
            }
        }
        return result.toString();
    }

}