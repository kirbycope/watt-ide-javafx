package application.models;

import java.util.ArrayList;
import java.util.List;

public class TestCase {
	// Properties
	List<TestStep> testSteps;

	// Constructor
	public TestCase() {
		this.testSteps = new ArrayList<TestStep>();
	}

	// Constructor
	public TestCase(List<TestStep> testSteps) {
		this.testSteps = testSteps;
	}

	public void AddStep(TestStep testStep) {
		this.testSteps.add(testStep);
	}

	public void RemoveStep(TestStep testStep) {
		this.testSteps.remove(testStep);
	}
}
