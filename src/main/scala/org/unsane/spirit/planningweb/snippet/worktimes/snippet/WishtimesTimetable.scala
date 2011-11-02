package org.unsane.spirit.planningweb.snippet.worktimes.snippet

import net.liftweb._
import http._
import common._
import scala.xml._
import org.unsane.spirit.planningweb
import planningweb.worktimemanagement.impl._

import js._
import JsCmds._
import JE._

/**
 * This trait is the view of the WishtimeTimetable to select Wishtimes
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
trait WishtimesTimetable extends WishtimesHelper {
  // this function generates an ajax radio button
  def spiritRadio[T](opts: List[T], deflt: Box[T], ajaxFunc: T => JsCmd)/*: ChoiceHolder[T]*/ = {
    import SHtml.ChoiceHolder
    import SHtml.ChoiceItem
    val choiceHolder = SHtml.ajaxRadio(opts, deflt, ajaxFunc)
    val items = choiceHolder.items
    val newItems =
      (opts zip items).map(key_i =>
        (key_i._2.xhtml \ "@checked").toString match {
          case "" =>
            ChoiceItem(key_i._1.toString, <input type="radio" value={key_i._1.toString} name={key_i._2.xhtml \ "@name"} onclick={key_i._2.xhtml \ "@onclick"}/>)
          case _ =>
            ChoiceItem(key_i._1.toString, <input checked="checked" type="radio" value={key_i._1.toString} name={key_i._2.xhtml \ "@name"} onclick={key_i._2.xhtml \ "@onclick"}/>)
        })
    ChoiceHolder(newItems)
  }

  val wishTimeColor = "wish"
  val availableTimeColor = "available"

 // to set a timeSlot
  def setSlot(times:List[Worktime], day: String, time: Int) : TimeSlot= {
    if(times.isEmpty) {
      initialTimeSlots.filter(s => s.day == day && s.time == time).head
    } else {
        val slots = times.head.timeSlots
        slots match {
          case List() => initialTimeSlots.filter(s => s.day == day && s.time == time).head
          case _ => slots.filter(s => s.day == day && s.time == time).head
        }
      }
  }

  // to generate the different timeSlots
  def makeRadio(day: String, slot: Int) = {
    val lable = List("W","K","N")
    val slotList = slots.filter(s => s.is.day == day && s.is.time == slot)
    def setStartVal = {
      Full(if (setSlot(worktimeOfDozent,day,slot).isWishtime) {
             slotList map(s => s(TimeSlot(s.is.day,s.is.time,true,false)))
             "W"
           } else if (setSlot(worktimeOfDozent,day,slot).isAvailableTime) {
             slotList map(s => s(TimeSlot(s.is.day,s.is.time,false,true)))
             "K"
             } else {slotList map(s => s(TimeSlot(s.is.day,s.is.time,false,false)))
                     "N"}
      )
    }

    slotList.map(slot =>
      spiritRadio(lable,setStartVal,
                 (s:String) => s match {
                   case "K" => slot(TimeSlot(slot.is.day,slot.is.time,false,true)); JsRaw("changeColor()")
                   case "W" => slot(TimeSlot(slot.is.day,slot.is.time,true,false)); JsRaw("changeColor()")
                   case _ => slot(TimeSlot(slot.is.day,slot.is.time,false,false)); JsRaw("changeColor()")}).toForm
    )
  }

  lazy val timetable =
          <table id="table-select">
            <thead>
             <th></th>
             <th>{"Montag"}</th><th>{"Dienstag"}</th><th>{"Mittwoch"}</th><th>{"Donnerstag"}</th><th>{"Freitag"}</th>
            </thead>
            <tr>
             <th>{"08:15-09:45"}</th>
             <th>{makeRadio("Mo",1)}</th>
             <th>{makeRadio("Tu",1)}</th>
             <th>{makeRadio("We",1)}</th>
             <th>{makeRadio("Th",1)}</th>
             <th>{makeRadio("Fr",1)}</th>
            </tr>
            <tr>
             <th>{"10:00-11:30"}</th>
             <th>{makeRadio("Mo",2)}</th>
             <th>{makeRadio("Tu",2)}</th>
             <th>{makeRadio("We",2)}</th>
             <th>{makeRadio("Th",2)}</th>
             <th>{makeRadio("Fr",2)}</th>
            </tr>
            <tr>
             <th>{"11:45-13:15"}</th>
             <th>{makeRadio("Mo",3)}</th>
             <th>{makeRadio("Tu",3)}</th>
             <th>{makeRadio("We",3)}</th>
             <th>{makeRadio("Th",3)}</th>
             <th>{makeRadio("Fr",3)}</th>
            </tr>
            <tr>
             <th>{"14:15-15:45"}</th>
             <th>{makeRadio("Mo",4)}</th>
             <th>{makeRadio("Tu",4)}</th>
             <th>{/*makeRadio("We",4)*/}</th>
             <th>{makeRadio("Th",4)}</th>
             <th>{makeRadio("Fr",4)}</th>
            </tr>
            <tr>
             <th>{"16:00-17:30"}</th>
             <th>{makeRadio("Mo",5)}</th>
             <th>{makeRadio("Tu",5)}</th>
             <th>{/*makeRadio("We",5)*/}</th>
             <th>{makeRadio("Th",5)}</th>
             <th>{makeRadio("Fr",5)}</th>
            </tr>
            <tr>
             <th>{"17:45-19:15"}</th>
             <th>{makeRadio("Mo",6)}</th>
             <th>{makeRadio("Tu",6)}</th>
             <th>{/*makeRadio("We",6)*/}</th>
             <th>{makeRadio("Th",6)}</th>
             <th>{makeRadio("Fr",6)}</th>
            </tr>
            <tfoot>
              <th>{"Legende"}</th>
              <th id={wishTimeColor}>{"W: Wunsch-Zeit"}</th>
              <th id={availableTimeColor}>{"K: Kann-Zeit"}</th>
              <th>{"N: N/A-Zeit"}</th>
              <th></th>
              <th></th>
            </tfoot>
          </table>
}