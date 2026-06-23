import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class CurrencyConverter {

    static Scanner scanner = new Scanner(System.in);

    // ── Supported currencies with display names ───────────────────────────────
    static final Map<String, String> CURRENCIES = new LinkedHashMap<>();
    static {
        CURRENCIES.put("USD", "US Dollar");
        CURRENCIES.put("INR", "Indian Rupee");
        CURRENCIES.put("EUR", "Euro");
        CURRENCIES.put("GBP", "British Pound");
        CURRENCIES.put("JPY", "Japanese Yen");
        CURRENCIES.put("AUD", "Australian Dollar");
        CURRENCIES.put("CAD", "Canadian Dollar");
        CURRENCIES.put("CHF", "Swiss Franc");
        CURRENCIES.put("CNY", "Chinese Yuan");
        CURRENCIES.put("SGD", "Singapore Dollar");
        CURRENCIES.put("AED", "UAE Dirham");
        CURRENCIES.put("SAR", "Saudi Riyal");
        CURRENCIES.put("MYR", "Malaysian Ringgit");
        CURRENCIES.put("KRW", "South Korean Won");
        CURRENCIES.put("THB", "Thai Baht");
    }

    // ── Fetch real-time exchange rate from open API ───────────────────────────
    // Uses exchangerate-api.com (free, no key needed for base endpoint)
    static double fetchExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) return -1;

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON manually (no external library needed)
            String json = response.toString();
            String ratesSection = json.substring(json.indexOf("\"rates\":{") + 9);

            // Find target currency rate
            String searchKey = "\"" + targetCurrency + "\":";
            int keyIndex = ratesSection.indexOf(searchKey);
            if (keyIndex == -1) return -1;

            int valueStart = keyIndex + searchKey.length();
            int valueEnd   = ratesSection.indexOf(",", valueStart);
            if (valueEnd == -1) valueEnd = ratesSection.indexOf("}", valueStart);

            String rateStr = ratesSection.substring(valueStart, valueEnd).trim();
            return Double.parseDouble(rateStr);

        } catch (Exception e) {
            return -1; // API unreachable — fallback will be used
        }
    }

    // ── Fallback static rates (if no internet) ───────────────────────────────
    // All rates relative to USD (as of June 2025)
    static double getFallbackRate(String from, String to) {
        Map<String, Double> usdRates = new LinkedHashMap<>();
        usdRates.put("USD", 1.0);
        usdRates.put("INR", 83.50);
        usdRates.put("EUR", 0.92);
        usdRates.put("GBP", 0.79);
        usdRates.put("JPY", 149.50);
        usdRates.put("AUD", 1.53);
        usdRates.put("CAD", 1.36);
        usdRates.put("CHF", 0.90);
        usdRates.put("CNY", 7.24);
        usdRates.put("SGD", 1.34);
        usdRates.put("AED", 3.67);
        usdRates.put("SAR", 3.75);
        usdRates.put("MYR", 4.72);
        usdRates.put("KRW", 1325.0);
        usdRates.put("THB", 35.10);

        if (!usdRates.containsKey(from) || !usdRates.containsKey(to)) return -1;

        // Cross rate: from → USD → to
        double fromToUsd = 1.0 / usdRates.get(from);
        double usdToTarget = usdRates.get(to);
        return fromToUsd * usdToTarget;
    }

    // ── Display currency menu ─────────────────────────────────────────────────
    static void showCurrencyList() {
        System.out.println("\n  ┌─────┬──────┬──────────────────────┐");
        System.out.println("  │ No. │ Code │ Currency             │");
        System.out.println("  ├─────┼──────┼──────────────────────┤");
        int i = 1;
        for (Map.Entry<String, String> entry : CURRENCIES.entrySet()) {
            System.out.printf("  │ %-3d │ %-4s │ %-20s │%n",
                    i++, entry.getKey(), entry.getValue());
        }
        System.out.println("  └─────┴──────┴──────────────────────┘");
    }

    // ── Currency selection ────────────────────────────────────────────────────
    static String selectCurrency(String prompt) {
        while (true) {
            showCurrencyList();
            System.out.print("  " + prompt + " (enter code, e.g. USD): ");
            String code = scanner.nextLine().trim().toUpperCase();
            if (CURRENCIES.containsKey(code)) return code;
            System.out.println("  ⚠  Invalid currency code. Please choose from the list.\n");
        }
    }

    // ── Get valid positive amount ─────────────────────────────────────────────
    static double getAmount(String baseCurrency) {
        while (true) {
            System.out.print("  Enter amount in " + baseCurrency + ": ");
            String input = scanner.nextLine().trim();
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.println("  ⚠  Amount must be greater than zero.\n");
                } else if (amount > 10_000_000) {
                    System.out.println("  ⚠  Amount too large. Max allowed: 10,000,000.\n");
                } else {
                    return amount;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Invalid input! Enter a numeric value.\n");
            }
        }
    }

    // ── Currency symbol helper ────────────────────────────────────────────────
    static String getSymbol(String code) {
        switch (code) {
            case "USD": return "$";
            case "INR": return "₹";
            case "EUR": return "€";
            case "GBP": return "£";
            case "JPY": return "¥";
            case "CNY": return "¥";
            case "KRW": return "₩";
            case "THB": return "฿";
            case "CHF": return "Fr";
            default:    return code + " ";
        }
    }

    // ── Main ──────────────────────────────────────────────────────────────────
    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║         CODSOFT CURRENCY CONVERTER          ║");
        System.out.println("║      Real-Time Exchange Rate Converter      ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        boolean continueConverting = true;

        while (continueConverting) {

            // Step 1: Currency Selection ──────────────────────────────────────
            System.out.println("\n  ── SELECT BASE CURRENCY (From) ──────────────");
            String baseCurrency = selectCurrency("Base currency");

            System.out.println("\n  ── SELECT TARGET CURRENCY (To) ──────────────");
            String targetCurrency = selectCurrency("Target currency");

            if (baseCurrency.equals(targetCurrency)) {
                System.out.println("\n  ⚠  Base and target currencies are the same!");
                System.out.println("  The converted amount will equal the input amount.\n");
            }

            // Step 3: Amount Input ────────────────────────────────────────────
            System.out.println("\n  ── ENTER AMOUNT ─────────────────────────────");
            double amount = getAmount(baseCurrency);

            // Step 2: Fetch real-time exchange rates ──────────────────────────
            System.out.println("\n  ⏳ Fetching live exchange rates...");
            double rate = fetchExchangeRate(baseCurrency, targetCurrency);
            boolean isLive = true;

            if (rate == -1) {
                System.out.println("  ⚠  Could not reach API. Using offline rates.");
                rate = getFallbackRate(baseCurrency, targetCurrency);
                isLive = false;
            } else {
                System.out.println("  ✅ Live rates fetched successfully!");
            }

            if (rate == -1) {
                System.out.println("  ❌ Conversion failed. Currency pair not supported.");
            } else {
                // Step 4: Perform Currency Conversion ─────────────────────────
                double convertedAmount = amount * rate;

                String baseSymbol   = getSymbol(baseCurrency);
                String targetSymbol = getSymbol(targetCurrency);

                // Step 5: Display Result ───────────────────────────────────────
                System.out.println("\n╔══════════════════════════════════════════════╗");
                System.out.println("║              CONVERSION RESULT              ║");
                System.out.println("╠══════════════════════════════════════════════╣");
                System.out.printf( "║  From   : %-34s║%n",
                        baseCurrency + " (" + CURRENCIES.get(baseCurrency) + ")");
                System.out.printf( "║  To     : %-34s║%n",
                        targetCurrency + " (" + CURRENCIES.get(targetCurrency) + ")");
                System.out.println("╠══════════════════════════════════════════════╣");
                System.out.printf( "║  Amount : %-34s║%n",
                        baseSymbol + String.format("%,.2f", amount));
                System.out.printf( "║  Rate   : 1 %-5s = %-26s║%n",
                        baseCurrency,
                        targetSymbol + String.format("%,.4f", rate) + " " + targetCurrency);
                System.out.println("╠══════════════════════════════════════════════╣");
                System.out.printf( "║  Result : %-34s║%n",
                        targetSymbol + String.format("%,.2f", convertedAmount)
                                + "  " + targetCurrency);
                System.out.println("╠══════════════════════════════════════════════╣");
                System.out.printf( "║  Source : %-34s║%n",
                        isLive ? "Live (exchangerate-api.com)" : "Offline / Static Rates");
                System.out.println("╚══════════════════════════════════════════════╝");

                // Bonus: reverse rate
                System.out.printf("%n  💡 Reverse: 1 %s = %s %.4f %s%n",
                        targetCurrency, baseSymbol, 1.0 / rate, baseCurrency);
            }

            // ── Convert again? ────────────────────────────────────────────────
            System.out.print("\n  Convert another amount? (yes / no): ");
            String again = scanner.nextLine().trim().toLowerCase();
            continueConverting = again.equals("yes") || again.equals("y");
        }

        System.out.println("\n  Thank you for using CODSOFT Currency Converter! 💱");
        scanner.close();
    }
}