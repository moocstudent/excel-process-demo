package moocstudent.github.io.excelprocessdemo.controller;

import com.alibaba.excel.EasyExcel;
import com.google.common.eventbus.EventBus;
import moocstudent.github.io.excelprocessdemo.handler.ExcelEventListener;
import moocstudent.github.io.excelprocessdemo.listener.UploadExcelInfoDataListener;
import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import moocstudent.github.io.excelprocessdemo.service.ExcelInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private EventBus eventBus;
    @Autowired
    private ExcelEventListener listener;

    /**
     * @param theExcel excel文件
     * @param num      商家编号
     * @param length   excel内容条目数量
     * @return
     */
    @PostMapping("/importExcel/{num}/{length}")
    public Integer importExcel(@RequestParam(value = "theExcel") MultipartFile theExcel,
                               @PathVariable String num,
                               @PathVariable Long length) throws IOException {

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("num", num);
        paramsMap.put("length", length);

        EasyExcel.read(theExcel.getInputStream(),
                ExcelInfo.class,
                new UploadExcelInfoDataListener(excelInfoService, eventBus,listener, paramsMap))
                .sheet().doRead();

        eventBus.unregister(listener);
        return 1;
    }
}
