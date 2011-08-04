package org.unsane.spirit.planningweb.snippet.lectures.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml._
import net.liftweb.util.Props
import js._
import JsCmds._
import JE._
import org.unsane.spirit.planningweb
import planningweb.lecturemanagement.impl._
import planningweb.persistence._
import planningweb.transform._

/**
 * This class is view to show a lecture
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class LecturesShow {

  val perLayer = Props.get("spirit.pers.layer") openOr ""
  val persistence:IPersistence = PersistenceFactory
                                   .createPersistence(TransformFactory
                                   .createTransformLecture(perLayer))
  val lectures = persistence.read.asInstanceOf[List[Lecture]]

  object LectureName extends SessionVar[String]("")

  /** this function creates the menue to show a lecture */
  def show() = {

    val toShowList = lectures filter(_.name == LectureName.is)
    val toShow = toShowList match {
                   case List() => Empty
                   case _ => Full(toShowList.head)
                 }
    val thisSide = "/lecture/show"

    // to set LectureName
    def setSession(set: String): JsCmd = {
      LectureName(set)
      RedirectTo(thisSide)
    }

    val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                           s => setSession(s))

    // to select a lecture to show
    def lectureShowChoice() = {
      SHtml.untrustedSelect(("","")::(lectures map (lecture => (lecture.name, lecture.name))),
                            Full(LectureName.is),
                            () => _,
                            "onchange" -> js.toJsCmd)
    }

    val showMenue = lectureShowChoice ++
                    <table id="table-box">
                     <thead>
                      <th>{"Lehrveranstaltung:"}</th>
                      <th>{"Studiengänge:"}</th>
                      <th>{"Dozenten:"}</th>
                      <th>{"SWS VL:"}</th>
                      <th>{"SWS Übung:"}</th>
                     </thead>
                      {
                        toShow match {
                          case Empty =>
                          case _ =>
                            <tr>
                             <th>{toShow.head.name}</th>
                             <th>{toShow.head.courseInfos.map(ci => Text(ci.course.name) ++ <br /> ++
                                                                    {ci.semesterInfos.map(ci =>
                                                                       ci.semester.toString + " "
                                                                     ).sorted
                                                                    } ++ <br />
                                                             )
                                 }
                             </th>
                             <th>
                               {toShow.head.courseInfos
                                 .head.semesterInfos
                                 .head.dozentInfos.map(di=> Text(di.dozent.name) ++ <br />)
                               }
                             </th>
                             <th>
                               {toShow.head.hoursOfLecture}
                             </th>
                             <th>
                               {toShow.head.hoursOfTutorial}
                             </th>
                            </tr>
                        }
                      }
                    </table>

    showMenue
  }

  def render = {
    "#show *" #> show
  }
}