import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Main {
    public static void main (String[] args) {
        HashMap<Integer, String> map = new HashMap<>(3);

        pl(map.insert(4, "four"));
        pl(map.get(4));

        pl(map.insert(4, "five"));
        pl(map.get(4));
    }

    public static void pl(Object o) {
        System.out.println(o.toString());
    }
}

class HashMap<K extends Comparable<K>, V> implements Comparator<K> {

    private class Tree {

        class Node {
            Node left;
            Node right;
            K key;
            V val;
            Node(K key, V val) {
                this.key = key;
                this.val = val;
            }
        }
    
        Node head;
        Tree() {}
    
        Tree (K key, V val) {
            head = new Node(key, val);
        }

        // Return false if the element already exists
        boolean insert(K key, V val) {
            if (head == null) {
                head = new Node(key, val);
                return true;
            }
            Node cur = head;
            while (true) {
                int comp = key.compareTo(key);
                if (comp < 0) {
                    if (cur.left == null) {
                        cur.left = new Node(key, val);
                        break;
                    }
                    cur = cur.left;
                }
                else if (comp > 0) {
                    if (cur.right == null) {
                        cur.right = new Node(key, val);
                        break;
                    }
                    cur = cur.right;
                }
                else {
                    return false;
                }
            }
            return true;
        }
    
        // return null if does not exist
        V get(K key) {
            Node cur = head;
            while (true) {
                if (cur == null) {
                    return null;
                }
                int comp = key.compareTo(cur.key);
                if (comp < 0) {
                    cur = cur.left;
                }
                else if (comp > 0) {
                    cur = cur.right;
                }
                else {
                    return cur.val;
                }
            }
        }
    }

    private List<Tree> trees;
    final int size;

    HashMap (int size) {
        if (size < 1) throw new IllegalArgumentException("Size must be greater than 0");
        this.size = size;
        trees = new ArrayList<Tree>(size);
        for (int i = 0; i < size; ++i) {
            trees.add(new Tree());
        }
    }

    boolean insert(K key, V val) {
        return trees.get(key.hashCode() % size).insert(key, val);
    }
    
    V get(K key) {
        return trees.get(key.hashCode() % size).get(key);
    }

    @Override
        public int compare(K key1, K key2) {
            return key1.compareTo(key2);
        }
}