package com.iron.kite_service.clients;

import com.iron.kite_service.dtos.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "person-service")
public interface PersonFeignClient {

    @GetMapping("/api/person/{nickName}")
    PersonDTO getPersonByNickName(String nickName);
}
