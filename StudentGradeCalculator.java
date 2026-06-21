import java.util.ArrayList;
import java.util.Scanner;

public class StudentGradeCalculator {

    static Scanner scanner = new Scanner(System.in);

    // ── Grade Assignment Logic ────────────────────────────────────────────────
    static String getGrade(double average) {
        if (average >= 90) return "A+ (Outstanding)";
        else if (average >= 80) return "A  (Excellent)";
        else if (average >= 70) return "B  (Very Good)";
        else if (average >= 60) return "C  (Good)";
        else if (average >= 50) return "D  (Average)";
        else if (average >= 40) return "E  (Pass)";
        else                    return "F  (Fail)";
    }

    // ── Remarks based on grade ────────────────────────────────────────────────
    static String getRemarks(double average) {
        if (average >= 90) return "🏆 Exceptional performance! Keep it up!";
        else if (average >= 80) return "🌟 Great job! You're doing very well!";
        else if (average >= 70) return "👍 Good work! A little more effort can get you higher!";
        else if (average >= 60) return "📚 Decent performance. Focus more to improve!";
        else if (average >= 50) return "⚠  Average. You need to work harder!";
        else if (average >= 40) return "⚠  Borderline pass. Serious improvement needed!";
        else                    return "❌ Failed. Please seek help and study harder!";
    }

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║      STUDENT GRADE CALCULATOR            ║");
        System.out.println("╚══════════════════════════════════════════╝\n");

        // ── Student Name ──────────────────────────────────────────────────────
        System.out.print("Enter Student Name : ");
        scanner.nextLine();
        String studentName = scanner.nextLine().trim();
        if (studentName.isEmpty()) studentName = "Student";

        // ── Number of Subjects ────────────────────────────────────────────────
        int numSubjects = 0;
        while (true) {
            System.out.print("Enter Number of Subjects: ");
            if (scanner.hasNextInt()) {
                numSubjects = scanner.nextInt();
                scanner.nextLine();
                if (numSubjects >= 1 && numSubjects <= 20) break;
                else System.out.println("⚠  Please enter between 1 and 20 subjects.\n");
            } else {
                System.out.println("⚠  Invalid input! Enter a whole number.\n");
                scanner.nextLine();
            }
        }

        // ── Subject Names & Marks Input ───────────────────────────────────────
        ArrayList<String> subjectNames = new ArrayList<>();
        ArrayList<Double> marks = new ArrayList<>();

        System.out.println();
        for (int i = 1; i <= numSubjects; i++) {
            // Subject name
            System.out.print("  Subject " + i + " Name  : ");
            String subName = scanner.nextLine().trim();
            if (subName.isEmpty()) subName = "Subject " + i;
            subjectNames.add(subName);

            // Marks for that subject
            double mark = -1;
            while (true) {
                System.out.print("  Marks Obtained (0-100): ");
                if (scanner.hasNextDouble()) {
                    mark = scanner.nextDouble();
                    scanner.nextLine();
                    if (mark >= 0 && mark <= 100) break;
                    else System.out.println("  ⚠  Marks must be between 0 and 100.\n");
                } else {
                    System.out.println("  ⚠  Invalid input! Enter a numeric value.\n");
                    scanner.nextLine();
                }
            }
            marks.add(mark);
            System.out.println();
        }

        // ── Calculations ──────────────────────────────────────────────────────
        double totalMarks = 0;
        for (double m : marks) totalMarks += m;

        double averagePercentage = totalMarks / numSubjects;
        String grade   = getGrade(averagePercentage);
        String remarks = getRemarks(averagePercentage);

        // ── Display Results ───────────────────────────────────────────────────
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                  RESULT CARD                    ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf( "║  Student Name   : %-30s║%n", studentName);
        System.out.printf( "║  No. of Subjects: %-30d║%n", numSubjects);
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  SUBJECT-WISE BREAKDOWN                         ║");
        System.out.println("╠══════════════════════════════════════════════════╣");

        for (int i = 0; i < numSubjects; i++) {
            String subjectLine = String.format("  %-20s: %6.2f / 100",
                    subjectNames.get(i), marks.get(i));
            // Pad to fixed width for box
            System.out.printf("║%-49s║%n", subjectLine);
        }

        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf( "║  Total Marks    : %-6.2f / %-22d║%n",
                totalMarks, numSubjects * 100);
        System.out.printf( "║  Average (%%)    : %-30.2f║%n", averagePercentage);
        System.out.printf( "║  Grade          : %-30s║%n", grade);
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf( "║  Remarks: %-38s║%n", remarks);
        System.out.println("╚══════════════════════════════════════════════════╝");

        // ── Pass/Fail Summary ─────────────────────────────────────────────────
        System.out.println("\n  📊 SUBJECT PERFORMANCE SUMMARY:");
        System.out.println("  ─────────────────────────────────────────");
        int passed = 0, failed = 0;
        for (int i = 0; i < numSubjects; i++) {
            String status = marks.get(i) >= 40 ? "✅ PASS" : "❌ FAIL";
            if (marks.get(i) >= 40) passed++; else failed++;
            System.out.printf("  %-20s → %6.2f  %s%n",
                    subjectNames.get(i), marks.get(i), status);
        }
        System.out.println("  ─────────────────────────────────────────");
        System.out.println("  Subjects Passed : " + passed);
        System.out.println("  Subjects Failed : " + failed);

        if (failed > 0) {
            System.out.println("\n  ⚠  You have failed in " + failed
                    + " subject(s). Focus on improvement!");
        } else {
            System.out.println("\n  🎉 Passed in all subjects. Congratulations, "
                    + studentName + "!");
        }

        scanner.close();
    }
}