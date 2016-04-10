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

	public static void main(String[] args) {

		SpringApplication.run(Main.class, args);
        MongoDB.init();
        Configuration.setAudioServiceLoader(AudioServiceLoader.getInstance());

		System.out.println("You can now start any client side application.");
	}

}
