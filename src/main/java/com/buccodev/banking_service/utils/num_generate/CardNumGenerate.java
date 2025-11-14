package com.buccodev.banking_service.utils.num_generate;

import java.util.Random;

public class CardNumGenerate {

    public static String generateNumCard() {
        String bin = "453960";
        StringBuilder number = new StringBuilder(bin);

        var rand = new Random();

        for (int i = 0; i < 9; i++) {
            number.append(rand.nextInt(10));
        }

        int checkDigit = getLuhnCheckDigit(number.toString());
        number.append(checkDigit);

        return number.toString();
    }

    public static int getLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(number.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }

        return (10 - (sum % 10)) % 10;
    }

    public static String generateCvv() {
        var rand = new Random();
        StringBuilder cvv = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cvv.append(rand.nextInt(10));
        }
        return cvv.toString();
    }

    public static String generatePassword() {
        var rand = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            password.append(rand.nextInt(10));
        }
        return password.toString();
    }
}
