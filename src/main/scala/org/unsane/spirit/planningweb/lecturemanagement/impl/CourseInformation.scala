package org.unsane.spirit.planningweb.lecturemanagement.impl

import org.unsane.spirit.planningweb
import planningweb.coursemanagement.impl.Course

/**
 * This class is the representation of a courseinformation object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

case class CourseInformation (course: Course,
                              semesterInfos: List[SemesterInformation])