package api.tasks;

import org.bson.types.ObjectId;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

@Module
public class ReverseAString implements Task {
  @Override
  @Provides
  @IntoSet
  public Task provideTask() {
    return new ReverseAString();
  }

  @Override
  public ObjectId getId() {
    return new ObjectId("65e8f6ad651cebb033a7688a");
  }

  @Override
  public String getName() {
    return "Reverse A String";
  }

  @Override
  public String getDescription() {
    return "" +
        "Write a program that reverses a string in place.";
  }

  @Override
  public String getTemplate() {
    return "" +
        "# write your answer within the function below\n" +
        "def ReverseString(s: str) -> str:\n" +
        "    return s";
  }
}
