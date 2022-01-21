package springboot.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import springboot.domain.bean.Person;

@FeignClient(name = "consumer", fallback = ConsumerGetPersonApi.ConsumerGetPersonApiImpl.class)
public interface ConsumerGetPersonApi {

    @GetMapping("get-one-person")
    Person getOnePerson();

    @Component
    class ConsumerGetPersonApiImpl implements ConsumerGetPersonApi {

        @Override
        public Person getOnePerson() {
            return null;
        }
    }

}
