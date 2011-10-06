package org.unsane.spirit.planningweb.snippet.worktimes.snippet

import net.liftweb._
import http._
import common._
import js._
import util.Helpers._
import scala.xml._
import scala.collection.mutable.Set
import net.liftweb.util.Props
import org.unsane.spirit.planningweb
import planningweb.snippet.BlockUI
import planningweb.worktimemanagement.impl._

import JsCmds._
import JE._

/**
 * This class is the view to manage the wishtimes
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class Wishtimes extends WishtimesHelper with WishtimesTimetable with BlockUI {
  OnLoad(new JsCmd{def toJsCmd = "changeColor"})
  val worktimeFactor = 1.25
  lazy val dozent = isFHSDozent.head.dozent 
 
  private def calculateWorktime = {
    val times = ((dozent.typeD.requiredTime - dozent.timeSelfManagement) *
                   (if(dozent.typeD.hasLectureship) 1 else worktimeFactor)) / 2
    val slots = if(times.intValue < times) {times.intValue + 1}
                else {if(times.intValue == times) {times.intValue} else {times.intValue}}
    slots
  }

  protected def save() = {
    val timeSlotsToSave = List(oneMo,oneTu,oneWe,oneTh,oneFr,
                               twoMo,twoTu,twoWe,twoTh,twoFr,
                               threeMo,threeTu,threeWe,threeTh,threeFr,
                               fourMo,fourTu,fourWe,fourTh,fourFr,
                               fiveMo,fiveTu,fiveWe,fiveTh,fiveFr,
                               sixMo,sixTu,sixWe,sixTh,sixFr)
    def calculateMinSelectedTimes() = {
      lazy val minWorkTime = calculateWorktime
      val selectedWorktime = timeSlotsToSave.filter(s => s.isWishtime == true || s.isAvailableTime == true).size
      S.notice("Ausgewählt: " + selectedWorktime.toString)
      if(selectedWorktime < minWorkTime) {
        false
      } else {
          true
        }
    }
    
    if(calculateMinSelectedTimes) {
      var alreadyInUse = false
      val before = worktimeOfDozent match {
        case List() => Worktime(dozent,initialTimeSlots,"")
        case _ => alreadyInUse = true
                  worktimeOfDozent.head
      }

      val after = Worktime(before.dozent,
                           timeSlotsToSave,
                           noteTextOfDozent)

      if(alreadyInUse) {
        persistenceWorktime update(before,after)
      } else {
          persistenceWorktime create after
        }
      saveNotificationOK
    } else {S.warning("Achtung: Bitte min. " + calculateWorktime + " Zeiten auswählen!"); saveNotificationError}
  }

  def select() = {
    if(status) {
      if(!isFHSDozent.isEmpty) {
        S.warning("Achtung: Bitte min. " + calculateWorktime + " Zeiten auswählen!")

        val notes = SHtml.textarea(noteTextOfDozent, n => noteTextOfDozent = n.trim, "cols" -> "80", "rows" -> "8")

        val saveButton = SHtml.submit("Speichern", ()=>())

        val wishtimeMenue = timetable ++ <h2>{"Wunschräume/Kommentare"}</h2> ++
          <table>
            <tr>
              <th>{notes}</th>
              <th>
                <strong>Formatierung:</strong>
                <br />**bold text**
                <br />__italic text__
                <br />*_ bold italic text _*
                <br />{"%{color:red}Text in red %"}
                <br /><strong>Aufz&auml;hlung:</strong>
                <br />* bulleted list
                <br />
                <br />* bulleted list
                <br />** 2-level
              </th>
            </tr>
          </table> ++ saveButton ++ <head>{Script(OnLoad(Call("changeColor")))}</head>

        wishtimeMenue
      } else {
          Text("Sie sind im System nicht mit FHS-ID registriert. Bitte wenden sie sich an den Lehrplaner!")
        }
    } else {
        Text("Die Wunschzeitenerfassung ist derzeit nicht freigeschaltet!")
      }
  }

  def render = {
    "#select *" #> (select ++ SHtml.hidden(save))
  }
}
