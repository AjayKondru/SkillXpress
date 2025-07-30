package com.skillxpress.skillxpress_api.repository;

import com.skillxpress.skillxpress_api.dto.DashboardClassDto;
import com.skillxpress.skillxpress_api.model.ClassSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClassSessionRepository  extends MongoRepository<ClassSession, String>,
        ClassSessionRepositoryCustom {
    // Custom queries can be defined here
    @Query(value = """
            { grade: ?0,
              subject: ?1,
              startTime: { $gte: ?2, $lt: ?3 },
              $expr: { $lt: [ { $size: "$studentIds" }, "$capacity" ] }
            }""")
    List<ClassSession> search(int grade,
                              String subject,
                              LocalDateTime windowStart,
                              LocalDateTime windowEnd);

    @Query("""
  select new com.skillxpress_api.dto.DashboardClassDto(
      cs.id, cs.subject, cs.grade, t.fullName,
      cs.startTime, cs.endTime,
      concat('https://meet.jit.si/skillxpress-', cs.id)
  )
  from ClassSession cs
  join User t on t.id = cs.tutorId
  where :studentId in elements(cs.studentIds)
    and cs.endTime > current_timestamp
  order by cs.startTime
""")
    List<DashboardClassDto> findUpcomingForStudent(@Param("studentId") String studentId);


}
