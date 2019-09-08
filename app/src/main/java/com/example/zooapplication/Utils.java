package com.example.zooapplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static boolean isValidEmail(String str) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
