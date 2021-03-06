package org.unsane.spirit.planningweb.snippet.worktimes.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml._
import js._
import JsCmds._
import JE._
import net.liftweb.util.Props
import net.liftweb.textile.TextileParser
import org.unsane.spirit.planningweb
import planningweb.worktimemanagement.impl._
import planningweb.dozentmanagement.impl._
import planningweb.persistence._
import planningweb.transform._

/**
 * This class is the view to show the selected rooms and worktimes
 * of the different dozents
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class WorktimesShow {
  val persLayer = Props.get("spirit.pers.layer") openOr ""

  val persistenceWorktime:IPersistence = PersistenceFactory
                                           .createPersistence(TransformFactory
                                           .createTransformWorktime(persLayer))

  val worktimes = persistenceWorktime.read.asInstanceOf[List[Worktime]]

  object DozentName extends SessionVar[String]("")

  // css colors are defined in tablestyle.css
  val wishTimeColor = "wish"
  val availableTimeColor = "available"
  val noTimeColor = "plane"

  def show() = {
    val toShowList = worktimes filter(_.dozent.name == DozentName.is)
    val toShow = toShowList match {
                   case List() => Empty
                   case _ => Full(toShowList.head)
                 }

    val notes = if(toShow.isEmpty) {
                  TextileParser.toHtml("%{color:red}Keine Kommentare vorhanden!%")
                } else {TextileParser.toHtml(toShow.head.notes)}

    val lectureship = if(toShow.isEmpty) {
                        Text("")
                      } else {if(toShow.head.dozent.typeD.hasLectureship) {Text("Lehrauftrag")} else {Text("")}}

    val thisSide = "/worktime/show"

    // to set LectureName
    def setSession(set: String): JsCmd = {
      DozentName(set)
      RedirectTo(thisSide)
    }

    val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                           s => setSession(s))

    // to select a lecture to show
    def worktimeShowChoice() = {
      SHtml.untrustedSelect(("","")::(worktimes map (worktime => (worktime.dozent.name, worktime.dozent.name))),
                            Full(DozentName.is),
                            () => _,
                            "onchange" -> js.toJsCmd)
    }

    def setTimeColors(day: String, time: Int) = {
      val slots = if(toShow.isEmpty) {
                    List()
                  } else {toShow.head.timeSlots}
      val slot = slots.filter(s => s.day == day && s.time == time)
      slot match {
        case List() => noTimeColor
        case _ if slot.head.isWishtime == true => wishTimeColor
        case _ if slot.head.isAvailableTime == true => availableTimeColor
        case _ => noTimeColor
      }
    }

    val timetable =
      <table id="table-wish">
        <thead>
          <th></th>
          <th>{"Montag"}</th>
          <th>{"Dienstag"}</th>
          <th>{"Mittwoch"}</th>
          <th>{"Donnerstag"}</th>
          <th>{"Freitag"}</th>
        </thead>
        <tr>
         <th>{"08:15-9:45"}</th>
         <th id={setTimeColors("Mo",1)}></th>
         <th id={setTimeColors("Tu",1)}></th>
         <th id={setTimeColors("We",1)}></th>
         <th id={setTimeColors("Th",1)}></th>
         <th id={setTimeColors("Fr",1)}></th>
        </tr>
        <tr>
         <th>{"10:00-11:30"}</th>
         <th id={setTimeColors("Mo",2)}></th>
         <th id={setTimeColors("Tu",2)}></th>
         <th id={setTimeColors("We",2)}></th>
         <th id={setTimeColors("Th",2)}></th>
         <th id={setTimeColors("Fr",2)}></th>
        </tr>
        <tr>
         <th>{"11:45-13:15"}</th>
         <th id={setTimeColors("Mo",3)}></th>
         <th id={setTimeColors("Tu",3)}></th>
         <th id={setTimeColors("We",3)}></th>
         <th id={setTimeColors("Th",3)}></th>
         <th id={setTimeColors("Fr",3)}></th>
        </tr>
        <tr>
         <th>{"14:15-15:45"}</th>
         <th id={setTimeColors("Mo",4)}></th>
         <th id={setTimeColors("Tu",4)}></th>
         <th id={setTimeColors("We",4)}></th>
         <th id={setTimeColors("Th",4)}></th>
         <th id={setTimeColors("Fr",4)}></th>
        </tr>
        <tr>
         <th>{"16:00-17:30"}</th>
         <th id={setTimeColors("Mo",5)}></th>
         <th id={setTimeColors("Tu",5)}></th>
         <th id={setTimeColors("We",5)}></th>
         <th id={setTimeColors("Th",5)}></th>
         <th id={setTimeColors("Fr",5)}></th>
        </tr>
        <tr>
         <th>{"17:45-19:15"}</th>
         <th id={setTimeColors("Mo",6)}></th>
         <th id={setTimeColors("Tu",6)}></th>
         <th id={setTimeColors("We",6)}></th>
         <th id={setTimeColors("Th",6)}></th>
         <th id={setTimeColors("Fr",6)}></th>
        </tr>
        <tfoot>
         <th>{"Zeiten"}</th>
         <th id={wishTimeColor}>{"Wunsch-Zeit"}</th>
         <th id={availableTimeColor}>{"Kann-Zeit"}</th>
         <th id={noTimeColor}></th>
         <th id={noTimeColor}></th>
         <th id={noTimeColor}></th>
        </tfoot>
      </table>

    worktimeShowChoice ++ lectureship ++ timetable ++ <h2>{"Wunschräume/Kommentare"}</h2> ++ notes
  }

  def render = {
    "#show *" #> show
  }
}