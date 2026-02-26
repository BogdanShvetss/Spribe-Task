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
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.PasswordGenerator;
import utils.PlayerFactory;

import java.util.UUID;

import static core.HttpStatus.OK;
import static models.player.Editor.ADMIN;
import static models.player.Editor.SUPERVISOR;

@Epic("Player API")
@Feature("Update Player")
public class PlayerUpdatePositiveTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();
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
    @Story("Supervisor can update player login")
    public void shouldUpdateLoginAsSupervisor() {
        String newLogin = "upd_" + UUID.randomUUID();

        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .login(newLogin)
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 when updating login but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayerDto fetched = playerClient.getPlayer(createdPlayerId.get()).as(PlayerDto.class);
        Assert.assertEquals(fetched.getLogin(), newLogin, "Expected login to be updated");
    }

    @Test
    @Story("Supervisor can update player screenName")
    public void shouldUpdateScreenNameAsSupervisor() {
        String newScreenName = "screen_" + UUID.randomUUID();

        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .screenName(newScreenName)
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 when updating screenName but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayerDto fetched = playerClient.getPlayer(createdPlayerId.get()).as(PlayerDto.class);
        Assert.assertEquals(fetched.getScreenName(), newScreenName, "Expected screenName to be updated");
    }

    @Test
    @Story("Supervisor can update player age")
    public void shouldUpdateAgeAsSupervisor() {
        int newAge = 30;

        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .age(newAge)
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 when updating age but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayerDto fetched = playerClient.getPlayer(createdPlayerId.get()).as(PlayerDto.class);
        Assert.assertEquals(fetched.getAge(), newAge, "Expected age to be updated");
    }

    @Test
    @Story("Supervisor can update player gender")
    public void shouldUpdateGenderAsSupervisor() {
        String newGender = "female";

        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .gender(newGender)
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 when updating gender but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayerDto fetched = playerClient.getPlayer(createdPlayerId.get()).as(PlayerDto.class);
        Assert.assertEquals(fetched.getGender(), newGender, "Expected gender to be updated");
    }

    @Test
    @Story("Supervisor can update player password")
    public void shouldUpdatePasswordAsSupervisor() {
        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .password(PasswordGenerator.validPassword())
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 when updating password but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test
    @Story("Supervisor can update all allowed fields at once")
    public void shouldUpdateAllFieldsAsSupervisor() {
        String newLogin = "upd_" + UUID.randomUUID();
        String newScreenName = "screen_" + UUID.randomUUID();
        int newAge = 35;
        String newGender = "female";

        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .login(newLogin)
                .screenName(newScreenName)
                .age(newAge)
                .gender(newGender)
                .password(PasswordGenerator.validPassword())
                .build();

        Response response = playerClient.updatePlayer(SUPERVISOR, createdPlayerId.get(), update);

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 when updating all fields but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayerDto fetched = playerClient.getPlayer(createdPlayerId.get()).as(PlayerDto.class);
        Assert.assertEquals(fetched.getLogin(), newLogin, "Login mismatch after update");
        Assert.assertEquals(fetched.getScreenName(), newScreenName, "ScreenName mismatch after update");
        Assert.assertEquals(fetched.getAge(), newAge, "Age mismatch after update");
        Assert.assertEquals(fetched.getGender(), newGender, "Gender mismatch after update");
    }

    @Test(enabled = false)
    @Story("Admin can update a player")
    @Issue("BUG: Admin gets 403 when trying to update a player")
    public void shouldUpdatePlayerAsAdmin() {
        PlayerDto player = PlayerFactory.validUser();
        int playerId = playerClient.createPlayer(SUPERVISOR, player).as(PlayerDto.class).getId();

        String newLogin = "upd_" + UUID.randomUUID();

        UpdatePlayerRequestDto update = new UpdatePlayerRequestDto.Builder()
                .login(newLogin)
                .build();

        Response response = playerClient.updatePlayer(ADMIN, playerId, update);

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 for admin but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayerDto fetched = playerClient.getPlayer(playerId).as(PlayerDto.class);
        Assert.assertEquals(fetched.getLogin(), newLogin, "Expected login to be updated by admin");
    }
}