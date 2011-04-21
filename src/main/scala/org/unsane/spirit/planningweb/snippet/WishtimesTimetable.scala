package org.unsane.spirit.planningweb.snippet

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

  val timetable =
          <table id="table-select">
            <thead>
             <th></th>
             <th>{"Montag"}<table id="table-check"><tr><th>{"W"}</th><th>{"K"}</th></tr></table></th>
             <th>{"Dienstag"}<table id="table-check"><tr><th>{"W"}</th><th>{"K"}</th></tr></table></th>
             <th>{"Mittwoch"}<table id="table-check"><tr><th>{"W"}</th><th>{"K"}</th></tr></table></th>
             <th>{"Donnerstag"}<table id="table-check"><tr><th>{"W"}</th><th>{"K"}</th></tr></table></th>
             <th>{"Freitag"}<table id="table-check"><tr><th>{"W"}</th><th>{"K"}</th></tr></table></th>
            </thead>
            <tr>
             <th>{"08:15-09:45"}</th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",1).isWishtime,
                                      if (_) oneMo = TimeSlot(oneMo.day,oneMo.time,true,oneMo.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",1).isAvailableTime,
                                      if (_) oneMo = TimeSlot(oneMo.day,oneMo.time,oneMo.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",1).isWishtime,
                                      if (_) oneTu = TimeSlot(oneTu.day,oneTu.time,true,oneTu.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",1).isAvailableTime,
                                      if (_) oneTu = TimeSlot(oneTu.day,oneTu.time,oneTu.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",1).isWishtime,
                                      if (_) oneWe = TimeSlot(oneWe.day,oneWe.time,true,oneWe.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",1).isAvailableTime,
                                      if (_) oneWe = TimeSlot(oneWe.day,oneWe.time,oneWe.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",1).isWishtime,
                                      if (_) oneTh = TimeSlot(oneTh.day,oneTh.time,true,oneTh.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",1).isAvailableTime,
                                      if (_) oneTh = TimeSlot(oneTh.day,oneTh.time,oneTh.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",1).isWishtime,
                                      if (_) oneFr = TimeSlot(oneFr.day,oneFr.time,true,oneFr.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",1).isAvailableTime,
                                      if (_) oneFr = TimeSlot(oneFr.day,oneFr.time,oneFr.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
            </tr>
            <tr>
             <th>{"10:00-11:30"}</th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",2).isWishtime,
                                      if (_) twoMo = TimeSlot(twoMo.day,twoMo.time,true,twoMo.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",2).isAvailableTime,
                                      if (_) twoMo = TimeSlot(twoMo.day,twoMo.time,twoMo.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",2).isWishtime,
                                      if (_) twoTu = TimeSlot(twoTu.day,twoTu.time,true,twoTu.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",2).isAvailableTime,
                                      if (_) twoTu = TimeSlot(twoTu.day,twoTu.time,twoTu.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",2).isWishtime,
                                      if (_) twoWe = TimeSlot(twoWe.day,twoWe.time,true,twoWe.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",2).isAvailableTime,
                                      if (_) twoWe = TimeSlot(twoWe.day,twoWe.time,twoWe.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",2).isWishtime,
                                      if (_) twoTh = TimeSlot(twoTh.day,twoTh.time,true,twoTh.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",2).isAvailableTime,
                                      if (_) twoTh = TimeSlot(twoTh.day,twoTh.time,twoTh.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",2).isWishtime,
                                      if (_) twoFr = TimeSlot(twoFr.day,twoFr.time,true,twoFr.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",2).isAvailableTime,
                                      if (_) twoFr = TimeSlot(twoFr.day,twoFr.time,twoFr.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
            </tr>
            <tr>
             <th>{"11:45-13:15"}</th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",3).isWishtime,
                                      if (_) threeMo = TimeSlot(threeMo.day,threeMo.time,true,threeMo.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",3).isAvailableTime,
                                      if (_) threeMo = TimeSlot(threeMo.day,threeMo.time,threeMo.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",3).isWishtime,
                                      if (_) threeTu = TimeSlot(threeTu.day,threeTu.time,true,threeTu.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",3).isAvailableTime,
                                      if (_) threeTu = TimeSlot(threeTu.day,threeTu.time,threeTu.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",3).isWishtime,
                                      if (_) threeWe = TimeSlot(threeWe.day,threeWe.time,true,threeWe.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",3).isAvailableTime,
                                      if (_) threeWe = TimeSlot(threeWe.day,threeWe.time,threeWe.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",3).isWishtime,
                                      if (_) threeTh = TimeSlot(threeTh.day,threeTh.time,true,threeTh.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",3).isAvailableTime,
                                      if (_) threeTh = TimeSlot(threeTh.day,threeTh.time,threeTh.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",3).isWishtime,
                                      if (_) threeFr = TimeSlot(threeFr.day,threeFr.time,true,threeFr.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",3).isAvailableTime,
                                      if (_) threeFr = TimeSlot(threeFr.day,threeFr.time,threeFr.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
            </tr>
            <tr>
             <th>{"14:15-15:45"}</th>
              <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",4).isWishtime,
                                      if (_) fourMo = TimeSlot(fourMo.day,fourMo.time,true,fourMo.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",4).isAvailableTime,
                                      if (_) fourMo = TimeSlot(fourMo.day,fourMo.time,fourMo.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",4).isWishtime,
                                      if (_) fourTu = TimeSlot(fourTu.day,fourTu.time,true,fourTu.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",4).isAvailableTime,
                                      if (_) fourTu = TimeSlot(fourTu.day,fourTu.time,fourTu.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",4).isWishtime,
                                      if (_) fourWe = TimeSlot(fourWe.day,fourWe.time,true,fourWe.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",4).isAvailableTime,
                                      if (_) fourWe = TimeSlot(fourWe.day,fourWe.time,fourWe.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",4).isWishtime,
                                      if (_) fourTh = TimeSlot(fourTh.day,fourTh.time,true,fourTh.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",4).isAvailableTime,
                                      if (_) fourTh = TimeSlot(fourTh.day,fourTh.time,fourTh.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",4).isWishtime,
                                      if (_) fourFr = TimeSlot(fourFr.day,fourFr.time,true,fourFr.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",4).isAvailableTime,
                                      if (_) fourFr = TimeSlot(fourFr.day,fourFr.time,fourFr.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
            </tr>
            <tr>
             <th>{"16:00-17:30"}</th>
              <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",5).isWishtime,
                                      if (_) fiveMo = TimeSlot(fiveMo.day,fiveMo.time,true,fiveMo.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",5).isAvailableTime,
                                      if (_) fiveMo = TimeSlot(fiveMo.day,fiveMo.time,fiveMo.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",5).isWishtime,
                                      if (_) fiveTu = TimeSlot(fiveTu.day,fiveTu.time,true,fiveTu.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",5).isAvailableTime,
                                      if (_) fiveTu = TimeSlot(fiveTu.day,fiveTu.time,fiveTu.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",5).isWishtime,
                                      if (_) fiveWe = TimeSlot(fiveWe.day,fiveWe.time,true,fiveWe.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",5).isAvailableTime,
                                      if (_) fiveWe = TimeSlot(fiveWe.day,fiveWe.time,fiveWe.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",5).isWishtime,
                                      if (_) fiveTh = TimeSlot(fiveTh.day,fiveTh.time,true,fiveTh.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",5).isAvailableTime,
                                      if (_) fiveTh = TimeSlot(fiveTh.day,fiveTh.time,fiveTh.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",5).isWishtime,
                                      if (_) fiveFr = TimeSlot(fiveFr.day,fiveFr.time,true,fiveFr.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",5).isAvailableTime,
                                      if (_) fiveFr = TimeSlot(fiveFr.day,fiveFr.time,fiveFr.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
            </tr>
            <tr>
             <th>{"17:45-19:15"}</th>
              <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",6).isWishtime,
                                      if (_) sixMo = TimeSlot(sixMo.day,sixMo.time,true,sixMo.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Mo",6).isAvailableTime,
                                      if (_) sixMo = TimeSlot(sixMo.day,sixMo.time,sixMo.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",6).isWishtime,
                                      if (_) sixTu = TimeSlot(sixTu.day,sixTu.time,true,sixTu.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Tu",6).isAvailableTime,
                                      if (_) sixTu = TimeSlot(sixTu.day,sixTu.time,sixTu.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",6).isWishtime,
                                      if (_) sixWe = TimeSlot(sixWe.day,sixWe.time,true,sixWe.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"We",6).isAvailableTime,
                                      if (_) sixWe = TimeSlot(sixWe.day,sixWe.time,sixWe.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",6).isWishtime,
                                      if (_) sixTh = TimeSlot(sixTh.day,sixTh.time,true,sixTh.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Th",6).isAvailableTime,
                                      if (_) sixTh = TimeSlot(sixTh.day,sixTh.time,sixTh.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
             <th>
               <table id="table-check">
                <tr>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",6).isWishtime,
                                      if (_) sixFr = TimeSlot(sixFr.day,sixFr.time,true,sixFr.isAvailableTime))}
                  </th>
                  <th>{SHtml.checkbox(setSlot(worktimeOfDozent,"Fr",6).isAvailableTime,
                                      if (_) sixFr = TimeSlot(sixFr.day,sixFr.time,sixFr.isWishtime,true))}
                  </th>
                </tr>
               </table>
             </th>
            </tr>
          </table>
}