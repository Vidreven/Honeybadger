package hr.zg.rus;

public class TxInTest {
    public static void main(String[] args) {
        OutPoint op = new OutPoint("3231f0e349994ce41f2d40b6a011be4ea76f94dd897b5de128704fc0445111ab", 0);
        String input_serialized = "ab115144c04f7028e15d7b89dd946fa74ebe11a0b6402d1fe44c9949e3f03132000000001aaffffffff";

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
        if(!in1.toString().equals(input_serialized)){
            System.out.println("Invalid input serialization!");
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
