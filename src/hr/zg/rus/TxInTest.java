package hr.zg.rus;

/**
 * Created by evedili on 28.8.2017..
 */
public class TxInTest {
    public static void main(String[] args) {
        OutPoint op = new OutPoint("e3a2381092a5d05bd6219a44f4b7f8d9bf02c739dcc435fc6b452191ee10de6a", 0);

        TxIn in1 = new TxIn(op, "aa");

        if(!op.equals(in1.getPrevout())){
            System.out.println("Invalid previous output!");
        }
        if(!in1.getScriptSig().equals("aa")){
            System.out.println("Invalid scriptSig!");
        }
        if(!in1.getSequence().equals("ffffffff")){
            System.out.println("Invalid sequence!");
        }

        TxIn in2 = new TxIn(op, "aa", "fefefefe");

        if(!op.equals(in2.getPrevout())){
            System.out.println("Invalid previous output!");
        }
        if(!in2.getScriptSig().equals("aa")){
            System.out.println("Invalid scriptSig!");
        }
        if(!in2.getSequence().equals("fefefefe")){
            System.out.println("Invalid sequence!");
        }
    }
}
