package hr.zg.rus;

/**
 * Created by hyperion on 8/18/17.
 */
public class TxIn {
    private OutPoint prevout;
    private String scriptSig;
    private String sequence;

    public TxIn(OutPoint prevout, String scriptSig){
        setOutPoint(prevout);
        setScriptSig(scriptSig);
        setSequence("ffffffff");
    }

    public TxIn(OutPoint prevout, String scriptSig, String sequence){
        setOutPoint(prevout);
        setScriptSig(scriptSig);
        setSequence(sequence);
    }

    public void setSequence(String sequence) {
        if(sequence.length() != 8) throw new IllegalArgumentException("Sequence must be 8 characters long!");
        Long.decode("0x" + sequence);

        this.sequence = sequence;
    }

    public void setScriptSig(String scriptSig) {
        if(scriptSig == null) throw new IllegalArgumentException("Invalid scriptSig!");
        this.scriptSig = scriptSig;
    }

    public void setOutPoint(OutPoint prevout){
        if(prevout == null) throw new IllegalArgumentException("Previous outpoint cannot be empty!");
        this.prevout = prevout;
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

    @Override
    public String toString(){
        return prevout.toString() + Integer.toHexString(scriptSig.length() >> 1) + scriptSig + sequence;
    }
}
