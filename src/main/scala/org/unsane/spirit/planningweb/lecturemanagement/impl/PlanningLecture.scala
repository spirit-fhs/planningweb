package org.unsane.spirit.planningweb.lecturemanagement.impl

/**
 * This class is the representation of a planninglecture object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb.dozentmanagement.impl.Dozent

case class PlanningLecture(name: String,
                           typeOfLecture: String,
                           groups: List[CourseSemester],
                           dozents: List[Dozent],
                           numberOfMembers: Int)