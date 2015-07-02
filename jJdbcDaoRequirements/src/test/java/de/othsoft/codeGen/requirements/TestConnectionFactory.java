/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.othsoft.codeGen.requirements;

import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory;
import java.io.File;
import java.io.FileReader;
import org.junit.Test;
import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.BeforeClass;


/**
 *
 * @author eiko
 */
public class TestConnectionFactory {
    private static Properties testProperties;

    @BeforeClass
    public static void setUpClass() {
        String userName = System.getenv("USER");
        try {
        testProperties = new Properties();
        File f = new File ("src/test/resources/config/"+userName+".properties");
        assertTrue("can't find config file",f.exists());
        testProperties.load( new FileReader( f ) );

        assertNotNull(testProperties.get("dbUser"));
        assertNotNull(testProperties.get("dbPwd"));
        assertNotNull(testProperties.get("dbConStr"));
        assertNotNull(testProperties.get("dbTestQuery"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createFactory() throws SQLException, DaoException {
        ConnectionFactory conFactory = new ConnectionFactory();
        conFactory.setConStr ((String) testProperties.get("dbConStr"));
        conFactory.setUserStr ((String) testProperties.get("dbUser"));
        conFactory.setPwdStr ((String) testProperties.get("dbPwd"));
        conFactory.setTestStr ((String) testProperties.get("dbTestQuery"));

        Connection con1 = conFactory.getCon();
        assertEquals( 1, conFactory.getPoolActiveConCount());

        Connection con2 = conFactory.getCon();
        assertEquals( 2, conFactory.getPoolActiveConCount());

        Connection con3 = conFactory.getCon();
        assertEquals( 3, conFactory.getPoolActiveConCount());

        Connection con4 = conFactory.getCon();
        assertEquals( 4, conFactory.getPoolActiveConCount());

        Connection con5 = conFactory.getCon();
        assertEquals( 5, conFactory.getPoolActiveConCount());
        
        con1.close();
        assertEquals( 4, conFactory.getPoolActiveConCount());

        con4.close();
        assertEquals( 3, conFactory.getPoolActiveConCount());

        con3.close();
        assertEquals( 2, conFactory.getPoolActiveConCount());

        con2.close();
        assertEquals( 1, conFactory.getPoolActiveConCount());
    
        con5.close();
        assertEquals( 0, conFactory.getPoolActiveConCount());

        con1 = conFactory.getCon();
        assertEquals( 1, conFactory.getPoolActiveConCount());
        
        con1.close();
    }

}
