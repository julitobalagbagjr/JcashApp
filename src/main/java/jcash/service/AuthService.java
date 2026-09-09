package jcash.service;

import jcash.model.Account;
import jcash.model.User;
import jcash.repository.AccountRepository;
import jcash.repository.UserRepository;
import jcash.security.PINHasher;

public class AuthService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    public AuthService() {
        userRepository = new UserRepository();
        accountRepository = new AccountRepository();
    }

    public boolean signup(
            String name,
            String email,
            String number,
            String pin
    ) {

        // Check if the mobile number is already registered
        User existingUser =
                userRepository.findByNumber(number);

        if (existingUser != null) {
            return false;
        }

        // Hash the PIN before saving it
        String pinHash = PINHasher.hashPIN(pin);

        User user = new User(
                name,
                email,
                number,
                pinHash
        );

        // Save the user
        boolean userSaved =
                userRepository.saveUser(user);

        if (!userSaved) {
            return false;
        }

        // Create the user's JCash account
        boolean accountCreated =
                accountRepository.createAccount(
                        user.getId()
                );

        return accountCreated;
    }

    public User login(String number, String pin) {

        // Find the user by mobile number
        User user =
                userRepository.findByNumber(number);

        if (user == null) {
            return null;
        }

        // Verify the entered PIN
        boolean validPIN =
                PINHasher.verifyPIN(
                        pin,
                        user.getPinHash()
                );

        if (!validPIN) {
            return null;
        }

        return user;
    }

    public User verifyRecoveryDetails(
            String number,
            String email
    ) {

        // Find the user by mobile number
        User user =
                userRepository.findByNumber(number);

        if (user == null) {
            return null;
        }

        // Check if the email matches
        if (!user.getEmail().equalsIgnoreCase(email)) {
            return null;
        }

        return user;
    }

    public Account getAccount(User user) {
        return accountRepository.findByUserId(
                user.getId()
        );
    }

    public boolean changePIN(
            User user,
            String currentPIN,
            String newPIN
    ) {

        // Verify the current PIN
        boolean validCurrentPIN =
                PINHasher.verifyPIN(
                        currentPIN,
                        user.getPinHash()
                );

        if (!validCurrentPIN) {
            return false;
        }

        // Hash the new PIN
        String newPinHash =
                PINHasher.hashPIN(newPIN);

        boolean updated =
                userRepository.updatePIN(
                        user.getId(),
                        newPinHash
                );

        // Update the user object after a successful change
        if (updated) {
            user.setPinHash(newPinHash);
        }

        return updated;
    }

    public boolean resetPIN(
            User user,
            String newPIN
    ) {

        // Hash the new PIN
        String newPinHash =
                PINHasher.hashPIN(newPIN);

        boolean updated =
                userRepository.updatePIN(
                        user.getId(),
                        newPinHash
                );

        // Update the user object after a successful reset
        if (updated) {
            user.setPinHash(newPinHash);
        }

        return updated;
    }
}
