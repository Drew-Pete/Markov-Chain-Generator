import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class main {

    public static void main(String[] args) throws IOException {
//        Random generator = new Random();
//        String book = Files.readString(Path.of("./moby-dick.txt"));
//        String[] wordList = book.split("\\s+");
//        int limit = wordList.length - (wordList.length / 10);
//        String[] trainingList = Arrays.copyOfRange(wordList, 0, limit);
//        String[] evalList = Arrays.copyOfRange(wordList, limit, wordList.length);
//        Map<String, List<String>> oneWordTable = MarkovChain.generateOneWordTable(trainingList);
//        Map<String, List<String>> twoWordTable = MarkovChain.generateTwoWordTable(trainingList);
//        String current = "";
//        String next = "";
//        String previous = "";
//
//        List<String> result = new ArrayList<>();
//        int otc = 0;
//        int ttc = 0;
//        int dtc = 0;
//
//
//        Double twoWordBackoff = twoWithBackOff(evalList, trainingList, twoWordTable, oneWordTable);//add perplexity calc here
//        Double oneWordBackoff = oneWithBackOff(evalList, trainingList, oneWordTable);
//        Double twoWordTrainingText = twoWithBackOff(trainingList, trainingList, twoWordTable, oneWordTable);

//        System.out.printf("Two Word Table With Backoff: %s \n", twoWordBackoff);
//        System.out.printf("One Word Table With Backoff: %s \n", oneWordBackoff);
//        System.out.printf("Two Word Table With Training Text: %s \n", twoWordTrainingText);

        String sentence1 = Files.readString(Path.of("./words.txt"));
        String sentence2 = "In outer aspect, Pip and Dough-Boy made a match, like a black pony and\n" +
                "a white one, of equal developments, though of dissimilar color, driven\n" +
                "in one eccentric span.";
        String sentence3 = "\"I hate Momunmonunsdays\", said Gazorpazorpfield.";

        BytePairEncoder merge50 = new BytePairEncoder("./moby-dick.txt");
        BytePairEncoder merge200 = new BytePairEncoder("./moby-dick.txt");
        BytePairEncoder merge1k = new BytePairEncoder("./moby-dick.txt");
        merge50.merge(100);
        merge200.merge(200);
        merge1k.merge(1000);

        System.out.println(merge50.getVocabulary().size());
        System.out.println(merge50.tokenize(sentence1));
        System.out.println(merge50.tokenize(sentence2));
        System.out.println(merge50.tokenize(sentence3));

        System.out.println(merge200.getVocabulary().size());
        System.out.println(merge200.tokenize(sentence1));
        System.out.println(merge200.tokenize(sentence2));
        System.out.println(merge200.tokenize(sentence3));

        System.out.println(merge1k.getVocabulary().size());
        System.out.println(merge1k.tokenize(sentence1));
        System.out.println(merge1k.tokenize(sentence2));
        System.out.println(merge1k.tokenize(sentence3));

    }

    private static Map<String, Double> generatePerpTable(Map<String, List<String>> oneWordTable) {
        Map<String, Double> table = new HashMap<>();
        oneWordTable.forEach((k, v) -> {
            table.put(k, null);
        });
        return table;
    }

    private static Double calcPerpNumber(Double totalProb, int testedWords) {
        return Math.exp((-totalProb / testedWords));
    }

    private static Double twoWithBackOff(String[] evalList, String[] trainingList, Map<String, List<String>> twoWordTable, Map<String, List<String>> oneWordTable) {
        String previous = evalList[0];
        String current = evalList[1];
        String next = "";
        Double totalprob = 0.0;
        int words = 0;

        for (int i = 2; i < evalList.length; i++) {
            next = evalList[i];


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
            int freq = Collections.frequency(Arrays.asList(trainingList), next);
            totalprob += Math.log(0.4 * 0.4 * ((double) freq + 1) / (trainingList.length + oneWordTable.size()));
            previous = current;
            current = next;
            words++;

        }
        return calcPerpNumber(totalprob, words);
    }

    private static Double oneWithBackOff(String[] evalList, String[] trainingList, Map<String, List<String>> oneWordTable) {
        String current = evalList[1];
        String next = "";
        Double totalprob = 0.0;
        int words = 0;

        for (int i = 1; i < evalList.length; i++) {
            next = evalList[i];

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
            int freq = Collections.frequency(Arrays.asList(trainingList), next);
            totalprob += Math.log(0.4 * 0.4 * ((double) freq + 1) / (trainingList.length + oneWordTable.size()));
            current = next;
            words++;

        }
        return calcPerpNumber(totalprob, words);    }

}