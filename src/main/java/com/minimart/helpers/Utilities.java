package com.minimart.helpers;

public class Utilities {
    public static String slugify(String str) {
        return str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    public static String slugify(String str, boolean upper) {
        return str.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
    }
}
