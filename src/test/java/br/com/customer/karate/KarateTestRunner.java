package br.com.customer.karate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.customer.Application;
import com.intuit.karate.Results;
import com.intuit.karate.junit5.Karate;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"test"})
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT,
    classes = {Application.class})
public class KarateTestRunner {

  @Autowired Environment environment;

  @Karate.Test
  public Karate run() {
    Karate karate =
        Karate.run(
                "classpath:karate/SavingCustomerTest.feature",
                "classpath:karate/SavingAddressTest.feature",
                "classpath:karate/SavingEmailTest.feature",
                "classpath:karate/SavingDocumentTest.feature")
            .outputCucumberJson(true)
            .systemProperty("port", environment.getProperty("local.server.port"));

    Results results = karate.parallel(4);
    generateReport(results.getReportDir());

    assertEquals(0, results.getFailCount(), results.getErrorMessages());

    return karate;
  }

  private void generateReport(String karateOutputPath) {
    Collection<File> jsonFiles =
        FileUtils.listFiles(new File(karateOutputPath), new String[] {"json"}, true);
    List<String> jsonPaths = new ArrayList<>(jsonFiles.size());
    jsonFiles.forEach(file -> jsonPaths.add(file.getAbsolutePath()));
    Configuration config = new Configuration(new File("target"), "customer-service");
    ReportBuilder reportBuilder = new ReportBuilder(jsonPaths, config);
    reportBuilder.generateReports();
  }
}
