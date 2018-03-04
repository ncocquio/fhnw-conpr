package bank.gui.tests;

public class EfficiencyTestCONPR extends EfficiencyTest {
	final static int NUMBER_OF_EFF_TESTS = 1000000;
	
	public EfficiencyTestCONPR() {
		super(NUMBER_OF_EFF_TESTS);
	}

	@Override
	public String getName() {
		return "Efficiency Test (CONPR)";
	}

}
