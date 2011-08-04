package org.unsane.spirit.planningweb.coursemanagement.impl

/**
 * This class is the representation of a course object
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

case class Course(name: String, shortcut: String, semesters: List[Semester])