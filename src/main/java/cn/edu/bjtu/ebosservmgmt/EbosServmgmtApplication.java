package cn.edu.bjtu.ebosservmgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EbosServmgmtApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbosServmgmtApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(3000);
        simpleClientHttpRequestFactory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        return restTemplate;
    }
}
