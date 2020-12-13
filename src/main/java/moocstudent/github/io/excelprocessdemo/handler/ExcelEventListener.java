package moocstudent.github.io.excelprocessdemo.handler;

import com.alibaba.fastjson.JSONObject;
import com.google.common.eventbus.Subscribe;
import moocstudent.github.io.excelprocessdemo.event.ExcelProcessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @Author: zhangQi
 * @Date: 2020-12-13 14:40
 */
public class ExcelEventListener extends AbstractWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(ExcelEventListener.class);

    private int eventsHandled;

    private boolean isAllOk;

    @Subscribe
    public void excelProcessEvent(ExcelProcessEvent event){
        eventsHandled+=event.getAlreadyInSize();
        isAllOk = event.getAllOk();
    }

    int getEventsHandled() {
        return eventsHandled;
    }

    boolean getAllOk(){
        return isAllOk;
    }

    void resetEventHandled(){
        eventsHandled = 0;
        isAllOk = false;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        log.info("WebSocket 建立连接...");
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)throws Exception{
        log.info("接收到消息: "+message.getPayload());
        Mono.delay(Duration.ofMillis(2000));
        //发送文本消息
        JSONObject jsonObject = new JSONObject();

//        jsonObject.put("total",1100); //total可由前端判定excel文件内容rows大小获得也可以
        jsonObject.put("currentSize",eventsHandled);
        jsonObject.put("isAllOk",isAllOk);

        session.sendMessage(new TextMessage(jsonObject.toString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus){
        log.info("WebSocket 连接关闭...");
    }

}
