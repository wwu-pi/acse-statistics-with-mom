package de.wwu.pi.statisticsbackend.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import de.wwu.pi.statisticsbackend.data.model.DataPoint;
import de.wwu.pi.statisticsbackend.data.repo.DataPointRepository;

@Component
public class StatisticsListener {
	
	@Autowired
	private DataPointRepository repo;
	
	@JmsListener(destination = "StatisticsQueue")
	public void listen(Double x) {
		DataPoint dp = new DataPoint(x);
		repo.save(dp);
		System.out.println(x);
	}

}
