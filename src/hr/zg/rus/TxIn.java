package hr.zg.rus;

/**
 * Created by hyperion on 8/18/17.
 */
public class TxIn {
    private OutPoint prevout;
    private String scriptSig;
    private String sequence;

    public TxIn(OutPoint prevout, String scriptSig){
        this.prevout = prevout;
        this.scriptSig = scriptSig;
        sequence = "ffffffff";
    }

    public TxIn(OutPoint prevout, String scriptSig, String sequence){
        this.prevout = prevout;
        this.scriptSig = scriptSig;
        this.sequence = sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public OutPoint getPrevout() {
        return prevout;
    }

    public String getScriptSig() {

        return scriptSig;
    }

    public String getSequence() {

        return sequence;
    }

    public void setScriptSig(String scriptSig) {
        this.scriptSig = scriptSig;
    }

    @Override
    public String toString(){
        return prevout.toString() + Integer.toHexString(scriptSig.length() >> 1) + scriptSig + sequence;
    }
}
