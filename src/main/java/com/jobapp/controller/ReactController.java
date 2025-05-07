package com.jobapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReactController {
    /**
     * Redirige toutes les routes non-API vers React
     */
    @RequestMapping(value = {
            "/",
            "/{path:^(?!api$|static$|favicon.ico$).*}/**"
    })
    public String forwardToReact() {
        return "forward:/index.html";
    }
}