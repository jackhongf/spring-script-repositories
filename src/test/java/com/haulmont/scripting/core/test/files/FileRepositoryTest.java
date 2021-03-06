package com.haulmont.scripting.core.test.files;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import com.haulmont.scripting.repository.config.AnnotationConfig;
import com.haulmont.scripting.repository.evaluator.ScriptEvaluationException;
import com.haulmont.scripting.repository.factory.ScriptRepositoryFactoryBean;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@ContextConfiguration(locations = {"classpath:com/haulmont/scripting/core/test/files/files-test-spring.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class FileRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(FileRepositoryTest.class);

    @Autowired
    private CustomerScriptRepository repo;

    @Autowired
    private ScriptRepositoryFactoryBean scriptRepositoryFactoryBean;

    @Before
    public void setUp() throws Exception {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }

    @Test
    public void testRunSimpleScript() {
        UUID customerId = UUID.randomUUID();
        String newName = RandomStringUtils.randomAlphabetic(8);
        String s = repo.renameCustomer(customerId, newName);
        log.info(s);
        assertNotNull(s);
        assertTrue(s.contains(customerId.toString()));
        assertTrue(s.contains(newName));
    }

    @Test
    public void testCreateObject() throws ParseException {
        String newName = RandomStringUtils.randomAlphabetic(8);
        Date birthDate = DateUtils.parseDate("1988-12-14", "yyyy-MM-dd");
        Customer c = repo.createCustomer(newName, birthDate);
        log.info("Customer: {}", c);
        assertEquals(newName, c.getName());
        assertEquals(birthDate, c.getBirthDate());
        assertNotNull(c.getId());

        assertEquals(Arrays.asList(c.getId().toString(), newName, birthDate.toString()), c.getMyData());

    }

    @Test
    public void testScriptMetadata() {
        Map<Method, AnnotationConfig> scripsMetadata = scriptRepositoryFactoryBean.getMethodInvocationsInfo();
        assertEquals(6, scripsMetadata.size());
        List<String> methods = scripsMetadata.keySet().stream().map(Method::getName).collect(Collectors.toList());
        assertTrue(methods.containsAll(
                Arrays.asList("renameCustomer", "createCustomer", "getDefaultName", "getDefaultError", "sayHello", "printData")));
    }

    @Test
    public void testDefaultMethodExecution(){
        String defaultName = repo.getDefaultName();
        assertEquals("NewCustomer", defaultName);
    }


    @Test(expected = ScriptEvaluationException.class)
    public void testErrorMethodExecution() {
        String errorLine = repo.getDefaultError();
        fail("Non-default method without an underlying script must throw an error instead of returning result: "+errorLine);
    }

    @Test
    public void testZeroArgScript(){
        String hello = repo.sayHello();
        assertEquals("Hello!", hello);
    }

    @Test
    public void testVoidMethods() {
        repo.printData("Test Data ");
    }

    @Test
    public void testVoidNullParamsMethods() {
        repo.printData(null);
    }

}