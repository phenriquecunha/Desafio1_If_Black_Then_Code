package IBM.Group2.TudoDeBom.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class MainController {

  @GetMapping
  public ModelAndView showDocumentation(){
    return new ModelAndView("redirect:/swagger-ui.html");
  }
}
