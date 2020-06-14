package cn.edu.bjtu.ebosservmgmt.service;

import cn.edu.bjtu.ebosservmgmt.entity.FileDescriptor;
import cn.edu.bjtu.ebosservmgmt.entity.FileSavingMsg;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String getThisJarPath();
    List<FileSavingMsg> saveFiles(MultipartFile[] multipartFiles, String path);
    List<FileDescriptor> getFileList(String path, String[] extensions);
    List<FileSavingMsg> sendFiles(String url, String path, String[] names);
    void execJar(String name);
    void killProcessByPort(int port);
}
