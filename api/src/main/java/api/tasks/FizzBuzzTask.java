package api.tasks;

import org.bson.types.ObjectId;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class FizzBuzzTask implements Task {
  @Override
  @Provides
  @IntoSet
  public Task provideTask() {
    return new FizzBuzzTask();
  }

  @Override
  public ObjectId getId() {
    return new ObjectId("65e456500575b443b71a8846");
  }

  @Override
  public String getName() {
    return "FizzBuzz";
  }

  @Override
  public String getDescription() {
    return "" +
        "Write a program that prints the numbers from 1 to 100. " +
        "But for multiples of three print “Fizz” instead of the number and for the multiples of five print “Buzz”. " +
        "For numbers which are multiples of both three and five print “FizzBuzz”.";
  }

  @Override
  public String getTemplate() {
    return "" +
        "# write your answer within the function below\n" +
        "def FizzBuzz(i: int) -> None:\n" +
        "    print(\"Fizz\")\n" +
        "    pass";
  }
}
