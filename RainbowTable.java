import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RainbowTable {
    private String Z = "0123456789abcdefghijklmnopqrstuvwxyz";
    private int chainsLength;
    private int chainsCount;
    private int passwordsLength;
    private Map<String, String> rainbowTable;

    public RainbowTable(int cl, int cc, int pl) {
        chainsLength = cl;
        chainsCount = cc;
        passwordsLength = pl;

        // generate rainbow table, should be 2000
        rainbowTable = generateRainbowTable();

        // Check for testing
        /*System.out.println("Beispiel: Die erste Kette:");
        String firstpw = "0000000";
        String lastpw = rainbowTable.get(firstpw);
        System.out.println(firstpw + " -> " + lastpw);*/
    }

    public String findPassword(String hash) {
        System.out.println("Please wait, we are looking for the password for the hash " + hash);
        String firstpw = "";
        boolean hasPwd = false;

        outerloop:
        for (int i = chainsCount - 1; i >= 0; i--) {
            String currentHash = hash;
            String pw = "";

            // encript until R2000
            for (int j = i; j < chainsLength; j++) {
                pw = reduction(currentHash, passwordsLength, j);
                currentHash = generateHash(pw);
            }

            if (rainbowTable.containsValue(pw)) {
                firstpw = getKey(pw);
                hasPwd = true;
                break outerloop;
            }
        }

        if (!hasPwd) {
            System.out.println("Sorry :( could not find the password from your hash...");
            return "no pw";
        }

        String cpw = firstpw;
        String ch = generateHash(cpw);
        // hash + reduktionsfunktion 2000 mal
        for (int j = 0; j < chainsLength; j++) {
            // could be the first hash like for example with pw 0000000 vom arbeitsblatt
            if (ch.equals(hash)){
                System.out.println("The start password is " + firstpw);
                System.out.println("It's the " + j + "th Reductionfunktion from the chain out of " + chainsLength);
                System.out.println("-------------------------------------------------------------------------------");
                System.out.println("This is your hash and the corresponding password:");
                System.out.println("Hash: " + hash);
                System.out.println("Password: " + cpw);
                break;
            }

            cpw = reduction(ch, passwordsLength, j);
            ch = generateHash(cpw);
        }
        return cpw;
    }

    // got this code from https://stackoverflow.com/questions/1383797/java-hashmap-how-to-get-key-from-value
    // since its one to one. probably not the smartest tho
    private String getKey(String s) {
        for (Map.Entry<String, String> entry : rainbowTable.entrySet()) {
            if (Objects.equals(s, entry.getValue())) {
                return entry.getKey().toString();
            }
        }
        return "";
    }

    // generate rainbowtable
    private Map<String, String> generateRainbowTable() {
        Map<String, String> rainbowTable = new HashMap<>();

        // 2000 pws mit 7 lenght
        String currentPassword;
        String currentHash;

        for (int i = 0; i < chainsCount; i++) {
            currentPassword = generatePassword(i, passwordsLength);
            currentHash = generateHash(currentPassword);
            String firstPw = currentPassword;

            // hash + reduktionsfunktion 2000 mal
            for (int j = 0; j < chainsLength; j++) {
                currentPassword = reduction(currentHash, passwordsLength, j);
                currentHash = generateHash(currentPassword);
            }

            // save first and last pw
            rainbowTable.put(firstPw, currentPassword);
        }

        System.out.println("RainbowTable successfully generated.");
        System.out.println();
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

    // reduktionsfunktion
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
