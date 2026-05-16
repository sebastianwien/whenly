package net.whenly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WhenlyApplication {

  public static void main(String[] args) {
    SpringApplication.run(WhenlyApplication.class, args);
  }
}
