package player;

import api.player.PlayerClient;
import core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.player.PlayerDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.PlayerFactory;

import static core.HttpStatus.OK;
import static models.player.Editor.SUPERVISOR;

@Epic("Player API")
@Feature("Get Player - Positive")
public class PlayerGetPositiveTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();

    @Test
    @Story("Should get existing player by id")
    public void shouldGetPlayerById() {
        PlayerDto player = PlayerFactory.validUser();

        Response createResponse = playerClient.createPlayer(SUPERVISOR, player);
        Assert.assertEquals(
                createResponse.getStatusCode(),
                OK.code(),
                "Precondition failed: could not create player. status="
                        + createResponse.getStatusCode()
                        + " body=" + createResponse.getBody().asString()
        );

        PlayerDto created = createResponse.as(PlayerDto.class);
        Assert.assertNotNull(created.getId(), "Created player id must not be null");

        try {
            Response getResponse = playerClient.getPlayer(created.getId());
            Assert.assertEquals(
                    getResponse.getStatusCode(),
                    OK.code(),
                    "Expected 200 on getPlayer, got: "
                            + getResponse.getStatusCode()
                            + " body=" + getResponse.getBody().asString()
            );

            PlayerDto fetched = getResponse.as(PlayerDto.class);
            Assert.assertEquals(fetched.getLogin(), player.getLogin(), "Login mismatch");
            Assert.assertEquals(fetched.getScreenName(), player.getScreenName(), "ScreenName mismatch");
            Assert.assertEquals(fetched.getAge(), player.getAge(), "Age mismatch");
            Assert.assertEquals(fetched.getGender(), player.getGender(), "Gender mismatch");
            Assert.assertEquals(fetched.getRole(), player.getRole(), "Role mismatch");
        } finally {
            playerClient.deletePlayer(SUPERVISOR, created.getId());
        }
    }
}