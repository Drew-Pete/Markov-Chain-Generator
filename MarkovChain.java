import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkovChain {


    public static Map<String, List<String>> generateOneWordTable(String[] wordList){
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

    public static Map<String, List<String>> generateTwoWordTable(String[] wordList){
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
}
