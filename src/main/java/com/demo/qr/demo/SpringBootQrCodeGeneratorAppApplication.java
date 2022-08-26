package com.pixeltrice.springbootQRcodegeneratorapp;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;


import com.github.tomakehurst.wiremock.http.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.contract.wiremock.WireMockSpring;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import java.io.IOException;
import java.util.stream.Stream;
import com.github.tomakehurst.wiremock.core.Options;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServer;
import com.github.tomakehurst.wiremock.direct.DirectCallHttpServerFactory;
import wiremock.com.google.common.base.Optional;


import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootApplication
@AutoConfigureWireMock()
public class SpringBootQrCodeGeneratorAppApplication {

	public static void main(String[] args) {

		SpringApplication.run(SpringBootQrCodeGeneratorAppApplication.class, args);
	}


	@Bean
	public Options wireMockOptions() throws IOException {
     	DirectCallHttpServerFactory factory = new DirectCallHttpServerFactory();
		WireMockServer wm = new WireMockServer(wireMockConfig().httpServerFactory(factory));
		wm.start(); // no-op, not required

		DirectCallHttpServer server = factory.getHttpServer();
		
		Request request = new Request() {
			@Override
			public String getUrl() {
				return "/api/mytest";
			}

			@Override
			public String getAbsoluteUrl() {
				return null;
			}

			@Override
			public RequestMethod getMethod() {
				return new RequestMethod("GET");
			}

			@Override
			public String getScheme() {
				return null;
			}

			@Override
			public String getHost() {
				return null;
			}

			@Override
			public int getPort() {
				return 0;
			}

			@Override
			public String getClientIp() {
				return null;
			}

			@Override
			public String getHeader(String s) {
				return " \"Content-Type\": \"application/json\",\n" +
						"            \"Cache-Control\": \"max-age=86400\"";
			}

			@Override
			public HttpHeader header(String s) {
				return null;
			}

			@Override
			public ContentTypeHeader contentTypeHeader() {
				return null;
			}

			@Override
			public HttpHeaders getHeaders() {
				return null;
			}

			@Override
			public boolean containsHeader(String s) {
				return false;
			}

			@Override
			public Set<String> getAllHeaderKeys() {
				return null;
			}

			@Override
			public Map<String, Cookie> getCookies() {
				return null;
			}

			@Override
			public QueryParameter queryParameter(String s) {
				return null;
			}

			@Override
			public byte[] getBody() {
				return new byte[0];
			}

			@Override
			public String getBodyAsString() {
				return null;
			}

			@Override
			public String getBodyAsBase64() {
				return null;
			}

			@Override
			public boolean isMultipart() {
				return false;
			}

			@Override
			public Collection<Part> getParts() {
				return null;
			}

			@Override
			public Part getPart(String s) {
				return null;
			}

			@Override
			public boolean isBrowserProxyRequest() {
				return false;
			}

			@Override
			public Optional<Request> getOriginalRequest() {
				return null;
			}
		};

		Response response = server.stubRequest(request);
// then use the `response`'s data, and map it accordingly
          System.out.println(response.getBodyAsString());


		final WireMockConfiguration options =  WireMockSpring.options();
		options.port(9990);
		return options;
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			Stream.of("John", "Julie", "Jennifer", "Helen", "Rachel").forEach(name -> {
				User user = new User(name, name.toLowerCase() + "@domain.com");
				userRepository.save(user);
			});
			userRepository.findAll().forEach(System.out::println);
		};
	}
}
