package org.unsane.spirit.planningweb.snippet

/**
 * This class is the view to delete a lecture
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
import org.unsane.spirit.planningweb
import planningweb.lecturemanagement.impl._
import planningweb.persistence._
import planningweb.transform._


class LecturesDelete {

  val usedPersistence = Props.get("spirit.pers.layer") openOr ""
  val peristence:IPersistence = PersistenceFactory
                                  .createPersistence(TransformFactory
                                  .createTransformLecture(usedPersistence))


  // to delete a lecture from persistence
  def delete () = {
    import scala.collection.mutable.Set
    val toDelete = Set[Lecture]()
    val lectures = peristence.read.asInstanceOf[List[Lecture]]

    def deleteLectures(toDelete: Set[Lecture]) = {
      toDelete foreach {
        lecture => peristence.delete(lecture)
      }
    }

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"löschen:"}</th>
                       <th>{"Lehrveranstaltung:"}</th>
                       <th>{"Typ:"}</th>
                       <th>{"SWS Vorlesung:"}</th>
                       <th>{"SWS Übung:"}</th>
                      </thead>
                      { lectures.flatMap {
                        lecture => <tr>
                                    <th>{SHtml.checkbox(false, if (_) toDelete += lecture)}</th>
                                    <th>{lecture.name}</th>
                                    <th>{lecture.lectureType.name}</th>
                                    <th>{lecture.hoursOfLecture}</th>
                                    <th>{lecture.hoursOfTutorial}</th>
                                   </tr>
                        }
                      }
                     </table>

    val delete = SHtml.submit("Löschen", () => deleteLectures(toDelete))
    checkboxes ++ delete
  }


  def render = {
    "#delete *" #> delete
  }
}