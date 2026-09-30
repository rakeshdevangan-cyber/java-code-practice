package practice.stream;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GroupByLength {

    public static void main(String[] args) {
        List<String> words =
                List.of("cat", "java", "dog", "spring", "code");

        Map<Integer, List<String>> map = words.stream().collect(Collectors.groupingBy(e->e.length()));

        System.out.println(map);
    }
}
