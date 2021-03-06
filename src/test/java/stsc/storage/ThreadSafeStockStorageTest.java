package stsc.storage;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import stsc.common.stocks.MemoryStock;
import stsc.common.stocks.Stock;

public class ThreadSafeStockStorageTest {

	@Test
	public void testThreadSafeMemoryStoskStorage() throws ClassNotFoundException, IOException, InterruptedException {
		final ThreadSafeStockStorage stockStorage = new ThreadSafeStockStorage();
		final Stock stock = new MemoryStock("aapl");
		stockStorage.updateStock(stock);

		Assert.assertFalse(stockStorage.getStock("nostock").isPresent());
		Assert.assertTrue(stockStorage.getStock("aapl").isPresent());
		Assert.assertNotNull(stockStorage.getStock("aapl").get());
	}

}
