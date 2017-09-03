package hr.zg.rus;

/**
 * Created by evedili on 29.8.2017..
 */
public class TxTest {
    public static void main(String[] args) {
        String scriptSig1 = "483045022100b1ae2f0e2639e0382189bb4fa547b9a23fcafdd25bfd2f1ea853222993c511e30220181d1cb60ffe1674da575cc109c3e990fc7a2e6d476faad10bad12b139edbe060147304402203fa8a58ed49b09e3a5326ffd84efb06b74d2ee4c334089840c5b400f5ebec6090220045664a82a1b76ecbb451a047c023a1805c45c3a058c4955554e06b9d220b108014c69522102178affc15573e78bb054ca53479318fb28e8332020a39542129c40745d0edd8a2102c2044225923f27ba829611ca1f7baf754a7a0981fc0fe289349fbacd8db0d70121034f92892a65258d8f9de0b324e4446688b4ad929fb93f0891bb3db9bf933ddd3853ae";
        TxOut out0 = new TxOut(20_000, "76a91496c8fe3196453eb48aaad26d5e1b96a8681e93c188ac");
        OutPoint prevout = new OutPoint("3231f0e349994ce41f2d40b6a011be4ea76f94dd897b5de128704fc0445111ab", 0);
        TxIn in0 = new TxIn(prevout, scriptSig1);

        TxIn[] ins = {in0};
        TxOut[] outs = {out0};

        Tx tx1 = new Tx(ins, outs);

        if(tx1.getIns()[0] != ins[0]){
            System.out.println("Invalid input!");
        }
        if(tx1.getOuts()[0] != outs[0]){
            System.out.println("Invalid output!");
        }
        if(!tx1.getLocktime().equals("00000000")){
            System.out.println("Invalid locktime!");
        }

        Tx tx2 = new Tx(ins, outs, "11111111");

        if(tx2.getIns()[0] != ins[0]){
            System.out.println("Invalid input!");
        }
        if(tx2.getOuts()[0] != outs[0]){
            System.out.println("Invalid output!");
        }
        if(!tx2.getLocktime().equals("11111111")){
            System.out.println("Invalid locktime!");
        }

        PrivateKey private_key = new PrivateKey();
        private_key.setHexKey("1111111111111111111111111111111111111111111111111111111111111111");

        PublicKey public_key = new PublicKey(private_key);
        public_key.generatePubKey();

        String sendAddress = "1p7XohmUr4QY1BVLdvxgUmoWduWhEz8qg";

        String scriptPubKey = public_key.makePubkeyScript(sendAddress);

        int amount = 100_000;

        TxOut out1 = new TxOut(amount, scriptPubKey);
        TxIn in1 = new TxIn(prevout, scriptPubKey);

        TxIn[] inputs = {in1};
        TxOut[] outputs = {out1};

        Tx transaction = new Tx(inputs, outputs);

        transaction = transaction.signTx(private_key);
        System.out.println(transaction);
    }
}
