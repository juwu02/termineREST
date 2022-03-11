package com.mosbach.demo.model.task;

import com.mosbach.demo.SimpleAWSCredentials;
import com.mosbach.demo.dataManager.TaskManager;
import com.mosbach.demo.dataManagerImpl.PostgresTaskManagerImpl;
import com.mosbach.demo.dataManagerImpl.PropertyFileTaskManagerImpl;
import com.mosbach.demo.model.student.Student;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;


import java.util.Collection;

public class TaskList {
	
	private Student student;
	private Collection<Task> tasks;
	// TaskManager taskManager = PropertyFileTaskManagerImpl.getPropertyFileTaskManagerImpl("src/main/resources/TaskList.properties");
	TaskManager taskManager = PostgresTaskManagerImpl.getPostgresTaskManagerImpl();

	public TaskList() { }

	public TaskList(Student student) {
		this.student = student;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Collection<Task> getTasks() {
		return tasks;
	}

	public void setTasks() {
		tasks = taskManager.getAllTasks(new Student("me", "me"));
	}

	@SuppressWarnings("deprecation")
	public void addTask(Task task) {
		taskManager.addTask(task, new Student("me", "me"));


		

		AWSCredentials awsCredentials = new SimpleAWSCredentials();         
		AmazonSQS sqs = new AmazonSQSClient(awsCredentials);
		Region euCentral = Region.getRegion(Regions.EU_CENTRAL_1);
		sqs.setRegion(euCentral);
		// sqs.withDelaySeconds(1);

		SendMessageRequest send_msg_request = new SendMessageRequest()
		        .withQueueUrl("https://sqs.eu-central-1.amazonaws.com/923270384842/mosbach-task-organizer")
		        .withMessageBody("Added the following task: " + task.getName() + " with priority: " + task.getPriority());
		sqs.sendMessage(send_msg_request);

		
		
	}


}