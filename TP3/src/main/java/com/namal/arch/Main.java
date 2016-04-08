package com.namal.arch;

import java.util.Arrays;

import org.apache.catalina.Server;
import org.apache.catalina.mbeans.MBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import com.namal.arch.models.services.AudioServiceLoader;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.MongoDB;
import org.springframework.core.env.Environment;

import javax.management.*;
import javax.servlet.ServletRequest;

/**
 * Main class.
 * @author namalgac
 *
 */
@SpringBootApplication
public class Main{

    @Autowired
    static Environment environment;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Uncomment/Comment for enable/disable UI


		ApplicationContext ctx = SpringApplication.run(Main.class, args);
        MongoDB.init();
        Configuration.setAudioServiceLoader(AudioServiceLoader.getInstance());

        AudioServiceLoader.getInstance().getAudioServices();
        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

	}

}
