package mx.com.brandonicr.chat.common.constants;

public class SpecialCharacterConstants {

    public static final String STR_EMPTY = "";
    public static final String STR_SPACE = " ";
    public static final String STR_ONE = "1";
    public static final String STR_SLASH = "/";
    public static final String STR_DOT = ".";

    public static final Character CHAR_HYPHEN = '-';
    public static final Character CHAR_SLASH = '/';
    public static final Character CHAR_INVERT_SLASH = '\\';

    public static final Integer INT_ZERO = 0;
    public static final Integer INT_ONE = 1;
    public static final Integer INT_ONE_HUNDRED = 100;

    private SpecialCharacterConstants(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
