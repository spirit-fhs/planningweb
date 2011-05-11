package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This class is the view to delete a lecture group or a tutorial group
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
import scala.collection.mutable.Set
import net.liftweb.util.Props
import js._
import JsCmds._
import JE._
import org.unsane.spirit.planningweb
import planningweb.lecturemanagement.impl._
import planningweb.persistence._
import planningweb.transform._

// this snippet is the menue to delete a lecture group or a tutorial group
class RelationshipsDelete {
  val perLayer = Props.get("spirit.pers.layer") openOr ""
  val persistence:IPersistence = PersistenceFactory
                                   .createPersistence(TransformFactory
                                   .createTransformLecture(perLayer))

  val lectures = persistence.read.asInstanceOf[List[Lecture]]

  object LectureName extends SessionVar[String]("")

  val thisSide = "/lecture/relationdelete"

  def delete() = {
    val toDeleteLecture = Set[LectureRelationship]()
    val toDeleteTutorial = Set[LectureRelationship]()


    val toChangeList = lectures filter(_.name == LectureName.is)
    val toChange = toChangeList match {
                   case List() => Empty
                   case _ => Full(toChangeList.head)
                 }
    //val thisSide = "/lecture/relationdelete"

    // to set LectureName
    def setSession(set: String): JsCmd = {
      LectureName(set)
      RedirectTo(thisSide)
    }

    val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                           s => setSession(s))

    // to select a lecture to delete a group
    def lectureChangeChoice() = {
      SHtml.untrustedSelect(("","")::(lectures map (lecture => (lecture.name, lecture.name))),
                            Full(LectureName.is),
                            () => _,
                            "onchange" -> js.toJsCmd)
    }

    val lectureGroups = toChange match {
                          case Empty => List()
                          case _ => toChange.head.hasLectureTogetherWith
                        }
    val tutorialGroups = toChange match {
                           case Empty => List()
                           case _ => toChange.head.hasTutorialTogetherWith
                         }

    def checkboxes(list: List[LectureRelationship], toDelete: Set[LectureRelationship]) = {
      <table id="table-box">
       <thead>
        <th>{"löschen:"}</th>
        <th>{"Gruppe:"}</th>
        <th>{"Dozent:"}</th>
       </thead>
        { list.flatMap {
            l => <tr>
                  <th>{SHtml.checkbox(false, if (_) toDelete += l)}</th>
                  <th>{l.these.sortBy(_.semester).map(cs => Text(cs.semester.toString + ". " + cs.course) ++ <br />)}</th>
                  <th>{l.withThose.map(d => Text(d.name) ++ <br />)}</th>
                 </tr>
          }
        }
      </table>
    }

    def del() = {
      if (toDeleteLecture.isEmpty && toDeleteTutorial.isEmpty) {
        S.notice("Nothing to delete selected!")
      } else {
          val newLectureGroup = toChange.head.hasLectureTogetherWith
                                  .diff(toDeleteLecture.toList)
          val newTutorialGroup = toChange.head.hasTutorialTogetherWith
                                  .diff(toDeleteTutorial.toList)

          val after = Lecture(toChange.head.name,
                              toChange.head.lectureType,
                              toChange.head.courseInfos,
                              newLectureGroup,
                              newTutorialGroup,
                              toChange.head.hoursOfLecture,
                              toChange.head.hoursOfTutorial,
                              toChange.head.inSummerSemester,
                              toChange.head.inWinterSemester
                             )
          persistence.update(toChange.head,after)
        }
    }

    val deleteButton = SHtml.submit("Löschen",del)

    val delMenue = <h3>{"Lehrveranstaltung auswählen:"}</h3> ++
                   lectureChangeChoice ++
                   <h3>{"Lehrveranstaltungsgruppen:"}</h3> ++
                   checkboxes(lectureGroups,toDeleteLecture) ++
                   <h3>{"Übungsgruppen:"}</h3> ++
                   checkboxes(tutorialGroups,toDeleteTutorial) ++
                   deleteButton

    delMenue
  }

  def render = {
    "#delete *" #> delete
  }
}