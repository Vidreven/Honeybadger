package hr.zg.rus;

import java.util.Hashtable;

public class TxTest {
    public static void main(String[] args) {
        Networks networks = new Networks();
        Hashtable<String, Hashtable> network = networks.getNetworks();

        String scriptSig1 = "483045022100ca178f82e897282419873e60003972f3874b78eaa2185a02979e18fdf2d0654c022008bb703f32dc5457118e08331d3e8ef548bd3479bf963408e5c9bb6754a9fc8a0141044f355bdcb7cc0af728ef3cceb9615d90684bb5b2ca5f859ab0f0b704075871aa385b6b1b8ead809ca67454d9683fcf2ba03456d6fe2c4abe2b07f0fbdbb2f1c1";
        TxOut out0 = new TxOut(100_000, "76a914e4e517ee07984a4000cd7b00cbcb545911c541c488ac");
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

        PrivateKey private_key = new PrivateKey(network.get("bitcoin"));
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
        String tx_serialized = "0200000001ab115144c04f7028e15d7b89dd946fa74ebe11a0b6402d1fe44c9949e3f03132000000008b" + scriptSig1
                + "ffffffff01a0860100000000001976a91408e9008a6e4b0214a5918b3b9dc00e4ad28fabf488ac00000000";

        if(!transaction.toString().equals(tx_serialized)){
            System.out.println("Wrong signature!");
        }

        private_key.setCompressed(true);
        public_key = new PublicKey(private_key);
        public_key.generatePubKey();

        transaction = transaction.signTx(private_key);

        if(transaction.toString().length() != 384){
            System.out.println("Invalid compressed public key transaction signature!");
        }

        PrivateKey pkey = new PrivateKey(network.get("testnet"));
        pkey.setHexKey("1111111111111111111111111111111111111111111111111111111111111111");
        public_key = new PublicKey(pkey);
        public_key.generatePubKey();
        transaction = transaction.signTx(pkey);

        if(!transaction.toString().equals(tx_serialized)){
            System.out.println("Wrong testnet signature!");
        }

        pkey.setCompressed(true);
        public_key = new PublicKey(pkey);
        public_key.generatePubKey();
        transaction = transaction.signTx(pkey);

        if(transaction.toString().length() != 384){
            System.out.println("Invalid compressed public key testnet signature!");
        }
    }
}
