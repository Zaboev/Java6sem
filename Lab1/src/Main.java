import java.io.*;
import java.nio.file.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        try {
            // Читаем конфигурацию скобок
            String configContent = readFile("Configuration.json");
            JSONObject jsonObject = new JSONObject(configContent);
            JSONArray bracketsArray = jsonObject.getJSONArray("bracket");

            Map<Character, Character> bracketPairs = new HashMap<>();
            Set<Character> openingBrackets = new HashSet<>();
            Set<Character> closingBrackets = new HashSet<>();

            for (int i = 0; i < bracketsArray.length(); i++) {
                JSONObject bracket = bracketsArray.getJSONObject(i);
                if (!bracket.has("left") || !bracket.has("right")) {
                    throw new IllegalArgumentException("Некорректный формат конфигурации: отсутствует 'left' или 'right'");
                }
                String leftStr = bracket.getString("left");
                String rightStr = bracket.getString("right");
                if (leftStr.length() != 1 || rightStr.length() != 1) {
                    throw new IllegalArgumentException("Скобки должны быть длиной 1 символ");
                }
                char left = leftStr.charAt(0);
                char right = rightStr.charAt(0);
                bracketPairs.put(right, left);
                openingBrackets.add(left);
                closingBrackets.add(right);
            }

            // Читаем файл с текстом для проверки
            String text = readFile("text.txt");

            // Проверяем правильность скобок
            Deque<Character> stack = new ArrayDeque<>();
            Deque<Integer> positions = new ArrayDeque<>();

            boolean is_closed = true;

            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);

                if (openingBrackets.contains(ch) && (is_closed == true)) {
                    stack.push(ch);
                    positions.push(i);
                    if (ch == '|'){
                        is_closed = false;
                    }
                } else if (closingBrackets.contains(ch)) {
                    if (stack.isEmpty()) {
                        System.out.println("Ошибка: лишняя закрывающая скобка '" + ch + "' на позиции " + i);
                        if (ch == '|'){
                            is_closed = true;
                        }
                        return;
                    } else if (stack.peek() != bracketPairs.get(ch)) {
                        System.out.println("Ошибка: неверная закрывающая скобка '" + ch + "' на позиции " + i +
                                ", ожидалась пара для '" + stack.peek() + "'");
                        return;

                    } else {

                        stack.pop();
                        positions.pop();
                    }
                }
            }

            if (!stack.isEmpty()) {
                System.out.println("Ошибка: незакрытая скобка '" + stack.peek() + "' на позиции " + positions.peek());
            } else {
                System.out.println("Скобки расставлены правильно.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }
}