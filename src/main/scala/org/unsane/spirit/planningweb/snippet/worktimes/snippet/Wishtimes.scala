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
import js.jquery._
import util._

/**
 * This class is the view to manage the wishtimes
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class Wishtimes extends WishtimesHelper with WishtimesTimetable with BlockUI {
  val worktimeFactor = 1.25
  lazy val dozent = isFHSDozent.head.dozent

  // this function calculates the min. number of worktimes which a dozent shoud select
  private def calculateWorktime = {
    val times = ((dozent.typeD.requiredTime - dozent.timeSelfManagement) *
                   (if(dozent.typeD.hasLectureship) 1 else worktimeFactor)) / 2
    val slots = if(times.intValue < times) {times.intValue + 1}
                else {if(times.intValue == times) {times.intValue} else {times.intValue}}
    slots
  }

  // this function returns a list with available timeslots to save
  private def timeSlotsToSave = List(oneMo,oneTu,oneWe,oneTh,oneFr,
                                     twoMo,twoTu,twoWe,twoTh,twoFr,
                                     threeMo,threeTu,threeWe,threeTh,threeFr,
                                     fourMo,fourTu,fourWe,fourTh,fourFr,
                                     fiveMo,fiveTu,fiveWe,fiveTh,fiveFr,
                                     sixMo,sixTu,sixWe,sixTh,sixFr)

  // this function returns the number of selected times
  private def selectedTimes = (timeSlotsToSave.map(_.is)).filter(s => s.isWishtime == true || s.isAvailableTime == true).size

  // this function calculates the number of selected worktimes
  protected def selectedWorktime = {
    timeSlotsToSave.map(_.is).
      intersect(List(TimeSlot("We",2,true,false),
                     TimeSlot("We",2,false,true),
                     TimeSlot("We",3,true,false),
                     TimeSlot("We",3,false,true))) match {
         case List(_,_) => selectedTimes - 1
         case _ => selectedTimes
       }
  }


  // the initial value of selected times
  lazy val count = ValueCell(selectedWorktime)

  // the view of selected times
  def countView(in: NodeSeq) = WiringUI.asText(in, count, JqWiringSupport.fade)

  // this function saves the seleced worktimes
  protected def save() = {
    def calculateMinSelectedTimes() = {
      lazy val minWorkTime = calculateWorktime

      S.warning("Ausgewählt: " + selectedWorktime.toString)
      if(selectedWorktime < minWorkTime) false else true
    }
    
    if(calculateMinSelectedTimes) {
      var alreadyInUse = false
      val before = worktimeOfDozent match {
        case List() => Worktime(dozent,initialTimeSlots,"")
        case _ => alreadyInUse = true
                  worktimeOfDozent.head
      }

      val after = Worktime(before.dozent,
                           timeSlotsToSave.map(_.is),
                           noteTextOfDozent)

      if(alreadyInUse) {
        persistenceWorktime update(before,after)
      } else {
          persistenceWorktime create after
        }
      saveNotificationOK
    } else {S.warning("Achtung: Bitte min. " + calculateWorktime + " Zeiten auswählen!"); saveNotificationError}
  }

  // this function represents the menu to select worktimes
  def select() = {
    if(!isFHSDozent.isEmpty) {
      S.warning("Die Auswahl des 2. und 3. Slots am Mi ist optional und wird nur einfach angerechnet.")
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
        Text("Sie sind im System nicht mit FHS-ID registriert. Daher kann das Menü nicht " +
          "vollständig angezeit werden. Bitte wenden sie sich an den Lehrplaner!")
      }
  }

  def render = {
    if(status) {
      "#counter *" #> countView _ &
      "#select *" #> (select ++ SHtml.hidden(save)) &
      "#count [onclick]" #> SHtml.ajaxInvoke(() => { count.set(selectedWorktime); Noop})}
    else {
      "#select *" #> Text("Die Wunschzeitenerfassung ist derzeit nicht freigeschaltet!")
    }
  }
}
