package cn.edu.bjtu.ebosservmgmt.controller;

import cn.edu.bjtu.ebosservmgmt.entity.FileDescriptor;
import cn.edu.bjtu.ebosservmgmt.entity.FileSavingMsg;
import cn.edu.bjtu.ebosservmgmt.service.FileService;
import cn.edu.bjtu.ebosservmgmt.service.LogService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Api(tags = "服务管理")
@RequestMapping("/api/service")
@RestController
public class ServiceController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LogService logService;
    @Autowired
    FileService fileService;

    @CrossOrigin
    @GetMapping("/list")
    public List<FileDescriptor> getLocalServiceList(){
        String[] ex = {"jar"};
        return fileService.getFileList(getServiceFolder(), ex);
    }

    @CrossOrigin
    @PostMapping("/ip/{ip}/name/{name}")
    public List<FileSavingMsg> deployService(@PathVariable String ip, @PathVariable String name){
        String url = "http://"+ip+":8090/api/instance/service";
        String[] names = {name};
        System.out.println("deploy service: ip="+ip+", name="+name);
        return fileService.sendFiles(url,getServiceFolder(),names);
    }

    @CrossOrigin
    @PostMapping()
    public List<FileSavingMsg> saveService(@RequestParam("file") MultipartFile[] multipartFiles){
        String path = getServiceFolder();
        return fileService.saveFiles(multipartFiles, path);
    }

    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){return "pong";}

    private String getServiceFolder(){
        return fileService.getThisJarPath() + File.separator + "service";
    }
}
