package stsc.storage;

import org.junit.Assert;
import org.junit.Test;

import stsc.algorithms.indices.primitive.stock.SeveralLastMin;
import stsc.common.algorithms.BadAlgorithmException;

public class AlgorithmsStorageTest {

	@Test
	public void testAlgorithmNamesStorage() throws BadAlgorithmException {
		final AlgorithmsStorage ans = AlgorithmsStorage.getInstance();
		Assert.assertNotNull(ans.getStock(".Sma"));
		Assert.assertNotNull(ans.getStock(".Ema"));
		Assert.assertNotNull(ans.getEod(".Sma"));
		Assert.assertNotNull(ans.getEod("eod.AdlAdl"));
		Assert.assertNull(ans.getEod(".TestingEodAlgorithm"));
		Assert.assertNull(ans.getStock("StockAlgorithmHelper"));
		Assert.assertNotNull(ans.getEod("SimpleTradingAlgorithm"));
		Assert.assertEquals(ans.getStock("IN").getName(), SeveralLastMin.class.getName());
	}

	@Test
	public void testAlgorithmsEquityTest() throws BadAlgorithmException {
		final AlgorithmsStorage ans = AlgorithmsStorage.getInstance();
		Assert.assertNotNull(ans.getEod(".StockMarketCycleEquity"));
	}
}
