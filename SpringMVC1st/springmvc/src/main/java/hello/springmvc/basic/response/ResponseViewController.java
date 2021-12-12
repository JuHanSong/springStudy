package hello.springmvc.basic.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1(){
        log.info("Path : {}", "/response-view-v1");
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");

        return mav;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model){
        model.addAttribute("data", "hello");

        return "response/hello";
    }

    @RequestMapping("/response-view-v3")
    public void responseViewV3(Model model){
        model.addAttribute("data", "hello");
    }
}
