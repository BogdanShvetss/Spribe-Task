package utils;

import net.datafaker.Faker;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final Faker faker = new Faker();
    private static final SecureRandom rnd = new SecureRandom();

    public static String validPassword() {
        int length = 7 + rnd.nextInt(9);
        return faker.lorem().characters(length, true, true);
    }

    public static String tooShort() {
        return faker.lorem().characters(3, true, true);
    }

    public static String tooLong() {
        return faker.lorem().characters(20, true, true);
    }

    public static String withInvalidChars() {
        return "漢字😊!!!";
    }
}