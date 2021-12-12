package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {
    @Test
    void statefulServiceSingleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //ThreadA : A사용자가 10000원을 주문
        statefulService1.order("userA" , 10000);
        int userAPrice = statefulService1.order2("userA", 10000);
        //ThreadA : A사용자가 20000원을 주문
        statefulService2.order("userB" , 20000);
        int userBPrice = statefulService1.order2("userA", 20000);
        //TreadA : 사용자A 주문금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);
        System.out.println("userAprice = " + userAPrice);
        System.out.println("userBprice = " + userBPrice);
        Assertions.assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}