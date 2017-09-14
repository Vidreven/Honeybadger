package hr.zg.rus;

import java.util.Hashtable;

public class Networks {

    private Hashtable<String, Byte> bitcoin = new Hashtable<>();
    private Hashtable<String, Byte> testnet = new Hashtable<>();
    private static Hashtable<String, Hashtable> networks = new Hashtable<>();

    public Networks(){
        bitcoin.put("pubKeyHash", (byte)0x0);
        bitcoin.put("wif", (byte)0x80);

        testnet.put("pubKeyHash", (byte)0x6f);
        testnet.put("wif", (byte)0xef);

        networks.put("bitcoin", bitcoin);
        networks.put("testnet", testnet);
    }

    public Hashtable<String, Hashtable> getNetworks() {
        return networks;
    }
}
