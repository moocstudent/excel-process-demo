package moocstudent.github.io.excelprocessdemo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.EventBus;
import moocstudent.github.io.excelprocessdemo.event.ExcelProcessEvent;
import moocstudent.github.io.excelprocessdemo.handler.ExcelEventListener;
import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import moocstudent.github.io.excelprocessdemo.service.ExcelInfoServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 15:05
 */
public class UploadExcelInfoDataListener extends AnalysisEventListener<ExcelInfo> {
    //根据前端给的进行变动,如果给的少于1000条,那么久没办法展示进度条了,所以按前端给的数据量来更改该值
    /**
     * 10000
     * 基数: 1/2/3
     * 10
     * 2条数据->50%->100%
     * excel的总数据条目,通过前端excel组件插件来获取,通过后端也可以,当前通过前端获取最好
     *
     * 10000
     * string=10000.length = 5
     * 9000+"".length=4  9个1000条
     * 680+"".length=3
     * 最合理的最大幂次: 3
     *
     * if(length<=最大幂次+1){ 基数*Math.pow(10,length-1) }
     * else{ 基数*Math.pow(10,最大幂次) }
     *
     * 3,000,000 条数据 导入
     * 3000*1000 = 3,000,000
     * 3000+3000 = 6000
     * 1000
     *
     * 6000/3,000,000 * 100% 进度
     *
     *
     *
     * 基数 和 最大幂次 在配置文件中设置
     *
     *
     */
    private int BATCH_COUNT = 3000;

    List<ExcelInfo> list = new ArrayList<ExcelInfo>();

    Map<String,Object> paramsMap;

    private ExcelInfoServiceImpl excelInfoService;

    private EventBus eventBus;

    private Map<String,Object> insertMap = new HashMap();

    public UploadExcelInfoDataListener(ExcelInfoServiceImpl excelInfoService,
                                       EventBus eventBus,
                                       ExcelEventListener listener,
                                       Map<String,Object> paramsMap){
        this.excelInfoService = excelInfoService;
        this.paramsMap = paramsMap;
        this.eventBus = eventBus;

        eventBus.register(listener);
    }

    @Override
    public void invoke(ExcelInfo excelInfo, AnalysisContext analysisContext) {
        System.out.println("解析到excel数据:"+JSON.toJSONString(excelInfo));
        //判定规则,符合规则记录到list,不符合规则放入redis?
//        if(excelInfo判定规则){
//
//        }
        list.add(excelInfo);

        if(list.size()>=BATCH_COUNT){
            saveData();
            list.clear();
        }

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        System.out.println("所有数据解析保存完成finally!");
        ExcelProcessEvent processEvent = ExcelProcessEvent.builder().allOk(true).build();
        eventBus.post(processEvent);
    }

    private void saveData(){
        Integer insertCode = excelInfoService.insertSelectBatch(list);
        if(insertCode>0){
            System.out.println("本次数据解析保存完成!");
        }
        //发送事件到ExcelHandler
        ExcelProcessEvent processEvent = ExcelProcessEvent.builder().alreadyInSize(list.size()).build();
        eventBus.post(processEvent);
    }
}
