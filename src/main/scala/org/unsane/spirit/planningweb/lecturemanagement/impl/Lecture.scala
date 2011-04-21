package org.unsane.spirit.planningweb.lecturemanagement.impl

/**
 * This class is the representation of a lecture object
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

case class Lecture (name: String,
                    lectureType: LectureType,
                    courseInfos: List[CourseInformation],
                    hasLectureTogetherWith: List[LectureRelationship],
                    hasTutorialTogetherWith: List[LectureRelationship],
                    hoursOfLecture: Int,
                    hoursOfTutorial: Int,
                    inSummerSemester: Boolean,
                    inWinterSemester:Boolean)