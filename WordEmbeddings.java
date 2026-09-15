import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class WordEmbeddings {

    private final String glovePath;
    private final HashMap<String, List<Double>> wordAndTheirNumbers = new HashMap<>();
    private final List<String> tempLines = new ArrayList<>();

    public WordEmbeddings(String path) throws IOException {
        this.glovePath = path;

        //get the words and their associated numbers

        //get the lines of the txt file and store each line as a string
        this.tempLines.addAll(Files.lines(Path.of(glovePath)).toList());

        //then from there further parse it to separate into a Stirng word and List<Double> numbers
        for (String s : tempLines) {
            List<String> temp = Arrays.stream(s.split("\\s+")).toList();
            String token = temp.get(0);
            wordAndTheirNumbers.putIfAbsent(token, new ArrayList<>());
            for (int i = 1; i < temp.size(); i++) {
                wordAndTheirNumbers.get(token).add(Double.valueOf(temp.get(i)));
            }
            //calculate magnitude here w/ magnitude helper function
            wordAndTheirNumbers.get(token).add(calcMagnitude(wordAndTheirNumbers.get(token)));
        }
    }

    public Map<String, List<Double>> getWordAndTheirNumbers() {
        return wordAndTheirNumbers;
    }

    //create cosine similarity function to calculate cosine similarity
    public Double calcCosineSimilarity(String a, String b) {
        Double dotProduct = 0.0;
        List<Double> aList = wordAndTheirNumbers.get(a);
        List<Double> bList = wordAndTheirNumbers.get(b);
        for (int i = 0; i < aList.size() - 2; i++) {
            dotProduct += aList.get(i) * bList.get(i);
        }

        return dotProduct / (aList.get(aList.size() - 1) * bList.get(bList.size() - 1));
    }


    //magnitude calculator function helper
    private Double calcMagnitude(List<Double> list) {
        Double sum = 0.0;
        for (Double d : list) {
            sum += d * d;
        }
        return Math.sqrt(sum);
    }

    public List<String> getSimilarWords(String word, int k, Set<String> excludeWords) {
        //create and populate map of compared word(key), cosine similarity
        Map<String, Double> wordCosineMap = new HashMap<>();

        //populate map
        for (String key : wordAndTheirNumbers.keySet()) {
            if (excludeWords.contains(key) || word.equals(key)) {
                continue;
            }
            //calc cosine based on word (from param) and key
            Double cosineCoef = calcCosineSimilarity(word, key);
            wordCosineMap.put(key, cosineCoef);
        }

        //find k similar words
        return wordCosineMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(k).map(e -> {
            System.out.println(e.getValue());
            return e.getKey();
        }).collect(Collectors.toList());
    }

    public List<Double> calcAnalogyVector(List<String> analogyWords) {
        List<Double> listA = wordAndTheirNumbers.get(analogyWords.get(0));
        List<Double> listB = wordAndTheirNumbers.get(analogyWords.get(1));
        List<Double> listC = wordAndTheirNumbers.get(analogyWords.get(2));

        List<Double> analogyVector = new ArrayList<>();
        for (int i = 0; i < listA.size() - 1; i++) {
            analogyVector.add(
                    //vector(b) − vector(a) + vector(c)
                    listB.get(i) - listA.get(i) + listC.get(i)
            );
        }
        //append magnitude to match the shape of entries in wordAndTheirNumbers
        analogyVector.add(calcMagnitude(analogyVector));

        return analogyVector;
    }

    public List<String> getSimilarWordsWithVector(List<String> analogyList, int k, Set<String> excludeWords) {
        //create and populate map of compared word(key), cosine similarity
        Map<String, Double> wordCosineMap = new HashMap<>();
        List<Double> vector = calcAnalogyVector(analogyList);

        //populate map
        for (String key : wordAndTheirNumbers.keySet()) {
            if (excludeWords.contains(key) || analogyList.contains(key)) {
                continue;
            }
            //calc cosine based on vector and key
            Double cosineCoef = calcCosineSimilarityWithVector(vector, key);
            wordCosineMap.put(key, cosineCoef);
        }

        //find k similar words
        return wordCosineMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(k).map(e -> {
            System.out.println(e.getValue());
            return e.getKey();
        }).collect(Collectors.toList());
    }

    public Double calcCosineSimilarityWithVector(List<Double> vector, String b) {
        Double dotProduct = 0.0;
        List<Double> aList = vector;
        List<Double> bList = wordAndTheirNumbers.get(b);
        for (int i = 0; i < aList.size() - 1; i++) {
            dotProduct += aList.get(i) * bList.get(i);
        }

        return dotProduct / (aList.get(aList.size() - 1) * bList.get(bList.size() - 1));
    }
}
