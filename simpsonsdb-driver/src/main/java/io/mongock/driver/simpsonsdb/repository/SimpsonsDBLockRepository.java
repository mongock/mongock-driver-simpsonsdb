package io.mongock.driver.simpsonsdb.repository;

import io.mongock.driver.core.lock.LockEntry;
import io.mongock.driver.core.lock.LockPersistenceException;
import io.mongock.driver.core.lock.LockRepository;
import com.simpsonsdb.SimpsonsDBTable;

import static io.mongock.driver.core.lock.LockEntry.KEY_FIELD;
/**
 * This class is basically the repository used for the lock in database. It's mainly used by the LockManager
 */
public class SimpsonsDBLockRepository extends SimpsonsDBRepositoryBase implements LockRepository {

  /**
   * This is just an example. It's very likely that something like a table is required, but it may differ or require
   * other parameters, depending on the driver's nature
   *
   * @param table the table/collection for the repository. In this case we use a dummy SimpsonsDBTale as example.
   */
  public SimpsonsDBLockRepository(SimpsonsDBTable table) {
    super(table, new String[]{KEY_FIELD});
  }

  /**
   * If there is a lock in the database with the same key, updates it if either is expired or both share the same owner.
   * If there is no lock with the same key, it's inserted.
   *
   * @param newLock lock to replace the existing one or be inserted.
   * @throws  LockPersistenceException if there is a lock in database with same key, but is expired and belong to
   *                                  another owner or cannot insert/update the lock for any other reason
   */
  @Override
  public void insertUpdate(LockEntry newLock)  {

  }

  /**
   * If there is a lock in the database with the same key and owner, updates it.Otherwise, throws a LockPersistenceException
   *
   * @param newLock lock to replace the existing one.
   * @throws LockPersistenceException if there is no lock in the database with the same key and owner or cannot update
   *                                  the lock for any other reason
   */
  @Override
  public void updateIfSameOwner(LockEntry newLock)  {
  }

  /**
   * Retrieves a lock by key
   *
   * @param lockKey key
   * @return LockEntry
   */
  @Override
  public LockEntry findByKey(String lockKey) {

    return null;
  }

  /**
   * Removes from database all the locks with the same key(only can be one) and owner
   *
   * @param lockKey lock key
   * @param owner   lock owner
   */
  @Override
  public void removeByKeyAndOwner(String lockKey, String owner) {

  }

}
