package org.unsane.spirit.planningweb.lecturemanagement.impl

import org.unsane.spirit.planningweb.dozentmanagement.impl.Dozent

/**
 * This class is the representation of a lecturerelationship object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */


case class LectureRelationship(these: List[CourseSemester], withThose: List[Dozent])