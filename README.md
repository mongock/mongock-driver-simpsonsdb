# Driver creation

This document explains how to create a new non-transactional driver for Mongock.

For this example we use an imaginary database called `SimpsonsDB`, which is represented by the module with the same name.

Let's crack...

## 1. Get assigned to develop the driver
If you have decided to develop a new driver, you either took a ticket published by the Mongock team or you need a driver
and want to crate it yourself.

In any case, this needs to be notified to the Mongock team, so we ensure there are no conflicts with other contributors 
and the required repository and any other resource are created.

If there is a issue created for this driver, please write a comment asking for it and we'll answer asap. Otherwise, please
feel free email us to [development@mongock.i](mailto:development@mongock.i)

## Parent project
### 2. Create project
Create a multi-module project called `mongock-driver-DATABASE_NAME`, where `DATABASE_NAME` is the name of the database 
this driver is targeting, it could mongodb, dymnamod, cassandra, etc.

In this example `mongock-driver-simpsonsdb`

### 3. Configure project pom
As mentioned in the step(1), it's a multi-module project, that means you need to use `<packaging>pom</packaging>`

You also need the following:

- Set Mongock drivers as parent
```xml
  <parent>
    <groupId>io.mongock</groupId>
    <artifactId>drivers</artifactId>
    <version>LATEST_MONGOCK_VERSION</version>
  </parent>
```

- Target JDK 8
```xml
  <properties>
    <java.version>1.8</java.version>
    <maven.compiler.target>1.8</maven.compiler.target>
    <maven.compiler.source>1.8</maven.compiler.source>
  </properties>
```

- Configure Mongock's bom
```xml
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>io.mongock</groupId>
        <artifactId>mongock-core-bom</artifactId>
        <version>LATEST_MONGOCK_VERSION</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
```

- Beta version. All the new drivers are released a BETA until the pass the maturity stage
```xml
  <version>LATEST_MONGOCK_VERSION.BETA-SNAPSHOT</version>
```

## Main driver module
This module contains the driver's logic and represents the core of the driver.

### 4. Create driver module
Inside the project create a module with te name `DATABASE_NAME-driver`, in this example `simpsonsdb-driver`


### 5. Configure driver module's pom
- Adding Mongock dependencies
```xml
    <dependency>
      <groupId>io.mongock</groupId>
      <artifactId>mongock-driver-core</artifactId>
    </dependency>

    <dependency>
      <groupId>io.mongock</groupId>
      <artifactId>mongock-api</artifactId>
    </dependency>

    <dependency>
      <groupId>io.mongock</groupId>
      <artifactId>mongock-utils</artifactId>
    </dependency>
```

- Add the required dependencies for the database, in this case `SimpsonsDB`
```xml
    <dependency>
      <groupId>com.simpsonsdb</groupId>
      <artifactId>simpsonsDB</artifactId>
      <version>1.0.0-SNAPSHOT</version>
    </dependency>
```
### 6. Create repositories package `io.mongock.driver.DATABASE_NAME.repository`

### 7. Implement the ChangeEntryService interface
This class is the class responsible for managing the change entries in the database. In this SimpsonsDB example we have 
created a parent class `SimpsonsDBRepositoryase` to hold some common code between the `SimpsonsDBChangeEntryRepository` 
and `SimpsonsDBLockRepository`, which will be explained in the next section.

In the class `io.mongock.driver.simpsonsdb.repository.SimpsonsDBChangeEntryRepository` you can see an example and 
the javadoc for the required methods.

### 8. Implement the LockRepository interface
This is the class responsible for managing the Lock in the database. In the class 
`io.mongock.driver.simpsonsdb.repository.SimpsonsDBLockRepository` you can see an example and the javadoc for the 
required methods.

### 9. Create driver package `io.mongock.driver.DATABASE_NAME.driver`

### 10. Create Driver class
This is the actual Drier, the one that is used by the users and Mongock autoconfiguration to generate the driver

It needs to extend the class `io.mongock.driver.core.driver.NonTransactionalConnectionDriverBase` and provide two static
builder methods. 

This is how these two static methods are implemented for the SimpsonsDB driver:


```java
    public static SimpsonsDBDriver withDefaultLock(SimpsonsDBClient client) {
        return SimpsonsDBDriver.withLockStrategy(client, DEFAULT_LOCK_ACQUIRED_FOR_MILLIS, DEFAULT_QUIT_TRYING_AFTER_MILLIS, DEFAULT_TRY_FREQUENCY_MILLIS);
    }

    public static SimpsonsDBDriver withLockStrategy(SimpsonsDBClient client,
                                                    long lockAcquiredForMillis,
                                                    long lockQuitTryingAfterMillis,
                                                    long lockTryFrequencyMillis) {
        return new SimpsonsDBDriver(client, lockAcquiredForMillis, lockQuitTryingAfterMillis, lockTryFrequencyMillis);
    }
```

### 11. Tests
For a Mongock driver to be submitted, tests are required. At the very minimum, integration tests validating the repositories.
We highly recommend using testcontainers for this.


## Springboot module
This module is about providing the required configuration to allow the driver to be used with Mongock autoconfiguration for Springoot.

### 12. Create Springboot module
Inside the project create a module with te name `DATABASE_NAME-springboot-driver`, in this example `simpsonsdb-springoot-driver`

### 13. Create package `io.mongock.driver.DATABASE_NAME.springboot.config`

### 14. Configure module's pom
- Import you recently created driver module. in this example:
```xml
        <dependency>
            <groupId>io.mongock</groupId>
            <artifactId>simpsonsdb-driver</artifactId>
            <version>${project.version}</version>
        </dependency>
```

- Import springboot dependencies
```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
            <version>${spring-boot.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <version>${spring-boot.version}</version>
            <optional>true</optional>
        </dependency>
```

### 15. Create custom database configuration, if required

If the driver requires any particular parameter, you need to create a configuration class annotated with
- `org.springframework.boot.context.properties.ConfigurationProperties("mongock.DATABASE_NAME-db")`
- `org.springframework.context.annotation.Configuration`

Take a look to the class `io.mongock.driver.simpsonsdb.springboot.config.SimpsonsDBConfiguration`

### 16. Create Context class
This Springboot context class will be used when Mongock is instructed to be autoconfigured. The context class
should be very similar to `io.mongock.driver.simpsonsdb.springboot.config.SpringbootSimpsonsDBContext`, it just needs 
to change teh references to the `SimpsonsDB`.

Notes:
- As mentioned in step(14), the configuration class is optional, if not required in this case, it can be removed.
- `driver.setConfigParameter(simpsonsDBConfiguration.getConfigParameter())` is also optional, if the driver doesn't have
any custom parameter, this can be removed too.
  
### 17. spring.factories
This file is required for Springoot to know which configuration classes to load. For this the file 
`src/resources/META-INF/spring.factories` needs to be created with the following content:
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  io.mongock.driver.DATABASE_NAME.springboot.config.CONTEXT_CLASS_NAME
```

## Driver BOM
At this point we have built a driver that can be used wit Mongock builder and Mongock Springboot autoconfiguration. As last
step we need to make the life of our users as easy as possible by providing a BOM.

As clarification, Mongock uses a federated BOM architecture, this means that every module(in this case a module it's a driver, runner, framework, etc) 
provides his own BOM, which is imported in the main Mongock BOM.

### 18. Create BOM module
Create module `mongock-driver-DATABASE_NAME-bom`

### 19. Configure BOM's pom
- Add set packaging `<packaging>pom</packaging>`
- Import the other two modules

## Final steps
Congrats, if you have got this far, we have a new driver for Mongock. You need to raise PR and will review it as soon as 
possible. Please make sure that there are solid tests covering the driver and you follow the best practices.

After the PR is reviewed and merged, the driver will be released and referenced in the main Mongock BOM.

Then the only missing part it's the documentation

### 20. Documentation
This [repository](https://github.com/mongock/mongock-docs)  is the one used Mongock uses for documentation. For this project 
and raise a PR in order to add the driver's documentation.

All the drivers are documented under the `driver` section. 

