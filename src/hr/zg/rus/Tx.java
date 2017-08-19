package hr.zg.rus;

import io.nayuki.bitcoin.crypto.Ecdsa;
import io.nayuki.bitcoin.crypto.Int256Math;
import io.nayuki.bitcoin.crypto.Sha256;
import io.nayuki.bitcoin.crypto.Sha256Hash;

import java.util.Arrays;

/**
 * Created by hyperion on 8/18/17.
 */
public class Tx {

    public static String VERSION = "02000000";
    private TxIn[] ins;
    private TxOut[] outs;
    public String locktime;

    public Tx(TxIn[] ins, TxOut[] outs){
        this.ins = ins;
        this.outs = outs;
        locktime = "00000000";
    }

    public Tx(TxIn[] ins, TxOut[] outs, String locktime){
        this.ins = ins;
        this.outs = outs;
        this.locktime = locktime;
    }

    public static Tx newInstance(Tx tx){
        return new Tx(tx.getIns(), tx.getOuts(), tx.getLocktime()); // used for deep copying
    }

    public TxIn[] getIns() {
        return ins;
    }

    public void setIns(TxIn[] ins) {
        this.ins = ins;
    }

    public TxOut[] getOuts() {
        return outs;
    }

    public void setOuts(TxOut[] outs) {
        this.outs = outs;
    }

    public String getLocktime() {
        return locktime;
    }

    public void setLocktime(String locktime) {
        this.locktime = locktime;
    }

    @Override
    public String toString(){
        String output = "";
        output += VERSION;

        int in_size = ins.length;
        output += String.format("%2s", Integer.toHexString(in_size)).replace(" ", "0");

        for (TxIn in : this.ins){
            output += in.toString();
        }

        int out_size = outs.length;
        output += String.format("%2s", Integer.toHexString(out_size)).replace(" ", "0");;
        for(TxOut out : this.outs){
            output += out.toString();
        }

        output += locktime;

        return output;
    }

    public Tx signTx(Tx tx, PrivateKey private_key){
        TxIn[] inputs = tx.getIns();

        for (int i = 0; i<inputs.length; i++) {
            tx = sign_input(tx, i, private_key);
        }

        return tx;
    }

    private Tx sign_input(Tx tx, int i, PrivateKey private_key){
        String sig;
        int sig_length;
        int pub_length;
        String scriptSig;
        String x, y, pubkey_string;

        PublicKey public_key = new PublicKey(private_key);
        public_key.generatePubKey();
        int[] pubkey = public_key.getPublic_key();

        //String address = public_key.toAddress();
        String script_pubkey = public_key.makePubkeyScript();
        Tx tx_copy = Tx.newInstance(tx);

        Tx signing_tx = signatureForm(tx_copy, i, script_pubkey);
        sig = sign(signing_tx, private_key);
        sig_length = sig.length() >> 1;

        x = Int256Math.uintToHex(pubkey, 0);
        y = Int256Math.uintToHex(pubkey, 8);
        pubkey_string = "04" + x + y;
        pub_length = pubkey_string.length() >> 1;

        scriptSig = Integer.toHexString(sig_length) + sig + Integer.toHexString(pub_length) + pubkey_string;
        tx_copy.getIns()[i].setScriptSig(scriptSig);

        return tx_copy;
    }

    private Tx signatureForm(Tx tx, int i, String script){
        TxIn[] inputs = tx.getIns();

        for(TxIn in : inputs){
            in.setScriptSig("");
        }

        inputs[i].setScriptSig(script);
        tx.setIns(inputs);

        return tx;
    }

    private String sign(Tx tx, PrivateKey priv){
        String hashcode = "01"; //supports only sighashall
        int[] outR = new int[8];
        int[] outS = new int[8];
        boolean signed = false;
        String result;

        String temp = tx.toString() + String.format("%-8s", hashcode).replace(' ', '0');

        byte[] to_hash = hexToBytes(temp);

        Sha256Hash sha = Sha256.getDoubleHash(to_hash);

        byte[] nonce = deterministic_generate_k(sha.toBytes(), priv.getPrivate_key());
        Int256Math.bytesToUint(nonce, outR, 0); //reusing outR

        while(!signed) {
            try {
                signed = Ecdsa.sign(priv.toUit(), sha, outR, outR, outS);
            } catch (IllegalArgumentException iae) {
                System.out.println(iae.toString());
            }
        }

        result = encodeSig(outR, outS);
        return result + hashcode;
    }

    // Encode to DER
    private String encodeSig(int[] R, int[] S){
        String v = "30";
        String type = "02";
        String R_string = Int256Math.uintToHex(R, 0);
        String S_string = Int256Math.uintToHex(S, 0);
        String prefix;
        int total_length;
        int R_length;
        int S_length;

        prefix = R_string.substring(0, 2);
        if(Integer.parseInt(prefix, 16) >= 0x80){
            R_string = "00" + R_string;
        }

        prefix = S_string.substring(0, 2);
        if(Integer.parseInt(prefix, 16) >= 0x80){
            S_string = "00" + S_string;
        }

        R_length = R_string.length() >> 1;
        S_length = S_string.length() >> 1;
        total_length = 4 + R_length + S_length;

        return v + Integer.toHexString(total_length) + type + Integer.toHexString(R_length) + R_string + type + Integer.toHexString(S_length) + S_string;
    }

    // https://tools.ietf.org/html/rfc6979#section-3.2
    private byte[] deterministic_generate_k(byte[] msghash, byte[] privkey){
        byte[] value = new byte[32];
        byte[] key = new byte[32];
        byte[] value_temp = new byte[value.length + 1 + privkey.length + msghash.length];

        Arrays.fill(value, (byte)1);

        System.arraycopy(value, 0, value_temp, 0, value.length);
        System.arraycopy(privkey, 0, value_temp, value.length + 1, privkey.length);
        System.arraycopy(msghash, 0, value_temp, 65, msghash.length);

        Sha256Hash key_hmac = Sha256.getHmac(key, value_temp);
        key = key_hmac.toBytes();

        Sha256Hash value_hmac = Sha256.getHmac(key, value);
        value = value_hmac.toBytes();

        System.arraycopy(value, 0, value_temp, 0, value.length);
        System.arraycopy(privkey, 0, value_temp, value.length + 1, privkey.length);
        System.arraycopy(msghash, 0, value_temp, 65, msghash.length);
        value_temp[32] = (byte)1;

        key_hmac = Sha256.getHmac(key, value_temp);
        key = key_hmac.toBytes();

        value_hmac = Sha256.getHmac(key, value);
        value = value_hmac.toBytes();

        return Sha256.getHmac(key, value).toBytes();
    }

    private byte[] hexToBytes(String s) {
        if (s.length() % 2 != 0)
            throw new IllegalArgumentException();

        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < s.length(); i += 2) {
            if (s.charAt(i) == '+' || s.charAt(i) == '-')
                throw new IllegalArgumentException();
            b[i / 2] = (byte)Integer.parseInt(s.substring(i, i + 2), 16);
        }
        return b;
    }
}
