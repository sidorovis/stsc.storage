package stsc.storage;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import stsc.common.stocks.Stock;
import stsc.common.stocks.StockLock;
import stsc.common.storage.StockStorage;

/**
 * This is thread safe {@link Stock} storage. (Do not store thread-safe Stock
 * elements). But updateStock / getStock / getStockNames methods are thread
 * safe.<br/>
 * This storage used as base class for another thread-safe stock storages.
 * 
 * @mark Read-Write {@link StockStorage}.
 */
public class ThreadSafeStockStorage implements StockStorage {

	protected final ConcurrentHashMap<String, StockLock> datafeed = new ConcurrentHashMap<String, StockLock>();

	public ThreadSafeStockStorage() {
		super();
	}

	@Override
	public Optional<Stock> getStock(final String name) {
		final StockLock stockLock = datafeed.get(name);
		if (stockLockIsNull(stockLock))
			return Optional.empty();
		final Stock stock = stockLock.getStock();
		return Optional.of(stock);
	}

	@Override
	public void updateStock(final Stock stock) {
		final String stockName = stock.getInstrumentName();
		final StockLock stockLock = datafeed.get(stockName);
		if (stockLockIsNull(stockLock))
			datafeed.put(stockName, new StockLock(stock));
		else
			stockLock.updateStock(stock);
	}

	@Override
	public Set<String> getStockNames() {
		return datafeed.keySet();
	}

	private boolean stockLockIsNull(final StockLock stockLock) {
		return stockLock == null;
	}

}