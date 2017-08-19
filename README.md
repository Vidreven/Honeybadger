Java library for creating and signing Bitcoin trnasactions based on [Nayuki's Bitcoin cryptography library](https://github.com/nayuki/Bitcoin-Cryptography-Library)

Example usage:

PrivateKey privateKey = new PrivateKey();

//privateKey.generatePrivateKey(true);

privateKey.setHexKey("1111111111111111111111111111111111111111111111111111111111111111");
privateKey.setCompressed(false);

PublicKey publicKey = new PublicKey(privateKey);
publicKey.generatePubKey();
String sender = "76a91408e9008a6e4b0214a5918b3b9dc00e4ad28fabf488ac";

TxOut out0 = new TxOut(100_000, sender);
OutPoint op0 = new OutPoint("3231f0e349994ce41f2d40b6a011be4ea76f94dd897b5de128704fc0445111ab", 0);

TxIn in0 = new TxIn(op0, sender);

TxIn[] inputs = {in0};
TxOut[] outputs = {out0};
Tx tx = new Tx(inputs, outputs);

tx = tx.signTx(tx, privateKey);

System.out.println(tx.toString());