import java.util.Random;
import java.util.Scanner;

public class NumberGame {

    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    static final int MIN = 1;
    static final int MAX = 100;
    static final int MAX_ATTEMPTS = 7;

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║       WELCOME TO THE NUMBER GAME     ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.println("Guess the number between " + MIN + " and " + MAX);
        System.out.println("You have " + MAX_ATTEMPTS + " attempts per round.\n");

        int totalRounds = 0;
        int roundsWon = 0;
        int totalScore = 0;

        // ── Multiple Rounds Loop ──────────────────────────────────────────────
        while (true) {
            totalRounds++;
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
            System.out.println("  ROUND " + totalRounds);
            System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

            // Step 1: Generate random number
            int secretNumber = random.nextInt(MAX - MIN + 1) + MIN;
            boolean guessedCorrectly = false;
            int attemptsUsed = 0;

            // Steps 2–4: Prompt user and give feedback; repeat until correct
            for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
                System.out.print("Attempt " + attempt + "/" + MAX_ATTEMPTS + " → Enter your guess: ");

                // Input validation
                if (!scanner.hasNextInt()) {
                    System.out.println("⚠  Invalid input! Please enter a number between "
                            + MIN + " and " + MAX + ".\n");
                    scanner.next(); // discard bad input
                    attempt--;     // don't count this as an attempt
                    continue;
                }

                int guess = scanner.nextInt();

                if (guess < MIN || guess > MAX) {
                    System.out.println("⚠  Out of range! Guess must be between "
                            + MIN + " and " + MAX + ".\n");
                    attempt--; // don't penalise for range error
                    continue;
                }

                attemptsUsed++;

                // Step 3: Compare and give feedback
                if (guess == secretNumber) {
                    System.out.println("✅ CORRECT! The number was " + secretNumber + ".");
                    guessedCorrectly = true;
                    break;
                } else if (guess < secretNumber) {
                    System.out.println("📉 Too LOW!  Try a higher number.\n");
                } else {
                    System.out.println("📈 Too HIGH! Try a lower number.\n");
                }
            }

            // Step 5: Limit reached — reveal answer if not guessed
            if (!guessedCorrectly) {
                System.out.println("❌ Out of attempts! The number was: " + secretNumber);
                attemptsUsed = MAX_ATTEMPTS;
            }

            // Step 7: Calculate score for this round
            int roundScore = 0;
            if (guessedCorrectly) {
                roundsWon++;
                // More attempts remaining = higher score
                roundScore = (MAX_ATTEMPTS - attemptsUsed + 1) * 10;
                totalScore += roundScore;
                System.out.println("⭐ Round Score: " + roundScore + " points"
                        + " (guessed in " + attemptsUsed + " attempt"
                        + (attemptsUsed == 1 ? "" : "s") + ")");
            } else {
                System.out.println("⭐ Round Score: 0 points");
            }

            // Step 6: Ask to play again
            System.out.println("\nPlay again? (yes / no): ");
            String choice = scanner.next().trim().toLowerCase();

            if (!choice.equals("yes") && !choice.equals("y")) {
                break;
            }
            System.out.println();
        }

        // ── Final Score Display ───────────────────────────────────────────────
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║              GAME OVER               ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.printf("║  Rounds Played : %-20d║%n", totalRounds);
        System.out.printf("║  Rounds Won    : %-20d║%n", roundsWon);
        System.out.printf("║  Rounds Lost   : %-20d║%n", totalRounds - roundsWon);
        System.out.printf("║  Total Score   : %-20d║%n", totalScore);
        System.out.println("╚══════════════════════════════════════╝");

        if (totalScore == 0) {
            System.out.println("Better luck next time! Keep practicing. 💪");
        } else if (roundsWon == totalRounds) {
            System.out.println("Perfect game! You won every round! 🏆");
        } else {
            System.out.println("Great effort! Thanks for playing! 🎮");
        }

        scanner.close();
    }
}