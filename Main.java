public class Main {
    public static void main(String[] args) {
        RainbowTable rt = new RainbowTable(2000, 2000, 7);
        rt.findPassword("1d56a37fb6b08aa709fe90e12ca59e12");

        // zur kontrolle (was too lazy for tests sorry)
//        rt.findPassword("c0e9a2f2ae2b9300b6f7ef3e63807e84"); // should be dues6fg
//        rt.findPassword("10e9a2f2ae2b9300b6f7ef3e63807e84"); // should fail
//        rt.findPassword("29c3eea3f305d6b823f562ac4be35217"); // should be 0000000
//        rt.findPassword("437988e45a53c01e54d21e5dc4ae658a"); // should be frrkiis
    }
}
