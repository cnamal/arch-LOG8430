package com.namal.arch;

import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by namalgac on 4/8/16.
 */
@Configuration
@EnableWebMvc
public class WebMVCConfig extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {
}
