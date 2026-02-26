package player;

import api.player.PlayerClient;
import core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.player.PlayerDto;
import models.player.UpdatePlayerRequestDto;
import net.datafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.PlayerFactory;

import static core.HttpStatus.BAD_REQUEST;
import static core.HttpStatus.FORBIDDEN;
import static models.player.Editor.SUPERVISOR;
import static models.player.Editor.USER;

@Epic("Player API")
@Feature("Update Player - Negative")
public class PlayerUpdateNegativeTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();
    private static final Faker faker = new Faker();

    private static final int MIN_VALID_AGE = 17;
    private static final int MAX_VALID_AGE = 59;

    private final ThreadLocal<Integer> createdPlayerId = new ThreadLocal<>();

    @BeforeMethod
    public void createPlayer() {
        PlayerDto player = PlayerFactory.validUser();
        createdPlayerId.set(playerClient.createPlayer(SUPERVISOR, player).as(PlayerDto.class).getId());
    }

    @AfterMethod
    public void deletePlayer() {
        playerClient.deletePlayer(SUPERVISOR, createdPlayerId.get());
        createdPlayerId.remove();
    }

    @Test
    @Story("Should fail when USER tries to update a player")
    public void shouldFailWhenEditorIsRegularUser() {
        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .age(25)
                .build();

        Response response = playerClient.updatePlayer(USER, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                FORBIDDEN.code(),
                "Expected 403 when USER tries to update player but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false)
    @Story("Should fail when updated age is below minimum threshold")
    @Issue("BUG: Player age can be updated to a value below minimum allowed age (17)")
    public void shouldFailWhenUpdatedAgeTooLow() {
        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .age(MIN_VALID_AGE - 1)
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 when age < " + MIN_VALID_AGE + " but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false)
    @Story("Should fail when updated age is above maximum threshold")
    @Issue("BUG: Player age can be updated to a value above maximum allowed age (59)")
    public void shouldFailWhenUpdatedAgeTooHigh() {
        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .age(MAX_VALID_AGE + 1)
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 when age > " + MAX_VALID_AGE + " but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false)
    @Story("Should fail when updated gender is invalid")
    @Issue("BUG: Gender validation is missing or incorrect on update")
    public void shouldFailWhenUpdatedGenderIsInvalid() {
        String invalidGender = faker.lorem().characters(8, 15);

        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .gender(invalidGender)
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 for invalid gender='" + invalidGender + "' but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }
}