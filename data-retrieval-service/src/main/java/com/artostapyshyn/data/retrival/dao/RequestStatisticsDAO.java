package com.artostapyshyn.data.retrival.dao;

import com.artostapyshyn.data.retrival.model.RequestStatistics;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestStatisticsDAO extends MongoRepository<RequestStatistics, ObjectId> {
}
