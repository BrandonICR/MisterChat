package mx.com.brandonicr.chat.common.dto;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.utils.FilePathsSolver;

public class FileChatBuilder {

    Logger log = Logger.getLogger(FileChatBuilder.class.getName());
    
    private ArrayList<FileChat> listFileFragments;
    private String absoluteFilePath;
    private String basePath;
    private String fileName;
    private Long fileSize;
    private Integer numberFragments;
    private Boolean isCompleted;

    public FileChatBuilder(String basePath, String fileName){
        this.basePath = basePath;
        this.fileName = fileName;
        listFileFragments = new ArrayList<>();
        isCompleted = false;
        absoluteFilePath = SpecialCharacterConstants.STR_EMPTY;
    }

    public boolean validateFileChat(FileChat fileChat){
        if(listFileFragments.isEmpty()){
            fileSize = fileChat.getFileSize();
            numberFragments = fileChat.getNumberFragments();
            return true;
        }
        return fileChat.getFileName().equals(fileName) && fileChat.getFileSize() == fileSize && listFileFragments.stream().noneMatch(file -> file.getFragmentPosition() == fileChat.getFragmentPosition());
    }

    public boolean processFileFragment(FileChat fileChat){
        if(!validateFileChat(fileChat))
            return false;
        listFileFragments.add(fileChat);
        isCompleted = false;
        return true;
    }

    public List<FileChat> getListFileFragments() {
        return this.listFileFragments;
    }

    public void setListFileFragments(List<FileChat> listFileFragments) {
        this.listFileFragments = (ArrayList<FileChat>)listFileFragments;
        isCompleted = false;
    }

    public boolean isCompleted(){
        if(listFileFragments.isEmpty())
            return false;
        ArrayList<FileChat> orderedListFileFragments = (ArrayList<FileChat>)listFileFragments.stream().sorted((file1, file2) -> file1.getFragmentPosition() - file2.getFragmentPosition()).collect(Collectors.toList());
        Integer fragmentCounter = 0;
        boolean hasAllParts = true;
        for(FileChat file : orderedListFileFragments){
            if(file.getFragmentPosition() != fragmentCounter++){
                hasAllParts = false;
                break;
            }
        }
        isCompleted = listFileFragments.stream().anyMatch(FileChat::isLast) && hasAllParts && fragmentCounter.equals(numberFragments);
        return isCompleted;
    }

    public boolean getIsCompleted(){
        return isCompleted;
    }

    public File buildAbsoluteFilePath(){
        File file = new File(basePath.concat(SpecialCharacterConstants.STR_SLASH).concat(fileName));
        file = file.exists() ? new File(FilePathsSolver.createTempFilePath(basePath.concat(SpecialCharacterConstants.STR_SLASH).concat(fileName))) : file;
        absoluteFilePath = file.getAbsolutePath();
        return file;
    }

    public boolean build(){
        if(Boolean.FALSE.equals(isCompleted))
            return false;
        ArrayList<FileChat> orderedListFileFragments = (ArrayList<FileChat>)listFileFragments.stream().sorted((file1, file2) -> file1.getFragmentPosition() - file2.getFragmentPosition()).collect(Collectors.toList());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(FileChat file : orderedListFileFragments){
            try {
                baos.write(file.getBytes());
                log.log(Level.INFO, "Apendeando fragmento: {0} con bytes {1} size del buffer {2} de {3}", new Object[]{file.getFragmentPosition(), file.getBytes().length, baos.size(), file.getFileSize()});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte []bytesFile = Base64.getDecoder().decode(baos.toByteArray());
        File file = buildAbsoluteFilePath();
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true));
            dos.write(bytesFile, 0, bytesFile.length);
            dos.flush();
            dos.close();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Error while creating the file", e);
            return false;
        }
        return true;
    }

    public String getFileName(){
        return fileName;
    }

    public String getAbsolutePathFile(){
        return absoluteFilePath;
    }

    public Integer getProgress(){
        return listFileFragments.size()*SpecialCharacterConstants.INT_ONE_HUNDRED/numberFragments;
    }

}
