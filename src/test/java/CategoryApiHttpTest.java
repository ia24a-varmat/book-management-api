import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryApiHttpTest {

    private static final String BASE_URL =
            "http://localhost:8080/book_management_api_war_exploded/api";

    private String adminAuth() {
        String login = "admin:1234";
        return "Basic " + Base64.getEncoder()
                .encodeToString(login.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testGetAllCategoriesPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(BASE_URL + "/categories");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testGetCategoryByIdPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(BASE_URL + "/categories/1");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testGetCategoryNotFoundNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(BASE_URL + "/categories/999999");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(404, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testCreateCategoryPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost request = new HttpPost(BASE_URL + "/categories");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            String json = """
                    {
                      "categoryId": 500,
                      "name": "JUnit Category",
                      "description": "Created by test"
                    }
                    """;

            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(201, response.getStatusLine().getStatusCode());
            }

            HttpDelete cleanup = new HttpDelete(BASE_URL + "/categories/500");
            cleanup.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            client.execute(cleanup).close();
        }
    }

    @Test
    public void testCreateCategoryWithoutAuthNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost request = new HttpPost(BASE_URL + "/categories");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            String json = """
                    {
                      "categoryId": 501,
                      "name": "Unauthorized",
                      "description": "Should fail"
                    }
                    """;

            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(401, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testUpdateCategoryPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost create = new HttpPost(BASE_URL + "/categories");
            create.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            create.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            create.setEntity(new StringEntity("""
                    {
                      "categoryId": 600,
                      "name": "Before Update",
                      "description": "Old description"
                    }
                    """, StandardCharsets.UTF_8));

            client.execute(create).close();

            HttpPut update = new HttpPut(BASE_URL + "/categories/600");
            update.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            update.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            update.setEntity(new StringEntity("""
                    {
                      "categoryId": 600,
                      "name": "After Update",
                      "description": "New description"
                    }
                    """, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(update)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }

            HttpDelete cleanup = new HttpDelete(BASE_URL + "/categories/600");
            cleanup.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            client.execute(cleanup).close();
        }
    }

    @Test
    public void testUpdateCategoryNotFoundNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPut update = new HttpPut(BASE_URL + "/categories/999999");
            update.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            update.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            update.setEntity(new StringEntity("""
                    {
                      "categoryId": 999999,
                      "name": "Not Found",
                      "description": "Test"
                    }
                    """, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(update)) {
                assertEquals(404, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testDeleteCategoryPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost create = new HttpPost(BASE_URL + "/categories");
            create.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            create.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            create.setEntity(new StringEntity("""
                    {
                      "categoryId": 700,
                      "name": "Delete Test",
                      "description": "To be deleted"
                    }
                    """, StandardCharsets.UTF_8));

            client.execute(create).close();

            HttpDelete delete = new HttpDelete(BASE_URL + "/categories/700");
            delete.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            try (CloseableHttpResponse response = client.execute(delete)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testDeleteCategoryNotFoundNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpDelete delete = new HttpDelete(BASE_URL + "/categories/999999");
            delete.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            try (CloseableHttpResponse response = client.execute(delete)) {
                assertEquals(404, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testDeleteCategoryWithoutAuthNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpDelete delete = new HttpDelete(BASE_URL + "/categories/1");

            try (CloseableHttpResponse response = client.execute(delete)) {
                assertEquals(401, response.getStatusLine().getStatusCode());
            }
        }
    }
}