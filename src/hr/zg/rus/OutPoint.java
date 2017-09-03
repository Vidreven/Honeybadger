package hr.zg.rus;

/**
 * Created by hyperion on 8/18/17.
 */
public class OutPoint {
    private String hash;
    private int n;

    public OutPoint(String hash, int n) {
        setHash(hash);
        setIndex(n);
    }

    public boolean equals(OutPoint other){

        return (this.hash.equals(other.hash)) && (this.n == other.n);
    }

    public String getHash() {
        return hash;
    }

    public int getIndex() {
        return n;
    }

    public void setHash(String hash) {
        if(hash.length() != 64) throw new IllegalArgumentException("Invalid transaction hash!");
        this.hash = hash;
    }

    public void setIndex(int n) {
        if(n < 0) throw new IllegalArgumentException("Index must be > 0!");
        this.n = n;
    }

    @Override
    public String toString(){
        String hex_n = Integer.toHexString(n); // does not use varint
        hex_n = String.format("%8s", hex_n).replace(' ', '0');

        char[] hash_chars = new char[64];
        char[] temp = new char[64];
        hash.getChars(0, hash.length(), hash_chars, 0);

        for (int i = 0; i < hash_chars.length; i= i + 2) {
            temp[temp.length - 1 - i] = hash_chars[i + 1];
            temp[temp.length - 1 - i - 1] = hash_chars[i];
        }

        String hash = new String(temp);

        return hash + hex_n;
    }
}
