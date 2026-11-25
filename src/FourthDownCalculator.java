import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class FourthDownCalculator {
    static String decisionSource;

    public static void main(String[] args) throws IOException {
        int[] inputs = getInput();

        int yardsNeeded = inputs[0];
        int yardsFromEndZone = inputs[1];
        int scoreDifference = inputs[2];
        int timeRemaining = inputs[3];

        ArrayList<FourthDownPlay> plays = readNFLData();

        double conversionRateBasedOnDistance = calculateConversionRateByDistance(plays, yardsNeeded);

        double conversionRateBySituation = calculateConversionRateBySituation(plays, yardsNeeded, yardsFromEndZone);

        System.out.println("conversionRateBasedOnDistance = " + conversionRateBasedOnDistance);

        System.out.println("ConversionRateBySituation = " + conversionRateBySituation);

        String statisticalSource = "Distance and Field Position";

        int playsCounted = countMatchingPlays(plays, yardsNeeded, yardsFromEndZone);

        if (playsCounted < 20) {
            conversionRateBySituation = conversionRateBasedOnDistance;

            statisticalSource = "Distance Only";

            playsCounted = countMatchingPlaysByDistance(plays, yardsNeeded);
        }

        String decision = makeDecision(inputs, conversionRateBySituation, playsCounted);

        System.out.println("Matching plays: " + playsCounted);

        displayResult(decision, conversionRateBySituation, playsCounted, statisticalSource);
    }


    public static int[] getInput() {
        int[] output = new int[4];

        int yardsNeeded = 0;
        String yardsNeededInput;

        while (yardsNeeded < 1 || yardsNeeded > 99) {
            try {
                yardsNeededInput = JOptionPane.showInputDialog("Yards needed: ");

                if (yardsNeededInput == null)
                    System.exit(0);

                yardsNeeded = Integer.parseInt(yardsNeededInput);

                if (yardsNeeded < 1 || yardsNeeded > 99) {
                    JOptionPane.showMessageDialog(null, "The Yards Needed has to be from 1-99");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        }

        JOptionPane.showMessageDialog(null, "Yards needed: " + yardsNeeded);

        output[0] = yardsNeeded;


        int yardsToEndZone = 0;
        String yardsToEndZoneInput;

        while (yardsToEndZone < 1 || yardsToEndZone > 99 || yardsNeeded > yardsToEndZone) {
            try {
                yardsToEndZoneInput = JOptionPane.showInputDialog("Yards to Endzone(1-99): ");

                if (yardsToEndZoneInput == null)
                    System.exit(0);

                yardsToEndZone = Integer.parseInt(yardsToEndZoneInput);

                if (yardsToEndZone < 1 || yardsToEndZone > 99) {
                    JOptionPane.showMessageDialog(null, "The Yards from endzone has to be from 1-99");
                } else if (yardsNeeded > yardsToEndZone) {
                    JOptionPane.showMessageDialog(null, "Yards needed cannot exceed yards to the endzone");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        }

        JOptionPane.showMessageDialog(null, "Yards to EndZone: " + yardsToEndZone);

        output[1] = yardsToEndZone;


        int scoreDifference = 0;
        String scoreDifferenceInput;
        boolean validScore = false;

        while (!validScore) {
            try {
                scoreDifferenceInput = JOptionPane.showInputDialog("What is the Score Difference(Positive is Winning, Negative Is losing): ");

                if (scoreDifferenceInput == null)
                    System.exit(0);

                scoreDifference = Integer.parseInt(scoreDifferenceInput);

                validScore = true;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        }

        JOptionPane.showMessageDialog(null, "Score Difference: " + scoreDifference);

        output[2] = scoreDifference;


        int timeRemaining = -1;
        String timeRemainingInput;

        while (timeRemaining < 0 || timeRemaining > 3600) {
            try {
                timeRemainingInput = JOptionPane.showInputDialog("How much time is remaining(In seconds)(0-3600): ");

                if (timeRemainingInput == null)
                    System.exit(0);

                timeRemaining = Integer.parseInt(timeRemainingInput);

                if (timeRemaining < 0 || timeRemaining > 3600) {
                    JOptionPane.showMessageDialog(null, "The time remaining has to be from 0-3600 seconds");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number");
            }
        }

        JOptionPane.showMessageDialog(null, "Time Remaining: " + timeRemaining);

        output[3] = timeRemaining;

        return output;
    }

    public static String makeDecision(int[] input, double conversionRate, int playsCounted) {
        if ((input[2] <= -1 && input[2] >= -3) && input[3] <= 120 && input[1] <= 35) {
            decisionSource = "Rule-based";
            return "FIELD GOAL";
        }

        if (input[2] <= -9 && input[3] <= 300 && input[0] <= 10) {
            decisionSource = "Rule-based";
            return "GO FOR IT";
        }
        if ((input[2] >= -8 && input[2] < 0) && input[3] <= 120 && input[0] <= 5) {
            decisionSource = "Rule-based";
            return "GO FOR IT";
        }

        if ((input[2] >= -8 && input[2] <= -1) && input[3] <= 300 && input[0] <= 3) {
            decisionSource = "Rule-based";
            return "GO FOR IT";
        }

        if ((input[2] >= 1 && input[2] <= 8) && input[3] <= 120 && input[1] > 35) {
            decisionSource = "Rule-based";
            return "PUNT";
        }

        if (input[1] >= 80 && input[3] > 300 && input[0] > 1) {
            decisionSource = "Rule-based";
            return "PUNT";
        }

        if (conversionRate >= 60 && playsCounted >= 20) {
            decisionSource = "NFL statistics";
            return "GO FOR IT";
        } else if (conversionRate >= 50 && playsCounted >= 20 && input[0] <= 3) {
            decisionSource = "NFL statistics";
            return "GO FOR IT";
        }

        if (input[0] <= 2 && input[1] <= 50) {
            decisionSource = "Rule-based";
            return "GO FOR IT";
        } else if (input[1] <= 35) {
            decisionSource = "Rule-based";
            return "FIELD GOAL";
        }
        decisionSource = "Rule-based";
        return "PUNT";
    }

    public static void displayResult(String decision, double conversionRate, int playsCounted, String statisticalSource) {
        String confidenceLvl = getConfidenceLevel(playsCounted);
        if (playsCounted == 0) {
            JOptionPane.showMessageDialog(null, "Recommendation: " + decision
                    + "\nConversion Rate: Not enough data"
                    + "\n4th Down Plays Counted: " + playsCounted + "\n Used " + decisionSource
                    + "\nConfidence Level: " + confidenceLvl
                    + "\nStatistical Source: " + statisticalSource);
        } else if (playsCounted >= 20) {
            JOptionPane.showMessageDialog(null, "Recommendation: " + decision
                    + "\n4th Down Conversion Rate: " + String.format("%.2f", conversionRate) + "%"
                    + "\n4th Down Plays Counted: " + playsCounted + "\n Used " + decisionSource
                    + "\nConfidence Level: " + confidenceLvl
                    + "\nStatistical Source: " + statisticalSource);
        } else {
            JOptionPane.showMessageDialog(null, "Recommendation: " + decision
                    + "\n4th Down Conversion Rate: " + String.format("%.2f", conversionRate) + "%"
                    + "\n4th Down Plays Counted: " + playsCounted + "\n Used " + decisionSource
                    + "\nConfidence Level: " + confidenceLvl
                    + "\nStatistical Source: " + statisticalSource);
        }
    }

    public static ArrayList<FourthDownPlay> readData() throws IOException {
        ArrayList<FourthDownPlay> plays = new ArrayList<>();

        FileReader file = new FileReader("fourth_down_data.csv");

        BufferedReader reader = new BufferedReader(file);

        String line = reader.readLine();

        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            System.out.println(data[0] + " | " + data[1] + " | " + data[2] + " | " + data[3]);
            int yardsNeeded = Integer.parseInt(data[0]);
            int yardsToEndzone = Integer.parseInt(data[1]);
            int yardsGained = Integer.parseInt(data[2]);
            boolean converted = Boolean.parseBoolean(data[3]);
            plays.add(new FourthDownPlay(yardsNeeded, yardsToEndzone, yardsGained, converted));
        }

        reader.close();

        System.out.println("Plays Sizde = " + plays.size());

        return plays;
    }

    public static double calculateConversionRate(ArrayList<FourthDownPlay> arr) {
        if (arr.size() == 0)
            return 0;
        double output = 0;
        for (FourthDownPlay p : arr) {
            boolean conversion = p.isConverted();
            if (conversion)
                output++;
        }

        return output / arr.size() * 100;
    }

    public static double calculateConversionRateByDistance(ArrayList<FourthDownPlay> arr, int yardsNeeded) {
        if (arr.size() == 0)
            return 0;
        double output = 0;
        double playsCounted = 0;
        for (FourthDownPlay p : arr) {
            if (p.getYardsNeeded() == yardsNeeded) {
                playsCounted++;
                if (p.isConverted())
                    output++;
            }
        }

        if (playsCounted == 0)
            return 0;

        return output / playsCounted * 100;
    }


    public static double calculateConversionRateBySituation(ArrayList<FourthDownPlay> plays, int yardsNeeded, int yardsToEndzone) {
        double output = 0;
        ArrayList<FourthDownPlay> playsNeeded = new ArrayList<>();
        for (FourthDownPlay p : plays) {
            if (p.getYardsNeeded() == yardsNeeded) {
                if (Math.abs(p.getYardsToEndzone() - yardsToEndzone) <= 10)
                    playsNeeded.add(p);
            }
        }

        if (playsNeeded.size() == 0)
            return 0;
        for (FourthDownPlay p : playsNeeded) {
            if (p.isConverted())
                output++;
        }


        return output / playsNeeded.size() * 100;
    }

    public static int countMatchingPlays(ArrayList<FourthDownPlay> plays, int yardsNeeded, int yardsToEndzone) {
        ArrayList<FourthDownPlay> playsNeeded = new ArrayList<>();
        for (FourthDownPlay p : plays) {
            if (p.getYardsNeeded() == yardsNeeded) {
                if (Math.abs(p.getYardsToEndzone() - yardsToEndzone) <= 10)
                    playsNeeded.add(p);
            }
        }

        return playsNeeded.size();
    }


    public static ArrayList<FourthDownPlay> readNFLData() throws IOException {
        int count = 0;
        ArrayList<FourthDownPlay> plays = new ArrayList<>();

        FileReader file = new FileReader("play_by_play_2025.csv");

        CSVParser parser = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get().parse(file);

        int sucessfulConversions = 0;

        for (CSVRecord record : parser) {
            String downString = record.get("down");
            if (downString.isEmpty())
                continue;

            int down = Integer.parseInt(downString);
            if (down != 4)
                continue;

            String playType = record.get("play_type");
            if (playType.equals("run") || playType.equals("pass")) {
                String yardsNeededString = record.get("ydstogo");
                String yardsToEndZoneString = record.get("yardline_100");
                String yardsGainedString = record.get("yards_gained");

                if (yardsNeededString.isEmpty() || yardsToEndZoneString.isEmpty() || yardsGainedString.isEmpty())
                    continue;

                int yardsNeededInt = Integer.parseInt(yardsNeededString);
                int yardsToEndZoneInt = Integer.parseInt(yardsToEndZoneString);
                int yardsGainedInt = Integer.parseInt(yardsGainedString);
                String convertedString = record.get("fourth_down_converted");
                boolean convertedBoolean = convertedString.equals("1");

                FourthDownPlay play = new FourthDownPlay(yardsNeededInt, yardsToEndZoneInt, yardsGainedInt, convertedBoolean);

                plays.add(play);
            }
        }
        System.out.println("Objects created: " + plays.size());
        parser.close();
        return plays;
    }

    public static String getConfidenceLevel(int playsCounted) {
        if (playsCounted < 20)
            return "Low";
        else if (playsCounted < 50)
            return "Medium";
        else
            return "High";
    }

    public static int countMatchingPlaysByDistance(ArrayList<FourthDownPlay> plays, int yardsNeeded) {
        int count = 0;
        for (FourthDownPlay p : plays) {
            if (p.getYardsNeeded() == yardsNeeded) {
                count++;
            }
        }
        return count;
    }
}

