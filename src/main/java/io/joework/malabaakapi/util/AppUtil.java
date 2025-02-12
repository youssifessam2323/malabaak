package io.joework.malabaakapi.util;

import jakarta.validation.constraints.NotEmpty;

public class AppUtil {
    public static String getUserFirstName(String name) {
        if(name.split(" ").length > 1) {
            return name.split(" ")[0];
        }
        return name;
    }

    public static String getUserLastName(String name) {
        if(name.split(" ").length > 1) {
            return name.split(" ")[1];
        }
        return name;
    }
}
