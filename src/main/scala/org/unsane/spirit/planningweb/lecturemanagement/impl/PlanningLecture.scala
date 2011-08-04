package org.unsane.spirit.planningweb.lecturemanagement.impl

import org.unsane.spirit.planningweb.dozentmanagement.impl.Dozent

/**
 * This class is the representation of a planninglecture object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

case class PlanningLecture(name: String,
                           typeOfLecture: String,
                           groups: List[CourseSemester],
                           dozents: List[Dozent],
                           numberOfMembers: Int)