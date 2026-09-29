import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class BytePairEncoder {
    private final String bookPath;
    private final List<String> words;
    private final List<String> tokens = new ArrayList<>();
    private final List<List<String>> mergeList = new ArrayList<>();

    BytePairEncoder(String path) throws IOException {
        this.bookPath = path;
        this.words = Arrays.stream(Files.readString(Path.of(bookPath)).split("\\s+")).map( e -> {
            return e + "_";
        }).collect(Collectors.toList());

        for(String word : words){
            char[] chars = word.toCharArray();
            for(char c : chars) {
                this.tokens.add(String.valueOf(c));
            }
        }
    }

    public List<String> getTokens(){
        return tokens;
    }
    public List<String> getWords(){
        return words;
    }
    public List<List<String>> getMergeList(){
        return mergeList;
    }

    public void merge(int count){
        for(int i = 0; i < count; i++){
            Map<String, List<Integer>> counts = new HashMap<>();
            String t1 = tokens.get(0);
            String t2;
            // get frequency
            for(int j = 1; j < tokens.size(); j++){
                t2 = tokens.get(j);
                if(t1.contains("_")){
                    t1 = t2;
                    continue;
                }
                counts.computeIfAbsent(t1+t2, k -> new ArrayList<>()).add(j-1);
                t1 = t2;
            }
            String maxKey = "";
            int maxFreq = 0;
            //find most frequent pair
            for(Map.Entry<String, List<Integer>> entry : counts.entrySet()){
                if(entry.getValue().size() > maxFreq){
                    maxKey = entry.getKey();
                    maxFreq = entry.getValue().size();
                }
            }

            //create merge list
            mergeList.add(new ArrayList<>(Arrays.asList(tokens.get(counts.get(maxKey).get(0)), tokens.get(counts.get(maxKey).get(0) + 1))));

            // merge most frequent pair
            for(Integer index : counts.getOrDefault(maxKey, new ArrayList<>())){
                tokens.set(index, maxKey);
            }
            for(int j = counts.getOrDefault(maxKey, new ArrayList<>()).size() - 1; j >= 0; j--){
                int index = counts.get(maxKey).get(j);
                tokens.remove(index + 1);
            }

        }
    }

    public List<String> tokenize(String text){
        List<String> tWords = Arrays.stream(text.split("\\s+")).map( e -> {
            return e + "_";
        }).toList();

        List<String> charList = new ArrayList<>();

        for(String word : tWords){
            char[] chars = word.toCharArray();
            for(char c : chars) {
                charList.add(String.valueOf(c));
            }
        }

        for(List<String> rule : mergeList){
            String r1 = rule.get(0);
            String r2 = rule.get(1);


            for(int i = 0; i < charList.size() - 1; i++){
                String t1 = charList.get(i);
                String t2 = charList.get(i + 1);
                if(t1.contains("_")){
                    continue;
                }
                if(t1.equals(r1) && t2.equals(r2)){
                    charList.set(i, t1 + t2);
                    charList.remove(i + 1);
                }

            }
        }

        return charList;
    }

    public String decode(List<String> tokenList){
        StringBuilder decoded = new StringBuilder();
        for(String token : tokenList){
            decoded.append(token.replace('_', ' '));
        }
        return decoded.toString();
    }

    public Set<String> getVocabulary(){
        return new HashSet<>(tokens);
    }



}
