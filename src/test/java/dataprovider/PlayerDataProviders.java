package dataprovider;

import org.testng.annotations.DataProvider;
import utils.PlayerFactory;

import static models.player.Editor.*;

public class PlayerDataProviders {

    @DataProvider(name = "validRoles")
    public static Object[][] validRoles() {
        return new Object[][]{
                {PlayerFactory.validUser()},
                {PlayerFactory.validAdmin()}
        };
    }

    @DataProvider(name = "validEditors")
    public static Object[][] validEditors() {
        return new Object[][]{
                {SUPERVISOR},
                {ADMIN}
        };
    }

    @DataProvider(name = "validGenders")
    public static Object[][] validGenders() {
        return new Object[][]{
                { "male" },
                { "female" }
        };
    }
}