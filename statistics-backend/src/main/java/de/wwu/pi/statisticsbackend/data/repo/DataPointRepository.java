package de.wwu.pi.statisticsbackend.data.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import de.wwu.pi.statisticsbackend.data.model.DataPoint;

public interface DataPointRepository extends JpaRepository<DataPoint, Long> {

}
