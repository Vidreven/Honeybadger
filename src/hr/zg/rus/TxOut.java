package hr.zg.rus;

/**
 * Created by hyperion on 8/18/17.
 */
public class TxOut {
    private int amount;
    private String scriptPubKey;

    public TxOut(){}

    public TxOut(String scriptPubKey){
        setScriptPubKey(scriptPubKey);
        setAmount(546);
    }

    public  TxOut(int satoshi, String scriptPubKey){
        setAmount(satoshi);
        setScriptPubKey(scriptPubKey);
    }

    public void setAmount(int amount) {
        if(amount < 546) throw new IllegalArgumentException("Minimum amount is 546 satoshi!");

        this.amount = amount;
    }

    public void setScriptPubKey(String scriptPubKey) {
        if((scriptPubKey.equals("")) || (scriptPubKey == null)) throw new IllegalArgumentException("Script must not be empty!");

        this.scriptPubKey = scriptPubKey;
    }

    public int getAmount() {
        return amount;
    }

    public String getScriptPubKey() {

        return scriptPubKey;
    }

    @Override
    public String toString(){
        int temp = Integer.reverseBytes(amount);
        String hex_amount = Integer.toHexString(temp);
        String padded_hex_amount = String.format("%-16s", hex_amount).replace(' ', '0');

        return padded_hex_amount + Integer.toHexString(scriptPubKey.length() >> 1) + scriptPubKey; // Adds 0x19 for script length
    }
}
