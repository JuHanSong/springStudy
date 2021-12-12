package hello.springmvc.basic.reqeust;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * {"username":"hello", "age":20}
 * content-type: application/json
 */

@Log4j2
@Controller
public class ReqeustBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void reqeustBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messegeBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        log.info("userName : {}, age :  {} ", helloData.getUsername(), helloData.getAge() );
        response.getWriter().write("ok");
    }

    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String reqeustBodyJsonV2(@RequestBody String messageBody) throws IOException {

        log.info("messegeBody = {}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("userName : {}, age :  {} ", helloData.getUsername(), helloData.getAge() );

        return "OK";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String reqeustBodyJsonV3(@RequestBody HelloData helloData){

        log.info("userName : {}, age :  {} ", helloData.getUsername(), helloData.getAge() );

        return "OK";
    }

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String reqeustBodyJsonV3(HttpEntity<HelloData> data){
        HelloData helloData = data.getBody();
        log.info("userName : {}, age :  {} ", helloData.getUsername(), helloData.getAge() );

        return "OK";
    }
}
