import java.io.*;
import java.util.*;

class KeyValue {
    String key;
    Vector<String> values;

    KeyValue(String key, String value) {
        this.key = key;
        this.values = new Vector<String>();
        this.values.add(value);
    }

    KeyValue() {
        this.key = "";
        this.values = new Vector<String>();
    }
}

class MyHashMap {
    int size = 27061;
    KeyValue[] myHashMap = new KeyValue[size];

    MyHashMap() {
        for (int i = 0; i < size; i++) {
            myHashMap[i] = new KeyValue();
        }
    }

    int hashVal1(String s) {
        char chars[] = s.toCharArray();
        int p = 37;
        int powP = 1;
        int hashVal = 0;
        for (char ch : chars) {
            hashVal = (hashVal + Math.abs(ch - 'a' + 1) * powP) % size;
            powP = (powP * p) % size;
        }
        return hashVal;
    }

    int hashVal2(String s) {
        char chars[] = s.toCharArray();
        int p = 41;
        int powP = 1;
        int hashVal = 0;
        for (char ch : chars) {
            hashVal = (hashVal + Math.abs(ch - 'a' + 1) * powP) % size;
            powP = (powP * p) % size;
        }
        return hashVal;
    }

    int getIndex(String key) {
        int hash1 = hashVal1(key);
        int hash2 = hashVal2(key);
        int i = 0;
        while (!myHashMap[(hash1 + i * hash2) % size].key.equals(key)) {
            if (myHashMap[(hash1 + i * hash2) % size].key.equals("")) {
                return -1;
            }
            i++;
        }
        return (hash1 + i * hash2) % size;
    }

    void insert(String key, String value) {
        if (getIndex(key) == -1) { // key already not present
            KeyValue keyVal = new KeyValue(key, value);
            int hash1 = hashVal1(key);
            int hash2 = hashVal2(key);
            int i = 0;
            while (!myHashMap[(hash1 + i * hash2) % size].key.equals("")) {
                i++;
            }
            myHashMap[(hash1 + i * hash2) % size] = keyVal;
        } else { // key already present
            int index = getIndex(key);
            myHashMap[index].values.add(value);
        }
    }

    boolean ifPresent(String key) {
        if (getIndex(key) == -1)
            return false;
        else
            return true;
    }

    Vector<String> getValues(String key) {
        return myHashMap[getIndex(key)].values;
    }
}

public class Anagram {

    public static void getSubsequencePair(String input, String word1, String word2, Vector<Vector<String>> pairs,
            TreeSet<String> set) {
        if (input.length() == 0) {
            if (word1.length() >= 3 && word2.length() >= 3 && (!set.contains(word1))) {
                Vector<String> temp = new Vector<String>();
                set.add(word1);
                temp.add(word1);
                temp.add(word2);
                pairs.add(temp);
            }
            return;
        }
        getSubsequencePair(input.substring(1), word1 + input.charAt(0), word2, pairs, set);
        getSubsequencePair(input.substring(1), word1, word2 + input.charAt(0), pairs, set);
    }

    public static void main(String[] args) throws Exception {
        File vocab = new File(args[0]);
        File inp = new File(args[1]);
        Scanner vocabulary = new Scanner(vocab);
        int N = vocabulary.nextInt();
        MyHashMap hashMap = new MyHashMap();
        for (int i = 0; i < N; i++) {
            String value = vocabulary.next();
            if (value.length() > 12)
                continue;
            char[] charArray = value.toCharArray();
            Arrays.sort(charArray);
            String key = new String(charArray);
            hashMap.insert(key, value);
        }
        Scanner input = new Scanner(inp);
        int tc = input.nextInt();
        for (int t = 0; t < tc; t++) {
            String s = input.next();
            char[] charArray = s.toCharArray();
            Arrays.sort(charArray);
            s = new String(charArray);
            Vector<String> anagrams = new Vector<String>();
            if (s.length() < 3) {
                System.out.println(-1);
                continue;
            }
            // One word anagram
            if (hashMap.ifPresent(s)) {
                Vector<String> oneWord = hashMap.getValues(s);
                for (String val : oneWord) {
                    anagrams.add(val);
                }
            }
            // Two word anagram
            if (s.length() >= 6) {
                Vector<Vector<String>> pairs = new Vector<Vector<String>>();
                TreeSet<String> set = new TreeSet<String>();
                getSubsequencePair(s, "", "", pairs, set);
                for (Vector<String> pair : pairs) {
                    if (hashMap.ifPresent(pair.get(0)) && hashMap.ifPresent(pair.get(1))) {
                        Vector<String> val1 = hashMap.getValues(pair.get(0));
                        Vector<String> val2 = hashMap.getValues(pair.get(1));
                        for (String word1 : val1) {
                            for (String word2 : val2) {
                                anagrams.add(word1 + " " + word2);
                            }
                        }
                    }
                }
            }
            // Three word anagram
            if (s.length() >= 9) {
                Vector<Vector<String>> pairs = new Vector<Vector<String>>();
                TreeSet<String> set = new TreeSet<String>();
                getSubsequencePair(s, "", "", pairs, set);
                for (Vector<String> pair : pairs) {
                    if (hashMap.ifPresent(pair.get(0)) && pair.get(1).length() >= 6) {
                        Vector<Vector<String>> innerPairs = new Vector<Vector<String>>();
                        TreeSet<String> innerSet = new TreeSet<String>();
                        getSubsequencePair(pair.get(1), "", "", innerPairs, innerSet);
                        for (Vector<String> innerPair : innerPairs) {
                            if (hashMap.ifPresent(innerPair.get(0)) && hashMap.ifPresent(innerPair.get(1))) {
                                Vector<String> val1 = hashMap.getValues(pair.get(0));
                                Vector<String> val2 = hashMap.getValues(innerPair.get(0));
                                Vector<String> val3 = hashMap.getValues(innerPair.get(1));
                                for (String word1 : val1) {
                                    for (String word2 : val2) {
                                        for (String word3 : val3)
                                            anagrams.add(word1 + " " + word2 + " " + word3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Collections.sort(anagrams);
            for (String str : anagrams) {
                System.out.println(str);
            }
            System.out.println(-1);
        }
    }
}