package stsc.storage;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Set;
import java.util.TimeZone;

import com.google.common.collect.Sets;

import stsc.common.stocks.UnitedFormatStock;
import stsc.common.storage.StockStorage;

/**
 * StockStorageFactory is a factory for {@link ThreadSafeStockStorage}. <br/>
 * Main idea is to provide possibility to load only predefined set of stock (by
 * names).<br/>
 * Has methods to load {@link StockStorage} stock defined by name and to load
 * set of stocks defined by names.
 */
public final class StockStorageFactory {

	static {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public StockStorageFactory() {
		// use static method to create StockStorage
	}

	public StockStorage createStockStorage(final String stockName, final String filterDataFolderPath) throws ClassNotFoundException, IOException, InterruptedException {
		return createStockStorage(Sets.newHashSet(new String[] { stockName }), filterDataFolderPath);
	}

	public StockStorage createStockStorage(final Set<String> stockNames, final String filterDataFolderPath) throws ClassNotFoundException, IOException, InterruptedException {
		return createStockStorage(stockNames, FileSystems.getDefault().getPath(filterDataFolderPath));
	}

	public StockStorage createStockStorage(final Set<String> stockNames, final Path filterDataPath) throws ClassNotFoundException, IOException, InterruptedException {
		final StockStorage stockStorage = new ThreadSafeStockStorage();
		for (String name : stockNames) {
			final String path = filterDataPath.resolve(name + UnitedFormatStock.EXTENSION).toString();
			stockStorage.updateStock(UnitedFormatStock.readFromUniteFormatFile(path));
		}
		return stockStorage;
	}

}
