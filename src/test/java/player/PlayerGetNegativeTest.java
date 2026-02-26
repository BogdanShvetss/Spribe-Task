package player;

import api.player.PlayerClient;
import core.BaseTest;
import core.HttpStatus;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static core.HttpStatus.BAD_REQUEST;

@Epic("Player API")
@Feature("Get Player - Negative")
public class PlayerGetNegativeTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();

    @Test(enabled = false)
    @Story("Should fail when playerId does not exist")
    @Issue("BUG: API returns 200 when player with a passed id doesn't exist")
    public void shouldFailWhenPlayerDoesNotExist() {
        int nonExistingId = 999999999;

        Response response = playerClient.getPlayer(nonExistingId);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND.code(),
                "Expected 404 for non-existing playerId, but got: "
                        + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false)
    @Story("Should fail when playerId is negative")
    @Issue("BUG: API returns 200 when playerId is negative")
    public void shouldFailWhenPlayerIdIsNegative() {
        Response response = playerClient.getPlayer(-1);

        Assert.assertEquals(response.getStatusCode(), BAD_REQUEST.code(),
                "Expected 400 for negative playerId, but got: "
                        + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false, description = "BUG: API returns 200 for playerId=0")
    @Story("Should fail when playerId is zero")
    @Issue("BUG: API accepts playerId=0 and returns 200")
    public void shouldFailWhenPlayerIdIsZero() {
        Response response = playerClient.getPlayer(0);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 for playerId=0, but got: "
                        + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }
}