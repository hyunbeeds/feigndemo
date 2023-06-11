package com.polarbear.feigndemo.service.user;

import com.polarbear.feigndemo.config.error.exceptions.ClientException;
import com.polarbear.feigndemo.config.error.exceptions.ServerException;
import com.polarbear.feigndemo.config.infra.resttemplate.RestTemplateProperty;
import com.polarbear.feigndemo.controller.view.UserView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Service
public class UserService {

    private final RestTemplateProperty property;
    private final RestTemplate restTemplate;
    private final UserFeignClient userFeignClient;

    public UserService(@Qualifier("userServiceProperty") RestTemplateProperty property, UserFeignClient userFeignClient) {
        this.property = property;
        this.restTemplate = new RestTemplateBuilder()
                .rootUri(property.getHost())
                .setReadTimeout(Duration.ofMillis(property.getReadTimeout()))
                .setConnectTimeout(Duration.ofMillis(property.getConnectionTimeout()))
                .errorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        return !response.getStatusCode().is2xxSuccessful();
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) throws IOException {
                        HttpStatus status = response.getStatusCode();
                        if (status.is5xxServerError()) {
                            throw new ServerException();
                        } else {
                            throw new ClientException();
                        }
                    }
                })
                .build();
        this.userFeignClient = userFeignClient;
    }

    public UserView getUserByFeignClient(Long id) {
        return userFeignClient.getUser(id);
    }

    public UserView getUserByRestTemplate(Long id) {
        String uri = UriComponentsBuilder.fromHttpUrl(this.property.getHost())
                .path("/users/{id}")
                .build(id)
                .toString();

        return restTemplate.getForObject(uri, UserView.class);
    }
}
