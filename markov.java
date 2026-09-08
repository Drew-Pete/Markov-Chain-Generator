import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

class MarkovChain{

    public void main(String[] args) throws IOException {
        Random generator = new Random();
        String book = Files.readString(Path.of("./moby-dick.txt"));
        String[] wordList = book.split("\\s+");

        int limit = wordList.length - (wordList.length / 10);

        String[] trainingList = Arrays.copyOfRange(wordList, 0, limit);
        String[] evalList = Arrays.copyOfRange(wordList, limit, wordList.length);

        String current = "";
        String next = "";
        String previous = "";

        //word tables
        Map<String, List<String>> oneWordTable = generateOneWordTable(trainingList);
        Map <String, List<String>> twoWordTable = generateTwoWordTable(trainingList);

        //perplexity tables
        Map<String, Double> oneWordPerpTable = generatePerpTable(oneWordTable);
        Map<String, Double> twoWordPerpTable = generatePerpTable(oneWordTable);



        List<String> result = new ArrayList<>();
        int otc = 0;
        int ttc = 0;
        int dtc = 0;

        previous = evalList[0];
        current = evalList[1];
        Double totalprob = 0.0;

        for(int i = 2; i < evalList.length; i++){
            next = evalList[i];


            //level 1 check
            List<String> temp = twoWordTable.get(previous + " " + current);
            if(Objects.nonNull(temp)){
                int freq = Collections.frequency(temp, next);
                if(freq != 0){
                    totalprob += (double)freq / temp.size();
                }
                continue;
            }
            //level 2 check
            temp = oneWordTable.get(current);
            if(Objects.nonNull(temp)){
                int freq = Collections.frequency(temp, next);
                if(freq != 0){
                    totalprob += 0.4 * (double)freq / temp.size();
                }
                continue;
            }
            //level 3 check
            int freq = Collections.frequency(Arrays.asList(trainingList), next);
            totalprob += 0.4 * 0.4 * ((double)freq + 1) / (trainingList.length + oneWordPerpTable.size());

        }
        Double perp = 0.0;//add perplexity calc here
//        System.out.println(result.stream().collect(Collectors.joining(" ")));
//        System.out.printf("Two Word Table Count: %s \n", ttc);
//        System.out.printf("One Word Table Count: %s \n", otc);
//        System.out.printf("Random Word Count: %s \n", dtc);

    }

    private Map<String, List<String>> generateOneWordTable(String[] wordList){
        Map<String, List<String>> table = new HashMap<>();
        String current = "";
        String next = "";

        for (int i = 0; i < wordList.length - 1 ; i++){
            current = wordList[i];
            next = wordList[i+1];
            List<String> temp = table.getOrDefault(current, new ArrayList<>());
            temp.add(next);

            table.put(current, temp);
        }
        if(!table.containsKey(next)){
            table.put(next, new ArrayList<>());
        }
        return table;
    }

    private Map<String, List<String>> generateTwoWordTable(String[] wordList){
        Map<String, List<String>> table = new HashMap<>();
        String current = "";
        String next = "";

        for (int i = 0; i < wordList.length - 2 ; i++){
            current = wordList[i] + " " + wordList[i+1];
            next = wordList[i+2];

            List<String> temp = table.getOrDefault(current , new ArrayList<>());
            temp.add(next);

            table.put(current, temp);
        }
        if(!table.containsKey(current)){
            table.put(next, new ArrayList<>());
        }
        return table;
    }

    private Map<String, Double> generatePerpTable(Map<String, List<String>> oneWordTable){
        Map<String, Double> table = new HashMap<>();
        oneWordTable.forEach((k, v) -> {
            table.put(k, null);
        });
        return table;
    }

    private Double calcPerpNumber(){
        Double perp;

        return perp;
    }
}