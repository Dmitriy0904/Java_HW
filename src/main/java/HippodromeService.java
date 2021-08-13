import java.util.Map;

public class HippodromeService {
    private final Hippodrome hippodrome;

    public HippodromeService(){
        hippodrome = new Hippodrome();
    }

    public void startRace(){
        hippodrome.startRace();
    }

    public Integer getQuantityOfHorses(){
        return hippodrome.getQuantityOfHorses();
    }

    public Integer getHorsePlace(Long horseId){
        Map<Long, Integer> horsePlace = hippodrome.getHorsePlace();
        return horsePlace.get(horseId);
    }
}
