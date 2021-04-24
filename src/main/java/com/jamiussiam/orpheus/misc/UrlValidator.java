package com.jamiussiam.orpheus.misc;

import com.jamiussiam.orpheus.model.QueryType;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UrlValidator {

    private static final String VALID_URL_REGEX = ".*(youtube.com/watch?|soundcloud.com).*";

    private static final String URL_REGEX = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

    private static Pattern VALID_URL_PATTERN;

    private static Pattern URL_PATTERN;

    public UrlValidator() {
        VALID_URL_PATTERN = Pattern.compile(VALID_URL_REGEX, Pattern.CASE_INSENSITIVE);
        URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);
    }

    public static QueryType getQueryType(String query) {
        if (VALID_URL_PATTERN.matcher(query).find()) {
            return QueryType.VALID_URL;
        } else if (URL_PATTERN.matcher(query).find()) {
            return QueryType.URL;
        } else {
            return QueryType.QUERY;
        }
    }
}
