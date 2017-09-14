package hr.zg.rus;

import java.util.Hashtable;

public class PublicKeyTest {
    public static void main(String[] args){
        Networks networks = new Networks();
        Hashtable<String, Hashtable> network = networks.getNetworks();

        PrivateKey private_key = new PrivateKey(network.get("bitcoin"));
        private_key.generatePrivateKey(false);
        PublicKey public_key = new PublicKey(private_key);

        if(public_key.isValid()){
            System.out.println("Public key should not be valid!");
        }
        if(public_key.isCompressed()){
            System.out.println("Public key should not be compressed since it's not valid!");
        }

        int[] decimal_key = public_key.generatePubKey();

        if(!public_key.isValid()){
            System.out.println("Public key should be valid!");
        }
        if(public_key.isCompressed()){
            System.out.println("Public key should not be compressed since private key isn't!");
        }
        if(decimal_key.length != 24){
            System.out.println("Invalid public key");
        }

        byte[] byte_key = public_key.toBin();

        if((byte_key.length != 65) && (byte_key[0] != 4)){
            System.out.println("Invalid byte representation!");
        }

        String hex_key = public_key.toHex();

        if((hex_key.length() != 130) && (!hex_key.substring(0, 1).equals("04"))){
            System.out.println("Invalid hex representation!");
        }

        String address = public_key.toAddress();

        if(!address.substring(0, 1).equals("1")){
            System.out.println("Invalid address!");
        }

        String script = public_key.makePubkeyScript();

        if(script.length() != 50){
            System.out.println("Invalid script!");
        }

        private_key.generatePrivateKey(true);
        public_key = new PublicKey(private_key);

        if(public_key.isValid()){
            System.out.println("Public key should not be valid!");
        }

        decimal_key = public_key.generatePubKey();

        if(!public_key.isValid()){
            System.out.println("Public key should be valid!");
        }
        if(!public_key.isCompressed()){
            System.out.println("Public key should be compressed since private key is!");
        }
        if(decimal_key.length != 24){
            System.out.println("Invalid public key");
        }

        byte_key = public_key.toBin();

        if((byte_key.length != 33) && (byte_key[0] != 2) && (byte_key[0] != 3)){
            System.out.println("Invalid byte representation!");
        }

        hex_key = public_key.toHex();

        if((hex_key.length() != 66) && (!hex_key.substring(0, 1).equals("02")) && (!hex_key.substring(0, 1).equals("03"))){
            System.out.println("Invalid hex representation!");
        }

        address = public_key.toAddress();

        if(!address.substring(0, 1).equals("1")){
            System.out.println("Invalid address!");
        }

        script = public_key.makePubkeyScript();

        if(script.length() != 50){
            System.out.println("Invalid script!");
        }

        PrivateKey pkey = new PrivateKey(network.get("bitcoin"));
        pkey.setHexKey("1111111111111111111111111111111111111111111111111111111111111111");
        hex_key = "044f355bdcb7cc0af728ef3cceb9615d90684bb5b2ca5f859ab0f0b704075871aa385b6b1b8ead809ca67454d9683fcf2ba03456d6fe2c4abe2b07f0fbdbb2f1c1";
        address = "1MsHWS1BnwMc3tLE8G35UXsS58fKipzB7a";
        String scriptPubKey = "76a914e4e517ee07984a4000cd7b00cbcb545911c541c488ac";

        PublicKey pubkey = new PublicKey(pkey);
        pubkey.generatePubKey();

        if(!pubkey.toHex().equals(hex_key)){
            System.out.println("Wrong public key generation!");
        }
        if(!pubkey.toAddress().equals(address)){
            System.out.println("Invalid address generation!");
        }
        if(!pubkey.makePubkeyScript().equals(scriptPubKey)){
            System.out.println("Invalid scriptPubKey generation!");
        }

        pkey.setCompressed(true);
        PublicKey pub = new PublicKey(pkey);
        pub.generatePubKey();
        hex_key = "034f355bdcb7cc0af728ef3cceb9615d90684bb5b2ca5f859ab0f0b704075871aa";
        address = "1Q1pE5vPGEEMqRcVRMbtBK842Y6Pzo6nK9";
        scriptPubKey = "76a914fc7250a211deddc70ee5a2738de5f07817351cef88ac";

        if(!pub.toHex().equals(hex_key)){
            System.out.println("Wrong compressed public key generation!");
        }
        if(!pub.toAddress().equals(address)){
            System.out.println("Invalid compressed address generation!");
        }
        if(!pub.makePubkeyScript().equals(scriptPubKey)){
            System.out.println("Invalid compressed scriptPubKEy generation!");
        }

        PrivateKey private2 = new PrivateKey(network.get("testnet"));
        private2.setHexKey("1111111111111111111111111111111111111111111111111111111111111111");
        private2.setCompressed(false);
        PublicKey pub2 = new PublicKey(private2);
        pub2.generatePubKey();

        if(!pub2.toHex().equals("044f355bdcb7cc0af728ef3cceb9615d90684bb5b2ca5f859ab0f0b704075871aa385b6b1b8ead809ca67454d9683fcf2ba03456d6fe2c4abe2b07f0fbdbb2f1c1")){
            System.out.println("Wrong public key generation!");
        }
        if((!pub2.toAddress().substring(0, 1).equals("m")) && !pub2.toAddress().substring(0, 1).equals("n")){
            System.out.println("Invalid address generation!");
        }
        if(!pub2.makePubkeyScript().equals("76a914e4e517ee07984a4000cd7b00cbcb545911c541c488ac")){
            System.out.println("Invalid scriptPubKey generation!");
        }
        private2.setCompressed(true);
        pub2 = new PublicKey(private2);
        pub2.generatePubKey();

        if(!pub2.toHex().equals(hex_key)){
            System.out.println("Wrong compressed public key generation!");
        }
        if((!pub2.toAddress().substring(0, 1).equals("m")) && !pub2.toAddress().substring(0, 1).equals("n")){
            System.out.println("Invalid address generation!");
        }
        if(!pub2.makePubkeyScript().equals(scriptPubKey)){
            System.out.println("Invalid compressed scriptPubKEy generation!");
        }
    }
}
