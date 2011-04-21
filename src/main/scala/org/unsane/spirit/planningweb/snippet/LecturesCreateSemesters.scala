package org.unsane.spirit.planningweb.snippet

/**
 * This trait is the view to add different semesters of a course to a lecture
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import net.liftweb._
import http._
import scala.xml._
import scala.collection.mutable.Set


trait LecturesCreateSemesters extends LecturesCreateHelper {

  // this function represents the screen to select the semester of some courses which are
  // necessary for the lecture
  def addSemesters() = {
    val toAdd = Set[(CourseName,SemesterNumber)]()
    val filteredCourses = courses filter(course => Courses.is.contains(course.name))
    val courseSemestersList = filteredCourses map(course => (course.name,course.semesters))
    val courseSemesterList = courseSemestersList.flatMap(cs => cs._2.map(sem => (cs._1,sem.name)))

    def next() = {
      val toSelect = filteredCourses map (_.name)
      val isSelected = (toAdd map (_._1)).toList.distinct
      toAdd.toList match {
        case List() => S.notice("Please select a semester!")
        case _ if (toSelect diff isSelected) != List() => S.notice("Please select all chosen courses!")
        case _ => Semesters(toAdd)
                  Status(LecturesCreateHelper.AddedSemester)
                  S.redirectTo(thisSide)
      }
    }

    def back() = {
      //Status(LecturesCreateHelper.AddedName)
      S.redirectTo(thisSide)
    }

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"Speichern:"}</th>
                       <th>{"Semester:"}</th>
                      </thead>
                      { courseSemesterList.flatMap {
                        cs => <tr>
                               <th>{SHtml.checkbox(if (Semesters.is.contains((cs._1,cs._2))) {
                                                     true
                                                   }
                                                   else {
                                                     false
                                                   }, if (_) toAdd += cs)}</th>
                               <th>{cs._2.toString + ". " + cs._1}</th>
                              </tr>
                        }
                      }
                     </table>

    val nextButton = SHtml.submit("Weiter", next)
    val backButton = SHtml.submit("Zurück", back)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val addSemesterMenue = checkboxes ++ backButton ++ cancleButton ++ nextButton

    addSemesterMenue
  }
}