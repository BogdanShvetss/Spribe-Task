package player;

import api.player.PlayerClient;
import core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.player.PlayerDto;
import models.player.PlayerShortDto;
import models.player.PlayersResponseDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.PlayerFactory;

import java.util.NoSuchElementException;

import static core.HttpStatus.OK;
import static models.player.Editor.SUPERVISOR;

@Epic("Player API")
@Feature("Get All Players - Positive")
public class PlayerGetAllPositiveTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();

    @Test
    @Story("Should return 200 with non-empty players list")
    public void shouldReturnPlayersListSuccessfully() {
        Response response = playerClient.getAllPlayers();

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 for getAllPlayers but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayersResponseDto all = response.as(PlayersResponseDto.class);
        Assert.assertNotNull(all.getPlayers(), "Players list must not be null");
        Assert.assertFalse(all.getPlayers().isEmpty(), "Players list must not be empty");
    }

    @Test
    @Story("Should contain created player in /player/get/all response")
    public void shouldContainCreatedPlayerInGetAll() {
        PlayerDto player = PlayerFactory.validUser();

        Response createResponse = playerClient.createPlayer(SUPERVISOR, player);
        Assert.assertEquals(
                createResponse.getStatusCode(),
                OK.code(),
                "Precondition failed: could not create player. status=" + createResponse.getStatusCode()
                        + " body=" + createResponse.getBody().asString()
        );

        PlayerDto created = createResponse.as(PlayerDto.class);
        Assert.assertNotNull(created.getId(), "Created player id must not be null");

        try {
            Response allResponse = playerClient.getAllPlayers();
            Assert.assertEquals(
                    allResponse.getStatusCode(),
                    OK.code(),
                    "Expected 200 for getAllPlayers but got: " + allResponse.getStatusCode()
                            + " body=" + allResponse.getBody().asString()
            );

            PlayersResponseDto all = allResponse.as(PlayersResponseDto.class);
            Assert.assertNotNull(all.getPlayers(), "Players list must not be null");
            Assert.assertFalse(all.getPlayers().isEmpty(), "Players list must not be empty");

            PlayerShortDto found = all.getPlayers().stream()
                    .filter(p -> created.getId().equals(p.getId()))
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Failed to find a player with id - " + created.getId()));

            Assert.assertEquals(found.getScreenName(), player.getScreenName(), "ScreenName mismatch");
            Assert.assertEquals(found.getAge(), player.getAge(), "Age mismatch");
            Assert.assertEquals(found.getGender().toLowerCase(), player.getGender().toLowerCase(), "Gender mismatch");
        } finally {
            playerClient.deletePlayer(SUPERVISOR, created.getId());
        }
    }
}