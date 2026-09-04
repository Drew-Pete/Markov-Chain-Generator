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
        String current = "";
        String next = "";
        String previous = "";
        Map<String, List<String>> oneWordTable = generateOneWordTable(book);
        Map <String, List<String>> twoWordTable = generateTwoWordTable(book);
        List<String> result = new ArrayList<>();
        int otc = 0;
        int ttc = 0;
        int dtc = 0;

        previous = wordList[generator.nextInt(wordList.length)];
        current = wordList[generator.nextInt(wordList.length)];
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
            next = wordList[generator.nextInt(wordList.length)];
            result.add(next);
            previous = current;
            current = next;
            dtc++;
        }
        System.out.println(result.stream().collect(Collectors.joining(" ")));
        System.out.printf("Two Word Table Count: %s \n", ttc);
        System.out.printf("One Word Table Count: %s \n", otc);
        System.out.printf("Random Word Count: %s \n", dtc);

    }

    private Map<String, List<String>> generateOneWordTable(String book){
        Map<String, List<String>> table = new HashMap<>();
        String[] wordList = book.split("\\s+");
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

    private Map<String, List<String>> generateTwoWordTable(String book){
        Map<String, List<String>> table = new HashMap<>();
        String[] wordList = book.split("\\s+");
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
}