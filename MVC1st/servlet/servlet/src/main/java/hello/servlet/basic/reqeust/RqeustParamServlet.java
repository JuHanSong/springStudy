package hello.servlet.basic.reqeust;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 1. 파라미터 전송기능.
* http://localhost:8080/request-param?useranme=hello&age=20
*  */
@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RqeustParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("RqeustParamServlet.service");
        System.out.println("전체 파라미터 조회 - Start");
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName-> System.out.println(paramName + " : " + request.getParameter(paramName)));

        System.out.println("전체 파라미터 조회 - End");
        System.out.println();

        System.out.println("단일 파라미터 조회");
        String userName = request.getParameter("userName");
        String age = request.getParameter("age");

        System.out.println("userName = " + userName);
        System.out.println("age = " + age);
        System.out.println();

        System.out.println("복수 파라미터 조회");
        String[] userNames = request.getParameterValues("userName");
        for (String name : userNames) {
            System.out.println("username = " + name);
        }

        response.getWriter().write("OK");
    }
}
