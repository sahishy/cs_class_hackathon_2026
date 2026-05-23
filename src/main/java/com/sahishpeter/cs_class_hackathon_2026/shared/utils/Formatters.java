package com.sahishpeter.cs_class_hackathon_2026.shared.utils;

import java.time.Duration;
import java.time.Instant;

public class Formatters {

    public static String formatTimeAgo(long millis) {

        Instant past = Instant.ofEpochMilli(millis);
        Instant now = Instant.now();
        Duration duration = Duration.between(past, now);

        long seconds = duration.getSeconds();
        long days = duration.toDays();

        String result = "";

        if (seconds < 60) {
            return "just now";
        } else if (seconds < 3600) {

            int num = (int)(seconds / 60);
            result = num + " minute" + (num == 1 ? "" : "s");

        } else if (seconds < 86400) {

            int num = (int)(seconds / 3600);
            result = num + " hour" + (num == 1 ? "" : "s");

        } else if (days < 30) {

            int num = (int)days;
            result = num + " day" + (num == 1 ? "" : "s");

        } else if (days < 365) {

            int num = (int)(days / 30);
            result = num + " month" + (num == 1 ? "" : "s");

        } else {

            int num = (int)(days / 365);
            result = num + " year" + (num == 1 ? "" : "s");

        }

        result += " ago";

        return result;

    }

}
