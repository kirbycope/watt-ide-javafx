package application.models;

public class TestStep {
	// Properties
	public boolean ExecuteStep;
	public String Description;
	public String Command;
	public String Target;
	public String Value;
	public boolean ContinueOnFailure;

	// Constructor
	public TestStep() {
		ExecuteStep = true;
		Description = "";
		Command = "";
		Target = "";
		Value = "";
		ContinueOnFailure = false;
	}

	// Constructor
	public TestStep(boolean executeStep, String description, String command, String target, String value, boolean continueOnFailure) {
		ExecuteStep = executeStep;
		Description = description;
		Command = command;
		Target = target;
		Value = value;
		ContinueOnFailure = continueOnFailure;
	}
}
