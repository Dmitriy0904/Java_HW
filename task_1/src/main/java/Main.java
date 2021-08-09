/*
Напишите приложение, которое создает 50 потоков один за
одним, каждый из потоков выводит сообщение "Hello from thread
(number of thread)", особенность заключается в том, что вывод
должен быть строго в обратном порядке, от 49 до 0
 */

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.FutureTask;

public class Main {
    public static void main(String[] args) {
        solution1();
        System.out.println("\n");
        solution2();
    }


    private static void solution1(){
        for(int i = 49; i >= 0; i--){
            HelloPrinter helloPrinter = new HelloPrinter(i);
            Thread thread = new Thread(new FutureTask<>(helloPrinter));
            System.out.println(helloPrinter.call());
            try {
                Thread.sleep(100);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }
    }


    private static void solution2(){
        Deque<String> deque = new ArrayDeque<>();
        for(int i = 0; i < 50; i++){
            HelloPrinter helloPrinter = new HelloPrinter(i);
            Thread thread = new Thread(new FutureTask<>(helloPrinter));
            deque.push(helloPrinter.call());
        }
        for (String s : deque) {
            System.out.println(s);
        }
    }
}