package com.course.datasource.fake;

import com.course.graphql.generated.types.Hello;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration
public class FakerHelloDataSource {

    @Autowired
    Faker faker;

    public static final List<Hello> HELLO_LIST = new ArrayList<>();

    @PostConstruct
    private void postConstruct(){
        HELLO_LIST.addAll(IntStream.range(0,20)
                .mapToObj(i -> Hello.newBuilder().randomNumber(faker.random().nextInt(5000))
                        .text(faker.company().name()).build()
                ).collect(Collectors.toList()));
    }
}
