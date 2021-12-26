package http.util;

import java.util.Arrays;
import java.util.regex.Pattern;

public final class InputValidator {

    public static final Pattern INT_PATTERN = Pattern.compile("^\\d+$"); //non contempla overflow
    public static final Pattern DOUBLE_PATTERN = Pattern.compile("^(-)?(0|[1-9]\\d+)\\.\\d+$");
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9.!_]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    public static final Pattern USERNAME_PATTERN = Pattern.compile("^[\\w\\-]+$");

    public static boolean assertMatch(String value, Pattern regex){
        return regex.matcher(value).matches();
    }

    public static boolean assertInt(String value){
        return assertMatch(value, INT_PATTERN);
    }

    public static boolean assertDouble(String value){
        return assertMatch(value, DOUBLE_PATTERN);
    }

    public static boolean assertEmail(String value){
        return assertMatch(value, EMAIL_PATTERN);
    }

    public static boolean assertInts(String[] values){
        return Arrays.stream(values).allMatch(InputValidator::assertInt);
    }
}
