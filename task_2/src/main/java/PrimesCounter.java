import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PrimesCounter implements Callable<List<Integer>> {
    private final List<Integer> allNumbers;

    public PrimesCounter(List<Integer> allNumbers) {
        this.allNumbers = allNumbers;
    }

    @Override
    public List<Integer> call() {
        List<Integer> primes = new ArrayList<>();
        for(Integer num : allNumbers){
            if(isPrime(num)){
                primes.add(num);
            }
        }
        return primes;
    }

    private boolean isPrime(Integer number){
        for(int i = 2; i <= Math.sqrt(number); i++){
            if(number % i == 0){
                return false;
            }
        }
        return number > 1;
    }
}