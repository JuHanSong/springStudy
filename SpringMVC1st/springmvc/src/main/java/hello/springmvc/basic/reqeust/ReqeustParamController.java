package hello.springmvc.basic.reqeust;

import hello.springmvc.basic.HelloData;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Log4j2
@Controller
public class ReqeustParamController {

    @RequestMapping("/request-param-v1")
    public void reqeustParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("usernae = {}", username);
        log.info("age : {}", age);

        response.getWriter().write("ok");
    }

    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge){

        log.info("memberAge = {}", memberAge);
        log.info("memberName : {}", memberName);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age){

        log.info("memberAge = {}", age);
        log.info("memberName : {}", username);

        return "ok";
    }


    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age){

        log.info("memberAge = {}", age);
        log.info("memberName : {}", username);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
                @RequestParam(required = true) String username,
                @RequestParam(required = false) Integer age){
        //int a = null (X)
        //Integer a = null (O)
        log.info("memberAge = {}", age);
        log.info("memberName : {}", username);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age){

        log.info("memberAge = {}", age);
        log.info("memberName : {}", username);

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap){
        log.info("userName : {}, age : {}", paramMap.get("username"), paramMap.get("age"));

        return "ok";
    }

    @ResponseBody
    @RequestMapping("/mapping-attribute-v1")
    public String modelAttriduteV1(@ModelAttribute HelloData helloData){

        log.info("userName : {}, age : {}", helloData.getUsername(), helloData.getAge());

        return "OK";
    }

    @ResponseBody
    @RequestMapping("/mapping-attribute-v2")
    public String modelAttriduteV2(HelloData helloData){

        log.info("userName : {}, age : {}", helloData.getUsername(), helloData.getAge());

        return "OK";
    }


}
