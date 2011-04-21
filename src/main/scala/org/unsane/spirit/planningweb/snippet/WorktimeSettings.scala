package org.unsane.spirit.planningweb.snippet

/**
 * This class is the view to manage the worktime settings
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
import planningweb.worktimemanagement.impl.WorktimeManager
import planningweb.persistence._
import planningweb.transform._

class WorktimeSettings {
  val persLayer = Props.get("spirit.pers.layer") openOr ""

  val persistence:IPersistence = PersistenceFactory
                                   .createPersistence(TransformFactory
                                   .createTransformWorktimeManager(persLayer))

  val worktimeManagers = persistence.read.asInstanceOf[List[WorktimeManager]]

  def set() = {
    var status = false

    worktimeManagers match {
      case List() => status = false
      case _ => status = worktimeManagers.head.status
    }

    def setStatus() {
      worktimeManagers match {
        case List() => persistence.create(WorktimeManager(status))
        case _ => persistence.update(worktimeManagers.head,WorktimeManager(status))
      }
      S.notice("Worktimeloggin was activated!")
    }

    val setButton = SHtml.submit("Speichern", setStatus)
    val statusSelect = SHtml.radio(List("Ja","Nein"),
                                   Full(if(status == true) {
                                          "Ja"
                                        }else {
                                          "Nein"
                                        }),
                                   s => if(s == "Ja") {status = true} else {status = false}).toForm

    val setMenue = <table id="table-plane">
                    <tr>
                     <th>{"Freischalten:"}</th>
                     <th>{statusSelect}</th>
                    </tr>
                   </table> ++ setButton

    setMenue
  }
  def render = {
    "#set *" #> set
  }
}