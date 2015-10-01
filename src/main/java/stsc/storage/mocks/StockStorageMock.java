package stsc.storage.mocks;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import stsc.common.stocks.UnitedFormatStock;
import stsc.common.storage.StockStorage;
import stsc.storage.ThreadSafeStockStorage;

/**
 * Mock for testing or development related tasks (when you would like to debug
 * some algorithm or whatever).
 */
public final class StockStorageMock {

	final static private String resourceToPath(final String resourcePath) throws URISyntaxException {
		return new File(StockStorageMock.class.getResource(resourcePath).toURI()).getAbsolutePath();
	}

	/**
	 * @return {@link ThreadSafeStockStorage} stock storage with 'aapl', 'adm',
	 *         'spy' stocks in it.
	 */
	public synchronized static StockStorage getStockStorage() {
		final StockStorage stockStorage = new ThreadSafeStockStorage();
		try {
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath("aapl.uf")));
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath("adm.uf")));
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath("spy.uf")));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e.getMessage());
		}
		return stockStorage;
	}

	/**
	 * @return {@link ThreadSafeStockStorage} stock storage for one selected
	 *         stock (available stocks are: aapl / adm / spy).
	 */
	public synchronized static StockStorage getStockStorageFor(final String stockName) {
		final StockStorage stockStorage = new ThreadSafeStockStorage();
		try {
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath(stockName + UnitedFormatStock.EXTENSION)));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e.getMessage());
		}
		return stockStorage;
	}

}