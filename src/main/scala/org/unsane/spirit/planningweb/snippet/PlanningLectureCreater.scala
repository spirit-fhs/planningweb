package org.unsane.spirit.planningweb.snippet

/**
 * This object creates the PlanningLectures for a summer or winter semester
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl._
import planningweb.coursemanagement.impl._
import planningweb.lecturemanagement.impl._

object PlanningLectureCreater {

  // function creates a list of planninglectures for one lecture
  private def buildPlanningLecture(lecture: Lecture): List[PlanningLecture] = {
    val minHouresOfLecture = 1
    val maxHouresOfLecture = lecture.hoursOfLecture
    val minHouresOfTutorial = 1
    val maxHouresOfTutorial = lecture.hoursOfTutorial
    val rangeHouresOfLecture = (minHouresOfLecture to maxHouresOfLecture).toList
    val rangeHouresOfTutorial = (minHouresOfTutorial to maxHouresOfTutorial).toList
    val lectureType = "Vorlesung"
    val tutorialType = "Übung"

    // we need this function to calculate the number of members for a planninglecture
    def calculateNumberOfMembers(courseInfos: List[CourseInformation]) = {
      // a ghostSemester don't exists, but we need this to calculate the number of members for one lecture
      val ghostSemesters =
        courseInfos.map(ci =>
                        ci.course
                        .semesters
                        .tail
                        .foldLeft(ci.course.semesters.head)((x,y)=> Semester(0,x.members + y.members)))

      val numberOfMembers = ghostSemesters
                              .tail
                              .foldLeft(ghostSemesters.head)((x,y)=> Semester(0,x.members + y.members))
                              .members
      numberOfMembers
    }

    // this function builds a list of planninglectures for a lecture with empty lists
    // for hasLectureTogetherWith and hast tutorialTogetherwith
    def buildPlanningLectureWithEmptyListsForHasLectureAndTutorialTogetherWith = {
      val courseSemesters = lecture.courseInfos.map(ci => ci.semesterInfos
                                                            .map(si => CourseSemester(ci.course.name,
                                                                                      si.semester))).flatten

      val dozentsForLecture = lecture.courseInfos.head.semesterInfos.head.dozentInfos
                                .filter(_.giveLecture == true)
                                .map(_.dozent)

      val dozentsForTutorial = lecture.courseInfos.head.semesterInfos.head.dozentInfos
                                 .filter(_.giveTutorial == true)
                                 .map(_.dozent)

      val lectureList = rangeHouresOfLecture
                          .map(nr => PlanningLecture(lecture.name,
                                                     lectureType,
                                                     courseSemesters,
                                                     dozentsForLecture,
                                                     calculateNumberOfMembers(lecture.courseInfos)))

      val tutorialList = rangeHouresOfTutorial
                           .map(nr => PlanningLecture(lecture.name,
                                                      tutorialType,
                                                      courseSemesters,
                                                      dozentsForTutorial,
                                                      calculateNumberOfMembers(lecture.courseInfos)))

      lectureList union tutorialList
    }

    // this function builds a list of planninglectures for a lecture with no empty lists
    // for hasLectureTogetherWith and hast tutorialTogetherwith
    def buildPlanningLectureWithNoEmptyListsForHasLectureAndTutorialTogetherWith = {

      def getCourseInfosFrom(lecture: Lecture, courseSemesters: List[CourseSemester]) = {
        lecture.courseInfos
               .filter(ci => courseSemesters.exists(_.course == ci.course.name))
      }

      val lecturesToPlan =
        lecture
          .hasLectureTogetherWith
          .map(lr => PlanningLecture(lecture.name,
                                     lectureType,
                                     lr.these,
                                     lr.withThose,
                                     calculateNumberOfMembers(getCourseInfosFrom(lecture,lr.these))))

      val lectureList = rangeHouresOfLecture
                          .map(nr => lecturesToPlan).flatten

      val tutorialsToPlan =
        lecture
          .hasTutorialTogetherWith
          .map(lr => PlanningLecture(lecture.name,
                                     tutorialType,
                                     lr.these,
                                     lr.withThose,
                                     calculateNumberOfMembers(getCourseInfosFrom(lecture,lr.these))))

      val tutorialList = rangeHouresOfTutorial
                          .map(nr => tutorialsToPlan).flatten

      lectureList union tutorialList
    }

    if(lecture.hasLectureTogetherWith.isEmpty && lecture.hasTutorialTogetherWith.isEmpty) {
      buildPlanningLectureWithEmptyListsForHasLectureAndTutorialTogetherWith
    } else {
        buildPlanningLectureWithNoEmptyListsForHasLectureAndTutorialTogetherWith
      }
  }

  // this function builds with a list of lectures the right number of planninglectures
  def buildPlan(lectures: List[Lecture]): List[PlanningLecture] = {
    lectures.map(buildPlanningLecture(_)).flatten
  }
}