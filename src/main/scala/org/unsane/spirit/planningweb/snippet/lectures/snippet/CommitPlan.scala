package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This class is the representation of the view to commit the Lecture-Plan
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
import net.liftweb.util.Props
import js._
import JsCmds._
import JE._
import org.unsane.spirit.planningweb
import planningweb.lecturemanagement.impl._
import planningweb.persistence._
import planningweb.transform._

class CommitPlan {

  val perLayer = Props.get("spirit.pers.layer") openOr ""
  val persistence:IPersistence = PersistenceFactory
                                   .createPersistence(TransformFactory
                                   .createTransformLecture(perLayer))
  val lectures = persistence.read.asInstanceOf[List[Lecture]]

  object SemesterType extends SessionVar[String]("")

  def commit() = {

    val lecturesSS = lectures.filter(_.inSummerSemester == true)
    val lecturesWS = lectures.filter(_.inWinterSemester == true)

    val toCommit = if(SemesterType.is != "" && SemesterType.is == "Sommersemester") {
                     PlanningLectureCreater.buildPlan(lecturesSS)
                   } else {
                       if(SemesterType.is == "Wintersemester") {
                         PlanningLectureCreater.buildPlan(lecturesWS)
                       } else {
                           List()
                         }
                     }

    val thisSide = "/lecture/commitplan"

    // to set the PlanningLecture
    def setSession(set: String): JsCmd = {
      SemesterType(set)
      RedirectTo(thisSide)
    }

    val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                           s => setSession(s))

    def semesterChoice() = {
      SHtml.untrustedSelect(List(("",""),("Sommersemester","Sommersemester"),("Wintersemester","Wintersemester")),
                            Full(SemesterType.is),
                            () => _,
                            "onchange" -> js.toJsCmd)


    }

    val commitMenue = semesterChoice ++
                      <table id="table-box">
                       <thead>
                        <th>{"Name:"}</th>
                        <th>{"Typ:"}</th>
                        <th>{"Gruppe:"}</th>
                        <th>{"Dozent:"}</th>
                        <th>{"Teilnehmerzahl:"}</th>
                       </thead>
                       { toCommit.map {
                           pl => <tr>
                                  <th>{pl.name}</th>
                                  <th>{pl.typeOfLecture}</th>
                                  <th>{pl.groups.sortBy(_.semester).map(cs => cs.semester.toString + ". " +cs.course ++ <br />)}</th>
                                  <th>{pl.dozents.map(_.name ++ <br />)}</th>
                                  <th>{pl.numberOfMembers.toString}</th>
                                 </tr>
                         }
                       }
                     </table>

    commitMenue
  }

  def render = {
    "#commit *" #> commit
  }
}