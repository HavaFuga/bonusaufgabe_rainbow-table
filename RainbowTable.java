import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RainbowTable {
    private static String Z = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        int chainsLength = 2000;
        int passwordsLength = 7;
        int chainsCount = 2000;


        // generate rainbow table, should be 2000
        Map<String, String> rainbowTable = generateRainbowTable(chainsLength, passwordsLength, chainsCount);

        // example
        System.out.println("Beispiel: Die erste Kette:");
        String firstPassword = "0000000";
        String lastHash = rainbowTable.get(firstPassword);
        System.out.println(firstPassword + " -> " + lastHash);

        // TODO: Ermittle has mit alg vom Unterricht
    }

    // Funktion zur Generierung der Rainbow-Tabelle
    public static Map<String, String> generateRainbowTable(int chainsLength, int passwordsLength, int chainsCount) {
        Map<String, String> rainbowTable = new HashMap<>();

        // 2000 pws mit 7 lenght
        String currentPassword;
        String currentHash;

        for (int i = 0; i < chainsCount; i++) {
            currentPassword = generatePassword(i, passwordsLength);
            currentHash = generateHash(currentPassword);

            System.out.println("currentHash: " + currentHash);

            // hash + reduktionsfunktion 2000 mal
            for (int j = 0; j < chainsLength - 1; j++) {
                currentPassword = reduction(currentHash, passwordsLength, j);
                currentHash = generateHash(currentPassword);
            }

            // Speichere die letzte Reduktion der Kette in der Rainbow-Tabelle
//            rainbowTable.put(currentPassword, currentHash);
        }

        return rainbowTable;
    }

    // generate pw
    public static String generatePassword(int index, int length) {
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(Z.charAt(index % Z.length()));
            index /= Z.length();
        }
        System.out.println("password.reverse().toString() " + password.reverse().toString());

        return password.reverse().toString();
    }

    // generate hash mit MD5
    public static String generateHash(String password) {
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
    public static String reduction(String hash, int length, int level) {
        StringBuilder result = new StringBuilder();
        long h = Long.parseLong(hash, 16);
        for (int i = 0; i < length; i++) {
            int r = (int)(h % Z.length());
            h = h / Z.length();
            result.insert(0, Z.charAt(r));
        }
        return result.toString();
    }

}
