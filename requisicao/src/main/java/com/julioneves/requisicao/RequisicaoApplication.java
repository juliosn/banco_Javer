package com.julioneves.requisicao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.julioneves.requisicao")
@EnableFeignClients(basePackages = "com.julioneves.requisicao.proxy")
public class RequisicaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(RequisicaoApplication.class, args);
	}

}
