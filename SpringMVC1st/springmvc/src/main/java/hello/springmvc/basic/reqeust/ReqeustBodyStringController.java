package hello.springmvc.basic.reqeust;


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
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Log4j2
@Controller
public class ReqeustBodyStringController {

    @PostMapping("/reqeust-body-string-v1")
    public void reqeustBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        response.getWriter().write("OK");
    }

    @PostMapping("/reqeust-body-string-v2")
    public void reqeustBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody = {}", messageBody);
        responseWriter .write("OK");
    }

    @PostMapping("/reqeust-body-string-v3")
    public HttpEntity<String> reqeustBodyStringV3(HttpEntity<String> httpEntity) throws IOException {
        String messageBody = httpEntity.getBody();

        log.info("messageBody = {}", messageBody);
        return new HttpEntity<>("OK");
    }

    @ResponseBody
    @PostMapping("/reqeust-body-string-v4")
    public String reqeustBodyStringV4(@RequestBody String messageBody) throws IOException {

        log.info("messageBody = {}", messageBody);
        return "OK";
    }
}
