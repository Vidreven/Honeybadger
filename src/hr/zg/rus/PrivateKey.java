package hr.zg.rus;

import io.nayuki.bitcoin.crypto.Int256Math;
import io.nayuki.bitcoin.crypto.Base58Check;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * Created by hyperion on 8/18/17.
 */
public class PrivateKey {
    private SecureRandom random;
    private byte[] private_key = new byte[32];
    private boolean valid;
    private boolean compressed;

    static final int[] ORDER = {0xD0364141, 0xBFD25E8C, 0xAF48A03B, 0xBAAEDCE6, 0xFFFFFFFE, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF};

    public PrivateKey(){
        valid = false;
        compressed = false;
    }

    // Returns private key as 32 bytes big endian.
    // Must be 0 < key < CurveOrder!
    public void generatePrivateKey(boolean compressed){

        random = new SecureRandom();

        random.nextBytes(private_key);

        while(!isValidPrivate(private_key)) {
            random.nextBytes(private_key);
        }
        valid = true;
        this.compressed = compressed;
    }

    public byte[] getPrivate_key() {
        if(!valid) throw new IllegalArgumentException("Private key not generated!");
        return private_key;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isCompressed() {
        return compressed;
    }

    // Returns private key as little endian uint256.
    public int[] toUit(){
        int[] val = new int[8];

        if(!valid) throw new IllegalArgumentException("Private key not generated!");

        Int256Math.bytesToUint(private_key, val, 0);

        return val;
    }

    // Big endian
    public String toHex(){
        int[] val;

        try{
            val = toUit();
        }
        catch (IllegalArgumentException iae){
            throw iae;
        }

        String hex;

        hex = Int256Math.uintToHex(val, 0);

        return hex;
    }

    public String toWIF(){
        byte[] priv;

        if(!valid) throw new IllegalArgumentException("Private key not generated!");

        if(!compressed){
            priv = new byte[33];
        }
        else{
            priv = new byte[34];
            priv[33] = (byte)0x01;
        }

        priv[0] = (byte)0x80;
        System.arraycopy(private_key, 0, priv, 1, 32);

        return Base58Check.bytesToBase58(priv);
    }

    public void setHexKey(String hexkey){
        if(isValid()) throw new IllegalArgumentException("Private key already generated. Cannot set new.");
        if(hexkey.length() != 64) throw new IllegalArgumentException("Invalid key!");

        int[] temp_uint = new int[8];

        Int256Math.hexToUint(hexkey, temp_uint, 0);
        private_key = Int256Math.uintToBytes(temp_uint, 0);

        if(!isValidPrivate(private_key)) throw new IllegalArgumentException("Private key out of range!");

        valid = true;

        if(hexkey.length() == 66){
            this.compressed = true;
        }
        else{
            this.compressed = false;
        }
    }

    public void setWIF(String wifkey){
        byte[] bytes;

        try{
            bytes = Base58Check.base58ToBytes(wifkey);
        }
        catch (IllegalArgumentException iae){
            throw iae;
        }

        System.arraycopy(bytes, 1, private_key, 0, 32);

        if(bytes.length == 33){
            compressed = false;
        }
        else if(bytes.length == 34){
            compressed = true;
        }

        valid = true;
    }

    public boolean equal(PrivateKey other){
        return this.compressed == other.compressed &&
                this.length() == other.length() &&
                Arrays.equals(private_key, other.getPrivate_key());
    }

    // Size of private key in bytes
    public int length(){
        return valid ? private_key.length : 0;
    }

    public String toString(){
        String str = toHex();
        if(compressed) str += "01";
        return str;
    }

    public void setCompressed(boolean compressed){
        if(isCompressed()) throw new IllegalArgumentException("Cannot change key compression!");
        this.compressed = compressed;
    }

    private boolean isValidPrivate(byte[] privkey){
        if(privkey.length > 32) return false;

        int[] decimal_privkey = new int[8];
        int zero;
        int[] val = new int[16];
        int diff;

        Int256Math.bytesToUint(privkey, decimal_privkey, 0);

        System.arraycopy(decimal_privkey, 0, val, 0, 8);

        zero = Int256Math.equalTo(val, 0, 8);

        if(zero == 1) return false;

        System.arraycopy(ORDER, 0, val, 0, 8);
        System.arraycopy(decimal_privkey, 0, val, 8, 8);
        diff = Int256Math.lessThan(val, 0, 8); // ORDER < privkey?

        if(diff == 1) return false;

        return true;
    }
}
