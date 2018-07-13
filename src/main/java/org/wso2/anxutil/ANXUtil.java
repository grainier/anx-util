/*
 *  Copyright (c) 2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.anxutil;

import org.apache.axiom.om.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Generic utility methods for analytics data source implementations.
 */
public class ANXUtil {

    private static final String ANALYTICS_USER_TABLE_PREFIX = "ANX";

    public static void main(String[] args) {
        System.out.println(generateTableUUID(-1, "grainier"));
    }

    /**
     * This method is used to generate an UUID from the target table name, to make sure, it is a compact
     * name that can be fitted in all the supported RDBMSs. For example, Oracle has a table name
     * length of 30. So we must translate source table names to hashed strings, which here will have
     * a very low probability of clashing.
     */
    public static String generateTableUUID(int tenantId, String tableName) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(byteOut);
            dout.writeInt(tenantId);
            /* we've to limit it to 64 bits */
            dout.writeInt(tableName.hashCode());
            dout.close();
            byteOut.close();
            String result = Base64.encode(byteOut.toByteArray());
            result = result.replace('=', '_');
            result = result.replace('+', '_');
            result = result.replace('/', '_');
            /* a table name must start with a letter */
            return ANALYTICS_USER_TABLE_PREFIX + result;
        } catch (IOException e) {
            /* this will never happen */
            throw new RuntimeException(e);
        }
    }
}
