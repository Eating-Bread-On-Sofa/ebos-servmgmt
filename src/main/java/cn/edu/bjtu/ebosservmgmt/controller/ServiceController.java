package cn.edu.bjtu.ebosservmgmt.controller;

import cn.edu.bjtu.ebosservmgmt.service.FileService;
import cn.edu.bjtu.ebosservmgmt.service.LogService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.File;

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
    public JSONArray getLocalServiceList(){
        String[] ex = {"jar"};
        return fileService.getFileList(getServiceFolder(), ex);
    }

    @CrossOrigin
    @PostMapping("/ip/{ip}/name/{name}")
    public JSONObject deployService(@PathVariable String ip, @PathVariable String name){
        String url = "http://"+ip+":8090/api/instance/service";
        String[] names = {name};
        System.out.println("deploy service: ip="+ip+", name="+name);
        return fileService.sendFiles(url,getServiceFolder(),names);
    }

    private String getServiceFolder(){
        return fileService.getThisJarPath() + File.separator + "service";
    }
}
