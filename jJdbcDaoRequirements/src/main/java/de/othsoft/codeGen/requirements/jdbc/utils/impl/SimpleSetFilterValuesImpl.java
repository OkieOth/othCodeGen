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
package de.othsoft.codeGen.requirements.jdbc.utils.impl;

import de.othsoft.codeGen.requirements.DaoException;
import de.othsoft.codeGen.requirements.QueryRestr;
import de.othsoft.codeGen.requirements.jdbc.utils.ISetFilterValuesImpl;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author eiko
 */
public class SimpleSetFilterValuesImpl implements ISetFilterValuesImpl {
    private int setOneFilterValue(int parameterIndex, PreparedStatement ps, QueryRestr r) throws SQLException, DaoException {
        List filterValues = r.getFilterValues();
        if (filterValues==null || filterValues.isEmpty())
            throw new DaoException ("no filter value is given: " + r.getId());
        ps.setObject(parameterIndex, filterValues.get(0));
        return parameterIndex+1;
    }

    private int setTwoFilterValues(int parameterIndex, PreparedStatement ps, QueryRestr r) throws SQLException, DaoException {
        List filterValues = r.getFilterValues();
        if (filterValues==null || filterValues.isEmpty())
            throw new DaoException ("no filter value is given: " + r.getId());
        if (filterValues.size()<2)
            throw new DaoException ("need tow filter values: " + r.getId());
        ps.setObject(parameterIndex, filterValues.get(0));
        parameterIndex++;
        ps.setObject(parameterIndex, filterValues.get(1));
        return parameterIndex+1;
    }

    @Override
    public void setFilterValues(PreparedStatement ps, List<QueryRestr> restr) throws SQLException, DaoException {
        int parameterIndex=1;
        for (QueryRestr r:restr) {
            switch(r.getType()) {
                case EQUAL:
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case NULL:
                    break;
                case LARGER:
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case SMALLER:
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case LARGEREQUAL:
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case SMALLEREQUAL:
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case LIKE:
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case IN:
                    // TODO - currently only one value is used, but multi values are better
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case LARGER_AND_SMALLEREQUAL:
                    parameterIndex = setTwoFilterValues(parameterIndex,ps,r);
                    break;
                case LARGEREQUAL_AND_SMALLER:
                    parameterIndex = setTwoFilterValues(parameterIndex,ps,r);
                    break;
                case NOT_EQUAL:
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case NOT_NULL:
                    break;
                case NOT_LIKE:
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case NOT_IN:
                    // TODO - currently only one value is used, but multi values are better
                    parameterIndex = setOneFilterValue(parameterIndex,ps,r);
                    break;
                case NOT_LARGER_AND_SMALLEREQUAL:
                    parameterIndex = setTwoFilterValues(parameterIndex,ps,r);
                    break;
                case NOT_LARGEREQUAL_AND_SMALLER:
                    parameterIndex = setTwoFilterValues(parameterIndex,ps,r);
                    break;
                case VALUE_OR_NULL:
                    parameterIndex = setTwoFilterValues(parameterIndex,ps,r);
                    break;
                case RAW:
                    break;
                default:
                    throw new DaoException("unknown restriction type"+r.getType());
            }
        }
    }
}
