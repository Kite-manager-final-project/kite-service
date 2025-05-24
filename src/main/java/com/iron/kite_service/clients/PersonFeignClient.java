package com.iron.kite_service.clients;

import com.iron.kite_service.dtos.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "person-service")
public interface PersonFeignClient {

    @GetMapping("/api/person/{nickName}")
    PersonDTO getPersonByNickName(@PathVariable String nickName);
}
