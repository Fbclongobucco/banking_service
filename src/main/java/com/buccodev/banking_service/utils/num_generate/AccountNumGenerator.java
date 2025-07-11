package com.buccodev.banking_service.utils.num_generate;

import java.util.Random;

public class AccountNumGenerator {

    public static String generateBBAccountNumber() {
        Random random = new Random();

        StringBuilder account = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            account.append(random.nextInt(10));
        }

        char checkDigit = calculateCheckDigit(account.toString());

        return account + "-" + checkDigit;
    }

    private static char calculateCheckDigit(String account) {
        int[] weights = {8, 7, 6, 5, 4, 3, 2};
        int sum = 0;

        for (int i = 0; i < account.length(); i++) {
            int digit = Character.getNumericValue(account.charAt(i));
            sum += digit * weights[i];
        }

        int remainder = sum % 11;

        if (remainder == 0 || remainder == 1) {
            return '0';
        } else if (remainder == 10) {
            return 'X';
        } else {
            int dv = 11 - remainder;
            return Character.forDigit(dv, 10);
        }
    }
}
