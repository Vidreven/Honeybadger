package hr.zg.rus;

/**
 * Created by evedili on 28.8.2017..
 */
public class OutPointTest {
    public static void main(String[] args) {
        String hash = "e3a2381092a5d05bd6219a44f4b7f8d9bf02c739dcc435fc6b452191ee10de6a";
        int index = 0;

        OutPoint op = new OutPoint(hash, index);

        String h = op.getHash();
        int n = op.getIndex();

        if(!hash.equals(h)){
            System.out.println("Input hash not equal to set hash!");
        }
        if(index != n){
            System.out.println("Input index not equal to set index!");
        }
    }
}
