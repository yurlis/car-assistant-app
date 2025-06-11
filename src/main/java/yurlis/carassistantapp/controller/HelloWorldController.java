package yurlis.carassistantapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
@Tag(name = "Test Endpoints",
        description = "Various test endpoints for checking the functionality of the application.")
public class HelloWorldController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello Zhavinka!";
    }
}
