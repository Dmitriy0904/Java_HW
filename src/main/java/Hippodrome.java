import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Hippodrome {
    private Race race;
    private List<Horse> horses;
    private Integer quantityOfHorses;

    public Hippodrome() {
        race = new Race();
        quantityOfHorses = 10;
        horses = new ArrayList<>();
        for(int i = 1; i <= quantityOfHorses; i++){
            horses.add(new Horse(race, (long) i));
        }
    }


    public void startRace(){
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(Horse horse : horses){
            executorService.execute(horse);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(50000, TimeUnit.SECONDS);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }


    public Map<Long, Integer> getHorsePlace(){
        return race.getHorsePlace();
    }

    public Integer getQuantityOfHorses(){
        return quantityOfHorses;
    }
}
