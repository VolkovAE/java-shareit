package ru.practicum.shareit.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Utils {
    public static void addPathParameter(StringBuilder stringBuilder, String nameParameter) {
        stringBuilder.append("/");
        stringBuilder.append(nameParameter);
    }

    public static void addParameterRequest(StringBuilder stringBuilder, String nameParameter) {
        if (stringBuilder.isEmpty()) stringBuilder.append("?");
        else {
            if (stringBuilder.indexOf("?") == -1) stringBuilder.append("?");
            else stringBuilder.append("&");
        }

        stringBuilder.append(nameParameter);
        stringBuilder.append("={");
        stringBuilder.append(nameParameter);
        stringBuilder.append("}");
    }
}
