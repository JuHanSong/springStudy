package hello.springmvc.basic.reqeust;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Log4j2
@RestController
public class ReqeustHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod method,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value = "myCooke", required = false)String cookie){
        log.info("reqeust : {}", request);
        log.info("response : {}", response);
        log.info("method : {}", method);
        log.info("locale : {}", locale);
        log.info("headerMap : {}", headerMap);
        log.info("host : {}", host);
        log.info("cookie : {}", cookie);

        return "oK";
    }
}
