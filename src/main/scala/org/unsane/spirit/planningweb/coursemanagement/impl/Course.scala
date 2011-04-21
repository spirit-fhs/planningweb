package org.unsane.spirit.planningweb.coursemanagement.impl

/**
 * This class is the representation of a course object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

case class Course(name: String, shortcut: String, semesters: List[Semester])