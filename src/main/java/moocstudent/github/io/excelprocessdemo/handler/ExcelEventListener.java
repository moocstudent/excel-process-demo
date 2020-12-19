package moocstudent.github.io.excelprocessdemo.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import moocstudent.github.io.excelprocessdemo.event.ExcelProcessEvent;
import moocstudent.github.io.excelprocessdemo.model.ExcelInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 14:40
 */
@Component
public class ExcelEventListener extends AbstractWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(ExcelEventListener.class);

    /**
     * 模拟了这个excel的num编号,用于区分其它的excel,但其实这并没有什么卵用
     * 估计用它用于判定吧,当并行和多线程产生时,当作一个区分的值也好
     */
    private String num;

    private int eventsHandled;

    private boolean isAllOk;

    private List<ExcelInfo> listNotValid = null;

    public String getNum() {
        return num;
    }

    public int getEventsHandled() {
        return eventsHandled;
    }

    /**
     * 如果人生一切都是allOk,那么这个人生也许并不是那么精彩
     *
     * @return
     */
    public boolean getAllOk() {
        return isAllOk;
    }

    public List<ExcelInfo> getListNotValid() {
        return listNotValid;
    }

    /**
     * 前端获取当前进度,通过轮询方式,这里已经通过@Subscribe拿到了进度数据
     * 但是最终会不会产生效果呢?这个还得问问老天爷--靠玄学吧:)
     * @param num
     * @return
     */
    public ExcelProcessEvent getProcessEventInListener(String num) {
        if (this.num.equals(num)) {
            return ExcelProcessEvent.builder()
                    .num(this.num)
                    .allOk(this.isAllOk)
                    .alreadyInSize(this.eventsHandled)
                    .listNotValid(this.listNotValid)
                    .build();
        } else {
            return null;
        }
    }

    /**
     * 通过在UploadExcelInfoDataListener中的事件event post过来数据
     * @param event
     */
    @Subscribe
    public void excelProcessEvent(ExcelProcessEvent event) {
        if (num == null || num == "") {
            num = event.getNum();
        }
        if(event.getListNotValid()!=null){
            this.listNotValid = new ArrayList<>();
        }
        System.out.println("current num:" + num);
        //每次将非法的excel条目传入
        this.listNotValid = event.getListNotValid();
        System.out.println("current listNotValid:" + listNotValid);
        eventsHandled += event.getAlreadyInSize();
        System.out.println("current eventsHandled:" + eventsHandled);
        isAllOk = event.getAllOk();
        System.out.println("current isAllOk:" + isAllOk);
    }

    public void resetEventHandled() {
        num = null;
        eventsHandled = 0;
        isAllOk = false;
        listNotValid = null;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WebSocket 建立连接...");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("接收到消息: " + message.getPayload());
        //发送文本消息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("num", num);
//        jsonObject.put("total",1100); //total可由前端判定excel文件内容rows大小获得也可以
        jsonObject.put("currentSize", eventsHandled);
        System.out.println("send currentSize:" + eventsHandled);
        jsonObject.put("isAllOk", isAllOk);
        jsonObject.put("listNotValid", listNotValid);
        System.out.println("send isAllOk:" + isAllOk);
        System.out.println("send listNotValid:" + listNotValid);
        session.sendMessage(new TextMessage(jsonObject.toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        log.info("WebSocket 连接关闭...");
    }


}
