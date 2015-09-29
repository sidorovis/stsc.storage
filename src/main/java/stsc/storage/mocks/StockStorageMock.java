package stsc.storage.mocks;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import stsc.common.stocks.Stock;
import stsc.common.stocks.UnitedFormatStock;
import stsc.common.storage.StockStorage;
import stsc.storage.ThreadSafeStockStorage;

/**
 * Singleton Mock for testing or small development related tasks (when you would
 * like to debug some algorithm ot whatever).
 * 
 */
public final class StockStorageMock implements StockStorage {

	final static private String resourceToPath(final String resourcePath) throws URISyntaxException {
		return new File(StockStorageMock.class.getResource(resourcePath).toURI()).getAbsolutePath();
	}

	@Override
	public Optional<Stock> getStock(String name) {
		return Optional.empty();
	}

	@Override
	public void updateStock(Stock stock) {
	}

	@Override
	public Set<String> getStockNames() {
		return Sets.newHashSet(new String[] { "aapl", "adm", "spy" });
	}

	private static StockStorage stockStorage = null;

	public synchronized static StockStorage reset() {
		stockStorage = null;
		return getStockStorage();
	}

	/**
	 * Extended Singleton object. Data for this mock stored at resources folder.
	 * TODO worst singleton ever, it could be reseted.
	 * 
	 * @return {@link ThreadSafeStockStorage} stock storage with 'aapl', 'adm',
	 *         'spy' stocks in it.
	 */
	public synchronized static StockStorage getStockStorage() {
		if (stockStorage == null) {
			stockStorage = new ThreadSafeStockStorage();
			try {
				stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath("aapl.uf")));
				stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath("adm.uf")));
				stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(resourceToPath("spy.uf")));
			} catch (IOException | URISyntaxException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return stockStorage;
	}
}