package io.mongock.driver.simpsonsdb.driver;

import com.simpsonsdb.SimpsonsDBClient;
import io.mongock.driver.api.entry.ChangeEntryService;
import io.mongock.driver.core.driver.NonTransactionalConnectionDriverBase;
import io.mongock.driver.core.lock.LockRepository;
import io.mongock.driver.simpsonsdb.repository.SimpsonsDBChangeEntryRepository;
import io.mongock.driver.simpsonsdb.repository.SimpsonsDBLockRepository;

import static io.mongock.utils.Constants.DEFAULT_LOCK_ACQUIRED_FOR_MILLIS;
import static io.mongock.utils.Constants.DEFAULT_QUIT_TRYING_AFTER_MILLIS;
import static io.mongock.utils.Constants.DEFAULT_TRY_FREQUENCY_MILLIS;

public class SimpsonsDBDriver extends NonTransactionalConnectionDriverBase {


    private final SimpsonsDBClient client;
    protected SimpsonsDBChangeEntryRepository changeEntryRepository;
    protected SimpsonsDBLockRepository lockRepository;

    //Custom fields
    private String configParameter;

    ////////////////////////////////////////////////////////////
    //BUILDER METHODS
    ////////////////////////////////////////////////////////////

    public static SimpsonsDBDriver withDefaultLock(SimpsonsDBClient client) {
        return SimpsonsDBDriver.withLockStrategy(client, DEFAULT_LOCK_ACQUIRED_FOR_MILLIS, DEFAULT_QUIT_TRYING_AFTER_MILLIS, DEFAULT_TRY_FREQUENCY_MILLIS);
    }

    public static SimpsonsDBDriver withLockStrategy(SimpsonsDBClient client,
                                                    long lockAcquiredForMillis,
                                                    long lockQuitTryingAfterMillis,
                                                    long lockTryFrequencyMillis) {
        return new SimpsonsDBDriver(client, lockAcquiredForMillis, lockQuitTryingAfterMillis, lockTryFrequencyMillis);
    }

    /**
     * This is just an example. It's very likely that something like database engine client will be required in the constructor,
     * but it may be different or require other parameters.
     *
     * @param client the database engine client.
     */
    protected SimpsonsDBDriver(SimpsonsDBClient client,
                               long lockAcquiredForMillis,
                               long lockQuitTryingAfterMillis,
                               long lockTryFrequencyMillis) {
        super(lockAcquiredForMillis, lockQuitTryingAfterMillis, lockTryFrequencyMillis);
        this.client = client;
    }


    /**
     * it returns the lockRepository. If not initialized yet, it's created
     *
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
     *
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


    //Custom parameters, specific to this driver
    public String getConfigParameter() {
        return configParameter;
    }

    public void setConfigParameter(String configParameter) {
        this.configParameter = configParameter;
    }
}
