package org.unsane.spirit.planningweb.snippet.lectures.snippet

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

/**
 * This trait is the view to select a lecture to create a group
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
trait RelationshipsLectureSelect extends RelationshipHelper {

  def setSession(set: String): JsCmd = {
      val toSet = lectures.filter(_.name == set)

      toSet match {
         case List() => ToAddRelation(Empty)
         case _ => ToAddRelation(Full(toSet.head))
                   Status(RelationshipHelper.SelectedLecture)
      }

      RedirectTo(thisSide)
    }

    val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                           s => setSession(s))

    def lectureRelationChoice() = {
      SHtml.untrustedSelect(("","")::(lectures map (lecture => (lecture.name, lecture.name))),
                            Empty,
                            () => _,
                            "onchange" -> js.toJsCmd)

    }

    val selectLectureMenue = <table id="table-plane">
                              <tr>
                               <th>{"Lehrveranstaltung:"}</th>
                               <th>{lectureRelationChoice}</th>
                              </tr>
                             </table>
}