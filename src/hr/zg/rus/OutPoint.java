package hr.zg.rus;

/**
 * Created by hyperion on 8/18/17.
 */
public class OutPoint {
    private String hash;
    private int n;

    public OutPoint(String hash, int n) {
        this.hash = hash;
        this.n = n;
    }

    public boolean equals(OutPoint other){

        return (this.hash.equals(other.hash)) && (this.n == other.n);
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
