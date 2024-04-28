import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RainbowTable {
    private String Z = "0123456789abcdefghijklmnopqrstuvwxyz";

    public RainbowTable() {
        int chainsLength = 2000;
        int passwordsLength = 7;
        int chainsCount = 2000;


        // generate rainbow table, should be 2000
        Map<String, String> rainbowTable = generateRainbowTable(chainsLength, passwordsLength, chainsCount);

        // example
        System.out.println("Beispiel: Die erste Kette:");
        String firstpw = "0000000";
        String lastpw = rainbowTable.get(firstpw);
        System.out.println(firstpw + " -> " + lastpw);
    }

    public String findPassword(String s) {

        return "";
    }

    // Funktion zur Generierung der Rainbow-Tabelle
    private Map<String, String> generateRainbowTable(int chainsLength, int passwordsLength, int chainsCount) {
        Map<String, String> rainbowTable = new HashMap<>();

        // 2000 pws mit 7 lenght
        String currentPassword;
        String currentHash;

        for (int i = 0; i < 1; i++) {
            currentPassword = generatePassword(i, passwordsLength);
            currentHash = generateHash(currentPassword);
            String firstPw = currentPassword;


            // hash + reduktionsfunktion 2000 mal
            for (int j = 0; j < 5; j++) {
                currentPassword = reduction(currentHash, passwordsLength, j);
                System.out.println("Current pw: " + currentPassword);
                currentHash = generateHash(currentPassword);
                System.out.println("Current Hash: " + currentHash);
            }

            String lastPw = reduction(currentHash, passwordsLength, 1999);

            // Speichere die letzte Reduktion der Kette in der Rainbow-Tabelle
            rainbowTable.put(firstPw, lastPw);
        }

        return rainbowTable;
    }

    // generate pw
    private String generatePassword(int index, int length) {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(Z.charAt(index % Z.length()));
            index /= Z.length();
        }

        return password.reverse().toString();
    }

    // generate hash mit MD5
    private String generateHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(password.getBytes());

            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b)); // format string
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // reduktionsfunktion (nicht funktional bruh....)
    private String reduction(String hash, int length, int level) {
        StringBuilder result = new StringBuilder();
        BigInteger h = new BigInteger(hash, 16);
        h = h.add(BigInteger.valueOf(level));

        for (int i = 0; i < length; i++) {
            int r = h.mod(BigInteger.valueOf(Z.length())).intValue(); // Long too little for hash. therefore BigInt
            h = h.divide(BigInteger.valueOf(Z.length()));
            result.insert(0, Z.charAt(r));
        }
        return result.toString();
    }

}
