import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class MarkovChain {

    private final Random generator = new Random();
    private final String bookPath;
    private final List<String> words;
    private final Map<String, List<String>> oneWordTable;
    private final Map<String, List<String>> twoWordTable;
    private final List<String> evalList;
    private final List<String> trainingList;
    private int limit;

    public MarkovChain(String path) throws IOException {
        this.bookPath = path;
        this.words = Arrays.stream(Files.readString(Path.of(bookPath)).split("\\s+")).collect(Collectors.toList());
        this.limit = words.size() - (words.size() / 10); //int at 90% of book
        this.evalList = words.subList(limit, words.size());
        this.trainingList = words.subList(0, limit);
        this.oneWordTable = generateOneWordTable(trainingList);
        this.twoWordTable = generateTwoWordTable(trainingList);
    }



    public static Map<String, List<String>> generateOneWordTable(List<String> wordList){
        Map<String, List<String>> table = new HashMap<>();
        String current = "";
        String next = "";

        for (int i = 0; i < wordList.size() - 1 ; i++){
            current = wordList.get(i);
            next = wordList.get(i + 1);
            List<String> temp = table.getOrDefault(current, new ArrayList<>());
            temp.add(next);

            table.put(current, temp);
        }
        if(!table.containsKey(next)){
            table.put(next, new ArrayList<>());
        }
        return table;
    }

    public static Map<String, List<String>> generateTwoWordTable(List<String> wordList){
        Map<String, List<String>> table = new HashMap<>();
        String current = "";
        String next = "";

        for (int i = 0; i < wordList.size() - 2 ; i++){
            current = wordList.get(i) + " " + wordList.get(i + 1);
            next = wordList.get(i + 2);

            List<String> temp = table.getOrDefault(current , new ArrayList<>());
            temp.add(next);

            table.put(current, temp);
        }
        if(!table.containsKey(current)){
            table.put(next, new ArrayList<>());
        }
        return table;
    }

    private Double calcPerpNumber(Double totalProb, int testedWords) {
        return Math.exp((-totalProb / testedWords));
    }

    //contains perplexity calc
    public Double twoWithBackOffWithPerp() {
        String previous = evalList.get(0);
        String current = evalList.get(1);
        String next = "";
        Double totalprob = 0.0;
        int words = 0;

        for (int i = 2; i < evalList.size(); i++) {
            next = evalList.get(i);


            //level 1 check
            List<String> temp = twoWordTable.get(previous + " " + current);
            if (Objects.nonNull(temp)) {
                int freq = Collections.frequency(temp, next);
                if (freq != 0) {
                    totalprob += Math.log((double) freq / temp.size());
                    previous = current;
                    current = next;
                    words++;
                    continue;
                }
            }
            //level 2 check
            temp = oneWordTable.get(current);
            if (Objects.nonNull(temp)) {
                int freq = Collections.frequency(temp, next);
                if (freq != 0) {
                    totalprob += Math.log(0.4 * (double) freq / temp.size());
                    previous = current;
                    current = next;
                    words++;
                    continue;
                }
            }
            //level 3 check
            int freq = Collections.frequency(trainingList, next);
            totalprob += Math.log(0.4 * 0.4 * ((double) freq + 1) / (trainingList.size() + oneWordTable.size()));
            previous = current;
            current = next;
            words++;

        }
        return calcPerpNumber(totalprob, words);
    }

    //contains perplexity calc
    public Double oneWithBackOffWithPerp() {
        String current = evalList.getFirst();
        String next = "";
        Double totalprob = 0.0;
        int words = 0;

        for (int i = 1; i < evalList.size(); i++) {
            next = evalList.get(i);

            //level 2 check
            List<String> temp = oneWordTable.get(current);
            if (Objects.nonNull(temp)) {
                int freq = Collections.frequency(temp, next);
                if (freq != 0) {
                    totalprob += Math.log(0.4 * (double) freq / temp.size());
                    current = next;
                    words++;
                    continue;
                }
            }
            //level 3 check
            int freq = Collections.frequency(trainingList, next);
            totalprob += Math.log(0.4 * 0.4 * ((double) freq + 1) / (trainingList.size() + oneWordTable.size()));
            current = next;
            words++;

        }
        return calcPerpNumber(totalprob, words);    }

    public List<String> twoWithBackOff(){
        List<String> result = new ArrayList<>();
        String previous = "";
        String current = "";
        String next = "";

        int otc = 0;    //one table count
        int ttc = 0;    //two table count
        int dtc = 0;    //default count

        previous = words.get(generator.nextInt(words.size() - 1));
        current = words.get(generator.nextInt(words.size() - 1));
        result.add(previous);
        result.add(current);
        for(int i = 0; i < 38; i++){
            //get word from twoWordTable
            List<String> temp = twoWordTable.get(previous + " " + current);
            if(Objects.nonNull(temp)){
                next = temp.get(generator.nextInt(temp.size()));
                result.add(next);
                previous = current;
                current = next;
                ttc++;
                continue;
            }
            temp = oneWordTable.get(current);
            if(Objects.nonNull(temp)){
                next = oneWordTable.get(current).get(generator.nextInt(oneWordTable.get(current).size()));
                result.add(next);
                previous = current;
                current = next;
                otc++;
                continue;
            }
            next = words.get(generator.nextInt(words.size() - 1));
            result.add(next);
            previous = current;
            current = next;
            dtc++;
        }

        return result;
    }

    public List<String> oneWithBackOff(){
        List<String> result = new ArrayList<>();
        String current = "";
        String next = "";

        int otc = 0;    //one table count
        int dtc = 0;    //default count

        current = words.get(generator.nextInt(words.size() - 1));
        result.add(current);
        for(int i = 0; i < 38; i++){
            //get word from twoWordTable
            List<String> temp = oneWordTable.get(current);
            if(Objects.nonNull(temp)){
                next = oneWordTable.get(current).get(generator.nextInt(oneWordTable.get(current).size()));
                result.add(next);
                current = next;
                otc++;
                continue;
            }
            next = words.get(generator.nextInt(words.size() - 1));
            result.add(next);
            current = next;
            dtc++;
        }

        return result;
    }
}
