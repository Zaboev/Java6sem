public class MaxNumberMatrix {
    private static int[][] matrix = {
            {9, 1, 4},
            {8, 7, 3},
            {2, 6, 5}
    };
    private static boolean[][] visited = new boolean[3][3];
    private static String maxNumber = "0"; // начальное значение для сравнения

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                visited = new boolean[3][3]; // сбрасываем visited для каждой стартовой точки
                findMaxNumber(i, j, "");
            }
        }
        System.out.println("Максимальное число: " + maxNumber);
    }

    private static void findMaxNumber(int row, int col, String current) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3 || visited[row][col]) {
            return;
        }

        String newCurrent = current + matrix[row][col];
        visited[row][col] = true;

        // Проверяем только полные пути длиной 9
        if (newCurrent.length() == 9) {
            if (newCurrent.compareTo(maxNumber) > 0) {
                maxNumber = newCurrent;
            }
        } else {
            findMaxNumber(row + 1, col, newCurrent); // вниз
            findMaxNumber(row - 1, col, newCurrent); // вверх
            findMaxNumber(row, col + 1, newCurrent); // вправо
            findMaxNumber(row, col - 1, newCurrent); // влево
        }

        visited[row][col] = false; // откат для других путей
    }
}