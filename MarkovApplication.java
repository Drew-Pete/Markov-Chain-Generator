import java.io.IOException;
import java.util.*;

class MarkovApplication {
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

//        String sentence1 = Files.readString(Path.of("./words.txt"));
//        String sentence2 = "In outer aspect, Pip and Dough-Boy made a match, like a black pony and\n" +
//                "a white one, of equal developments, though of dissimilar color, driven\n" +
//                "in one eccentric span.";
//        String sentence3 = "\"I hate Momunmonunsdays\", said Gazorpazorpfield.";
//
//        BytePairEncoder merge50 = new BytePairEncoder("./moby-dick.txt");
//        BytePairEncoder merge200 = new BytePairEncoder("./moby-dick.txt");
//        BytePairEncoder merge1k = new BytePairEncoder("./moby-dick.txt");
//        merge50.merge(100);
//        merge200.merge(200);
//        merge1k.merge(1000);
//
//        System.out.println(merge50.getVocabulary().size());
//
//        List<String> merge50Tokens = merge50.tokenize(sentence1);
//        List<String> merge50TokensTwo = merge50.tokenize(sentence2);
//        List<String> merge50TokensThree = merge50.tokenize(sentence3);
//
//        System.out.println(merge50Tokens);
//        System.out.println(merge50.decode(merge50Tokens));
//
//        System.out.println(merge50TokensTwo);
//        System.out.println(merge50.decode(merge50TokensTwo));
//
//        System.out.println(merge50TokensThree);
//        System.out.println(merge50.decode(merge50TokensThree));


//        System.out.println(merge200.getVocabulary().size());
//        System.out.println(merge200.tokenize(sentence1));
//        System.out.println(merge200.tokenize(sentence2));
//        System.out.println(merge200.tokenize(sentence3));
//
//
//        System.out.println(merge1k.getVocabulary().size());
//        System.out.println(merge1k.tokenize(sentence1));
//        System.out.println(merge1k.tokenize(sentence2));
//        System.out.println(merge1k.tokenize(sentence3));



//        WordEmbeddings wordEmbeddings = new WordEmbeddings("textFiles/glove.50k.txt");
//        System.out.println(wordEmbeddings.getWordAndTheirNumbers());
//        System.out.println(wordEmbeddings.calcCosineSimilarity("government", "people"));


        MarkovChain markovChain = new MarkovChain("./textFiles/moby-dick.txt");
        System.out.println(markovChain.oneWithBackOff());
        System.out.println(markovChain.twoWithBackOff());
        System.out.println(markovChain.oneWithBackOffWithPerp());
        System.out.println(markovChain.twoWithBackOffWithPerp());


        //tests
        //nearest 10
//        System.out.println("\nstarting word: king " + wordEmbeddings.getSimilarWords("king", 10, new HashSet<>()));
//        System.out.println("\nstarting word: java " + wordEmbeddings.getSimilarWords("java", 10, new HashSet<>()));
//        System.out.println("\nstarting word: coffee " + wordEmbeddings.getSimilarWords("coffee", 10, new HashSet<>()));
//        System.out.println("\nstarting word: pez " + wordEmbeddings.getSimilarWords("pez", 10, new HashSet<>()));
//
//        //calc similar words with a vector
//        List<String> analogyList1 = new ArrayList<>(Arrays.asList("man", "king", "woman"));
//        System.out.println("\nAnalogy: man -> king :: woman -> " + wordEmbeddings.getSimilarWordsWithVector(analogyList1, 3, new HashSet<>()));
//
//        List<String> analogyList2 = new ArrayList<>(Arrays.asList("france", "paris", "italy"));
//        System.out.println("\nAnalogy: france -> paris :: italy -> " + wordEmbeddings.getSimilarWordsWithVector(analogyList2, 3, new HashSet<>()));
//
//        List<String> analogyList3 = new ArrayList<>(Arrays.asList("walk", "walked", "swim"));
//        System.out.println("\nAnalogy: walked -> walk :: swim -> " + wordEmbeddings.getSimilarWordsWithVector(analogyList3, 3, new HashSet<>()));
//
//        List<String> analogyList4 = new ArrayList<>(Arrays.asList("dog", "walk", "fish"));
//        System.out.println("\nAnalogy: dog -> walk :: fish -> " + wordEmbeddings.getSimilarWordsWithVector(analogyList4, 3, new HashSet<>()));
//
//        List<String> analogyList5 = new ArrayList<>(Arrays.asList("gaon", "thermonuclear", "prawns"));
//        System.out.println("\nAnalogy: gaon -> thermonuclear :: prawns -> " + wordEmbeddings.getSimilarWordsWithVector(analogyList5, 3, new HashSet<>()));
//
//
//        System.out.println("Cosine Similarity: surgeon man " + wordEmbeddings.calcCosineSimilarity("surgeon", "man"));
//        System.out.println("Cosine Similarity: surgeon woman " + wordEmbeddings.calcCosineSimilarity("surgeon", "woman"));
//
//        System.out.println("Cosine Similarity: nurse man " + wordEmbeddings.calcCosineSimilarity("nurse", "man"));
//        System.out.println("Cosine Similarity: nurse woman " + wordEmbeddings.calcCosineSimilarity("nurse", "woman"));

    }
}