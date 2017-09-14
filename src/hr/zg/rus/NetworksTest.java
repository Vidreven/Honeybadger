package hr.zg.rus;

import java.util.Hashtable;

public class NetworksTest {

    public static void main(String[] args) {
        Networks networks = new Networks();

        Hashtable<String, Hashtable> network = networks.getNetworks();
        byte value;
        value = (byte)network.get("bitcoin").get("wif");

        if(value != -128){
            System.out.println("Invalid mainnet WIF prefix!");
        }

        value = (byte)network.get("bitcoin").get("pubKeyHash");
        if(value != 0){
            System.out.println("Invalid mainnet pubKeyHash prefix!");
        }

        value = (byte)network.get("testnet").get("wif");
        if(value != -17){
            System.out.println("Invalid testnet WIF prefix!");
        }

        value = (byte)network.get("testnet").get("pubKeyHash");
        if(value != 111){
            System.out.println("Invalid testnet pubKeyHash prefix!");
        }
    }
}