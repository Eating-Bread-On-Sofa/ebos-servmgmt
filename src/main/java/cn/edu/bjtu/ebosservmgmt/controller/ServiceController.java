package cn.edu.bjtu.ebosservmgmt.controller;

import cn.edu.bjtu.ebosservmgmt.entity.FileDescriptor;
import cn.edu.bjtu.ebosservmgmt.entity.FileSavingMsg;
import cn.edu.bjtu.ebosservmgmt.service.FileService;
import cn.edu.bjtu.ebosservmgmt.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "查看服务中心所有的微服务")
    @CrossOrigin
    @GetMapping("/list")
    public List<FileDescriptor> getLocalServiceList(){
        String[] ex = {"jar"};
        return fileService.getFileList(getServiceFolder(), ex);
    }

    @ApiOperation(value = "向ip网关下发指定name的微服务")
    @CrossOrigin
    @PostMapping("/ip/{ip}/name/{name}")
    public List<FileSavingMsg> deployService(@PathVariable String ip, @PathVariable String name){
        String url = "http://"+ip+":8090/api/instance/service";
        String[] names = {name};
        System.out.println("deploy service: ip="+ip+", name="+name);
        return fileService.sendFiles(url,getServiceFolder(),names);
    }

    @ApiOperation(value = "上传新的微服务")
    @CrossOrigin
    @PostMapping()
    public List<FileSavingMsg> saveService(@RequestParam("file") MultipartFile[] multipartFiles){
        String path = getServiceFolder();
        return fileService.saveFiles(multipartFiles, path);
    }

    @ApiOperation(value = "微服务健康检测")
    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){return "pong";}

    private String getServiceFolder(){
        return fileService.getThisJarPath() + File.separator + "service";
    }
}
