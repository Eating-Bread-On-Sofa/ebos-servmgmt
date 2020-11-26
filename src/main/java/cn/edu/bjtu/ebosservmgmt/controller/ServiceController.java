package cn.edu.bjtu.ebosservmgmt.controller;

import cn.edu.bjtu.ebosservmgmt.entity.FileDescriptor;
import cn.edu.bjtu.ebosservmgmt.entity.FileSavingMsg;
import cn.edu.bjtu.ebosservmgmt.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    SubscribeService subscribeService;
    @Autowired
    MqFactory mqFactory;

    public static final List<RawSubscribe> status = new LinkedList<>();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 50,3, TimeUnit.SECONDS,new SynchronousQueue<>());


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
        logService.info("update","向"+ip+"网关下发"+name+"微服务");
        return fileService.sendFiles(url,getServiceFolder(),names);
    }

    @ApiOperation(value = "上传新的微服务")
    @CrossOrigin
    @PostMapping()
    public List<FileSavingMsg> saveService(@RequestParam("file") MultipartFile[] multipartFiles){
        String path = getServiceFolder();
        logService.info("create","上传了一个新的微服务");
        return fileService.saveFiles(multipartFiles, path);
    }

    @ApiOperation(value = "微服务订阅mq的主题")
    @CrossOrigin
    @PostMapping("/subscribe")
    public String newSubscribe(RawSubscribe rawSubscribe){
        if(!ServiceController.check(rawSubscribe.getSubTopic())){
            try{
                status.add(rawSubscribe);
                subscribeService.save(rawSubscribe.getSubTopic());
                threadPoolExecutor.execute(rawSubscribe);
                logService.info("create","服务管理成功订阅主题"+ rawSubscribe.getSubTopic());
                return "订阅成功";
            }catch (Exception e) {
                e.printStackTrace();
                logService.error("create","服务管理订阅主题"+rawSubscribe.getSubTopic()+"时，参数设定有误。");
                return "参数错误!";
            }
        }else {
            logService.error("create","服务管理已订阅主题"+rawSubscribe.getSubTopic()+",再次订阅失败");
            return "订阅主题重复";
        }
    }

    public static boolean check(String subTopic){
        boolean flag = false;
        for (RawSubscribe rawSubscribe : status) {
            if(subTopic.equals(rawSubscribe.getSubTopic())){
                flag=true;
                break;
            }
        }
        return flag;
    }

    @ApiOperation(value = "删除微服务订阅mq的主题")
    @CrossOrigin
    @DeleteMapping("/subscribe/{subTopic}")
    public boolean delete(@PathVariable String subTopic){
        boolean flag;
        synchronized (status){
            flag = status.remove(search(subTopic));
        }
        return flag;
    }

    public static RawSubscribe search(String subTopic){
        for (RawSubscribe rawSubscribe : status) {
            if(subTopic.equals(rawSubscribe.getSubTopic())){
                return rawSubscribe;
            }
        }
        return null;
    }

    @ApiOperation(value = "微服务向mq的某主题发布消息")
    @CrossOrigin
    @PostMapping("/publish")
    public String publish(@RequestParam(value = "topic") String topic,@RequestParam(value = "message") String message){
        MqProducer mqProducer = mqFactory.createProducer();
        mqProducer.publish(topic,message);
        return "发布成功";
    }

    @ApiOperation(value = "微服务健康检测")
    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }

    private String getServiceFolder(){
        return fileService.getThisJarPath() + File.separator + "service";
    }
}
