package com.course;


import com.course.graphql.generated.client.BooksByReleaseGraphQLQuery;
import com.course.graphql.generated.client.BooksGraphQLQuery;
import com.course.graphql.generated.client.BooksProjectionRoot;
import com.course.graphql.generated.types.Author;
import com.course.graphql.generated.types.ReleaseHistoryInput;
import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.DgsQueryExecutor;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FakeBookDataResolverTest {

    @Autowired
    DgsQueryExecutor dgsQueryExecutor;
    @Autowired
    Faker faker;

    @Test
    void testAllBook() {
        var graphqlQuery = new BooksGraphQLQuery.Builder().build();
        var projectionRoot = new BooksProjectionRoot().title().author().name()
                .originCountry()
                .getRoot().released()
                .year();
        var graphQueryRequest = new GraphQLQueryRequest(graphqlQuery,projectionRoot).serialize();

        final List<String> titles = dgsQueryExecutor.executeAndExtractJsonPath(graphQueryRequest, "data.books[*].title");
        assertNotNull(titles);
        assertFalse(titles.isEmpty());

        List<Author> authors = dgsQueryExecutor.executeAndExtractJsonPathAsObject(
                graphQueryRequest, "data.books[*].author", new TypeRef<List<Author>>(){}
        );
        assertNotNull(authors);
        assertFalse(authors.isEmpty());
        assertEquals(titles.size(), authors.size());

        List<Integer> releaseYears = dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQueryRequest, "data.books[*].released.year", new TypeRef<List<Integer>>(){});
        assertNotNull(releaseYears);
        assertEquals(titles.size(), releaseYears.size());
    }

    @Test
    public void testBooksWithInput() {
        int expectedYear = faker.number().numberBetween(2019, 2021);
        boolean expectedPrintedEdition = faker.bool().bool();

        var releaseHistoryInput = ReleaseHistoryInput.newBuilder()
                .year(expectedYear)
                .printedEdition(expectedPrintedEdition)
                .build();
        var graphQuery = BooksByReleaseGraphQLQuery.newRequest().releasedInput(releaseHistoryInput).build();
        var bookProjection = new BooksProjectionRoot().released().year().printedEdition();

        var graphQueryRequest = new GraphQLQueryRequest(graphQuery,bookProjection).serialize();

        List<Integer> releaseYears = dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQueryRequest, "data.booksByRelease[*].released.year", new TypeRef<List<Integer>>() {
        });
        Set<Integer> uniqueReleaseYears = new HashSet<>(releaseYears);
        assertNotNull(uniqueReleaseYears);
        assertTrue(uniqueReleaseYears.size() <= 1);
        if(!uniqueReleaseYears.isEmpty()) assertTrue(uniqueReleaseYears.contains(expectedYear));

         List<Boolean> releasedPrintedEditions = dgsQueryExecutor.executeAndExtractJsonPathAsObject(graphQueryRequest,
                 "data.booksByRelease[*].released.printedEdition", new TypeRef<List<Boolean>>() {});

        Set<Boolean> uniqueReleasePrintedEdition = new HashSet<>(releasedPrintedEditions);
        assertNotNull(uniqueReleasePrintedEdition);
        assertTrue(uniqueReleasePrintedEdition.size() <= 1);
        if(!uniqueReleasePrintedEdition.isEmpty()) assertTrue(uniqueReleasePrintedEdition.contains(expectedPrintedEdition));

    }
}
