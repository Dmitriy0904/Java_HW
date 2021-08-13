import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Controller {
    private final HippodromeService hippodromeService = new HippodromeService();

    public void placeBet(){
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){

            System.out.println("Please enter the number of the horse you want to bet on (from 1 to 10)");
            Long horseId = Long.parseLong(bf.readLine());
            isCorrectBet(horseId);

            System.out.println("Bets are placed. The race begins!");
            hippodromeService.startRace();
            System.out.println("The race is over!");

            printResult(horseId);

        } catch (IOException exception){
            System.out.println("Incorrect value entered");
        }
    }


    private void printResult(Long horseId){
        Integer place = hippodromeService.getHorsePlace(horseId);
        System.out.println("Your horse finished in " + place + " place");
        if(place == 1){
            System.out.println("You are winner!");
        }
    }

    private void isCorrectBet(Long horseId) throws IOException {
        if(horseId < 1 || horseId > hippodromeService.getQuantityOfHorses()){
            throw new IOException("Incorrect horse id for bet entered");
        }
    }
}