package hr.zg.rus;

import io.nayuki.bitcoin.crypto.*;

import java.util.Arrays;

/**
 * Created by hyperion on 8/18/17.
 */
public class PublicKey {
    private int[] val = new int[3 * 8 + 1 * 8 + CurvePointMath.MULTIPLY_TEMP_WORDS];
    private int[] public_key = new int[24];
    private boolean valid;
    private boolean compressed;
    private PrivateKey private_key;

    // Should accept PrivateKey as argument.
    public PublicKey(PrivateKey privkey){
        if(!privkey.isValid())
            throw new IllegalArgumentException("Generate private key before creating public key");

        valid = false;
        this.private_key = privkey;
        this.compressed = privkey.isCompressed();
    }

    // Returns public key in little endian.
    // point = [x, y, z]
    // x = little endian
    // y = little endian
    // z = little endian
    // Must be on curve!
    public int[] generatePubKey(){

        int[] decimal_key = new int[24];

        try {
            decimal_key = private_key.toUit();
        }
        catch (IllegalArgumentException iae){
            System.out.println(iae);
        }

        decimal_key = reverseArray(decimal_key); //store private key as little endian.
        int[] basepoint = CurvePointMath.getBasePoint();

        // Copy the base point into the val array
        System.arraycopy(basepoint, 0, val, 0, 3 * 8 );

        // Copy the number to multiply the base point with (private key)
        System.arraycopy(decimal_key, 0, val, 3 * 8, 8);

        CurvePointMath.multiply(val, 0,  3 * 8, 32);
        CurvePointMath.normalize(val, 0, 32);

        if(CurvePointMath.isOnCurve(val, 0, 24) == 0)
            throw new IllegalArgumentException("Error calculating public key! Generate a new private key.");

        public_key = Arrays.copyOfRange(val, 0, 24);
        valid = true;

        return public_key;
    }

    // Big endian
    public byte[] toBin(){
        byte[] byte_pubkey;
        int header;

        if(!valid) throw new IllegalArgumentException("Generate a public key!");

        // Parses coordinates from public_key
        byte[] XCOORD = Int256Math.uintToBytes(public_key, 0);

        if(!compressed){
            byte_pubkey = new byte[65];

            header = 4;
            byte_pubkey[0] = (byte)header;

            byte[] YCOORD = Int256Math.uintToBytes(public_key, 8);
            System.arraycopy(YCOORD, 0, byte_pubkey, 33, 32);
        }
        else {
            byte_pubkey = new byte[33];

            header = getHeader();
            byte_pubkey[0] = (byte)header;
        }

        System.arraycopy(XCOORD, 0, byte_pubkey, 1, 32);


        return byte_pubkey;
    }

    // Big endian
    public String toHex(){
        int header;
        String hex_pubkey = new String();

        if(!valid) throw new IllegalArgumentException("Generate a public key!");

        String XCOORD = Int256Math.uintToHex(public_key, 0);

        if(!compressed) {
            header = 4;

            String YCOORD = Int256Math.uintToHex(public_key, 8);
            hex_pubkey = "0" + Integer.toHexString(header) + XCOORD + YCOORD;
        }
        else{
            header = getHeader();

            hex_pubkey = "0" + Integer.toHexString(header) + XCOORD;
        }

        return hex_pubkey;
    }

    public String toAddress(){
        byte[] byte_pubkey;

        byte_pubkey = toBin();
        Sha256Hash sha;

        sha = Sha256.getHash(byte_pubkey);
        byte[] rmdsha;

        rmdsha = Ripemd160.getHash(sha.toBytes());
        byte[] raw_key = new byte[21];
        raw_key[0] = 0;
        System.arraycopy(rmdsha, 0, raw_key, 1, 20);

        return Base58Check.bytesToBase58(raw_key);
    }

    public String makePubkeyScript(String address){
        byte[] raw_script = Base58Check.base58ToBytes(address); // version byte + PKH
        byte[] raw_pkh = new byte[32];

        System.arraycopy(raw_script, 1, raw_pkh, 12, 20);
        int[] temp = new int[8];
        Int256Math.bytesToUint(raw_pkh, temp, 0);
        String hex_key = Int256Math.uintToHex(temp, 0);
        hex_key = hex_key.substring(24, hex_key.length());

        return "76a914" + hex_key + "88ac";
    }

    public String makePubkeyScript(){
        String address = toAddress();
        byte[] raw_script = Base58Check.base58ToBytes(address); // version byte + PKH
        byte[] raw_pkh = new byte[32];

        System.arraycopy(raw_script, 1, raw_pkh, 12, 20);
        int[] temp = new int[8];
        Int256Math.bytesToUint(raw_pkh, temp, 0);
        String hex_key = Int256Math.uintToHex(temp, 0);
        hex_key = hex_key.substring(24, hex_key.length());

        return "76a914" + hex_key + "88ac";
    }

    public boolean isValid() {
        return valid;
    }

    public int[] getPublic_key() {
        if(!valid) throw new IllegalArgumentException("Public key not generated!");
        return public_key;
    }

    public boolean equal(PublicKey other){
        return Arrays.equals(this.public_key, other.getPublic_key());
    }

    public boolean isCompressed() {
        return compressed;
    }

    public String toString(){
        return toHex();
    }

    private int[] reverseArray(int[] arr){
        int size = arr.length;
        int[] temp = new int[size];

        for (int i = 0; i < arr.length; i++) {
            temp[size-1-i] = arr[i];
        }

        return temp;
    }

    private int getHeader(){
        int header;

        header = Integer.reverseBytes(public_key[8]); // Last byte of Y is stored here
        header = 2 + Math.abs((header % 2));

        return header;
    }
}
