/*
 * Copyright (c) 2010-2011 Lockheed Martin Corporation
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
package org.eurekastreams.server.action.execution;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import org.eurekastreams.commons.actions.context.Principal;
import org.eurekastreams.commons.actions.context.TaskHandlerActionContext;
import org.eurekastreams.server.domain.Person;
import org.eurekastreams.server.persistence.BackgroundMapper;
import org.eurekastreams.server.persistence.PersonMapper;
import org.eurekastreams.server.persistence.mappers.DomainMapper;
import org.eurekastreams.server.persistence.mappers.cache.CacheKeys;
import org.eurekastreams.server.search.modelview.PersonModelView;
import org.eurekastreams.server.testing.TestContextCreator;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * This class contains the test suite for the {@link UpdatePersonExecution} class.
 * 
 */
public class UpdatePersonExecutionTest
{
    /**
     * System under test.
     */
    private UpdatePersonExecution sut;

    /**
     * Context for building mock objects.
     */
    private final Mockery context = new JUnit4Mockery()
    {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };

    /**
     * Mocked instance of the {@link BackgroundMapper}.
     */
    private final BackgroundMapper backgroundMapperMock = context.mock(BackgroundMapper.class);

    /**
     * Mocked instance of the {@link PersonMapper}.
     */
    private final PersonMapper personMapperMock = context.mock(PersonMapper.class);

    /**
     * Mocked instance of the {@link PersistResourceExecution}.
     */
    private final PersistResourceExecution persistResourceExecutionMock = context.mock(PersistResourceExecution.class);

    /**
     * Mocked instance of the {@link Person}.
     */
    private final Person personMock = context.mock(Person.class);

    /**
     * Mocked isntance of the {@link Principal}.
     */
    private final Principal principalMock = context.mock(Principal.class);

    /**
     * Test fields to use in this suite.
     */
    private HashMap<String, Serializable> fields;

    /**
     * DAO for deleting cache keys.
     */
    private DomainMapper<Set<String>, Boolean> deleteCacheKeyDAO = context.mock(DomainMapper.class);

    /**
     * Prepare the system under test.
     */
    @Before
    public void setup()
    {
        sut = new UpdatePersonExecution(personMapperMock, persistResourceExecutionMock, backgroundMapperMock,
                deleteCacheKeyDAO);
        fields = new HashMap<String, Serializable>();
        fields.put("skills", "stuff, things");
    }

    /**
     * Perform successful test of execute.
     */
    @Test
    public void testExecute()
    {
        context.checking(new Expectations()
        {
            {
                oneOf(persistResourceExecutionMock).execute(with(any(TaskHandlerActionContext.class)));
                will(returnValue(personMock));

                oneOf(personMock).getOpenSocialId();
                will(returnValue("53"));

                oneOf(personMock).getId();
                will(returnValue(5L));

                oneOf(deleteCacheKeyDAO).execute(Collections.singleton(CacheKeys.PERSON_BY_ID + 5L));

                oneOf(backgroundMapperMock).findOrCreatePersonBackground("53");

                oneOf(personMapperMock).flush();
            }
        });

        sut.execute(TestContextCreator.createTaskHandlerContextWithPrincipal(fields, principalMock));
        context.assertIsSatisfied();
    }

    /**
     * Perform successful test of execute with skills.
     */
    @Test
    public void testExecuteWithSkills()
    {
        fields.put("skills", "stuff, things");

        context.checking(new Expectations()
        {
            {
                oneOf(persistResourceExecutionMock).execute(with(any(TaskHandlerActionContext.class)));
                will(returnValue(personMock));

                oneOf(personMock).getOpenSocialId();
                will(returnValue("53"));
                oneOf(backgroundMapperMock).findOrCreatePersonBackground("53");

                oneOf(personMapperMock).flush();

                oneOf(personMock).getId();
                will(returnValue(5L));

                oneOf(deleteCacheKeyDAO).execute(Collections.singleton(CacheKeys.PERSON_BY_ID + 5L));
            }
        });

        sut.execute(TestContextCreator.createTaskHandlerContextWithPrincipal(fields, principalMock));
        context.assertIsSatisfied();
    }

    /**
     * Perform successful test of execute with skills.
     */
    @Test
    public void testExecuteWithNoSkills()
    {
        fields.put("skills", "");

        context.checking(new Expectations()
        {
            {
                oneOf(persistResourceExecutionMock).execute(with(any(TaskHandlerActionContext.class)));
                will(returnValue(personMock));

                oneOf(personMock).getOpenSocialId();
                will(returnValue("53"));
                oneOf(backgroundMapperMock).findOrCreatePersonBackground("53");

                oneOf(personMapperMock).flush();

                oneOf(personMock).getId();
                will(returnValue(5L));

                oneOf(deleteCacheKeyDAO).execute(Collections.singleton(CacheKeys.PERSON_BY_ID + 5L));
            }
        });

        sut.execute(TestContextCreator.createTaskHandlerContextWithPrincipal(fields, principalMock));
        context.assertIsSatisfied();
    }

    /**
     * Tests that non-client-settable fields are not passed to the DAO.
     */
    @Test
    public void testExecuteRemoveBlockedFields()
    {
        fields.put("skills", "");

        // make sure these are pulled out
        fields.put("isAdministrator", true);
        fields.put(PersonModelView.CELLPHONE_KEY, "222-333-4444");
        fields.put(PersonModelView.FAX_KEY, "999-999-9999");

        context.checking(new Expectations()
        {
            {
                oneOf(persistResourceExecutionMock).execute(with(any(TaskHandlerActionContext.class)));
                will(returnValue(personMock));

                oneOf(personMock).getOpenSocialId();
                will(returnValue("53"));
                oneOf(backgroundMapperMock).findOrCreatePersonBackground("53");

                oneOf(personMapperMock).flush();

                oneOf(personMock).getId();
                will(returnValue(5L));

                oneOf(deleteCacheKeyDAO).execute(Collections.singleton(CacheKeys.PERSON_BY_ID + 5L));
            }
        });

        sut.execute(TestContextCreator.createTaskHandlerContextWithPrincipal(fields, principalMock));
        context.assertIsSatisfied();
        Assert.assertFalse(fields.containsKey("isAdministrator"));
        Assert.assertFalse(fields.containsKey(PersonModelView.CELLPHONE_KEY));
        Assert.assertFalse(fields.containsKey(PersonModelView.FAX_KEY));
    }
}
