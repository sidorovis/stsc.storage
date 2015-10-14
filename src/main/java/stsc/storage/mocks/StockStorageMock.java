package stsc.storage.mocks;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import stsc.common.stocks.UnitedFormatHelper;
import stsc.common.stocks.UnitedFormatStock;
import stsc.common.storage.StockStorage;
import stsc.storage.ThreadSafeStockStorage;

/**
 * Mock for testing or development related tasks (when you would like to debug
 * some algorithm or whatever).
 */
public final class StockStorageMock {

	final static private InputStream resourceToPath(final String resourcePath) throws URISyntaxException {
		return StockStorageMock.class.getResourceAsStream(resourcePath);
	}

	/**
	 * @return {@link ThreadSafeStockStorage} stock storage with 'aapl', 'adm',
	 *         'spy' stocks in it.
	 */
	public synchronized static StockStorage getStockStorage() {
		final ThreadSafeStockStorage stockStorage = new ThreadSafeStockStorage();
		try {
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath(UnitedFormatHelper.toFilesystem("aapl").getFilename())));
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath(UnitedFormatHelper.toFilesystem("adm").getFilename())));
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath(UnitedFormatHelper.toFilesystem("spy").getFilename())));
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
		final ThreadSafeStockStorage stockStorage = new ThreadSafeStockStorage();
		try {
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath(UnitedFormatHelper.toFilesystem(stockName).getFilename())));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException(e.getMessage());
		}
		return stockStorage;
	}

}