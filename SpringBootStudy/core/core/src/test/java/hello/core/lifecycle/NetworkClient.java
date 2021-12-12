package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//public class NetworkClient implements InitializingBean, DisposableBean {
public class NetworkClient{
    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);

    }

    public void setUrl(String url) {
        this.url = url;
    }

    //서비스 시작시 호출
    public void connet(){
        System.out.println("connet = " + url);
    }

    public void call(String message){
        System.out.println("call = " + url + ", message : " + message);
    }

    //서비스 종료시 호출
    public void discounnet(){
        System.out.println("close = " + url);
    }

    @PostConstruct
    public void init(){
        System.out.println("NetworkClient.init");
        connet();
        call("초기화 연결 메세지");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        discounnet();
    }


//    @Override // 연결시
//    public void afterPropertiesSet() throws Exception {
//        System.out.println("NetworkClient.afterPropertiesSet");
//        connet();
//        call("초기화 연결 메세지");
//    }
//
//    @Override // 연결이 끊겼을 때
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destroy");
//        discounnet();
//    }
}
