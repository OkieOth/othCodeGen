/*
Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements.
See the NOTICE file distributed with this work for additional information regarding copyright ownership.  
The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
specific language governing permissions and limitations under the License.
 */


package de.othsoft.codeGen.test.integration.generated

import de.gCodeGen.test.beans.Adresse
import de.gCodeGen.test.dao.jdbc.DataFactory_rman
import de.othsoft.codeGen.requirements.DaoException
import de.othsoft.codeGen.requirements.jdbc.ConnectionFactory
import static org.junit.Assert.*

/**
 *
 * @author eiko
 */
class InsertTestData {
    
    void dummy(DataFactory_rman dataFactory) {
        Adresse adresse = dataFactory.create_Adresse()
        adresse.setHausnr('12a')
        adresse.setLandIdTxt('Deutschland')
        adresse.setOrtIdTxt('Berlin')
        adresse.setStrIdTxt('Testweg')
        adresse.setBemerkung('this is a simple test entry')
        adresse.insert(null,null)
        
        assertNotNull (adresse.getId())
        
        Adresse adresse2 = dataFactory.byId_Adresse(null,null,adresse.getId())
        assertNotNull (adresse2)
        
        assertNotNull (adresse.getHausnr())
        assertNotNull (adresse2.getHausnr())
        assertEquals (adresse.getHausnr(),adresse.getHausnr())
        
        println ('try to get addresse3')
        Adresse adresse3 = dataFactory.byId_Adresse(null,null,adresse.getId())
        assertNotNull (adresse3)

        println ('try to delete addresse3')
        adresse3.delete(null,null)

        Adresse adresse4 = dataFactory.byId_Adresse(null,null,adresse.getId())
        assertNull (adresse4)

        println ('adresse3 deleted')

        // TODO - test for equality
        // assertEquals (adresse,adresse2)        
    }
}

