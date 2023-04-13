package com.group_1.sharedS3.repository;

/**
 * com.group_1.sharedS3.repository
 * Created by NhatLinh - 19127652
 * Date 4/12/2023 - 1:28 AM
 * Description: ...
 */
public interface FileRepository {
    boolean folderExists(String name);
    void createFolder(String name);
    void deleteFolder(String name);
    void deleteFile(String folder, String fileName);
    void uploadFile(String folder, String fileName, String contentType, byte[] content);
}