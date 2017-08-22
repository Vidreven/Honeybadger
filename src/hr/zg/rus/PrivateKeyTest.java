package hr.zg.rus;

/**
 * Created by hyperion on 8/20/17.
 */
public class PrivateKeyTest {
    public static void main(String[] args){
        PrivateKey private_key = new PrivateKey();

        boolean valid;
        boolean compressed;

        valid = private_key.isValid();
        compressed = private_key.isCompressed();

        if(valid){
            System.out.println("Private key is valid when it should not be!");
        }
        if(compressed){
            System.out.println("Private key is compressed when it should not be!");
        }
        if(private_key.length() != 0){
            System.out.println("Private key should not have been generated!");
        }

        private_key.generatePrivateKey(false);
        valid = private_key.isValid();
        compressed = private_key.isCompressed();

        if(!valid){
            System.out.println("Private key is invalid when it should be!");
        }
        if(compressed){
            System.out.println("Private key is compressed when it should not be!");
        }
        if(private_key.length() > 32){
            System.out.println("Invalid private key! Too long.");
        }

        byte[] bytes = private_key.getPrivate_key();

        if(bytes.length > 32){
            System.out.println("Invalid private key! Too long.");
        }

        int[] ints = private_key.toUit();

        if(ints.length != 8){
            System.out.println("Private key should be 8 words long!");
        }

        String hex = private_key.toHex();

        if(hex.length() != 64){
            System.out.println("Private key should be 64 characters long!");
        }

        String string_key = private_key.toString();

        if(!string_key.equals(hex)){
            System.out.println("Serialized string key should be identical to it's hex form!");
        }

        String wif = private_key.toWIF();

        if(!wif.substring(0, 1).equals("5")){
            System.out.println("Invalid WIF");
        }

        private_key.setCompressed(true);
        compressed = private_key.isCompressed();

        if(!compressed){
            System.out.println("Private key is not compressed when it should be!");
        }

        private_key.generatePrivateKey(true);
        valid = private_key.isValid();
        compressed = private_key.isCompressed();

        if(!valid){
            System.out.println("Private key is invalid when it should be!");
        }
        if(!compressed){
            System.out.println("Private key is not compressed when it should be!");
        }

        bytes = private_key.getPrivate_key();

        if(bytes.length > 32){
            System.out.println("Invalid private key! Too long.");
        }

        ints = private_key.toUit();

        if(ints.length != 8){
            System.out.println("Private key should be 8 words long!");
        }

        hex = private_key.toHex();

        if(hex.length() != 64){
            System.out.println("Private key should be 64 characters long!");
        }

        string_key = private_key.toString();

        if(!string_key.substring(string_key.length() - 2, string_key.length()).equals("01")){
            System.out.println("Key not properly serialized!");
        }

        wif = private_key.toWIF();

        if(!wif.substring(0, 1).equals("K") && !wif.substring(0, 1).equals("L")){
            System.out.println("Invalid WIF " + wif);
        }

        PrivateKey pkey = new PrivateKey();
        pkey.setHexKey("1111111111111111111111111111111111111111111111111111111111111111");

        if(!pkey.isValid()){
            System.out.println("Private key should be valid!");
        }

        PrivateKey key = new PrivateKey();
        key.setWIF("5K4MmZeDavqifLDdG5WSoDEFDECQMyQQeyNyf8h7omBpSonYggz");

        if(!key.isValid()){
            System.out.println("Private key should be valid!");
        }

        PrivateKey pk = new PrivateKey();
        pk.setWIF("L5dVXG7jMqad1pRJN7pkNKsvELGkbDxPBgFH6A3ZN2GDRRDCPuGt");

        if(!pk.isValid()){
            System.out.println("Private key should be valid!");
        }
    }
}