/*
Напишите приложение, которое в 2 потока будет считать
количество простых чисел, которые заданы в List, выводить
результат и возвращать его в главный поток.
Главный поток подсчитывает и выводит общее количество.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.FutureTask;

public class Main {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 0, -1, -2, -3, -4, -5 ,-6);
        List<Integer> first = numbers.subList(0, numbers.size() / 2);
        List<Integer> second = numbers.subList(numbers.size() / 2, numbers.size());
        List<Integer> primes = countPrimes(first, second);
        printResults(primes);
    }

    private static List<Integer> countPrimes(List<Integer> first, List<Integer> second){
        PrimesCounter firstCounter = new PrimesCounter(first);
        PrimesCounter secondCounter = new PrimesCounter(second);
        Thread thread1 = new Thread(new FutureTask<>(firstCounter));
        Thread thread2 = new Thread(new FutureTask<>(secondCounter));

        thread1.start();
        thread2.start();

        List<Integer> allPrimes = new ArrayList<>();
        try {
            allPrimes.addAll(firstCounter.call());
            allPrimes.addAll(secondCounter.call());
            return allPrimes;

        } catch (Exception exception) {
            throw new RuntimeException();
        }
    }


    private static void printResults(List<Integer> primes){
        System.out.println("Quantity of the primes: " + primes.size());
        for(Integer number : primes){
            System.out.print(number + " ");
        }
    }
}