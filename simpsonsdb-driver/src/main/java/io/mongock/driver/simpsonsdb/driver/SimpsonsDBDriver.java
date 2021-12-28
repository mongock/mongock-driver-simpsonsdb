package io.mongock.driver.simpsonsdb.driver;


import com.simpsonsdb.SimpsonsDBClient;

import static io.mongock.utils.Constants.DEFAULT_LOCK_ACQUIRED_FOR_MILLIS;
import static io.mongock.utils.Constants.DEFAULT_QUIT_TRYING_AFTER_MILLIS;
import static io.mongock.utils.Constants.DEFAULT_TRY_FREQUENCY_MILLIS;

/**
 * This is the class it will be used to instanstiate the driver.
 * You can see an example in the simpsonsdb-springboot-driver, class SpringbootSimpsonsDBContext
 */
public class SimpsonsDBDriver extends SimpsonsDBDriverBase {

  protected SimpsonsDBDriver(SimpsonsDBClient client,
                             long lockAcquiredForMillis,
                             long lockQuitTryingAfterMillis,
                             long lockTryFrequencyMillis) {
    super(client, lockAcquiredForMillis, lockQuitTryingAfterMillis, lockTryFrequencyMillis);
  }

  ////////////////////////////////////////////////////////////
  //BUILDER METHODS
  ////////////////////////////////////////////////////////////

  public static SimpsonsDBDriver withDefaultLock(SimpsonsDBClient client, String databaseName) {
    return SimpsonsDBDriver.withLockStrategy(client, DEFAULT_LOCK_ACQUIRED_FOR_MILLIS, DEFAULT_QUIT_TRYING_AFTER_MILLIS, DEFAULT_TRY_FREQUENCY_MILLIS);
  }

  public static SimpsonsDBDriver withLockStrategy(SimpsonsDBClient client,
                                                  long lockAcquiredForMillis,
                                                  long lockQuitTryingAfterMillis,
                                                  long lockTryFrequencyMillis) {
    return new SimpsonsDBDriver(client, lockAcquiredForMillis, lockQuitTryingAfterMillis, lockTryFrequencyMillis);
  }
}
