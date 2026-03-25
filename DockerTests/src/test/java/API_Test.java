import Utils.DbUtils;
import com.github.javafaker.Faker;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import Utils.UserDto;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Utils.ConfigProvider;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class API_Test {

    @BeforeMethod(enabled = false)
    public void cleanUp() {
        DbUtils.executeSqlFile("clean_db.sql");
    }

    @Test(description = "Тест API")
    public void API_Test() {
        RestAssured.baseURI = ConfigProvider.getHostUrl() + "/api";
        RestAssured.filters(new AllureRestAssured());
        Faker faker = new Faker();
        String username = faker.name().username();
        String password = faker.internet().password();

        UserDto user = UserDto.builder()
                .username(username)
                .password(password)
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post("/register")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", equalTo(username))
                .body("password", equalTo(password))
                .body("id", notNullValue());
    }
}
