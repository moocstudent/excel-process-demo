package moocstudent.github.io.excelprocessdemo.controller;

import com.alibaba.excel.EasyExcel;
import com.google.common.eventbus.EventBus;
import moocstudent.github.io.excelprocessdemo.event.ExcelProcessEvent;
import moocstudent.github.io.excelprocessdemo.handler.ExcelEventListener;
import moocstudent.github.io.excelprocessdemo.listener.UploadExcelInfoDataListener;
import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import moocstudent.github.io.excelprocessdemo.service.ExcelInfoServiceImpl;
import moocstudent.github.io.excelprocessdemo.service.RulesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 14:59
 */
@RequestMapping("/excel")
@RestController
public class ExcelImportController {

    @Autowired
    private ExcelInfoServiceImpl excelInfoService;
    @Autowired
    private RulesServiceImpl rulesService;
    @Autowired
    private EventBus eventBus;
    @Qualifier("excelEventListener")
    @Autowired
    private ExcelEventListener listener;

    /**
     * @param theExcel excel文件
     * @param num      商家编号
     * @return
     */
    @PostMapping("/importExcel/{num}")
    public Mono<ExcelProcessEvent> importExcel(@RequestParam(value = "theExcel") MultipartFile theExcel,
                                               @PathVariable String num) throws IOException {
        //这里输出前端传入的num了? 传入就开始操作下一个步骤吧
        System.out.println("获取了num:" + num);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("num", num);
        //开始读数据
        EasyExcel.read(theExcel.getInputStream(),
                ExcelInfo.class,
                new UploadExcelInfoDataListener(excelInfoService, rulesService, eventBus, listener, paramsMap))
                .sheet().doRead();

        eventBus.unregister(listener);
        /*
        websocket轮询获取进度, 这里是进行了补充
        因为经过测试 发现websocket和excel的导入其中总会有一个进行了阻塞
        并不能在excel导入时将数据发到websocket回传给前端
        而只能通过在结束时, 将最终数据发给前端(不过这也是有了一个不小的提升不是吗 ?)
       */
        ExcelProcessEvent event = listener.getProcessEventInListener(num);
        //当一切上传完毕,归零
        if (event.getAllOk()) {
            System.out.println("isAllOk!!");
            listener.resetEventHandled();
        }
        return Mono.just(event);
    }
}
