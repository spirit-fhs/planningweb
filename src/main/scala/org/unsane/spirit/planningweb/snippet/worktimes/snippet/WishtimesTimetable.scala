package org.unsane.spirit.planningweb.snippet.worktimes.snippet

/**
 * This trait is the view of the WishtimeTimetable to select Wishtimes
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
import org.unsane.spirit.planningweb
import planningweb.worktimemanagement.impl._

trait WishtimesTimetable extends WishtimesHelper {

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

  def makeRadio(day: String, slot: Int) = {
    val lable = List("W","K","N")
    def setStartVal = {
      Full(if (setSlot(worktimeOfDozent,day,slot).isWishtime) {
             "W"
           } else if (setSlot(worktimeOfDozent,day,slot).isAvailableTime) {
               "K"
             } else {"N"})
    }
    (day,slot) match {
      case ("Mo",1) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { oneMo = TimeSlot(oneMo.day,oneMo.time,oneMo.isWishtime,true) }
                              else if (s == "W") {
                                oneMo = TimeSlot(oneMo.day,oneMo.time,true,oneMo.isAvailableTime)
                         }).toForm
      case ("Tu",1) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { oneTu = TimeSlot(oneTu.day,oneTu.time,oneTu.isWishtime,true) }
                              else if (s == "W") {
                                oneTu = TimeSlot(oneTu.day,oneTu.time,true,oneTu.isAvailableTime)
                         }).toForm
      case ("We",1) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { oneWe = TimeSlot(oneWe.day,oneWe.time,oneWe.isWishtime,true) }
                              else if (s == "W") {
                                oneWe = TimeSlot(oneWe.day,oneWe.time,true,oneWe.isAvailableTime)
                         }).toForm
      case ("Th",1) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { oneTh = TimeSlot(oneTh.day,oneTh.time,oneTh.isWishtime,true) }
                              else if (s == "W") {
                                oneTh = TimeSlot(oneTh.day,oneTh.time,true,oneTh.isAvailableTime)
                         }).toForm
      case ("Fr",1) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { oneFr = TimeSlot(oneFr.day,oneFr.time,oneFr.isWishtime,true) }
                              else if (s == "W") {
                                oneFr = TimeSlot(oneFr.day,oneFr.time,true,oneFr.isAvailableTime)
                         }).toForm
      case ("Mo",2) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { twoMo = TimeSlot(twoMo.day,twoMo.time,twoMo.isWishtime,true) }
                              else if (s == "W") {
                                twoMo = TimeSlot(twoMo.day,twoMo.time,true,twoMo.isAvailableTime)
                         }).toForm
      case ("Tu",2) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { twoTu = TimeSlot(twoTu.day,twoTu.time,twoTu.isWishtime,true) }
                              else if (s == "W") {
                                twoTu = TimeSlot(twoTu.day,twoTu.time,true,twoTu.isAvailableTime)
                         }).toForm
      case ("We",2) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { twoWe = TimeSlot(twoWe.day,twoWe.time,twoWe.isWishtime,true) }
                              else if (s == "W") {
                                twoWe = TimeSlot(twoWe.day,twoWe.time,true,twoWe.isAvailableTime)
                         }).toForm
      case ("Th",2) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { twoTh = TimeSlot(twoTh.day,twoTh.time,twoTh.isWishtime,true) }
                              else if (s == "W") {
                                twoTh = TimeSlot(twoTh.day,twoTh.time,true,twoTh.isAvailableTime)
                         }).toForm
      case ("Fr",2) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { twoFr = TimeSlot(twoFr.day,twoFr.time,twoFr.isWishtime,true) }
                              else if (s == "W") {
                                twoFr = TimeSlot(twoFr.day,twoFr.time,true,twoFr.isAvailableTime)
                         }).toForm
      case ("Mo",3) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { threeMo = TimeSlot(threeMo.day,threeMo.time,threeMo.isWishtime,true) }
                              else if (s == "W") {
                                threeMo = TimeSlot(threeMo.day,threeMo.time,true,threeMo.isAvailableTime)
                         }).toForm
      case ("Tu",3) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { threeTu = TimeSlot(threeTu.day,threeTu.time,threeTu.isWishtime,true) }
                              else if (s == "W") {
                                threeTu = TimeSlot(threeTu.day,threeTu.time,true,threeTu.isAvailableTime)
                         }).toForm
      case ("We",3) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { threeWe = TimeSlot(threeWe.day,threeWe.time,threeWe.isWishtime,true) }
                              else if (s == "W") {
                                threeWe = TimeSlot(threeWe.day,threeWe.time,true,threeWe.isAvailableTime)
                         }).toForm
      case ("Th",3) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { threeTh = TimeSlot(threeTh.day,threeTh.time,threeTh.isWishtime,true) }
                              else if (s == "W") {
                                threeTh = TimeSlot(threeTh.day,threeTh.time,true,threeTh.isAvailableTime)
                         }).toForm
      case ("Fr",3) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { threeFr = TimeSlot(threeFr.day,threeFr.time,threeFr.isWishtime,true) }
                              else if (s == "W") {
                                threeFr = TimeSlot(threeFr.day,threeFr.time,true,threeFr.isAvailableTime)
                         }).toForm
      case ("Mo",4) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fourMo = TimeSlot(fourMo.day,fourMo.time,fourMo.isWishtime,true) }
                              else if (s == "W") {
                                fourMo = TimeSlot(fourMo.day,fourMo.time,true,fourMo.isAvailableTime)
                         }).toForm
      case ("Tu",4) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fourTu = TimeSlot(fourTu.day,fourTu.time,fourTu.isWishtime,true) }
                              else if (s == "W") {
                                fourTu = TimeSlot(fourTu.day,fourTu.time,true,fourTu.isAvailableTime)
                         }).toForm
      case ("We",4) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fourWe = TimeSlot(fourWe.day,fourWe.time,fourWe.isWishtime,true) }
                              else if (s == "W") {
                                fourWe = TimeSlot(fourWe.day,fourWe.time,true,fourWe.isAvailableTime)
                         }).toForm
      case ("Th",4) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fourTh = TimeSlot(fourTh.day,fourTh.time,fourTh.isWishtime,true) }
                              else if (s == "W") {
                                fourTh = TimeSlot(fourTh.day,fourTh.time,true,fourTh.isAvailableTime)
                         }).toForm
      case ("Fr",4) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fourFr = TimeSlot(fourFr.day,fourFr.time,fourFr.isWishtime,true) }
                              else if (s == "W") {
                                fourFr = TimeSlot(fourFr.day,fourFr.time,true,fourFr.isAvailableTime)
                         }).toForm
      case ("Mo",5) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fiveMo = TimeSlot(fiveMo.day,fiveMo.time,fiveMo.isWishtime,true) }
                              else if (s == "W") {
                                fiveMo = TimeSlot(fiveMo.day,fiveMo.time,true,fiveMo.isAvailableTime)
                         }).toForm
      case ("Tu",5) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fiveTu = TimeSlot(fiveTu.day,fiveTu.time,fiveTu.isWishtime,true) }
                              else if (s == "W") {
                                fiveTu = TimeSlot(fiveTu.day,fiveTu.time,true,fiveTu.isAvailableTime)
                         }).toForm
      case ("We",5) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fiveWe = TimeSlot(fiveWe.day,fiveWe.time,fiveWe.isWishtime,true) }
                              else if (s == "W") {
                                fiveWe = TimeSlot(fiveWe.day,fiveWe.time,true,fiveWe.isAvailableTime)
                         }).toForm
      case ("Th",5) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fiveTh = TimeSlot(fiveTh.day,fiveTh.time,fiveTh.isWishtime,true) }
                              else if (s == "W") {
                                fiveTh = TimeSlot(fiveTh.day,fiveTh.time,true,fiveTh.isAvailableTime)
                         }).toForm
      case ("Fr",5) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { fiveFr = TimeSlot(fiveFr.day,fiveFr.time,fiveFr.isWishtime,true) }
                              else if (s == "W") {
                                fiveFr = TimeSlot(fiveFr.day,fiveFr.time,true,fiveFr.isAvailableTime)
                         }).toForm
      case ("Mo",6) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { sixMo = TimeSlot(sixMo.day,sixMo.time,sixMo.isWishtime,true) }
                              else if (s == "W") {
                                sixMo = TimeSlot(sixMo.day,sixMo.time,true,sixMo.isAvailableTime)
                         }).toForm
      case ("Tu",6) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { sixTu = TimeSlot(sixTu.day,sixTu.time,sixTu.isWishtime,true) }
                              else if (s == "W") {
                                sixTu = TimeSlot(sixTu.day,sixTu.time,true,sixTu.isAvailableTime)
                         }).toForm
      case ("We",6) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { sixWe = TimeSlot(sixWe.day,sixWe.time,sixWe.isWishtime,true) }
                              else if (s == "W") {
                                sixWe = TimeSlot(sixWe.day,sixWe.time,true,sixWe.isAvailableTime)
                         }).toForm
      case ("Th",6) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { sixTh = TimeSlot(sixTh.day,sixTh.time,sixTh.isWishtime,true) }
                              else if (s == "W") {
                                sixTh = TimeSlot(sixTh.day,sixTh.time,true,sixTh.isAvailableTime)
                         }).toForm
      case ("Fr",6) => SHtml.radio(lable,setStartVal,
                         s => if(s == "K") { sixFr = TimeSlot(sixFr.day,sixFr.time,sixFr.isWishtime,true) }
                              else if (s == "W") {
                                sixFr = TimeSlot(sixFr.day,sixFr.time,true,sixFr.isAvailableTime)
                         }).toForm
    }
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
             <th>{/*makeRadio("We",3)*/}</th>
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
              <th>{"W: Wunsch-Zeit"}</th>
              <th>{"K: Kann-Zeit"}</th>
              <th>{"N: N/A-Zeit"}</th>
              <th></th>
              <th></th>
            </tfoot>
          </table>
}