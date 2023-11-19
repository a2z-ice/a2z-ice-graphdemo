package com.course;

import com.netflix.graphql.dgs.DgsQueryExecutor;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FakeHelloDataResolverTest {
    @Autowired
    DgsQueryExecutor dgsQueryExecutor;

    @Test
    void testOneHello() {
        var graphQuery = """
                {
                 oneHello{
                  text
                  randomNumber
                 }
                }
                """;
        String text = dgsQueryExecutor.executeAndExtractJsonPath(graphQuery, "data.oneHello.text");
        Integer randomNumber = dgsQueryExecutor.executeAndExtractJsonPath(graphQuery, "data.oneHello.randomNumber");

        assertFalse(StringUtils.isBlank(text));
        assertNotNull(randomNumber);
    }
    @Test
    void testAllHellos(){
        var graphQuery = """
                {
                 allHellos{
                    text
                    randomNumber
                 }
                }
                """;
        final List<String> textList = dgsQueryExecutor.executeAndExtractJsonPath(graphQuery, "data.allHellos[*].text");
        final List<Integer> randomNumberList = dgsQueryExecutor.executeAndExtractJsonPath(graphQuery, "data.allHellos[*].randomNumber");

        assertNotNull(textList);
        assertFalse(textList.isEmpty());
        assertEquals(textList.size(), randomNumberList.size());
    }
}
