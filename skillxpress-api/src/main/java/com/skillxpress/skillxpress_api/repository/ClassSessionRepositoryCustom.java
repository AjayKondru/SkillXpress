package com.skillxpress.skillxpress_api.repository;

public interface ClassSessionRepositoryCustom  {
    /** returns true if student added, false if class full */
    boolean addStudentIfSpace(String classId, String studentId);



}
