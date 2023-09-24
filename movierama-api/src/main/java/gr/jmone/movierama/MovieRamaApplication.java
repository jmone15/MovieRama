package gr.jmone.movierama;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
public class MovieRamaApplication {

  public static void main(String[] args) {
    SpringApplication.run(MovieRamaApplication.class, args);
  }
}
