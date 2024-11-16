import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AgeDobCalculator {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java AgeDobCalculator <DOB/AGE> <Reference Date> <Date Format> <DLC>");
            return;
        }

        String inputParam = args[0];
        String referenceDateParam = args[1];
        String dateFormat = args[2].replace("dlc", args[3]);  // Replace 'dlc' with actual delimiter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

        try {
            LocalDate referenceDate = LocalDate.parse(referenceDateParam, formatter);

            if (inputParam.startsWith("DOB=")) {
                String dobString = inputParam.substring(4);
                LocalDate dob = LocalDate.parse(dobString, formatter);
                calculateAge(dob, referenceDate);
            } else if (inputParam.startsWith("AGE=")) {
                String ageString = inputParam.substring(4);
                LocalDate dob = calculateDob(ageString, referenceDate, args[3]);
                System.out.println("Calculated DOB: " + dob.format(formatter));
            } else {
                System.out.println("Invalid input. Must start with 'DOB=' or 'AGE='.");
            }
        } catch (DateTimeParseException e) {
            System.out.println("Error in date format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void calculateAge(LocalDate dob, LocalDate referenceDate) {
        Period age = Period.between(dob, referenceDate);

        System.out.println("Age is: " + age.getYears() + " years, " + age.getMonths() + " months, " + age.getDays() + " days.");
    }

    private static LocalDate calculateDob(String ageString, LocalDate referenceDate, String dlc) {
        String[] parts = ageString.split(dlc);
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid age format. Use 'AGE=YY" + dlc + "MM" + dlc + "DD'.");
        } 

        int years = Integer.parseInt(parts[0]);
        int months = Integer.parseInt(parts[1]);
        int days = Integer.parseInt(parts[2]);

        return referenceDate.minusYears(years).minusMonths(months).minusDays(days);
    }
}
