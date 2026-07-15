import java.util.HashMap;
import java.util.LinkedList;

public class Example {

    public int returnIndexOfNorRepeatingCharacter(String str) {

        Map<Character, List<Integer> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (!map.contains(str.getChars(i))) {
                List<Integer> list = new LinkedList<>();
                list.add(i);
                map.put(str.getChars(i), list);
            } else {
                List<Integer> list = map.getValue(str.getChars(i));
                map.put(str.getChars(i), list.add());
            }


        }

        return 0;
    }
}
