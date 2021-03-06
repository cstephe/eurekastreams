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
package org.eurekastreams.server.persistence.mappers.db.metrics;

import java.util.Date;

import javax.persistence.Query;

import org.eurekastreams.commons.date.DateDayExtractor;
import org.eurekastreams.server.persistence.mappers.BaseArgDomainMapper;

/**
 * DB Mapper to get the unique visitor count of a specific day.
 */
public class GetDailyUniqueVisitorCountDbMapper extends BaseArgDomainMapper<Date, Long>
{
    /**
     * Get the number of unique visitors on a specific day.
     * 
     * @param inDay
     *            the day to get data for
     * @return the number of unique visitors on the input day
     */
    @Override
    public Long execute(final Date inDay)
    {
        Date startOfDay, endOfDay;
        Query q;

        startOfDay = DateDayExtractor.getStartOfDay(inDay);
        endOfDay = DateDayExtractor.getEndOfDay(inDay);
        q = getEntityManager().createQuery(
                "SELECT COUNT(DISTINCT actorPersonId) FROM UsageMetric "
                        + "WHERE created >= :startDate AND created < :endDate").setParameter("startDate", startOfDay)
                .setParameter("endDate", endOfDay);

        return (Long) q.getSingleResult();
    }
}
