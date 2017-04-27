package com.searchmetrics.audit.job.config.dao;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.searchmetrics.audit.job.config.dto.DomainConfig;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

/**
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CassandraConfig.class)
public class DomainConfigRepositoryIT {
    private static final Log LOGGER = LogFactory.getLog(DomainConfigRepositoryIT.class);

    public static final String KEYSPACE_CREATION_QUERY = "CREATE KEYSPACE IF NOT EXISTS audit_job_config WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '1' };";

    public static final String KEYSPACE_ACTIVATE_QUERY = "USE audit_job_config;";

    public static final String DATA_TABLE_NAME = "domainConfigs";

    // Added additional time to bring up the server, was failing on work machine...default is 10000 ms
    public static final Long EMBEDDED_SERVER_STARTUP_TIMEOUT = 30000L;

    @Autowired
    private DomainConfigRepository domainConfigRepository;

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @BeforeClass
    public static void startCassandraEmbedded() throws InterruptedException, TTransportException, ConfigurationException, IOException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(EMBEDDED_SERVER_STARTUP_TIMEOUT);
        final Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9142).build();
        LOGGER.info("Server Started at 127.0.0.1:9042... ");
        final Session session = cluster.connect();
        session.execute(KEYSPACE_CREATION_QUERY);
        session.execute(KEYSPACE_ACTIVATE_QUERY);
        LOGGER.info("KeySpace created and activated.");
    }

    @Before
    public void createTable() throws InterruptedException, TTransportException, ConfigurationException, IOException {
        adminTemplate.createTable(true, CqlIdentifier.cqlId(DATA_TABLE_NAME), DomainConfig.class, new HashMap<String, Object>());
    }

    @Test
    public void testCreateDomainConfig() {
        DomainConfig testConfig = new DomainConfig(
            "m.adidas.com/us/",
            false,
            false,
            null,
            false,
            null,
            null,
            null,
            null,
            DomainConfig.ProxyType.NONE.name(),
            20,
            null,
            DomainConfig.UserAgentType.SEARCHMETRICSBOT.name()
        );
        domainConfigRepository.save(testConfig);
        DomainConfig actualConfig = domainConfigRepository.findAll().iterator().next();

        if (null == actualConfig) {
            fail("Ya been hoodwinked!");
        }
        else {
            assertEquals(testConfig, actualConfig);
        }
    }

    @After
    public void dropTable() {
        adminTemplate.dropTable(CqlIdentifier.cqlId(DATA_TABLE_NAME));
    }

    @AfterClass
    public static void stopCassandraEmbedded() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
