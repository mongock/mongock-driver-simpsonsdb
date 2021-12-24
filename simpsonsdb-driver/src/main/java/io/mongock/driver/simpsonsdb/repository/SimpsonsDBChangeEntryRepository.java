package io.mongock.driver.simpsonsdb.repository;

import io.mongock.api.exception.MongockException;
import io.mongock.driver.api.entry.ChangeEntry;
import io.mongock.driver.api.entry.ChangeEntryService;
import com.simpsonsdb.SimpsonsDBTable;

import java.util.Collections;
import java.util.List;

import static io.mongock.driver.api.entry.ChangeEntry.KEY_AUTHOR;
import static io.mongock.driver.api.entry.ChangeEntry.KEY_CHANGE_ID;
import static io.mongock.driver.api.entry.ChangeEntry.KEY_EXECUTION_ID;

/**
 * This class is basically the repository used for the change entries in database
 */
public class SimpsonsDBChangeEntryRepository extends SimpsonsDBRepositoryBase implements ChangeEntryService {


    /**
     * This is just an example. It's very likely that something like a table is required, but it may differ or require
     * other parameters, depending on the driver's nature
     *
     * @param table the table/collection for the repository. In this case we use a dummy SimpsonsDBTale as example.
     */
    public SimpsonsDBChangeEntryRepository(SimpsonsDBTable table) {
        super(table, new String[]{KEY_EXECUTION_ID, KEY_AUTHOR, KEY_CHANGE_ID});
    }

    /**
     * @return all the changeEntries in the table/collection
     */
    @Override
    public List<ChangeEntry> getEntriesLog() {
        return Collections.emptyList();
    }

    /**
     * If there is already an ChangeEntry with the same  key(executionId, id and author), it's updated. Otherwise,
     * the new changeEntry is inserted.
     *
     * @param changeEntry Entry to be upsert
     * @throws MongockException if any i/o exception occurs
     */
    @Override
    public void saveOrUpdate(ChangeEntry changeEntry) throws MongockException {

    }


}
