package upm;


import java.util.Random;

public class RandomGenerator {
    private static final Random random = new Random();

    //TODO: CHECK THE IDS DOESNT ALREADY EXIST

    public static String generateCashierId() {
        return "UW" + String.format("%07d", random.nextInt(10000000));
    }
    public  static String generateDNI(){
        int number = random.nextInt(100_000_000); // 0 to 99,999,999
        char letter = (char) ('A' + random.nextInt(26)); // random uppercase A-Z
        return String.format("%08d%c", number, letter);
    }
}
