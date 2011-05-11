package org.unsane.spirit.planningweb.snippet.dozents.snippet

/**
 * This class is the representation of the view to manage the FHSDozents
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
import planningweb.dozentmanagement.impl._
import planningweb.persistence._
import planningweb.transform._

class FHSDozents {

  val persLayer = Props.get("spirit.pers.layer") openOr ""

  val persistenceFHSDozent:IPersistence = PersistenceFactory
                                            .createPersistence(TransformFactory
                                            .createTransformFHSDozent(persLayer))

  val persistenceDozent:IPersistence = PersistenceFactory
                                         .createPersistence(TransformFactory
                                         .createTransformDozent(persLayer))

  val fhsdozents = persistenceFHSDozent.read.asInstanceOf[List[FHSDozent]]
  val dozents = persistenceDozent.read.asInstanceOf[List[Dozent]]

  // to store a fhsdozent object
  def add() = {

    var fhsId = ""
    var dozentName = ""

    // to store a fhsdozent object
    def addFHSDozent() {
      val alreadyAvailable = fhsdozents.filter(fd => fd.fhsId == fhsId || fd.dozent.name == dozentName)

       if (fhsId != "" && dozentName != "" && alreadyAvailable.isEmpty) {
         val toAdd = dozents.filter(_.name == dozentName)
           persistenceFHSDozent.create(FHSDozent(fhsId, toAdd.head))
       }
       else {
         S.notice("FHS-Dozent already exist or Input is empty!")
       }
    }

    // to select a Dozent
    def dozentChoice = {
      val first = dozents match {
        case List() => Empty
        case _ => Full(dozents.head.name)
      }
      SHtml.untrustedSelect(dozents map (d => (d.name, d.name)),
                            first,
                            dozentName = _)
    }

    val dozentToAddChoice = dozentChoice
    val fhsIdToAddText = SHtml.text("", fd => fhsId = fd.trim)
    val addButton = SHtml.submit("Hinzufügen", addFHSDozent)

    val addMenue = <table id="table-plane">
                    <tr>
                     <th>{"FHS-ID:" ++ <br /> ++ fhsIdToAddText}</th>
                     <th>{"Dozent:" ++ <br /> ++ dozentChoice}</th>
                    </tr>
                   </table> ++ addButton

    addMenue
  }

  def delete() = {
    import scala.collection.mutable.Set
    val toDelete = Set[FHSDozent]()
    // to delete a fhsdozent object
    def deleteFHSDozents(toDelete: Set[FHSDozent]) = {
      toDelete foreach {
        fhsdozent => persistenceFHSDozent.delete(fhsdozent)
      }
    }

    // to select a fhsdozent to delete
    val checkboxes = <table id="table-box">
                       <thead>
                        <th>{"löschen:"}</th>
                        <th>{"FHS-ID:"}</th>
                        <th>{"Dozent:"}</th>
                     </thead>
                       { fhsdozents.flatMap {
                           fhsdozent => <tr>
                                         <th>{SHtml.checkbox(false, if (_) toDelete += fhsdozent)}</th>
                                         <th>{fhsdozent.fhsId}</th>
                                         <th>{fhsdozent.dozent.name}</th>
                                        </tr>
                           }
                       }
                     </table>

    val del = SHtml.submit("Löschen", () => deleteFHSDozents(toDelete))
    checkboxes ++ del
  }

  def render = {
    "#add *" #> add &
    "#delete *" #> delete
  }
}