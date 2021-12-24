package io.mongock.driver.simpsonsdb.springboot.config;

import com.simpsonsdb.SimpsonsDBClient;
import io.mongock.api.config.MongockConfiguration;
import io.mongock.driver.api.driver.ConnectionDriver;
import io.mongock.driver.simpsonsdb.driver.SimpsonsDBDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("${mongock.enabled:true}")
@ConditionalOnBean(MongockConfiguration.class)
@EnableConfigurationProperties(SimpsonsDBConfiguration.class)
public class SpringbootSimpsonsDBContext {

    @Bean
    public ConnectionDriver connectionDriver(SimpsonsDBClient client,
                                             MongockConfiguration monngockConfiguration,
                                             SimpsonsDBConfiguration simpsonsDBConfiguration) {
        SimpsonsDBDriver driver = SimpsonsDBDriver.withLockStrategy(
                client,
                monngockConfiguration.getLockAcquiredForMillis(),
                monngockConfiguration.getLockQuitTryingAfterMillis(),
                monngockConfiguration.getLockTryFrequencyMillis()
        );
        driver.setConfigParameter(simpsonsDBConfiguration.getConfigParameter());
        return driver;
    }
}
