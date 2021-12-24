package io.mongock.driver.simpsonsdb.driver;

import io.mongock.driver.api.entry.ChangeEntryService;
import io.mongock.driver.core.driver.NonTransactionalConnectionDriverBase;
import io.mongock.driver.core.lock.LockRepository;
import io.mongock.driver.simpsonsdb.repository.SimpsonsDBChangeEntryRepository;
import io.mongock.driver.simpsonsdb.repository.SimpsonsDBLockRepository;
import com.simpsonsdb.SimpsonsDBClient;

public abstract class SimpsonsDBDriverBase extends NonTransactionalConnectionDriverBase {


  private final SimpsonsDBClient client;
  protected SimpsonsDBChangeEntryRepository changeEntryRepository;
  protected SimpsonsDBLockRepository lockRepository;

  /**
   * This is just an example. It's very likely that something like database engine client will be required in the constructor,
   * but it may be different or require other parameters.
   * @param client the database engine client.
   */
  protected SimpsonsDBDriverBase(SimpsonsDBClient client,
                                 long lockAcquiredForMillis,
                                 long lockQuitTryingAfterMillis,
                                 long lockTryFrequencyMillis) {
    super(lockAcquiredForMillis, lockQuitTryingAfterMillis, lockTryFrequencyMillis);
    this.client = client;
  }


  /**
   * it returns the lockRepository. If not initialized yet, it's created
   * @return lockRepository
   */
  @Override
  protected synchronized LockRepository getLockRepository() {
    if (lockRepository == null) {
      lockRepository = new SimpsonsDBLockRepository(client.getTable(getLockRepositoryName()));
      lockRepository.setIndexCreation(isIndexCreation());
    }
    return lockRepository;
  }

  /**
   * it returns the ChangeEntryRepository. If not initialized yet, it's created
   * @return ChangeEntryRepository
   */
  @Override
  public synchronized ChangeEntryService getChangeEntryService() {
    if (changeEntryRepository == null) {
      changeEntryRepository = new SimpsonsDBChangeEntryRepository(client.getTable(getMigrationRepositoryName()));
      changeEntryRepository.setIndexCreation(isIndexCreation());
    }
    return changeEntryRepository;
  }




}
