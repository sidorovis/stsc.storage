package stsc.storage.mocks;

import org.junit.Assert;
import org.junit.Test;

import stsc.common.storage.StockStorage;

public class StockStorageMockTest {

	@Test
	public void testStockStorageMock() {
		final StockStorage ss = StockStorageMock.getStockStorage();
		Assert.assertEquals(3, ss.getStockNames().size());
		Assert.assertTrue(ss.getStock("aapl").isPresent());
		Assert.assertTrue(ss.getStock("adm").isPresent());
		Assert.assertTrue(ss.getStock("spy").isPresent());
		Assert.assertFalse(ss.getStock("dmi").isPresent());
	}

}
