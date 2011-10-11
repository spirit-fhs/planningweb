package org.unsane.spirit.planningweb.snippet.worktimes.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
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

  val wishTimeColor = "wish"
  val availableTimeColor = "available"

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
                         s => s match {
                           case "K" => oneMo = TimeSlot(oneMo.day,oneMo.time,oneMo.isWishtime,true)
                           case "W" => oneMo = TimeSlot(oneMo.day,oneMo.time,true,oneMo.isAvailableTime)
                           case _ => oneMo = TimeSlot(oneMo.day,oneMo.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Tu",1) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => oneTu = TimeSlot(oneTu.day,oneTu.time,oneTu.isWishtime,true)
                           case "W" => oneTu = TimeSlot(oneTu.day,oneTu.time,true,oneTu.isAvailableTime)
                           case _ => oneTu = TimeSlot(oneTu.day,oneTu.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("We",1) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => oneWe = TimeSlot(oneWe.day,oneWe.time,oneWe.isWishtime,true)
                           case "W" => oneWe = TimeSlot(oneWe.day,oneWe.time,true,oneWe.isAvailableTime)
                           case _ => oneWe = TimeSlot(oneWe.day,oneWe.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Th",1) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => oneTh = TimeSlot(oneTh.day,oneTh.time,oneTh.isWishtime,true)
                           case "W" => oneTh = TimeSlot(oneTh.day,oneTh.time,true,oneTh.isAvailableTime)
                           case _ => oneTh = TimeSlot(oneTh.day,oneTh.time,false,false)
                         }
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Fr",1) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => oneFr = TimeSlot(oneFr.day,oneFr.time,oneFr.isWishtime,true)
                           case "W" => oneFr = TimeSlot(oneFr.day,oneFr.time,true,oneFr.isAvailableTime)
                           case _ =>   oneFr = TimeSlot(oneFr.day,oneFr.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Mo",2) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => twoMo = TimeSlot(twoMo.day,twoMo.time,twoMo.isWishtime,true)
                           case "W" => twoMo = TimeSlot(twoMo.day,twoMo.time,true,twoMo.isAvailableTime)
                           case _ => twoMo = TimeSlot(twoMo.day,twoMo.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Tu",2) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => twoTu = TimeSlot(twoTu.day,twoTu.time,twoTu.isWishtime,true)
                           case "W" => twoTu = TimeSlot(twoTu.day,twoTu.time,true,twoTu.isAvailableTime)
                           case _ => twoTu = TimeSlot(twoTu.day,twoTu.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("We",2) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => twoWe = TimeSlot(twoWe.day,twoWe.time,twoWe.isWishtime,true)
                           case "W" => twoWe = TimeSlot(twoWe.day,twoWe.time,true,twoWe.isAvailableTime)
                           case _ => twoWe = TimeSlot(twoWe.day,twoWe.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Th",2) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => twoTh = TimeSlot(twoTh.day,twoTh.time,twoTh.isWishtime,true)
                           case "W" => twoTh = TimeSlot(twoTh.day,twoTh.time,true,twoTh.isAvailableTime)
                           case _ => twoTh = TimeSlot(twoTh.day,twoTh.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Fr",2) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => twoFr = TimeSlot(twoFr.day,twoFr.time,twoFr.isWishtime,true)
                           case "W" => twoFr = TimeSlot(twoFr.day,twoFr.time,true,twoFr.isAvailableTime)
                           case _ => twoFr = TimeSlot(twoFr.day,twoFr.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Mo",3) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => threeMo = TimeSlot(threeMo.day,threeMo.time,threeMo.isWishtime,true)
                           case "W" => threeMo = TimeSlot(threeMo.day,threeMo.time,true,threeMo.isAvailableTime)
                           case _ => threeMo = TimeSlot(threeMo.day,threeMo.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Tu",3) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => threeTu = TimeSlot(threeTu.day,threeTu.time,threeTu.isWishtime,true)
                           case "W" => threeTu = TimeSlot(threeTu.day,threeTu.time,true,threeTu.isAvailableTime)
                           case _  => threeTu = TimeSlot(threeTu.day,threeTu.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("We",3) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => threeWe = TimeSlot(threeWe.day,threeWe.time,threeWe.isWishtime,true)
                           case "W" => threeWe = TimeSlot(threeWe.day,threeWe.time,true,threeWe.isAvailableTime)
                           case _ => threeWe = TimeSlot(threeWe.day,threeWe.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Th",3) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => threeTh = TimeSlot(threeTh.day,threeTh.time,threeTh.isWishtime,true)
                           case "W" =>  threeTh = TimeSlot(threeTh.day,threeTh.time,true,threeTh.isAvailableTime)
                           case _ =>  threeTh = TimeSlot(threeTh.day,threeTh.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Fr",3) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => threeFr = TimeSlot(threeFr.day,threeFr.time,threeFr.isWishtime,true)
                           case "W" => threeFr = TimeSlot(threeFr.day,threeFr.time,true,threeFr.isAvailableTime)
                           case _ => threeFr = TimeSlot(threeFr.day,threeFr.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Mo",4) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fourMo = TimeSlot(fourMo.day,fourMo.time,fourMo.isWishtime,true)
                           case "W" => fourMo = TimeSlot(fourMo.day,fourMo.time,true,fourMo.isAvailableTime)
                           case _ => fourMo = TimeSlot(fourMo.day,fourMo.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Tu",4) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fourTu = TimeSlot(fourTu.day,fourTu.time,fourTu.isWishtime,true)
                           case "W" => fourTu = TimeSlot(fourTu.day,fourTu.time,true,fourTu.isAvailableTime)
                           case _ => fourTu = TimeSlot(fourTu.day,fourTu.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("We",4) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fourWe = TimeSlot(fourWe.day,fourWe.time,fourWe.isWishtime,true)
                           case "W" => fourWe = TimeSlot(fourWe.day,fourWe.time,true,fourWe.isAvailableTime)
                           case _ => fourWe = TimeSlot(fourWe.day,fourWe.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Th",4) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fourTh = TimeSlot(fourTh.day,fourTh.time,fourTh.isWishtime,true)
                           case "W" => fourTh = TimeSlot(fourTh.day,fourTh.time,true,fourTh.isAvailableTime)
                           case _ => fourTh = TimeSlot(fourTh.day,fourTh.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Fr",4) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fourFr = TimeSlot(fourFr.day,fourFr.time,fourFr.isWishtime,true)
                           case "W" => fourFr = TimeSlot(fourFr.day,fourFr.time,true,fourFr.isAvailableTime)
                           case _ => fourFr = TimeSlot(fourFr.day,fourFr.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Mo",5) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fiveMo = TimeSlot(fiveMo.day,fiveMo.time,fiveMo.isWishtime,true)
                           case "W" => fiveMo = TimeSlot(fiveMo.day,fiveMo.time,true,fiveMo.isAvailableTime)
                           case _ =>  fiveMo = TimeSlot(fiveMo.day,fiveMo.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Tu",5) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fiveTu = TimeSlot(fiveTu.day,fiveTu.time,fiveTu.isWishtime,true)
                           case "W" => fiveTu = TimeSlot(fiveTu.day,fiveTu.time,true,fiveTu.isAvailableTime)
                           case _ => fiveTu = TimeSlot(fiveTu.day,fiveTu.time,false,false)}
                         ,"onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("We",5) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fiveWe = TimeSlot(fiveWe.day,fiveWe.time,fiveWe.isWishtime,true)
                           case "W" => fiveWe = TimeSlot(fiveWe.day,fiveWe.time,true,fiveWe.isAvailableTime)
                           case _ => fiveWe = TimeSlot(fiveWe.day,fiveWe.time,false,false)}
                         ,"onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Th",5) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fiveTh = TimeSlot(fiveTh.day,fiveTh.time,fiveTh.isWishtime,true)
                           case "W" => fiveTh = TimeSlot(fiveTh.day,fiveTh.time,true,fiveTh.isAvailableTime)
                           case _ =>  fiveTh = TimeSlot(fiveTh.day,fiveTh.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Fr",5) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => fiveFr = TimeSlot(fiveFr.day,fiveFr.time,fiveFr.isWishtime,true)
                           case "W" => fiveFr = TimeSlot(fiveFr.day,fiveFr.time,true,fiveFr.isAvailableTime)
                           case _ => fiveFr = TimeSlot(fiveFr.day,fiveFr.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Mo",6) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => sixMo = TimeSlot(sixMo.day,sixMo.time,sixMo.isWishtime,true)
                           case "W" => sixMo = TimeSlot(sixMo.day,sixMo.time,true,sixMo.isAvailableTime)
                           case _ => sixMo = TimeSlot(sixMo.day,sixMo.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Tu",6) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => sixTu = TimeSlot(sixTu.day,sixTu.time,sixTu.isWishtime,true)
                           case "W" => sixTu = TimeSlot(sixTu.day,sixTu.time,true,sixTu.isAvailableTime)
                           case _ => sixTu = TimeSlot(sixTu.day,sixTu.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("We",6) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => sixWe = TimeSlot(sixWe.day,sixWe.time,sixWe.isWishtime,true)
                           case "W" => sixWe = TimeSlot(sixWe.day,sixWe.time,true,sixWe.isAvailableTime)
                           case _ => sixWe = TimeSlot(sixWe.day,sixWe.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Th",6) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => sixTh = TimeSlot(sixTh.day,sixTh.time,sixTh.isWishtime,true)
                           case "W" => sixTh = TimeSlot(sixTh.day,sixTh.time,true,sixTh.isAvailableTime)
                           case _ => sixTh = TimeSlot(sixTh.day,sixTh.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
      case ("Fr",6) => SHtml.radio(lable,setStartVal,
                         s => s match {
                           case "K" => sixFr = TimeSlot(sixFr.day,sixFr.time,sixFr.isWishtime,true)
                           case "W" => sixFr = TimeSlot(sixFr.day,sixFr.time,true,sixFr.isAvailableTime)
                           case _ => sixFr = TimeSlot(sixFr.day,sixFr.time,false,false)}
                         , "onclick" -> JsRaw("changeColor()").toJsCmd).toForm
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