package runclass;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin={"json:target/WebService.json","html:target/WebService_Html"}, features = "src/test/resources/Featurefiles",tags="@Run")

public class RunWebService {

}
