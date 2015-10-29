package stsc.storage;

import stsc.common.stocks.Stock;

/**
 * {@link StockLock} is a thread safe synchronizer for {@link Stock}
 */
public final class StockLock {

	private Stock stock;

	public StockLock(final Stock stock) {
		this.stock = stock;
	}

	public synchronized void updateStock(final Stock stock) {
		this.stock = stock;
	}

	public synchronized Stock getStock() {
		return stock;
	}
}