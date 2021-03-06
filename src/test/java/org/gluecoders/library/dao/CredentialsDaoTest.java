package org.gluecoders.library.dao;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.gluecoders.library.config.Database;
import org.gluecoders.library.testHelpers.dao.FakeMongo;
import org.gluecoders.library.models.Credentials;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Anand_Rajneesh on 7/2/2017.
 */
@RunWith(SpringRunner.class)
@Import(value = {FakeMongo.class, Database.class})
@EnableMongoRepositories(basePackageClasses = {CredentialsDao.class})
public class CredentialsDaoTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CredentialsDao credentialsDao;

    @Rule
    public MongoDbRule embeddedMongoDbRule = newMongoDbRule().defaultSpringMongoDb("mockDB");

    private String username = "username";

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
    public void testFindDistinctByUsername_NoDataAvailable(){
        Credentials credentials = credentialsDao.findDistinctByUsername(username);
        assertNull("Credentials should be null when no data is available", credentials);
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT, locations = "/creds/credentials.json")
    public void testFindDistinctByUsername_NoDataMatching(){
        Credentials credentials = credentialsDao.findDistinctByUsername("notmatchingusername");
        assertNull("Credentials should be null when no match is available", credentials);
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT, locations = "/creds/credentials.json")
    public void testFindDistinctByUsername_DataMatching(){
        Credentials credentials = credentialsDao.findDistinctByUsername(username);
        assertNotNull("Credentials should be not null when match is available", credentials);
    }


}
