package cn.edu.bjtu.ebosservmgmt.controller;

import cn.edu.bjtu.ebosservmgmt.service.LogService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "服务管理")
@RequestMapping("/api/service")
@RestController
public class ServiceController {
    @Autowired
    LogService logService;
}
