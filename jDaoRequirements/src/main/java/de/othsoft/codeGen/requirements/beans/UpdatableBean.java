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
package de.othsoft.codeGen.requirements.beans;

import de.othsoft.codeGen.requirements.CmdData;
import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.QueryRestr;
import de.othsoft.codeGen.requirements.RestrType;
import de.othsoft.codeGen.requirements.UserData;
import static java.lang.StrictMath.log;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eiko
 */
public abstract class UpdatableBean<T> extends InsertableBean {

    protected T origState = null;

    protected boolean changeble = false;

    public boolean isChangeble() {
        return changeble;
    }

    public void setChangeble(boolean changeble) {
        this.changeble = changeble;
    }

    @Override
    public void resetChanged() {
        super.resetChanged();
        origState = clone();
    }

    protected void resetChangedWithoutSaveOriginalState() {
        super.resetChanged();
    }

    public void update(CmdData cmdData, UserData userData) throws DaoException {
        // dummy implementation ... saves generated code
    }
    
    public T getOrigState() {
        return origState;
    }

    public abstract T clone();
}
