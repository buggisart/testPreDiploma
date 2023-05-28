import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Mesto1Test {

    String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NDFiOGFhNzI0OGQ0NTAwMzYyN2Y4MzMiLCJpYXQiOjE2ODUzMDkxMTcsImV4cCI6MTY4NTkxMzkxN30.dZCOG4QzFGJvk6vjIV4PC_EcAhtvj5F7NelG5w9hENs";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check user name")
    @Description("This test is for check current user's name.")
    public void checkUserName() {
        given()
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .get("/api/users/me") // Делаем GET-запрос
                .then().assertThat().body("data.name", equalTo("Incorrect Name")); // Проверяем, что имя соответствует ожидаемому
    }

    @Test
    @DisplayName("Like the first photo")
    @Description("This test is for liking the first photo on Mesto.")
    public void likeTheFirstPhoto() {
        String photoId = getTheFirstPhotoId();

        likePhotoById(photoId);
        deleteLikePhotoById(photoId);
    }

    @Step("Take the first photo from the list")
    private String getTheFirstPhotoId() {
        // Получение списка фотографий и выбор первой из него
        return given()
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .get("/api/cards") // Делаем GET-запрос
                .then().extract().body().path("data[0]._id"); // Получаем ID фотографии из массива данных
    }

    @Step("Like a photo by id")
    private void likePhotoById(String photoId) {
        // Лайк фотографии по photoId
        given()
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .put("/api/cards/{photoId}/likes", photoId) // Делаем PUT-запрос
                .then().assertThat().statusCode(200); // Проверяем, что сервер вернул код 200
    }

    @Step("Delete like from the photo by id")
    private void deleteLikePhotoById(String photoId) {
        // Снять лайк с фотографии по photoId
        given()
                .auth().oauth2(bearerToken) // Передаём токен для аутентификации
                .delete("/api/cards/{photoId}/likes", photoId) // Делаем DELETE-запрос
                .then().assertThat().statusCode(200); // Проверяем, что сервер вернул код 200
    }

}