package org.unsane.spirit.planningweb.snippet

/**
 * This class is the view to manage the wishtimes
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
import scala.collection.mutable.Set
import net.liftweb.util.Props
import org.unsane.spirit.planningweb
import planningweb.worktimemanagement.impl._
import planningweb.roommanagement.impl.Room

class Wishtimes extends WishtimesHelper with WishtimesTimetable {
  val worktimeFactor = 1.25
  val roomsToSave = Set[Room]()

  def select() = {
    if(status) {
      if(!isFHSDozent.isEmpty) {

        def save() = {
          val dozent = isFHSDozent.head.dozent
          val timeSlotsToSave = List(oneMo,oneTu,oneWe,oneTh,oneFr,
                                     twoMo,twoTu,twoWe,twoTh,twoFr,
                                     threeMo,threeTu,threeWe,threeTh,threeFr,
                                     fourMo,fourTu,fourWe,fourTh,fourFr,
                                     fiveMo,fiveTu,fiveWe,fiveTh,fiveFr,
                                     sixMo,sixTu,sixWe,sixTh,sixFr)

          def calculateMinSelectedTimes() = {
            lazy val minWorkTime = (dozent.typeD.requiredTime - dozent.timeSelfManagement) * worktimeFactor
            S.notice("Min: " + minWorkTime.toString)
            val selectedWorktime = timeSlotsToSave.filter(s => s.isWishtime == true || s.isAvailableTime == true).size
            S.notice("Selected: " + selectedWorktime.toString)
            if(selectedWorktime < minWorkTime) {
              false
            } else {
                true
              }
          }

          if(calculateMinSelectedTimes) {
            var alreadyInUse = false
            val before = worktimeOfDozent match {
              case List() => Worktime(dozent,initialTimeSlots,List())
              case _ => alreadyInUse = true
                        worktimeOfDozent.head
            }

            val after = Worktime(before.dozent,
                                 timeSlotsToSave,
                                 roomsToSave.toList)

            if(alreadyInUse) {
              persistenceWorktime update(before,after)
            } else {
                persistenceWorktime create after
              }
            S.notice("Your worktimes and rooms were saved!")
          } else {S.warning("Not enough times selected")}
        }

        val tableRooms = <table id="table-box">
                          <thead>
                           <th>{"Auswahl:"}</th>
                           <th>{"Gebäude:"}</th>
                           <th>{"Raum-Nr.:"}</th>
                           <th>{"Größe:"}</th>
                           <th>{"Ausstattung:"}</th>
                          </thead>
                          { rooms.flatMap {
                              room => <tr>
                                       <th>{SHtml.checkbox(isSelectedRoom(room), if (_) roomsToSave += room)}</th>
                                       <th>{room.building}</th>
                                       <th>{room.number}</th>
                                       <th>{room.roomSize.toString}</th>
                                       <th>{room.roomEquipment.map(_ ++ <br />)}</th>
                                      </tr>
                            }
                          }
                         </table>

        val saveButton = SHtml.submit("Speichern", save)

        timetable ++ tableRooms ++ saveButton
      } else {
          Text("Sie sind im System nicht mit FHS-ID registriert. Bitte wenden sie sich an den Lehrplaner!")
        }
    } else {
        Text("Die Wunschzeitenerfassung ist derzeit nicht freigeschaltet!")
      }
  }

  def render = {
    "#select *" #> select
  }
}