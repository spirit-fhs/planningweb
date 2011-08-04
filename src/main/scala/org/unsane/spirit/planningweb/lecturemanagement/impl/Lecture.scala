package org.unsane.spirit.planningweb.lecturemanagement.impl

/**
 * This class is the representation of a lecture object
 *
 * @version 1.0
 * @author Christoph Schmidt
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