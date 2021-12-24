package io.mongock.driver.simpsonsdb.repository;

import io.mongock.driver.api.common.RepositoryIndexable;
import com.simpsonsdb.SimpsonsDBTable;
import io.mongock.utils.Process;

public class SimpsonsDBRepositoryBase implements Process, RepositoryIndexable {


  protected SimpsonsDBTable table;
  private final String[] uniqueFields;
  private boolean indexCreation = true;
  /**
   * This is just an example. It's very likely that something like a table is required, but it may differ or require
   * other parameters, depending on the driver's nature
   *
   * @param table the table/collection for the repository. In this case we use a dummy SimpsonsDBTale as example.
   * @param uniqueFields the list of fields that makes the required unique index. For changeEntryRepository, it should
   *                     be (executionId, changeId, author), fot LockEntryRepository should be "key"
   */
  public SimpsonsDBRepositoryBase(SimpsonsDBTable table, String[] uniqueFields) {
    this.table = table;
    this.uniqueFields = uniqueFields;
  }

  /**
   * - Checks the table/collection is created
   * - Checks there is unique index for uniqueFields in the table/collection
   * - if, for any reason, uniqueFields are not unique in the table and indexCreation = true,
   *   it should create the index for the uniqueFields. Otherwise it should throws a MongockException
   *
   * @see io.mongock.api.exception.MongockException
   */
  @Override
  public synchronized void initialize() {

  }

  /**
   * It just indicates if Mongock should crate the table and its indexes in case they are not already created.
   * @param indexCreation
   */
  @Override
  public void setIndexCreation(boolean indexCreation) {
    this.indexCreation = indexCreation;
  }
}
