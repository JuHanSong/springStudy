# @Configuration 과 바이트코드 조작

스프링 컨테이너느 싱글톤 레지스트리다. 따라서 스프링 빈이 싱글톤이 되도록 보장해주어야 한다. 그런데 스프링이 자바 코드까지 어떻게 하기는 어렵다.

저 자바코드를 보면 분명 3번호풀이 되어야 하는것이 맞다.

그래서 스프링은 클래스의 바이트코드를 조작하는 라이브러리를 사용한다.

모든 비밀은 @Configuration 을 적용한 AppConfig 에 있다.



```
public class ConfiurationSingletonTest {

    @Test
    void configurationTest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println("memberService -> memberRepository = " + memberRepository1);
        System.out.println("orderService -> memberRepository = " + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean = " + bean.getClass());
    }
}
```

* 사실 AnnotationConfigApplicationContext 에 파라미터로 넘긴 값은 스프링 빈으로 등록된다. 그래서 AppConfig 도 스프링 빈이 된다.

* 그런데 예상과 다르게 클래스 명에 xxxCGLIB 가 붙으면서 상당히 복잡해진 것을 볼수 있다. 이것은 내가 만든 클래스가 아니라

  스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 Appconfig 클래스를 상속받은 임의의 다른 클래스를 만들고, 그 다른 클래스를 스프링 빈으로 등록한 것이다.

![image-20210728223721670](C:\Users\jhson\AppData\Roaming\Typora\typora-user-images\image-20210728223721670.png)

## 정리

* @Bean 만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장할지 않는다.
  * memberRepository() 처럼 의존관게 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.

* 크게 고민할 것이 없다. 스프링 설정 정보는 항상 @Configuration 을 사용하자.



# 컴포넌트 스캔과 의존관계 자동 주입 시작하기

* 지금까지 스프링 빈을 등록할 때는 자바코드의 @Bean 이나 @Bean XML<bean> 등을 통해서 설정 정보에 직접 등록 할 스프링 빈을 나열했다.
* 예제에서는 몇개가 안되었지만, 이렇게 등록해야 할 스프링 빈이 많아지만 관리가 힘들고, 설정 정보도 커지고, 누락하는 문제도 발생한다.
* 그래서 스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔이라는 기능을 제공한다.
* 또 의존관계도 자동으로 주입하는 @Autowire 라는 기능도 제공한다.
* @Component 애노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록한다.



##  MemoryMemberRepository @Component 추가

```
@Component
public class MemoryMemberRepository implements MemberRepository{

    private  static Map<Long, Member> store = new HashMap<>();

    @Override
    public void save(Member member) {
        store.put((member.getId()), member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
```

## RateDiscountPolicy @Component 추가

```
@Component
public class RateDiscountpolicy implements DiscountPolicy{
    private int discountPercent = 10;

    @Override
    public int discount(Member member, int price) {
        if(member.getGrade() == Grade.VIP){
            return price * discountPercent / 100;
        }else {
            return 0;
        }

    }
}
```

## MemberServicelmpl @Component, @Autowired 추가

```
@Component
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Autowired // ac.getBean(MemberReopsitory.class);
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    // 테스트용도.
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
```

* 이전에 AppConf 에서  @Bean 으로 직접 설정 정보를 작성헀고, 의존관게도 직접 명시헀다. 이제는 이런 설정 정보 자체가 없기 때문에, 의존관계 주입도이 클래스 안에서 해결해야 한다.
* @Autowired 는 의존관계를 자동으로 주입해준다.

## AutoAppConfigTest.java

```
public class AutoAppConfigTest {

    @Test
    void basicScan(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

        MemberService memberService = ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
```

* AnnotationConfigApplicationContext 를 사용하는 것은 기존과 동일하다.
* 설정 정보로 AutoAppConfig 클래스를 넘겨준다.
* 실행해보면 기존과 같이 잘 동작하는 것을 확인할 수있다.

## 탐색 위치와 기본대상.

* 탐색할  패키지의 시작 위치지정.
  * 모든 자바 클래스를 다 컴포넌트 스캔하면 시간이 오래 걸린다. 그래서 꼭 필요한 위치부터 탐색하도록 시작 위치를 지정할 수있다.
  * basePackaged : 탐색할 패키지의 시작 위치를 지정한다. 이 패키지를 포함해서 하위 패키지를 모두 탐색한다.
    * basePackages = {"hello.core", "hello.service"} 이렇게 여러 시작 위치를 지정할 수도 있다.

* 만약 지정하지 않그면 @ComponentScan 이붙은 설정 정보 클래스의 패키지 시작 위치가 된다.



* 권장하는 방법

  개인적으로 즐겨 사용하는 방법은 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는것이다. 최근 스프링 부트도 이 방법을 기본적으로 제공한다.

  * com.hell
  * com.hell.service
  * comhello.repository

  com.hell 프로젝트 시작 루트, 여기에 AppConfig 같은 메인 설정 정보를 두고, @ComponentScan 애노테이션을 붙이고, basePackages 지정은 생략.

  이렇게 하면, com.hell 를 포함한 하위는 모두 자동으로 컴포넌트 스캔의 대상이 된다. 그래고 프로젝트 메인 설정 정보는 프로젝트를 대표하는 정보이기 때문에 프로젝트 시작 루트 위치에 두는 것이 좋다 생각한다.

  참고로 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 @SpringBootApplication 를 이 프로제트 시작 루트 위치에 두는 것이 관례이다.

## 컴포넌트 스캔 기본대상.

* 컴포넌트 스캔은 @Component 뿐만 아니라 다음과 내용도 추가로 대상에 포함한다.
  * @Component : 컴포넌트 스캔에서 사용
  * @Controller : 스프링 MVC컨트롤러에서 사용.
  * @Service : 스프링 비지니스 로직에서 사용.
  * @Repository : 스프링 데이터 접근 계층에서 사용.
  * @Configuration : 스프링 설정 정보에서 사용.

# 필터

```
@ComponentScan(
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyExcludeCompnent.class)
)
```

* includeFilter 에 MyIncludeComponent애노테이션을 추가해서 BeanA가 스프링 빈에 등록한다.
* excludeFilters 에 MyExcludeComponent 애노테이션을 추가해서 BeanB가 스프링 빈에 등록되지 않는다.



## FilterType Option

* ANOTATION : 기본값, 애노테이션을 인식해서 동작.
  * ex) org.example.SomeAnnotation

* ASSIGNALBE-TYPE : 지정한 타이과 자식 타입을 인식해서 동작.
  * ex) org.example.SomeClass

* ASPECTJ : AspectJ패턴사용
  * ex) org.example...*Service+

* REGEX : 정규 표현식
  * ex) org.\example\.DEfault.*

* CUSTOM : TypeFitler 이라는 인터페이스를 구현해서 처리
  * ex) org.example.MyTypeFitler

## 중복 등록과 충돌

1. 자동 빈 등록 vs 자동 빈 등록.
2. 수동 빈 등록 vs 자동 빈등록.

### 자동 빈 등록 vs 자동 빈 등록

* 컴포넌트 스캔에 의해 자동으로 스프링 빈이 등록되는데, 그 이름이 같은 경우 스프링은 오류를 발생시킨다.
  * ConfilictingBeanDefinitionException 예외 발생.

```
@Configuration
@ComponentScan(
        basePackages = "hello.core.member",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)// ComponentScan 애노테이션은 컴포넌트가 붙은것을 전부 Bean 등록윽 진행하는데 필터 설정에 따라 제외를 시켜줄 수 있다.
public class AutoAppConfig {

    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
```

* 이 경우 수동 빈 들록이 우선권을 가진다.(수동 빈이 자동빈을 오버라이딩 해버린다.)

수동빈 등록시 남는 로그 : Overriding bean definition for bean 'memoryMemberRepository' with a different definition: replacing 

## @Autowired 필드 명, @Quilifier, @Primary 

조회 대상빈이 2개 이상일 때 해결 방법

* @Autowired 필드명 매칭
* @Quilifier - @Quilifier 끼리매칭 - 빈 이름매칭
* @Primary 사용

### @Autowired 필드 명 매칭

* Autowired 는 타입 매칭을 시도하고, 이때 여러 빈이 있으면 필드 이름(파라미터 이름)으로 빈 이름을 추가 매칭한다.

