package mx.com.brandonicr.chat.common.utils;

public class ObjectValidator {

    public static Object ifTrueReturnOrElse(boolean condition, Object trueObject, Object elseObject){
        return condition ? trueObject : elseObject;
    }

    private ObjectValidator(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }

}
