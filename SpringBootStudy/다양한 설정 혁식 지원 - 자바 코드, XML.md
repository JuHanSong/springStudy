# 다양한 설정 혁식 지원 - 자바 코드, XML

* 스프링 컨테이너느 다양한 형식으 ㅣ설정 정보를 받아드릴 수 있게 유연하게 설계되어 있다.

  * 자바코드, XML. Grooby 등...

    ![image-20210726222928800](C:\Users\jhson\AppData\Roaming\Typora\typora-user-images\image-20210726222928800.png)

# 어노케이션 기반 자바 코드 설정

* new AnnotationconfigApplicationContext(AppConfig.class);
* AnnotationConfigApplicationContext 클래스를 사용하면서 자바 코드로 된 설정 정보를 넘기면된다.

# XML설정 사용

* 최근에는 스프링 부트를 많이 사용하면서 XML 기반의 설정은 잘못 사용하지 않는다. 아직 많은 레거시 프로젝트 들이 XML로 되어 있고, 또 XML을 사용하면 컴파일 없이 빈 설정 정보를 변경할 수 있는 장점도 있으므로 한번쯤 배워두는 것도 괜찮다.

* GenericXmlApplicationContext 를 사용하면 xml 설정 파일을 넘기면 된다.







# 스프링 빈 설정 메타 정보 - BeanDefinition

* 스프링이 다양한 설정 형식을 지원하는 중심에는 BeanDefinition 이라는 추상화가 있다.
* 쉽게 이야기해서 역할과 구현을 개념적으로 나눈것 이다.
  * XML 을 읽어서 BeanDefinition을 만들면 된다.
  * 자바 코드를 읽어서 BeanDifinition을 만들면 된다.

* BeanDefinition 을 빈 설정 메타정보라 한다.
  * @Bean, <bean> 당 각각 하나씩 메타 정보가 생성.

* 스프링 컨테이너는 이 메타정보를 기반으로 스프링 빈을 생성.

![image-20210726225642520](C:\Users\jhson\AppData\Roaming\Typora\typora-user-images\image-20210726225642520.png)

![image-20210726225927522](C:\Users\jhson\AppData\Roaming\Typora\typora-user-images\image-20210726225927522.png)

```
public class BeanDefinitionTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 설정 메타정보 확인.")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("beanDefinitionName = " + beanDefinitionName + "beanDefinition =" + beanDefinition);
            }
        }
    }
}
```