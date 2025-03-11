package com.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

@Controller("/demo")
public class DemoController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }
}