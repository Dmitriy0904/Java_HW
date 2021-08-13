import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Race {
    private Map<Long, Integer> horsePlace;
    private Integer place;

    public Race() {
        horsePlace = new ConcurrentHashMap<>();
        place = 1;
    }

    public void finish(Horse horse){
        horsePlace.put(horse.getHorseId(), place);
        place++;
    }

    public Map<Long, Integer> getHorsePlace() {
        return horsePlace;
    }
}
