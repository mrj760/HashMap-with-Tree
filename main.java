import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Main {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>(3);

        pl(map.insert(4, "four"));
        pl(map.get(4));

        pl(map.insert(4, "five"));
        pl(map.get(4));

        pl(map.insert(2, "two"));
        pl(map.get(2));

        pl(map.insert(7, "seven"));
        pl(map.get(7));

        pl(map.size());
        String balance = "{ ";
        for (int size : map.hashBalance()) {
            balance += "(" + size + ") ";
        }
        balance += "}";
        pl(balance);
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

        Tree() {
        }

        Tree(K key, V val) {
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
                int comp = key.compareTo(cur.key);
                if (comp < 0) {
                    if (cur.left == null) {
                        cur.left = new Node(key, val);
                        break;
                    }
                    cur = cur.left;
                } else if (comp > 0) {
                    if (cur.right == null) {
                        cur.right = new Node(key, val);
                        break;
                    }
                    cur = cur.right;
                } else {
                    return false;
                }
            }
            return true;
        }

        boolean contains(K key) {
            Node cur = head;
            while (true) {
                if (head == null) {
                    return false;
                }
                int comp = key.compareTo(cur.key);
                if (comp < 0) {
                    cur = cur.left;
                } else if (comp > 0) {
                    cur = cur.right;
                } else {
                    return true;
                }
            }
        }

        int size() {
            return size(head);
        }

        private int size(Node node) {
            if (node == null)
                return 0;
            return 1 + size(node.left) + size(node.right);
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
                } else if (comp > 0) {
                    cur = cur.right;
                } else {
                    return cur.val;
                }
            }
        }
    }

    private List<Tree> trees;
    final int hashSlots;
    private int size = 0;

    HashMap(int hashSlots) {
        if (hashSlots < 1)
            throw new IllegalArgumentException("HashSlots must be greater than 0");
        this.hashSlots = hashSlots;
        trees = new ArrayList<Tree>(hashSlots);
        for (int i = 0; i < hashSlots; ++i) {
            trees.add(new Tree());
        }
    }

    public boolean insert(K key, V val) {
        if (trees.get(key.hashCode() % hashSlots).insert(key, val)) {
            size++;
            return true;
        }
        return false;
    }

    public V get(K key) {
        return trees.get(key.hashCode() % hashSlots).get(key);
    }

    @Override
    public int compare(K key1, K key2) {
        return key1.compareTo(key2);
    }

    public int size() {
        // int size = 0;
        // for (Tree tree : trees) {
        //     size += tree.size();
        // }
        // return size;
        return this.size;
    }

    public int[] hashBalance() {
        int[] treeSizes = new int[hashSlots];
        for (int i = 0; i < hashSlots; ++i) {
            treeSizes[i] = trees.get(i).size();
        }
        return treeSizes;
    }
}