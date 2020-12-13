//package moocstudent.github.io.excelprocessdemo.listener;
//
//import com.google.common.eventbus.Subscribe;
//import moocstudent.github.io.excelprocessdemo.event.ExcelProcessEvent;
//
///**
// * @Author: zhangQi
// * @Date: 2020-12-13 17:26
// */
//public class UploadExcelSuccessEventListener {
//
//    private int eventsHandled;
//
//    private Boolean isAllOk;
//
//    @Subscribe
//    public void excelProcessEvent(ExcelProcessEvent event){
//        eventsHandled+=event.getAlreadyInSize();
//        isAllOk = event.getAllOk();
//    }
//
//    int getEventsHandled() {
//        return eventsHandled;
//    }
//
//    boolean getAllOk(){
//        return isAllOk;
//    }
//
//    void resetEventHandled(){
//        eventsHandled = 0;
//    }
//}
