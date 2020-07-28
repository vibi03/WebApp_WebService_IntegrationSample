package runclass;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin= {"json:target/CalculatorUI.json","html:target/CalculatorReport"},
features= {"src/test/resources"},tags= "@Run")
public class RunCalculatorUI {

}
