package player;

import api.player.PlayerClient;
import core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.player.PlayerDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.PlayerFactory;

import static core.HttpStatus.*;
import static models.player.Editor.ADMIN;
import static models.player.Editor.SUPERVISOR;

@Epic("Player API")
@Feature("Delete Player")
public class PlayerDeletePositiveTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();

    @Test
    @Story("Supervisor can delete a user role player")
    public void shouldDeleteUserRolePlayerAsSupervisor() {
        PlayerDto player = PlayerFactory.validUser();
        Response createResponse = playerClient.createPlayer(SUPERVISOR, player);
        Assert.assertEquals(
                createResponse.getStatusCode(),
                OK.code(),
                "Precondition failed: could not create player. status=" + createResponse.getStatusCode()
                        + " body=" + createResponse.getBody().asString()
        );
        int playerId = createResponse.as(PlayerDto.class).getId();

        Response response = playerClient.deletePlayer(SUPERVISOR, playerId);

        Assert.assertEquals(
                response.getStatusCode(),
                NO_CONTENT.code(),
                "Expected 204 when supervisor deletes user role player but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test
    @Story("Supervisor can delete an admin role player")
    public void shouldDeleteAdminRolePlayerAsSupervisor() {
        PlayerDto player = PlayerFactory.validAdmin();
        int playerId = playerClient.createPlayer(SUPERVISOR, player).as(PlayerDto.class).getId();

        Response response = playerClient.deletePlayer(SUPERVISOR, playerId);

        Assert.assertEquals(
                response.getStatusCode(),
                NO_CONTENT.code(),
                "Expected 204 when supervisor deletes admin role player but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test
    @Story("Admin can delete a user role player")
    public void shouldDeleteUserRolePlayerAsAdmin() {
        PlayerDto player = PlayerFactory.validUser();
        int playerId = playerClient.createPlayer(SUPERVISOR, player).as(PlayerDto.class).getId();

        Response response = playerClient.deletePlayer(ADMIN, playerId);

        Assert.assertEquals(
                response.getStatusCode(),
                NO_CONTENT.code(),
                "Expected 204 when admin deletes user role player but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test
    @Story("Admin can delete another admin role player")
    public void shouldDeleteAdminRolePlayerAsAdmin() {
        PlayerDto admin = PlayerFactory.validAdmin();
        int playerId = playerClient.createPlayer(SUPERVISOR, admin).as(PlayerDto.class).getId();

        Response response = playerClient.deletePlayer(ADMIN, playerId);

        Assert.assertEquals(
                response.getStatusCode(),
                NO_CONTENT.code(),
                "Expected 204 when admin deletes admin role player but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false)
    @Story("Deleted player should not be retrievable")
    @Issue("BUG: GET player returns 200 for non-existent/deleted player id, should return 404")
    public void shouldReturnNotFoundForDeletedPlayer() {
        PlayerDto player = PlayerFactory.validUser();
        int playerId = playerClient.createPlayer(SUPERVISOR, player).as(PlayerDto.class).getId();

        playerClient.deletePlayer(SUPERVISOR, playerId);

        Response getResponse = playerClient.getPlayer(playerId);
        Assert.assertEquals(
                getResponse.getStatusCode(),
                NOT_FOUND.code(),
                "Expected 404 for deleted player but got: " + getResponse.getStatusCode()
                        + " body=" + getResponse.getBody().asString()
        );
    }
}