package org.unsane.spirit.planningweb.snippet

/**
 * This class is the view to manage the rooms
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
import planningweb.roommanagement.impl.Room
import planningweb.persistence._
import planningweb.transform._
import scala.collection.mutable.Set

class Rooms {
  val peristence:IPersistence = PersistenceFactory
                                    .createPersistence(TransformFactory
                                    .createTransformRoom(Props.get("spirit.pers.layer") openOr ""))
  var building = ""
  var number = ""
  var roomSize = "0"
  var roomEquipment = Set[String]()

  // to add a new Room to persistence

  def add() = {
    def addRoom () {
      val rooms = peristence.read.asInstanceOf[List[Room]]
      val alreadyAvailable = rooms filter {room => room.building == building && room.number == number}

      if (building != "" && number != "" && roomSize != "0" && roomEquipment != List() && alreadyAvailable.isEmpty) {
        try {
          peristence.create(Room(building,number,roomSize.toInt,roomEquipment.toList))
        } catch {
            case e: Exception => S.notice(roomSize + " is not a Number!")
          }
      }
      else {
        S.notice("Room already exist or Input is empty!")
      }
    }

    val buildingText = SHtml.text("", b => building = b.trim)
    val numberText = SHtml.text("", n => number = n.trim)
    val roomSizeText = SHtml.text("0", s => roomSize = s.trim)

    val beamerCheck = SHtml.checkbox(false, if (_) roomEquipment += "Beamer")
    val overheadCheck = SHtml.checkbox(false, if (_) roomEquipment += "Projektor")
    val boardCheck = SHtml.checkbox(false, if (_) roomEquipment += "Tafel")
    val pcCheck = SHtml.checkbox(false, if (_) roomEquipment += "PC")
    val linuxCheck = SHtml.checkbox(false, if (_) roomEquipment += "Linux")

    val addButton = SHtml.submit("Hinzufügen", addRoom)

    <table id="table-plane">
      <tr>
       <th>{"Gebäude:" ++ <br /> ++ buildingText}</th>
       <th>{"Bezeinung:" ++ <br /> ++ numberText}</th>
       <th>{"Größe:" ++ <br /> ++ roomSizeText}</th>
      </tr>
    </table> ++
    <table>
      <tr>
       <th>{"Beamer:" ++ <br /> ++ beamerCheck}</th>
       <th>{"Projektor:" ++ <br /> ++ overheadCheck}</th>
       <th>{"Tafel:" ++ <br /> ++ boardCheck}</th>
       <th>{"PC:" ++ <br /> ++ pcCheck}</th>
       <th>{"Linux" ++ <br /> ++  linuxCheck}</th>
      </tr>
     </table> ++ addButton
  }
  // to delete a room from persistence
  def delete () = {
    val toDelete = Set[Room]()
    val rooms = peristence.read.asInstanceOf[List[Room]]

    def deleteRooms(toDelete: Set[Room]) = {
      toDelete foreach {
        room => peristence.delete(room)
      }
    }

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"löschen:"}</th>
                       <th>{"Gebäude:"}</th>
                       <th>{"Raum:"}</th>
                       <th>{"Größe"}</th>
                       <th>{"Ausstattung:"}</th>
                      </thead>
                      { rooms.flatMap {
                          room => <tr>
                                   <th>{SHtml.checkbox(false, if (_) toDelete += room)}</th>
                                   <th>{room.building}</th>
                                   <th>{room.number}</th>
                                   <th>{room.roomSize.toString}</th>
                                   <th>{room.roomEquipment.map(_ ++ <br />)}</th>
                                  </tr>
                        }
                      }
                     </table>

    val deleteButton = SHtml.submit("Löschen", () => deleteRooms(toDelete))

    checkboxes ++ deleteButton
  }

  def render = {
   "#add *" #> add &
   "#delete *" #> delete
  }
}