package ru.otus.demo;

import org.apache.commons.lang3.time.StopWatch;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.ArrayList;
import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final List<Long> INIT_IDS = new ArrayList<>();

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        saveClients(dbServiceClient, 100);

        /*
        Замер скорости получения клиентов без кеширования
         */
        StopWatch withoutCacheTime = new StopWatch();
        withoutCacheTime.start();
        List<Client> clientsFromDb = getClients(dbServiceClient, 50);
        withoutCacheTime.stop();
        long notCacheDeltaTime = withoutCacheTime.getTime();
        log.info("Время за которое были получены 100 клиентов без кэша, ms: {}", notCacheDeltaTime);

        /*
        Предварительный запрос клиентов и кэширование
         */

        StopWatch cacheTime = new StopWatch();
        cacheTime.start();
        List<Client> clientFromCache = getClients(dbServiceClient, 50);
        cacheTime.stop();
        long cachDeltTime = cacheTime.getTime();
        log.info("Время за которое были получены клиенты с использование кэша, ms: {}", cachDeltTime);

        log.info("Разница между кэшом и без кэша, ms: {}", notCacheDeltaTime - cachDeltTime);

        /*
        Очистка кэша
         */
        System.gc();


        StopWatch afterGcTime = new StopWatch();
        afterGcTime.start();
        List<Client> clientFromDbAfterGc = getClients(dbServiceClient, 50);
        afterGcTime.stop();
        long afterGcDeltaTime = afterGcTime.getTime();
        log.info("Время за которое были получены клиенты после очистки, ms: {}", afterGcDeltaTime);
    }

    private static List<Client> getClients(DBServiceClient dbServiceClient, int limit) {
        return INIT_IDS.stream()
                .limit(limit)
                .map(id -> dbServiceClient.getClient(id).get())
                .toList();
    }

    private static void saveClients(DbServiceClientImpl dbServiceClient, int amount) {
        for (int i = 0; i < amount; i++) {
            Client client = dbServiceClient.saveClient(createClient(1));
            INIT_IDS.add(client.getId());
        }
    }


    private static Client createClient(int index) {
        return new Client(null, "Vasya" + index,
                new Address(null, "AnyStreet"),
                List.of(
                        new Phone(null, "13-555-22" + index),
                        new Phone(null, "14-666-333" + index))
        );
    }
}
