package mx.com.brandonicr.chat.common.utils;

import java.io.File;
import java.util.Date;

import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;

public class FilePathsSolver {

    public static String iconSolverName(String name){
        return name.substring(name.indexOf(SpecialCharacterConstants.CHAR_HYPHEN, SpecialCharacterConstants.INT_ZERO)+SpecialCharacterConstants.INT_ONE, name.lastIndexOf(SpecialCharacterConstants.CHAR_HYPHEN));
    }

    public static String iconSolverPath(String name){
        String fileName = iconSolverName(name);
        return FilePaths.fileBasePath.concat(solveSlash(new File(SpecialCharacterConstants.STR_EMPTY).getAbsolutePath()))
        .concat(FilePaths.source).concat(SpecialCharacterConstants.STR_SLASH).concat(FilePaths.iconPath).concat(SpecialCharacterConstants.STR_SLASH).concat(fileName);
    }

    public static String solveSlash(String path){ 
        return path.replace(SpecialCharacterConstants.CHAR_INVERT_SLASH, SpecialCharacterConstants.CHAR_SLASH);
    }

    public static String createTempFilePath(String path){
        int indexDotExtention = path.lastIndexOf(SpecialCharacterConstants.STR_DOT);
        if(indexDotExtention == -1)
            return path;
        return path.substring(SpecialCharacterConstants.INT_ZERO, indexDotExtention)
            .concat(Utils.formatDateFile(new Date())).concat(path.substring(indexDotExtention, path.length()));
    }

    private FilePathsSolver(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
