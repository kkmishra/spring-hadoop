/*
 * Copyright 2011-2012 the original author or authors.
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
package org.springframework.data.hadoop.cascading;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.hadoop.TestUtils;
import org.springframework.data.hadoop.batch.JobsTrigger;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Costin Leau
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CascadingBatchTest {

	{
		TestUtils.hackHadoopStagingOnWin();
	}

	@Autowired
	ApplicationContext ctx;

	@Test
	public void testSanity() {
		assertTrue(ctx.containsBean("copyFlow"));
	}

	//@Test
	public void testCascadeTasklet() throws Exception {
		List<JobExecution> startJobs = JobsTrigger.startJobs(ctx);
		assertFalse(startJobs.isEmpty());

		// check records
		Collection<StepExecution> steps = startJobs.get(0).getStepExecutions();
		for (StepExecution stepExecution : steps) {
			if ("do-cascade".equals(stepExecution.getStepName())) {
				assertTrue(stepExecution.getReadCount() > 0);
			}
		}
	}
}
