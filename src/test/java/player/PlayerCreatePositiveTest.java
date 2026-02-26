package player;

import api.player.PlayerClient;
import core.BaseTest;
import dataprovider.PlayerDataProviders;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.player.PlayerDto;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import utils.PlayerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static core.HttpStatus.OK;
import static models.player.Editor.ADMIN;
import static models.player.Editor.SUPERVISOR;

@Epic("Player API")
@Feature("Create Player")
public class PlayerCreatePositiveTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();
    private final List<Integer> createdPlayerIds = Collections.synchronizedList(new ArrayList<>());

    @AfterMethod
    public void cleanup() {
        createdPlayerIds.forEach(id -> playerClient.deletePlayer(SUPERVISOR, id));
        createdPlayerIds.clear();
    }

    @Test
    @Story("Supervisor can create a player with valid data and verify via GET")
    public void shouldCreatePlayerAsSupervisor() {
        PlayerDto player = PlayerFactory.validUser();

        Response createResponse = playerClient.createPlayer(SUPERVISOR, player);
        Assert.assertEquals(
                createResponse.getStatusCode(),
                OK.code(),
                "Expected 200 when supervisor creates player but got: " + createResponse.getStatusCode()
                        + " body=" + createResponse.getBody().asString()
        );

        PlayerDto created = createResponse.as(PlayerDto.class);
        Assert.assertNotNull(created.getId(), "Expected created player to have id");
        createdPlayerIds.add(created.getId());

        PlayerDto fetched = playerClient.getPlayer(created.getId()).as(PlayerDto.class);
        assertPlayerFields(fetched, player);
    }

    @Test(enabled = false)
    @Story("Admin can create a player with valid data")
    @Issue("BUG: Admin gets 403 when trying to create a player")
    public void shouldCreatePlayerAsAdmin() {
        PlayerDto player = PlayerFactory.validUser();

        Response createResponse = playerClient.createPlayer(ADMIN, player);
        Assert.assertEquals(
                createResponse.getStatusCode(),
                OK.code(),
                "Expected 200 when admin creates player but got: " + createResponse.getStatusCode()
                        + " body=" + createResponse.getBody().asString()
        );

        PlayerDto created = createResponse.as(PlayerDto.class);
        Assert.assertNotNull(created.getId(), "Expected created player to have id");
        createdPlayerIds.add(created.getId());

        PlayerDto fetched = playerClient.getPlayer(created.getId()).as(PlayerDto.class);
        assertPlayerFields(fetched, player);
    }

    @Test(dataProvider = "validRoles", dataProviderClass = PlayerDataProviders.class)
    @Story("Should create player with allowed roles: USER and ADMIN")
    public void shouldCreatePlayerWithAllowedRoles(PlayerDto player) {
        Response response = playerClient.createPlayer(SUPERVISOR, player);

        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 for role=" + player.getRole()
                        + " but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayerDto created = response.as(PlayerDto.class);
        createdPlayerIds.add(created.getId());

        PlayerDto fetched = playerClient.getPlayer(created.getId()).as(PlayerDto.class);
        Assert.assertEquals(
                fetched.getRole(),
                player.getRole(),
                "Created player role mismatch"
        );
    }

    @Test(enabled = false)
    @Story("Create player with valid data and verify there's no password in response")
    @Issue("BUG: API returns password in response. Should be null/absent.")
    public void shouldNotReturnPasswordInGetResponse() {
        PlayerDto player = PlayerFactory.validUser();
        PlayerDto created = playerClient.createPlayer(SUPERVISOR, player).as(PlayerDto.class);
        createdPlayerIds.add(created.getId());

        PlayerDto fetched = playerClient.getPlayer(created.getId()).as(PlayerDto.class);
        Assert.assertNull(fetched.getPassword(), "Password must not be returned by API");
    }

    @Test(enabled = false)
    @Issue("BUG: Create response doesn't return persisted fields (screenName/gender/age/role).")
    public void createResponseShouldContainPlayerFields() {
        PlayerDto player = PlayerFactory.validUser();
        PlayerDto created = playerClient.createPlayer(SUPERVISOR, player).as(PlayerDto.class);
        createdPlayerIds.add(created.getId());

        assertPlayerFields(created, player);
    }

    @Test(dataProvider = "validGenders", dataProviderClass = PlayerDataProviders.class)
    @Story("Should create player with allowed gender values: male, female")
    public void shouldCreatePlayerWithAllowedGender(String gender) {
        PlayerDto player = PlayerFactory.validUser();
        player.setGender(gender);

        Response response = playerClient.createPlayer(SUPERVISOR, player);
        Assert.assertEquals(
                response.getStatusCode(),
                OK.code(),
                "Expected 200 when creating player with gender=" + gender
                        + " but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );

        PlayerDto created = response.as(PlayerDto.class);
        createdPlayerIds.add(created.getId());

        PlayerDto fetched = playerClient.getPlayer(created.getId()).as(PlayerDto.class);
        Assert.assertEquals(
                fetched.getGender(),
                gender,
                "Expected gender=" + gender + " but got=" + fetched.getGender()
        );
    }

    private void assertPlayerFields(PlayerDto fetched, PlayerDto expected) {
        Assert.assertEquals(fetched.getLogin(), expected.getLogin(), "Login mismatch");
        Assert.assertEquals(fetched.getScreenName(), expected.getScreenName(), "ScreenName mismatch");
        Assert.assertEquals(fetched.getAge(), expected.getAge(), "Age mismatch");
        Assert.assertEquals(fetched.getGender(), expected.getGender(), "Gender mismatch");
        Assert.assertEquals(fetched.getRole(), expected.getRole(), "Role mismatch");
    }
}