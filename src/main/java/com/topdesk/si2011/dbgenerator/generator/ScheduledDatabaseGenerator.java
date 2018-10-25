package com.topdesk.si2011.dbgenerator.generator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdesk.si2011.dbgenerator.communication.DatabaseCommunication;
import com.topdesk.si2011.dbgenerator.configurator.ConfigReader;
import com.topdesk.si2011.dbgenerator.dbcache.DbCache;
import com.topdesk.si2011.dbgenerator.dbstructure.IDbStructure;
import com.topdesk.si2011.dbgenerator.task.Task;
import com.topdesk.si2011.dbgenerator.task.TaskBuilder;

public class ScheduledDatabaseGenerator implements DatabaseGenerator {
	private final static Logger logger = LoggerFactory.getLogger(ScheduledDatabaseGenerator.class);

	@Override
	public void generate(ConfigReader configuration, IDbStructure structure,
			DatabaseCommunication dbComm) {
		TaskBuilder builder = new TaskBuilder(structure, configuration, new DbCache(dbComm, structure));
		List<Task> build = builder.build();
		
		int performedTasks = 0;
		int lastCount = 0;
		while(performedTasks < build.size()) {
			for(Task task : build) {
				if(task.canPerform()) {
					logger.debug("Performing " + task.getDescription());
					task.perform();
					performedTasks++;
				}
			}
			if (lastCount == performedTasks)
				throw new RuntimeException("Infinite loop detected, aborting...");
			lastCount = performedTasks;
		}
	}

}
