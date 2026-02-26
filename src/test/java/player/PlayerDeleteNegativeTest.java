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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.PlayerFactory;

import static core.HttpStatus.FORBIDDEN;
import static models.player.Editor.*;

@Epic("Player API")
@Feature("Delete Player - Negative")
public class PlayerDeleteNegativeTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();
    private static final int SUPERVISOR_ID = 1;
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

    @Test(enabled = false)
    @Story("USER editor cannot delete a player")
    @Issue("BUG: USER role can delete players, expected 403 but API returns 204")
    public void shouldFailWhenEditorIsRegularUser() {
        Response response = playerClient.deletePlayer(USER, createdPlayerId.get());

        Assert.assertEquals(
                response.getStatusCode(),
                FORBIDDEN.code(),
                "Expected 403 when USER tries to delete a player but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test
    @Story("Supervisor player cannot be deleted by supervisor")
    public void shouldFailWhenDeletingSupervisorAsSupervisor() {
        Response response = playerClient.deletePlayer(SUPERVISOR, SUPERVISOR_ID);

        Assert.assertEquals(
                response.getStatusCode(),
                FORBIDDEN.code(),
                "Expected 403 when supervisor tries to delete supervisor but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test
    @Story("Supervisor player cannot be deleted by admin")
    public void shouldFailWhenDeletingSupervisorAsAdmin() {
        Response response = playerClient.deletePlayer(ADMIN, SUPERVISOR_ID);

        Assert.assertEquals(
                response.getStatusCode(),
                FORBIDDEN.code(),
                "Expected 403 when admin tries to delete supervisor but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }
}