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

package de.othsoft.codeGen.requirements;

import org.slf4j.Logger;

/**
 *
 * @author eiko
 */
public class DaoException extends Exception {

    /**
     * Creates a new instance of <code>DaoException</code> without detail
     * message.
     */
    public DaoException() {
    }

    /**
     * Constructs an instance of <code>DaoException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DaoException(String msg) {
        super(msg);
    }

    public DaoException(Logger logger,String msg) {
        super(msg);
        logger.error(msg);
    }

    public DaoException(Logger logger,Exception e) {
        super(e);
        logger.error("["+e.getClass()+"] "+e.getMessage());
    }

    public DaoException(Logger logger,Exception e,String location) {
        super(e);
        logger.error(location + " -> ["+e.getClass()+"] "+e.getMessage());
    }
}
