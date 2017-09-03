package hr.zg.rus;

/**
 * Created by evedili on 28.8.2017..
 */
public class OutPointTest {
    public static void main(String[] args) {
        String hash = "3231f0e349994ce41f2d40b6a011be4ea76f94dd897b5de128704fc0445111ab";
        int index = 0;
        String prevout_serialized = "ab115144c04f7028e15d7b89dd946fa74ebe11a0b6402d1fe44c9949e3f0313200000000";

        OutPoint op = new OutPoint(hash, index);

        String h = op.getHash();
        int n = op.getIndex();

        if(!hash.equals(h)){
            System.out.println("Input hash not equal to set hash!");
        }
        if(index != n){
            System.out.println("Input index not equal to set index!");
        }
        if(!op.toString().equals(prevout_serialized)){
            System.out.println("Invalid serialization!");
        }
    }
}
