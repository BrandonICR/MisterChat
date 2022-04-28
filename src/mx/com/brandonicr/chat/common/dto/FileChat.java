package mx.com.brandonicr.chat.common.dto;

import java.io.Serializable;

/**
 *
 * @author BrandonICR
 */
public class FileChat implements Serializable{
    
    private byte []bytes;
    private String fileName;
    private long fileSize;
    private int fragmentSize;
    private int fragmentPosition;
    private int numberFragments;
    private boolean isLast;

    public FileChat() {
    }

    public FileChat(byte[] bytes, String fileName, long fileSize, int fragmentSize, int fragmentPosition, int numberFragments, boolean isLast) {
        this.bytes = bytes;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fragmentSize = fragmentSize;
        this.fragmentPosition = fragmentPosition;
        this.numberFragments = numberFragments;
        this.isLast = isLast;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getFragmentSize() {
        return this.fragmentSize;
    }

    public void setFragmentSize(int fragmentSize) {
        this.fragmentSize = fragmentSize;
    }

    public int getFragmentPosition() {
        return this.fragmentPosition;
    }

    public void setFragmentPosition(int fragmentPosition) {
        this.fragmentPosition = fragmentPosition;
    }

    public int getNumberFragments() {
        return this.numberFragments;
    }

    public void setNumberFragments(int numberFragments) {
        this.numberFragments = numberFragments;
    }

    public boolean isLast() {
        return this.isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }


    @Override
    public String toString() {
        return "{" +
            " bytes_len='" + getBytes().length + "'" +
            ", fileName='" + getFileName() + "'" +
            ", fileSize='" + getFileSize() + "'" +
            ", fragmentSize='" + getFragmentSize() + "'" +
            ", fragmentPosition='" + getFragmentPosition() + "'" +
            ", numberFragments='" + getNumberFragments() + "'" +
            ", isLast='" + isLast() + "'" +
            "}";
    }
    
}
