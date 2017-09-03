package hr.zg.rus;

/**
 * Created by evedili on 23.8.2017..
 */
public class TxOutTest {
    public static void main(String[] args) {
        int amount;
        String script;
        String scriptPubKey = "76a91496c8fe3196453eb48aaad26d5e1b96a8681e93c188ac";

        TxOut out0 = new TxOut();

        amount = out0.getAmount();

        if(amount != 0){
            System.out.println("Invalid amount!");
        }

        script = out0.getScriptPubKey();

        if (!(script == null)){
            System.out.println("Invalid script!");
        }

        out0.setAmount(100_000);
        amount = out0.getAmount();

        if(amount != 100_000){
            System.out.println("Set amount not equal to stored amount!");
        }

        script = out0.getScriptPubKey();

        if (!(script == null)){
            System.out.println("Invalid script!");
        }

        out0.setScriptPubKey(scriptPubKey);

        if (out0.getScriptPubKey() == null){
            System.out.println("Script should not be empty after setting!");
        }

        TxOut out1 = new TxOut(scriptPubKey);
        String out1_serialized = "220200000000000019" + scriptPubKey;

        if(out1.getScriptPubKey() == null){
            System.out.println("Script should not be empty!");
        }
        if(out1.getAmount() < 546){
            System.out.println("Amount should be at leas 546 satoshi!");
        }
        if(!out1.toString().equals(out1_serialized)){
            System.out.println("Wrong output serialization!");
        }

        TxOut out2 = new TxOut(100_000, scriptPubKey);
        String out2_serialized = "a08601000000000019" + scriptPubKey;

        if(out2.getScriptPubKey() == null){
            System.out.println("Script should not be empty!");
        }
        if(out2.getAmount() < 546){
            System.out.println("Amount should be at leas 546 satoshi!");
        }
        if(!out2.toString().equals(out2_serialized)){
            System.out.println("Wrong output serialization!");
        }
    }
}
