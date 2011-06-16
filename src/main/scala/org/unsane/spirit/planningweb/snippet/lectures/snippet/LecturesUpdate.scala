package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This class is the view to update a lecture
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml._
import js._
import JsCmds._
import JE._
import net.liftweb.util.Props
import scala.collection.mutable.Set
import org.unsane.spirit.planningweb
import planningweb.transform._
import planningweb.persistence._
import planningweb.lecturemanagement.impl._
import planningweb.coursemanagement.impl.Course
import planningweb.dozentmanagement.impl.Dozent


class LecturesUpdate extends LecturesCreateNavigator {

  val peristence:IPersistence = PersistenceFactory
                                  .createPersistence(TransformFactory
                                  .createTransformLecture(usedPersistence))

  // we have to override this value, because the default value is for creating a lecture
  override val thisSide = "/lecture/update"


  // we have to call this function, because we need a differentiation between
  // a update process and a normal create process
  checkWhereWasTheUserBefore(false)

  // this function represents the different screens
  def update() = {
    val lectures = peristence.read.asInstanceOf[List[Lecture]]

    // to set lecture to update
    def setSession(set: String): JsCmd = {
      val toUpdateList = lectures filter(_.name == set)
      val toUpdate = toUpdateList match {
                       case List() => Empty
                       case _ => Full(toUpdateList.head)
                      }
      // set all session variables to update
      toUpdate match {
        case _ if toUpdate.isEmpty => Text("Nothing to update!")
        case _ => // we have to transform the immutable Lists to mutable Sets, because
                  // Courses, Semesters and Dozents expect a mutable Set
                  val courseSet = Set(toUpdate.head.courseInfos.map(_.course.name)).flatten
                  val courseSemInfos = toUpdate.head.courseInfos.map(ci => (ci.course.name, ci.semesterInfos))
                  val semesterList = (courseSemInfos.map(cs => cs._2.map(si => (cs._1,si.semester)))).flatten
                  val semesterSet = Set(semesterList).flatten
                  val dozentList = toUpdate.head.courseInfos.head.semesterInfos.head.dozentInfos.map(
                                     di => (di.dozent.name,di.giveLecture,di.giveTutorial)
                                   )
                  val dozentSet = Set(dozentList).flatten
                  // We have to fill the session variables with the values of the update lecture
                  Name((toUpdate.head.name,toUpdate.head.lectureType.name))
                  Courses(courseSet)
                  Semesters(semesterSet)
                  Dozents(dozentSet)
                  Houres((toUpdate.head.hoursOfLecture,toUpdate.head.hoursOfTutorial))
                  //this is necessary, because somebody can change the name of the lecture and
                  //we would not know which is the lecture to update
                  NameBeforeUpdate(toUpdate.head.name)

      }
      RedirectTo(thisSide)
    }

    val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                           s => setSession(s))

    // to select a lecture to update
    def lectureUpdateChoice() = {
      SHtml.untrustedSelect(("","")::(lectures map (lecture => (lecture.name, lecture.name))),
                            Full(Name.is._1),
                            () => _,
                            "onchange" -> js.toJsCmd)
    }

    val updateMenue = <table id="table-plane">
                       <tr>
                        <th>{"Lehrveranstaltung:"}</th>
                        <th>{lectureUpdateChoice}</th>
                       </tr>
                      </table>

    if (Name.is._1 != "") {
      navigation
    }
    else {
      updateMenue
    }
  }

  def render = {
    "#update *" #> update
  }

}