package org.unsane.spirit.planningweb.snippet

/**
 * This class is the representation of the view to manage the DozentTypes
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
import planningweb.dozentmanagement.impl.DozentType
import planningweb.persistence._
import planningweb.transform._


class Dozenttypes {
  val peristence:IPersistence = PersistenceFactory
                                    .createPersistence(TransformFactory
                                    .createTransformDozentType(Props.get("spirit.pers.layer") openOr ""))
  var hasLectureship = false
  var name = ""
  var requiredTime = "0"

  // to add a new DozentType to persistence
  def add () {
    val dozentTypes = peristence.read.asInstanceOf[List[DozentType]]
    val alreadyAvailable = dozentTypes filter {typeD => typeD.name == name}

    if (name != "" && alreadyAvailable.isEmpty) {
      try {
        peristence.create(DozentType(name,requiredTime.toInt, hasLectureship))
      } catch {
          case e: Exception => S.notice(requiredTime + " is not a Number!")
        }
    }
    else {
      S.notice("Type already exist or Input is empty!")
    }
  }
  // to delete a DozentType from persistence
  def delete () = {
    import scala.collection.mutable.Set
    val toDelete = Set[DozentType]()
    val dozentTypes = peristence.read.asInstanceOf[List[DozentType]]

    def deleteDozentTypes(toDelete: Set[DozentType]) = {
      toDelete foreach {
        typeD => peristence.delete(typeD)
      }
    }

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"löschen:"}</th>
                       <th>{"Typ:"}</th>
                       <th>{"Pflichtstunden:"}</th>
                       <th>{"Lehrauftrag:"}</th>
                      </thead>
                      { dozentTypes.flatMap {
                        typeD => <tr>
                                  <th>{SHtml.checkbox(false, if (_) toDelete += typeD)}</th>
                                  <th>{typeD.name}</th>
                                  <th>{typeD.requiredTime}</th>
                                  <th>{typeD.hasLectureship}</th>
                                 </tr>
                        }
                      }
                     </table>

    val delete = SHtml.submit("Löschen", () => deleteDozentTypes(toDelete))
    checkboxes ++ delete
  }

  // to render the dozent-type menue
  def render = {
    "#hasLectureship *" #> SHtml.checkbox(false, if (_) hasLectureship = true) &
    "#name *" #> SHtml.text("", n => name = n.trim) &
    "#requiredTime *" #> SHtml.text("0", r => requiredTime = r.trim) &
    "#add *" #> SHtml.submit("Hinzufügen", add) &
    "#delete *" #> delete
  }
}