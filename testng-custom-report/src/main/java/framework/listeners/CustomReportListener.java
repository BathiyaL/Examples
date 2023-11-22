package framework.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class CustomReportListener implements IReporter {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomReportListener.class);
	private static final String REPORT_NAME = "RegressionTest.html";
	private static final String REPORT_FOLDER= "custom-reports";

	//private static final String ROW_TEMPLATE = "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";
	private static final String ROW_TEMPLATE = "<tr><td>%s</td><td>%s</td><td>%s</td><td class=\"%s\">%s</td><td style=\"text-align: center; vertical-align: middle;\">%s</td></tr>";

	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		String reportTemplate = initReportTemplate();
		
		String suiteName = suites.get(0).getName();	
    	int testsCount = 0;
        int passedTests = 0;
        int skippedTests = 0;
        int failedTests = 0;
		
		for(String key :  suites.get(0).getResults().keySet()) {
	        passedTests += suites.get(0).getResults().get(key).getTestContext().getPassedTests().size();
	        skippedTests += suites.get(0).getResults().get(key).getTestContext().getSkippedTests().size();
	        failedTests += suites.get(0).getResults().get(key).getTestContext().getFailedTests().size();			
		}
		testsCount = passedTests + skippedTests + failedTests;
		reportTemplate = reportTemplate.replace("$reportTitle", getReportTitle(suiteName));
		reportTemplate = reportTemplate.replace("$passedTestCount", Integer.toString(passedTests));
		reportTemplate = reportTemplate.replace("$failedTestCount", Integer.toString(failedTests));
		reportTemplate = reportTemplate.replace("$skippedTestCount", Integer.toString(skippedTests));
		
		
		final String body = suites.stream().flatMap(suiteToResults()).collect(Collectors.joining());
		//saveReportTemplate(outputDirectory, reportTemplate.replaceFirst("</tbody>", String.format("%s</tbody>", body)));
		saveReportTemplate(outputDirectory, reportTemplate.replace("$testResults",body));
		
		//saveReportTemplate(outputDirectory, reportTemplate);
	}
	
    protected String getReportTitle(String title) {
		return title + " [" + getCurrentDateTime() + "]" ;
	}

	private Function<ISuite, Stream<? extends String>> suiteToResults() {
		return suite -> suite.getResults().entrySet().stream().flatMap(resultsToRows(suite));
	}

	private Function<Map.Entry<String, ISuiteResult>, Stream<? extends String>> resultsToRows(ISuite suite) {
		return e -> {
			ITestContext testContext = e.getValue().getTestContext();

			Set<ITestResult> failedTests = testContext.getFailedTests().getAllResults();
			Set<ITestResult> passedTests = testContext.getPassedTests().getAllResults();
			Set<ITestResult> skippedTests = testContext.getSkippedTests().getAllResults();
			
			// testContext.getFailedTests().getAllMethods().getAllTestMethods()[0].getDescription();			

			String suiteName = suite.getName();

			return Stream.of(failedTests, passedTests, skippedTests)
					.flatMap(results -> generateReportRows(e.getKey(), suiteName, results).stream());
		};
	}

	private List<String> generateReportRows(String testName, String suiteName, Set<ITestResult> allTestResults) {
		return allTestResults.stream().map(testResultToResultRow(testName, suiteName)).collect(toList());
	}

	private Function<ITestResult, String> testResultToResultRow(String testName, String suiteName) {
		// testResult.getName() // test method name
		// testResult.getMethod().getDescription() // test description
		// testResult.getName() // test method name
		return testResult -> {
			
			String fullyQualifiedName = testResult.getTestClass().getName();
			String testClassName = fullyQualifiedName.substring(fullyQualifiedName.lastIndexOf(".")+1);
			String testGroup = testResult.getMethod().getGroups()[0];
			String testDescription = testResult.getMethod().getDescription();
			
			switch (testResult.getStatus()) {
			case ITestResult.FAILURE:
				return String.format(ROW_TEMPLATE, testGroup, testClassName, testDescription,"bg-danger", "FAILED", "NA");

			case ITestResult.SUCCESS:
				return String.format(ROW_TEMPLATE, testGroup, testClassName, testDescription,"bg-success", "PASSED",
						String.valueOf(testResult.getEndMillis() - testResult.getStartMillis()));

			case ITestResult.SKIP:
				return String.format(ROW_TEMPLATE,testGroup, testClassName, testDescription, "bg-warning", "SKIPPED",
						"NA");

			default:
				return "";
			}
		};
	}

	private String initReportTemplate() {
		String template = null;
		byte[] reportTemplate;
		Path resourceDirectoryPath = Paths.get("src","test","resources","ReportTemplateV1.html");
		try {
			reportTemplate = Files.readAllBytes(Paths.get(resourceDirectoryPath.toUri()));
			template = new String(reportTemplate, "UTF-8");
		} catch (IOException e) {
			LOGGER.error("Problem initializing template", e);
		}
		return template;
	}

	private void saveReportTemplate(String outputDirectory, String reportTemplate) {
		
		// create custom report path to hold custom reports only
		File outputDirectoryFilePath = new File(outputDirectory,REPORT_FOLDER);
		outputDirectoryFilePath.mkdirs();
		try {
			PrintWriter reportWriter = new PrintWriter(
					new BufferedWriter(new FileWriter(new File(outputDirectoryFilePath.getPath(), REPORT_NAME))));
			reportWriter.println(reportTemplate);
			reportWriter.flush();
			reportWriter.close();
		} catch (IOException e) {
			LOGGER.error("Problem saving template", e);
		}
	}
	
	public static String getCurrentDateTime() {
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy:HH.mm.ss");
		return formatter.format(currentDate.getTime());
	}
}
