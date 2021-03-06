/*
 * Copyright (c) 2010 Lockheed Martin Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eurekastreams.server.persistence.mappers.ldap;

import java.util.ArrayList;

import org.springframework.ldap.core.DistinguishedName;

/**
 * Represent LdapGroup.
 * 
 */
public class LdapGroup
{
    /**
     * {@link DistinguishedName}.
     */
    private DistinguishedName distinguishedName;

    /**
     * Optional inclusive list of groups that were traversed to locate this group.
     */
    private ArrayList<String> sourceList;

    /**
     * Constructor.
     * 
     * @param inDistinguishedName
     *            {@link DistinguishedName}.
     */
    public LdapGroup(final DistinguishedName inDistinguishedName)
    {
        distinguishedName = inDistinguishedName;
    }

    /**
     * @return the distinguishedName
     */
    public DistinguishedName getDistinguishedName()
    {
        return distinguishedName;
    }

    /**
     * @return the sourceList
     */
    public ArrayList<String> getSourceList()
    {
        return sourceList;
    }

    /**
     * @param inSourceList
     *            the sourceList to set
     */
    public void setSourceList(final ArrayList<String> inSourceList)
    {
        sourceList = inSourceList;
    }

}
