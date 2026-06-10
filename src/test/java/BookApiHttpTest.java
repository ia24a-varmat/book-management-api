import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookApiHttpTest {

    private static final String BASE_URL =
            "http://localhost:8080/book_management_api_war_exploded/api";

    private String adminAuth() {
        String login = "admin:1234";
        return "Basic " + Base64.getEncoder()
                .encodeToString(login.getBytes(StandardCharsets.UTF_8));
    }
    private String userAuth() {
        String login = "user:1234";
        return "Basic " + Base64.getEncoder()
                .encodeToString(login.getBytes(StandardCharsets.UTF_8));
    }

    private String wrongAuth() {
        String login = "admin:wrong";
        return "Basic " + Base64.getEncoder()
                .encodeToString(login.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testPingPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/books/ping");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testGetAllBooksPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/books");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testGetBookNotFoundNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/books/999999");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(404, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testCreateCategoryWithAuthPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/categories");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            String json = """
                    {
                      "categoryId": 50,
                      "name": "JUnit Category",
                      "description": "Test category"
                    }
                    """;

            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(201, response.getStatusLine().getStatusCode());
            }

            HttpDelete cleanup = new HttpDelete(BASE_URL + "/categories/50");
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
                      "categoryId": 51,
                      "name": "No Auth",
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
    public void testCreateBookWithAuthPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost categoryPost = new HttpPost(BASE_URL + "/categories");
            categoryPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            categoryPost.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            categoryPost.setEntity(new StringEntity("""
                    {
                      "categoryId": 60,
                      "name": "Book Test Category",
                      "description": "Category for book tests"
                    }
                    """, StandardCharsets.UTF_8));
            client.execute(categoryPost).close();

            HttpPost bookPost = new HttpPost(BASE_URL + "/books");
            bookPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            bookPost.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            String bookJson = """
                    {
                      "bookId": 60,
                      "title": "JUnit Book",
                      "author": "Test Author",
                      "publishDate": "2025-01-01",
                      "price": 19.90,
                      "pages": 200,
                      "available": true,
                      "category": {
                        "categoryId": 60
                      }
                    }
                    """;

            bookPost.setEntity(new StringEntity(bookJson, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(bookPost)) {
                assertEquals(201, response.getStatusLine().getStatusCode());
            }

            HttpDelete deleteBook = new HttpDelete(BASE_URL + "/books/60");
            deleteBook.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            client.execute(deleteBook).close();

            HttpDelete deleteCategory = new HttpDelete(BASE_URL + "/categories/60");
            deleteCategory.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            client.execute(deleteCategory).close();
        }
    }

    @Test
    public void testCreateBookInvalidValidationNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/books");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            String json = """
                    {
                      "bookId": 61,
                      "title": "",
                      "author": "",
                      "publishDate": null,
                      "price": -5,
                      "pages": 0,
                      "available": true,
                      "category": null
                    }
                    """;

            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(400, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testUpdateBookPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost categoryPost = new HttpPost(BASE_URL + "/categories");
            categoryPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            categoryPost.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            categoryPost.setEntity(new StringEntity("""
                    {
                      "categoryId": 70,
                      "name": "Update Category",
                      "description": "For update"
                    }
                    """, StandardCharsets.UTF_8));
            client.execute(categoryPost).close();

            HttpPost bookPost = new HttpPost(BASE_URL + "/books");
            bookPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            bookPost.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            bookPost.setEntity(new StringEntity("""
                    {
                      "bookId": 70,
                      "title": "Before Update",
                      "author": "Author",
                      "publishDate": "2025-01-01",
                      "price": 10.00,
                      "pages": 100,
                      "available": true,
                      "category": {
                        "categoryId": 70
                      }
                    }
                    """, StandardCharsets.UTF_8));
            client.execute(bookPost).close();

            HttpPut put = new HttpPut(BASE_URL + "/books/70");
            put.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            put.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            put.setEntity(new StringEntity("""
                    {
                      "bookId": 70,
                      "title": "After Update",
                      "author": "Author",
                      "publishDate": "2025-01-01",
                      "price": 20.00,
                      "pages": 150,
                      "available": false,
                      "category": {
                        "categoryId": 70
                      }
                    }
                    """, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(put)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }

            HttpDelete deleteBook = new HttpDelete(BASE_URL + "/books/70");
            deleteBook.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            client.execute(deleteBook).close();

            HttpDelete deleteCategory = new HttpDelete(BASE_URL + "/categories/70");
            deleteCategory.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            client.execute(deleteCategory).close();
        }
    }

    @Test
    public void testDeleteBookPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost categoryPost = new HttpPost(BASE_URL + "/categories");
            categoryPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            categoryPost.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            categoryPost.setEntity(new StringEntity("""
                    {
                      "categoryId": 80,
                      "name": "Delete Category",
                      "description": "For delete"
                    }
                    """, StandardCharsets.UTF_8));
            client.execute(categoryPost).close();

            HttpPost bookPost = new HttpPost(BASE_URL + "/books");
            bookPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            bookPost.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            bookPost.setEntity(new StringEntity("""
                    {
                      "bookId": 80,
                      "title": "Delete Book",
                      "author": "Author",
                      "publishDate": "2025-01-01",
                      "price": 10.00,
                      "pages": 100,
                      "available": true,
                      "category": {
                        "categoryId": 80
                      }
                    }
                    """, StandardCharsets.UTF_8));
            client.execute(bookPost).close();

            HttpDelete deleteBook = new HttpDelete(BASE_URL + "/books/80");
            deleteBook.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            try (CloseableHttpResponse response = client.execute(deleteBook)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }

            HttpDelete deleteCategory = new HttpDelete(BASE_URL + "/categories/80");
            deleteCategory.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            client.execute(deleteCategory).close();
        }
    }

    @Test
    public void testDeleteWithoutAuthNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(BASE_URL + "/books/1");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(401, response.getStatusLine().getStatusCode());
            }
        }
    }
    @Test
    public void testCountBooksPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/books/count");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testGetBooksByAvailablePositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/books/available/true");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testGetBooksByPricePositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + "/books/price/20.00");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testCreateMultipleBooksPositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/books/bulk");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            String json = """
                    [
                      {
                        "bookId": 901,
                        "title": "Bulk JUnit Book 1",
                        "author": "Author A",
                        "publishDate": "2026-01-01",
                        "price": 15.90,
                        "pages": 120,
                        "available": true,
                        "category": {
                          "categoryId": 1
                        }
                      },
                      {
                        "bookId": 902,
                        "title": "Bulk JUnit Book 2",
                        "author": "Author B",
                        "publishDate": "2026-01-01",
                        "price": 25.90,
                        "pages": 180,
                        "available": false,
                        "category": {
                          "categoryId": 1
                        }
                      }
                    ]
                    """;

            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(201, response.getStatusLine().getStatusCode());
            }

            HttpDelete cleanup = new HttpDelete(BASE_URL + "/books/date/2026-01-01");
            cleanup.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());
            client.execute(cleanup).close();
        }
    }

    @Test
    public void testCreateMultipleBooksWithoutAuthNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/books/bulk");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

            String json = """
                    []
                    """;

            request.setEntity(new StringEntity(json, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(401, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testDeleteBooksByDatePositive() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost create = new HttpPost(BASE_URL + "/books/bulk");
            create.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            create.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            create.setEntity(new StringEntity("""
                    [
                      {
                        "bookId": 910,
                        "title": "Delete Date Book 1",
                        "author": "Author A",
                        "publishDate": "2026-02-01",
                        "price": 15.90,
                        "pages": 120,
                        "available": true,
                        "category": {
                          "categoryId": 1
                        }
                      },
                      {
                        "bookId": 911,
                        "title": "Delete Date Book 2",
                        "author": "Author B",
                        "publishDate": "2026-02-01",
                        "price": 25.90,
                        "pages": 180,
                        "available": false,
                        "category": {
                          "categoryId": 1
                        }
                      }
                    ]
                    """, StandardCharsets.UTF_8));

            client.execute(create).close();

            HttpDelete delete = new HttpDelete(BASE_URL + "/books/date/2026-02-01");
            delete.setHeader(HttpHeaders.AUTHORIZATION, adminAuth());

            try (CloseableHttpResponse response = client.execute(delete)) {
                assertEquals(200, response.getStatusLine().getStatusCode());
            }
        }
    }

    @Test
    public void testDeleteBooksByDateWithoutAuthNegative() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(BASE_URL + "/books/date/2026-03-01");

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(401, response.getStatusLine().getStatusCode());
            }
        }
    }
    @Test
    public void testWrongPasswordIsRejected() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/books");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setHeader(HttpHeaders.AUTHORIZATION, wrongAuth());

            request.setEntity(new StringEntity("""
                {
                  "bookId": 990,
                  "title": "Wrong Auth",
                  "author": "Test",
                  "publishDate": "2026-01-01",
                  "price": 10.00,
                  "pages": 100,
                  "available": true,
                  "category": {
                    "categoryId": 1
                  }
                }
                """, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(401, response.getStatusLine().getStatusCode());
            }
        }
    }
    @Test
    public void testUserRoleCannotCreateBook() throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + "/books");
            request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            request.setHeader(HttpHeaders.AUTHORIZATION, userAuth());

            request.setEntity(new StringEntity("""
                {
                  "bookId": 991,
                  "title": "User Role Test",
                  "author": "Test",
                  "publishDate": "2026-01-01",
                  "price": 10.00,
                  "pages": 100,
                  "available": true,
                  "category": {
                    "categoryId": 1
                  }
                }
                """, StandardCharsets.UTF_8));

            try (CloseableHttpResponse response = client.execute(request)) {
                assertEquals(401, response.getStatusLine().getStatusCode());
            }
        }
    }
}