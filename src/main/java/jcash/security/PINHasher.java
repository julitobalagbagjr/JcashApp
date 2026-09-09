package jcash.security;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PINHasher {

    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    public static String hashPIN(String pin) {

        try {
            // Generate a random salt
            byte[] salt = new byte[SALT_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            // Create the PIN hash
            PBEKeySpec spec = new PBEKeySpec(
                    pin.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            byte[] hash =
                    factory.generateSecret(spec).getEncoded();

            // Store the salt and hash together
            String saltString =
                    Base64.getEncoder()
                            .encodeToString(salt);

            String hashString =
                    Base64.getEncoder()
                            .encodeToString(hash);

            return saltString + ":" + hashString;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error hashing PIN",
                    e
            );
        }
    }

    public static boolean verifyPIN(
            String pin,
            String storedHash
    ) {

        try {
            String[] parts = storedHash.split(":");

            if (parts.length != 2) {
                return false;
            }

            byte[] salt =
                    Base64.getDecoder()
                            .decode(parts[0]);

            byte[] storedHashBytes =
                    Base64.getDecoder()
                            .decode(parts[1]);

            // Hash the entered PIN using the stored salt
            PBEKeySpec spec = new PBEKeySpec(
                    pin.toCharArray(),
                    salt,
                    ITERATIONS,
                    KEY_LENGTH
            );

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256"
                    );

            byte[] enteredHash =
                    factory.generateSecret(spec).getEncoded();

            // Compare the stored hash with the entered PIN hash
            return MessageDigest.isEqual(
                    storedHashBytes,
                    enteredHash
            );

        } catch (Exception e) {
            return false;
        }
    }
}
