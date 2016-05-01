package com.namal.arch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.namal.arch.models.services.AudioServiceLoader;
import com.namal.arch.utils.Configuration;
import com.namal.arch.utils.MongoDB;

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
