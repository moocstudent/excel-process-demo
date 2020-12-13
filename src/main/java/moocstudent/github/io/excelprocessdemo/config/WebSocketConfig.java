package moocstudent.github.io.excelprocessdemo.config;

import moocstudent.github.io.excelprocessdemo.handler.ExcelEventListener;
import moocstudent.github.io.excelprocessdemo.interceptor.WebSocketHandshakeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @Author: zhangQi 仿照github开源代码
 * @Date: 2020-12-13 10:56
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     *
     * @param registry 该对象可以调用addHandler()来注册信息处理器
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(excelHandler(), "/excel")
                .addInterceptors(webSocketHandshakeInterceptor()) //声明拦截器
                .setAllowedOrigins("*"); //声明允许访问的主机列表

        //声明启用SockJS连接,如果前端还用new WebSocket(url); 会报: Error during WebSocket handshake: Unexpected response code: 200
//        registry.addHandler(excelHandler(),"/marcoSockJS")
//                .setAllowedOrigins("*")
//                .withSockJS();
    }

    @Bean
    public ExcelEventListener excelHandler() {
        return new ExcelEventListener();
    }

    @Bean
    public WebSocketHandshakeInterceptor webSocketHandshakeInterceptor() {
        return new WebSocketHandshakeInterceptor();
    }
}
