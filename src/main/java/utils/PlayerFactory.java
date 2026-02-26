package utils;

import models.player.PlayerDto;
import models.player.Role;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

public class PlayerFactory {

    private static final String MALE = "MALE";

    public static PlayerDto validUser() {
        return new PlayerDto.Builder()
                .login("user_" + UUID.randomUUID())
                .password(PasswordGenerator.validPassword())
                .role(Role.USER)
                .screenName("screen_" + UUID.randomUUID())
                .age(25)
                .gender(MALE)
                .build();
    }

    public static PlayerDto validAdmin() {
        return new PlayerDto.Builder()
                .login("admin_" + UUID.randomUUID())
                .password(PasswordGenerator.validPassword())
                .role(Role.ADMIN)
                .screenName("screen_" + UUID.randomUUID())
                .age(30)
                .gender(MALE)
                .build();
    }

    private static String randomPassword() {
        byte[] bytes = new byte[8];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}