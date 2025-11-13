import java.util.*;
import java.io.*;
import java.math.BigInteger;

public class OptimalArrangement {

    // record for an item on land with label and weight
    static class Item {
        String label;
        int weight;
        int pos;

        Item(String label, int weight, int pos) { // constructor
            this.label = label;
            this.weight = weight;
            this.pos = pos;
        }
    }

    static class Result {
        long minCost;
        String order;

        Result(long minCost, String order) {
            this.minCost = minCost;
            this.order = order;
        }
    }

    // BigInteger to stay safe for large permutations
    private static final List<BigInteger> Fact = new ArrayList<>();
    static {
        Fact.add(BigInteger.ONE); // 0!
    }
    private static void growFact(int n) {
        for (int i = Fact.size(); i <= n; i++) {
            Fact.add(Fact.get(i - 1).multiply(BigInteger.valueOf(i)));
        }
    }
    private static BigInteger fact(int n) {
        growFact(n);
        return Fact.get(n);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine().trim());
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] parts = br.readLine().trim().split("\\s+");
            String label = parts[0];
            int weight = Integer.parseInt(parts[1]);
            items.add(new Item(label, weight, i + 1));
        }
        int k = Integer.parseInt(br.readLine().trim());

        Result res = bestArrangement(items, k);

        System.out.println(res.minCost);
        System.out.println(res.order);
    }

    private static Result bestArrangement(List<Item> items, int k) {
        // base cost: sum(weight * original position)
        long baseCost = 0;
        Map<String, Long> sumByLabel = new HashMap<>();
        for (Item it : items) {
            baseCost += (long) it.weight * it.pos;
            sumByLabel.merge(it.label, (long) it.weight, Long::sum);
        }

        // group labels by total weight (descending), labels inside group in lexicographic order
        TreeMap<Long, List<String>> buckets = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Long> e : sumByLabel.entrySet()) {
            buckets.computeIfAbsent(e.getValue(), w -> new ArrayList<>()).add(e.getKey());
        }
        List<List<String>> groups = new ArrayList<>();
        List<Long> grpW = new ArrayList<>();
        for (Map.Entry<Long, List<String>> entry : buckets.entrySet()) {
            List<String> labels = entry.getValue();
            Collections.sort(labels); // keep lexicographic order within equal-weight group
            groups.add(labels);
            grpW.add(entry.getKey());
        }

        // minimal extra cost by placing heavier groups earlier; order within same-weight group doesn't change cost
        long pos = 1;
        long extra = 0;
        int gi = 0;
        for (List<String> g : groups) {
            long w = grpW.get(gi++);
            for (int i = 0; i < g.size(); i++) {
                extra += pos * w;
                pos++;
            }
        }
        long minCost = baseCost + extra;

        // count total permutations
        int G = groups.size();
        BigInteger total = BigInteger.ONE;
        int[] sz = new int[G];
        for (int i = 0; i < G; i++) {
            sz[i] = groups.get(i).size();
            total = total.multiply(fact(sz[i]));
        }

        // k is 1-based
        BigInteger r = BigInteger.valueOf(k - 1L);
        if (r.compareTo(total.subtract(BigInteger.ONE)) > 0) {
            r = total.subtract(BigInteger.ONE);
        }

        // suffix products: permutations contributed by later groups
        BigInteger[] suf = new BigInteger[G + 1];
        suf[G] = BigInteger.ONE;
        for (int i = G - 1; i >= 0; i--) {
            suf[i] = suf[i + 1].multiply(fact(sz[i]));
        }

        // build the K-th lexicographic arrangement among minimum-cost ones
        List<String> order = new ArrayList<>();
        for (int i = 0; i < G; i++) {
            BigInteger later = suf[i + 1];
            BigInteger[] qr = r.divideAndRemainder(later);
            BigInteger which = qr[0]; // which permutation of this group
            r = qr[1];                // remainder for later groups
            order.addAll(kPerm(groups.get(i), which));
        }

        String orderStr = String.join(" ", order);
        return new Result(minCost, orderStr);
    }

    // returns correct permutation of sorted list
    private static List<String> kPerm(List<String> sorted, BigInteger idx) {
        // factoradic selection
        List<String> rem = new ArrayList<>(sorted);
        int n = rem.size();
        List<String> result = new ArrayList<>(n);
        BigInteger k = idx;

        for (int i = n; i >= 1; i--) {
            BigInteger f = fact(i - 1);
            BigInteger[] qr = k.divideAndRemainder(f);
            int pick = qr[0].intValue(); // 0..i-1
            result.add(rem.remove(pick));
            k = qr[1];
        }
        return result;
    }
}