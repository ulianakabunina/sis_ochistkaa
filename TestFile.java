// Это простой класс для демонстрации обфускации
public class TestFile {

    private int number;
    private String message;

    // Конструктор класса
    public TestFile(int number, String message) {
        this.number = number; // присваиваем значения
        this.message = message;
    }

    // Метод, который печатает сообщение указанное количество раз
    public void printMessage() {
        for (int i = 0; i < number; i++) {
            System.out.println(message + " #" + i);
        }
    }

    // Метод, который возвращает длину строки
    public int getMessageLength() {
        return message.length();
    }

    /* Многострочный комментарий:
       Метод main создаёт объект и вызывает методы */
    public static void main(String[] args) {
        TestFile tf = new TestFile(3, "Hello!");
        tf.printMessage();
        System.out.println("Length: " + tf.getMessageLength());
    }
}
