import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Main {
    public static void main(String[] args) {
        HashMap<Integer, String> map = new HashMap<>(3);

        insertAndTest(map, 4, "four");
        insertAndTest(map, 4, "five");
        insertAndTest(map, 7, "seven");
        insertAndTest(map, 10, "ten");
        insertAndTest(map, 13, "thirteen");
        insertAndTest(map, 16, "sixteen");

        insertAndTest(map, 20, "twenty");
        insertAndTest(map, 17, "seventeen");
        insertAndTest(map, 14, "fourteen");
        insertAndTest(map, 11, "eleven");
        insertAndTest(map, 8, "eight");

        pl(map.size());
        String balance = "{ ";
        for (int size : map.hashBalance()) {
            balance += "(" + size + ") ";
        }
        balance += "}";
        pl(balance);

        pl(map);
    }

    private static void insertAndTest(HashMap<Integer, String> map, int i, String s) {
        pl(map.insert(i, s));
        pl(map.get(i));
    }

    public static void pl(Object o) {
        if (o == null) {
            System.out.println("NULL");
            return;
        }
        System.out.println(o.toString());
    }
}

class HashMap<K extends Comparable<K>, V> implements Comparator<K> {

    private class Tree {

        class Node {
            Node left;
            Node right;
            Node parent;
            K key;
            V val;
            int lh, rh;

            Node(K key, V val) {
                this.key = key;
                this.val = val;
            }

            @Override
            public String toString() {
                return "{ K:(" + key + "), V:(" + val + "), LH:" + lh + ", RH:" + rh + "}";
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

            // Main.pl("Inserting: {" + key + "," + val + "}");

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
                        cur.left.parent = cur;
                        backtrackAfterInsertion(cur.left);
                        break;
                    }
                    cur = cur.left;
                } else if (comp > 0) {
                    if (cur.right == null) {
                        cur.right = new Node(key, val);
                        cur.right.parent = cur;
                        backtrackAfterInsertion(cur.right);
                        break;
                    }
                    cur = cur.right;
                } else {
                    return false;
                }
            }
            return true;
        }

        void backtrackAfterInsertion(Node n) {
            boolean cameFromLeft = n.key.compareTo(n.parent.key) < 0;
            n = n.parent;
            int balance;
            boolean rotated = false;

            while (true) {

                if (cameFromLeft) {
                    if (!rotated) {
                        n.lh++;
                    } else {
                        rotated = false;
                    }
                } else /* Came From Right */ {
                    if (!rotated) {
                        n.rh++;
                    } else {
                        rotated = false;
                    }
                }
                balance = n.lh - n.rh;
                if (balance < -1) {
                    rotateLeft(n);
                    rotated = true;
                } else if (balance > 1) {
                    rotateRight(n);
                    rotated = true;
                }

                if (n == head) {
                    break;
                }

                cameFromLeft = n.key.compareTo(n.parent.key) < 0;
                n = n.parent;
            }
        }

        void rotateLeft(Node n) {
            // Main.pl("before rot: " + n + n.left + n.right + n.right.left + n.right.right);
            Node temp = n;
            n = n.right;
            Node temp2 = n.left;
            n.left = temp;
            n.parent = temp.parent;

            temp.parent = n;
            temp.right = temp2;
            if (temp2 != null) {
                temp2.parent = n;
            }

            updateHeight(temp);
            updateHeight(n);
            updateHeight(n.parent);

            // Main.pl("after rot: " + n + n.left + n.right);

            if (temp == head) {
                head = n;
            } else {
                int comp = n.key.compareTo(n.parent.key);
                if (comp < 0) {
                    n.parent.left = n;
                } else {
                    n.parent.right = n;
                }
            }
        }

        void rotateRight(Node n) {
            Node temp = n;
            n = n.left;
            Node temp2 = n.right;
            n.right = temp;
            n.parent = temp.parent;
            temp.parent = n;
            temp.left = temp2;
            if (temp2 != null) {
                temp2.parent = n;
            }

            updateHeight(temp);
            updateHeight(n);
            updateHeight(n.parent);

            if (temp == head) {
                head = n;
            } else {
                int comp = n.key.compareTo(n.parent.key);
                if (comp < 0) {
                    n.parent.left = n;
                } else {
                    n.parent.right = n;
                }
            }
        }

        void updateHeight(Node n) {
            if (n == null) {
                return;
            }
            if (n.left != null) {
                int max = n.left.lh > n.left.rh ? n.left.lh : n.left.rh;
                n.lh = max + 1;
            } else {
                n.lh = 0;
            }

            if (n.right != null) {
                int max = n.right.lh > n.right.rh ? n.right.lh : n.right.rh;
                n.rh = max + 1;
            } else {
                n.rh = 0;
            }
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

        @Override
        public String toString() {
            if (head == null) {
                return "Empty Tree";
            }
            return toString(head);
        }

        private String toString(Node n) {
            if (n.left == null && n.right == null) {
                return n.toString();
            } else if (n.left == null) {
                return n.toString() + toString(n.right);
            } else if (n.right == null) {
                return n.toString() + toString(n.left);
            }
            return n.toString() + toString(n.left) + toString(n.right);
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

    @Override
    public String toString() {
        String s = "";
        for (Tree t : trees) {
            s += t.toString() + "\n";
        }
        return s;
    }
}