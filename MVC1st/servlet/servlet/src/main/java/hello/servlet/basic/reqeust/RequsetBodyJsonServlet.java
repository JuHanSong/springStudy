package hello.servlet.basic.reqeust;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "reqeustBodyJsonServlet", urlPatterns = "/requset-body-json")
public class RequsetBodyJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String meessageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("meessageBody = " + meessageBody);

        HelloData helloData = objectMapper.readValue(meessageBody, HelloData.class);
        System.out.println("helloData.userName = " + helloData.getUsername());
        System.out.println("helloData.userAge = " + helloData.getAge());
        
    }
}
