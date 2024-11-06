package currencyconverter1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CurrencyConverter1 {

    private static String rate;

    public static void main(String[] args) {

        //Currecy Codes
        String[] CurrencyCodes = {"AED", "CAD", "ETB", "EUR", "USD"};
        
  System.out.println("Welcome to our currency converter app! ");
  
        System.out.println("The avaliable currency codes are:");
        for (String code : CurrencyCodes) {
            System.out.println(code);
        }

        Scanner input = new Scanner(System.in);
        boolean continueConverting = true;

        while (continueConverting) {

            System.out.print("Enter the currency code you would like to convert FROM: ");
            String from = input.nextLine().toUpperCase();

            System.out.print("Enter the currency code you would like to convert TO: ");
            String to = input.nextLine().toUpperCase();
            
//Checks if the user entered a valid currency code
            if (!Arrays.asList(CurrencyCodes).contains(from) || !Arrays.asList(CurrencyCodes).contains(to)) {
                System.out.println("Invalid currency code! Please try again.");
                return;
            }

            double rate = getExchangeRate(from, to);

            System.out.println("The exchange rate is " + rate);

            System.out.print("Enter the amount you want to convert: ");

            String inputAmount = input.nextLine();

            //checks if the user entered valid amount   
            try {

                double amount = Double.parseDouble(inputAmount);
                double converted = rate * amount;

                //check if the user's input amount, if there is an error, it will show an error message
                if (amount <= 0) {
                    System.out.println("Please insert a number greater than 0. ");

                    return;
                }

                System.out.println(amount + " " + from + " = " + converted + " " + to);

            } catch (NumberFormatException e) {
                System.out.println("Your input amount is invalid! Please try again.");
                return;
            }

            System.out.print("Do you want to make another currency conversion? (YES/NO) ");
            String choice = input.nextLine();
            if (!choice.equalsIgnoreCase("yes")) {
                continueConverting = false;
                System.out.println("Thank you for using our currency converter app!");
            }
        }

//get latest exchange rate
    }

    private static double getExchangeRate(String from, String to) {

        try {
            // Read the conversion JSON file from https://api.exchangerate-api.com/v4/latest/USD
            String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + from;
            double exchangeRate = 0.0;
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            String jsonResponse = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
            connection.disconnect();
            // Find the index of "USD" in the JSON response and extract the exchange rate
            int usdIndex = jsonResponse.indexOf("\"" + to + "\":");
            int endIndex = jsonResponse.indexOf(",", usdIndex);
            if (usdIndex != -1 && endIndex != -1) {
                String usdRateSubstring = jsonResponse.substring(usdIndex + 6, endIndex);
                exchangeRate = Double.parseDouble(usdRateSubstring);
            } else {
                // Handle parsing error
                return -998;
            }
            return exchangeRate;
        } catch (Exception ex) {
            System.out.println("Error : " + ex.getMessage());
            return -999;
        }

    }

}
