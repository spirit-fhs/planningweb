package org.unsane.spirit.planningweb.snippet.lectures.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml._
import net.liftweb.util.Props

import org.unsane.spirit.planningweb
import planningweb.lecturemanagement.impl.LectureType
import planningweb.persistence._
import planningweb.transform._

/**
 * This class is the view to add and delete a LectureType
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class Lecturetypes {
  val peristence:IPersistence = PersistenceFactory
                                  .createPersistence(TransformFactory
                                  .createTransformLectureType(Props.get("spirit.pers.layer") openOr ""))

  var name = ""

  /** to add a new LectureType to persistence */
  def add () {
    val lectureTypes = peristence.read.asInstanceOf[List[LectureType]]
    val alreadyAvailable = lectureTypes filter {typeL => typeL.name == name}

    if (name != "" && alreadyAvailable.isEmpty) {
      peristence.create(LectureType(name))
    }
    else {
      S.notice("Type already exist or Input is empty!")
    }
  }

  /** to delete a LectureType from persistence */
  def delete () = {
    import scala.collection.mutable.Set
    val toDelete = Set[LectureType]()
    val lectureTypes = peristence.read.asInstanceOf[List[LectureType]]

    def deleteLectureTypes(toDelete: Set[LectureType]) = {
      toDelete foreach {
        typeL => peristence.delete(typeL)
      }
    }

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"löschen:"}</th>
                       <th>{"Typ:"}</th>
                      </thead>
                      { lectureTypes.flatMap {
                        typeL => <tr>
                                  <th>{SHtml.checkbox(false, if (_) toDelete += typeL)}</th>
                                  <th>{typeL.name}</th>
                                 </tr>
                        }
                      }
                     </table>

    val delete = SHtml.submit("Löschen", () => deleteLectureTypes(toDelete))
    checkboxes ++ delete
  }

  // to render the letcure-type menue
  def render = {
    "#name *" #> SHtml.text("", n => name = n.trim) &
    "#add *" #> SHtml.submit("Hinzufügen", add) &
    "#delete *" #> delete
  }
}