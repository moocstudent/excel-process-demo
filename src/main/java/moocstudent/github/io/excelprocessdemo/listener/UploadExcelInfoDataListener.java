package moocstudent.github.io.excelprocessdemo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.google.common.eventbus.EventBus;
import moocstudent.github.io.excelprocessdemo.event.ExcelProcessEvent;
import moocstudent.github.io.excelprocessdemo.handler.ExcelEventListener;
import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import moocstudent.github.io.excelprocessdemo.service.ExcelInfoServiceImpl;
import moocstudent.github.io.excelprocessdemo.service.RulesServiceImpl;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 15:05
 */
public class UploadExcelInfoDataListener extends AnalysisEventListener<ExcelInfo> {

    //其实一次1000条已经算很少了
    private int BATCH_COUNT = 1000;
    //基数
    private int BASE_NUM = 1;
    //最大幂次
    private int MAX_POW = 3;

    private Integer totalCount;

    private Integer totalLength;

    List<ExcelInfo> list = new ArrayList<ExcelInfo>();

    List<ExcelInfo> listNotValid = null;

    Map<String, Object> paramsMap;

    private ExcelInfoServiceImpl excelInfoService;

    private RulesServiceImpl rulesService;

    private EventBus eventBus;

    private Map<String, Object> insertMap = new HashMap();

    public UploadExcelInfoDataListener(ExcelInfoServiceImpl excelInfoService,
                                       RulesServiceImpl rulesService,
                                       EventBus eventBus,
                                       ExcelEventListener listener,
                                       Map<String, Object> paramsMap) {

        System.out.println("new UploadExcelInfoDataListener");

        this.excelInfoService = excelInfoService;
        this.rulesService = rulesService;
        this.paramsMap = paramsMap;
        this.eventBus = eventBus;

        this.eventBus.register(listener);
    }

    @Override
    public void invoke(ExcelInfo excelInfo, AnalysisContext analysisContext) {
        System.out.println("解析到excel数据:" + JSON.toJSONString(excelInfo));
        //通过这个getTotalCount方法可以获取excel总条目
        if (totalCount == null) {
            totalCount = analysisContext.getTotalCount();
            totalLength = totalCount.toString().length();
            /**
             * 根据总条目来选择适合导入的批次数量
             * 如果给的少于1000条,那么久没办法展示进度条了,所以按前端给的数据量来更改该值
             * 10000
             * 基数: 1/2/3
             * 10
             * 2条数据->50%->100%
             * excel的总数据条目,通过前端excel组件插件来获取,通过后端也可以,当前通过前端获取最好
             * <p>
             * 10000
             * string=10000.length = 5
             * 9000+"".length=4  9个1000条
             * 680+"".length=3
             * 最合理的最大幂次: 3
             * <p>
             * if(length<=最大幂次+1){ 基数*Math.pow(10,length-1) }
             * else{ 基数*Math.pow(10,最大幂次) }
             * <p>
             * 3,000,000 条数据 导入
             * 3000*1000 = 3,000,000
             * 3000+3000 = 6000
             * 1000
             * <p>
             * 6000/3,000,000 * 100% 进度
             * <p>
             * <p>
             * <p>
             * 基数 和 最大幂次 在配置文件中设置
             */
            if (totalLength <= MAX_POW + 1) {
                BATCH_COUNT = (int) (BASE_NUM * (Math.pow(10, totalLength - 1)));
            } else {
                BATCH_COUNT = (int) (BASE_NUM * Math.pow(10, MAX_POW));
            }
            System.out.println("totalCount:" + totalCount);
            System.out.println("BATCH_COUNT:" + BATCH_COUNT);
        }

        //判定规则,符合规则记录到list,不符合规则放入redis?none:不符合规则放入另一个list,并发送给event,最终返回给前端

        if (rulesService.checkExcelWithRules(excelInfo)) {
            list.add(excelInfo);
            if (list.size() >= BATCH_COUNT) {
                System.out.println("本次导入数据list:"+list);
                saveData();
                //发送事件到ExcelHandler
                postEvent(list.size(), false);
                list.clear();
            }
        } else {
            if (listNotValid == null) {
                listNotValid = new ArrayList<>();
            }
            //没有通过规则校验的数据,放入了listNotValid,并且不予以导入
            listNotValid.add(excelInfo);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        saveData();
        System.out.println("所有数据解析保存完成finally!");
        postEvent(list.size(), true);
    }

    private void postEvent(Integer size, Boolean allOk) {
        //because alreadyInSize get before build() , so set to zero , otherwise it will NullPointer
        ExcelProcessEvent processEvent =
                ExcelProcessEvent.builder()
                        .alreadyInSize(size)
                        .allOk(allOk)
                        .num(paramsMap.get("num").toString())
                        .listNotValid(listNotValid)
                        .build();
        eventBus.post(processEvent);
    }

    /**
     * 经过多次尝试
     * 不管是通过websocket回传
     * 还是通过restful接口回传
     * 在线程内的参数都会进行阻塞(与easyexcel的上传)
     */
    private void saveData() {
        excelInfoService.insertSelectBatch(list).subscribeOn(Schedulers.immediate()).blockLast();
    }
}
