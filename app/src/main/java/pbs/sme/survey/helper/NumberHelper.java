package pbs.sme.survey.helper;

public class NumberHelper {
    private static final String[] belowTwenty = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Eineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static final String[] thousands = {
            "", "Thousand,", "Million,", "Billion,", "Trillion,"
    };

    public static String convertToWords(long number) {
        if (number == 0) return "zero";

        StringBuilder words = new StringBuilder();
        int thousandIndex = 0;

        while (number > 0) {
            if (number % 1000 != 0) {
                String segment = convertThreeDigitSegment((int) (number % 1000));
                words.insert(0, segment + (thousands[thousandIndex].isEmpty() ? "" : " " + thousands[thousandIndex]) + " ");
            }
            number /= 1000;
            thousandIndex++;
        }


        return words.toString().trim()+" Only";
    }

    private static String convertThreeDigitSegment(int num) {
        StringBuilder segment = new StringBuilder();

        if (num >= 100) {
            segment.append(belowTwenty[num / 100]).append(" Hundred and ");
            num %= 100;
        }

        if (num >= 20) {
            segment.append(tens[num / 10]).append(" ");
            num %= 10;
        }

        if (num > 0) {
            segment.append(belowTwenty[num]);
        }

        return segment.toString().trim();
    }
}
