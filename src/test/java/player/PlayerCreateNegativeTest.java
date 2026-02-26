package player;

import api.player.PlayerClient;
import core.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import models.player.PlayerDto;
import models.player.Role;
import net.datafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.PasswordGenerator;
import utils.PlayerFactory;

import static core.HttpStatus.*;
import static models.player.Editor.SUPERVISOR;
import static models.player.Editor.USER;

@Epic("Player API")
@Feature("Create Player - Negative")
public class PlayerCreateNegativeTest extends BaseTest {

    private final PlayerClient playerClient = new PlayerClient();

    private static final int MIN_VALID_AGE = 17;
    private static final int MAX_VALID_AGE = 59;
    private static final Faker faker = new Faker();

    @Test
    @Story("Should fail when age is below minimum threshold")
    public void shouldFailWhenAgeTooLow() {
        PlayerDto player = PlayerFactory.validUser();
        player.setAge(MIN_VALID_AGE - 1);

        Response response = playerClient.createPlayer(SUPERVISOR, player);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 when age < " + MIN_VALID_AGE + " but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false)
    @Story("Should fail when age is above maximum threshold")
    @Issue("BUG: Player with age above maximum threshold can be registered")
    public void shouldFailWhenAgeTooHigh() {
        PlayerDto player = PlayerFactory.validUser();
        player.setAge(MAX_VALID_AGE + 1);

        Response response = playerClient.createPlayer(SUPERVISOR, player);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 when age > " + MAX_VALID_AGE + " but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false)
    @Story("Should fail when password is shorter than minimum length")
    @Issue("BUG: Player can be registered with password shorter than min allowed length (7)")
    public void shouldFailWhenPasswordTooShort() {
        PlayerDto player = PlayerFactory.validUser();
        player.setPassword(PasswordGenerator.tooShort());

        Response response = playerClient.createPlayer(SUPERVISOR, player);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 for too short password but got: " + response.getStatusCode()
                        + " password=" + player.getPassword()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false)
    @Story("Should fail when password is longer than maximum length")
    @Issue("BUG: Player can be registered with password longer than max allowed length (15)")
    public void shouldFailWhenPasswordTooLong() {
        PlayerDto player = PlayerFactory.validUser();
        player.setPassword(PasswordGenerator.tooLong());

        Response response = playerClient.createPlayer(SUPERVISOR, player);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 for too long password but got: " + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false, description = "BUG: Duplicate login returns 200 and existing player instead of 400")
    @Story("Should fail when login is not unique")
    @Issue("BUG: Duplicate login is not rejected; API returns existing player")
    public void shouldFailWhenLoginNotUnique() {
        PlayerDto player1 = PlayerFactory.validUser();

        Response r1 = playerClient.createPlayer(SUPERVISOR, player1);
        Assert.assertEquals(
                r1.getStatusCode(),
                OK.code(),
                "Precondition failed: could not create first player. status=" + r1.getStatusCode()
                        + " body=" + r1.getBody().asString()
        );

        PlayerDto created1 = r1.as(PlayerDto.class);
        Assert.assertNotNull(created1.getId(), "Expected created player to have id");

        PlayerDto player2 = PlayerFactory.validUser();
        player2.setLogin(player1.getLogin());

        Response r2 = playerClient.createPlayer(SUPERVISOR, player2);

        Assert.assertEquals(
                r2.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 for duplicate login but got: " + r2.getStatusCode()
                        + " body=" + r2.getBody().asString()
        );
    }

    @Test(enabled = false, description = "BUG: Duplicate screenName is allowed (should be 400)")
    @Story("Should fail when screenName is not unique")
    @Issue("BUG: Duplicate screenName is not rejected")
    public void shouldFailWhenScreenNameNotUnique() {
        PlayerDto player1 = PlayerFactory.validUser();

        Response firstResponse = playerClient.createPlayer(SUPERVISOR, player1);
        Assert.assertEquals(
                firstResponse.getStatusCode(),
                OK.code(),
                "Precondition failed: could not create first player. status=" + firstResponse.getStatusCode()
                        + " body=" + firstResponse.getBody().asString()
        );

        PlayerDto created1 = firstResponse.as(PlayerDto.class);
        Assert.assertNotNull(created1.getId(), "Expected created player to have id");

        PlayerDto player2 = PlayerFactory.validUser();
        player2.setScreenName(player1.getScreenName());

        Response responseDuplicateScreenName = playerClient.createPlayer(SUPERVISOR, player2);

        Assert.assertEquals(
                responseDuplicateScreenName.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 for duplicate screenName but got: " + responseDuplicateScreenName.getStatusCode()
                        + " body=" + responseDuplicateScreenName.getBody().asString()
        );
    }

    @Test
    @Story("Only supervisor or admin can create players")
    public void shouldFailWhenEditorIsRegularUser() {
        PlayerDto player = PlayerFactory.validUser();

        Response response = playerClient.createPlayer(USER, player);

        Assert.assertEquals(response.getStatusCode(), FORBIDDEN.code(), "Expected 403 when USER tries to create player, but got: "
                + response.getStatusCode() + " body=" + response.getBody().asString());
    }

    @Test
    @Story("Should fail when role is not in allowed list (admin, user)")
    public void shouldFailWhenRoleIsNotAllowed() {
        PlayerDto player = PlayerFactory.validUser();
        player.setRole(Role.SUPERVISOR);

        Response response = playerClient.createPlayer(SUPERVISOR, player);

        Assert.assertEquals(
                response.getStatusCode(), BAD_REQUEST.code(),
                "Expected 400 when creating player with unsupported role, but got: "
                        + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }

    @Test(enabled = false, description = "BUG: API accepts invalid gender values")
    @Story("Should fail when gender is not male or female")
    @Issue("BUG: Gender validation is missing or incorrect")
    public void shouldFailWhenGenderIsInvalid() {
        PlayerDto player = PlayerFactory.validUser();
        String invalidGender = faker.lorem().characters(8, 15);
        player.setGender(invalidGender);

        Response response = playerClient.createPlayer(SUPERVISOR, player);

        Assert.assertEquals(
                response.getStatusCode(),
                BAD_REQUEST.code(),
                "Expected 400 for invalid gender='" + invalidGender + "' but got: "
                        + response.getStatusCode()
                        + " body=" + response.getBody().asString()
        );
    }
}