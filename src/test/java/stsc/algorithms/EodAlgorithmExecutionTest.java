package stsc.algorithms;

import stsc.algorithms.BadAlgorithmException;
import stsc.algorithms.EodAlgorithmExecution;
import stsc.algorithms.primitive.TestingEodAlgorithm;
import junit.framework.TestCase;

public class EodAlgorithmExecutionTest extends TestCase {
	public void testEodAlgorithmExecutionConstructor() {
		boolean exception = false;
		try {
			new EodAlgorithmExecution("execution1", "algorithm1", new AlgorithmSettings());
		} catch (BadAlgorithmException e) {
			exception = true;
		}
		assertTrue(exception);
	}

	public void testNameInstallingMethod() throws BadAlgorithmException {
		final EodAlgorithmExecution eae = new EodAlgorithmExecution("e1",
				"stsc.algorithms.primitive.TestingEodAlgorithm", new AlgorithmSettings());
		assertEquals("stsc.algorithms.primitive.TestingEodAlgorithm", eae.getAlgorithmName());
	}

	public void testExecution() throws BadAlgorithmException {
		EodAlgorithmExecution e3 = new EodAlgorithmExecution("e1", TestingEodAlgorithm.class.getName(),
				new AlgorithmSettings());
		assertEquals(TestingEodAlgorithm.class.getName(), e3.getAlgorithmName());
		assertEquals("e1", e3.getName());
	}
}