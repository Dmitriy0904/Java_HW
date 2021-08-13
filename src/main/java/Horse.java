public class Horse extends Thread {
    private final Long horseId;
    private Race race;

    public Horse(Race race, Long horseId) {
        this.race = race;
        this.horseId = horseId;
    }

    @Override
    public void run() {
        int timeForSleep;
        int totalDistance = 0;

        while(totalDistance < 1000){
            int distance = (int)(Math.random() * 100) + 100;
            totalDistance += distance;

            timeForSleep = (int)(Math.random() * 100) + 400;
            try{
                Thread.sleep(timeForSleep);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }
        }

        race.finish(this);
    }

    public Long getHorseId() {
        return horseId;
    }
}