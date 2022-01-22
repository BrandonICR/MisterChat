package mx.com.brandonicr.chat.common.utils;

import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;

public class FilePathsSolver {

    public static String iconSolverName(String name){
        return name.substring(name.indexOf(SpecialCharacterConstants.CHAR_HYPHEN, SpecialCharacterConstants.INT_ZERO), name.lastIndexOf(SpecialCharacterConstants.CHAR_HYPHEN));
    }

    public static String iconSolverPath(String name){
        String fileName = iconSolverName(name);
        return FilePaths.iconPath.concat(fileName);
    }

    private FilePathsSolver(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
