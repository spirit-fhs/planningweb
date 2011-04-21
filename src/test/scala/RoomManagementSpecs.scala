package org.unsane.spirit.planningweb


/**
 * Developer: Christoph Schmidt
 */

import net.liftweb.util.Props
import org.specs._
import persistence._
import net.liftweb.mongodb._
import roommanagement.impl._
import transform._

object RoomManagementSpecs extends Specification("RoomManagement Specification") {

  val usedPersistence = Props.get("spirit.pers.layer") openOr ""

  usedPersistence match {
    case "mongoDB" =>
      MongoDB.defineDbAuth(DefaultMongoIdentifier,
        MongoAddress(MongoHost("127.0.0.1", 27017), "spirit_curriculum"),
        "spirit_curriculum",
        "spirit_curriculum")
      case _ =>
  }

  val room = Room("building","123",25,List("Beamer"))
  val roomUpdate = Room("buildingUpdate","007",10,List("Tafel"))

  "Room" should {
    "when add a room" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformRoom(usedPersistence))
      persistence create(room)
      "contain this room in persistence" >> {
        val rooms = persistence.read.asInstanceOf[List[Room]]
        rooms contains(room) must_==  true
      }
    }
    "when update a room" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformRoom(usedPersistence))
      persistence.update(room,roomUpdate)
      "contain the updated room in persistence" >> {
        val rooms = persistence.read.asInstanceOf[List[Room]]
        rooms contains(roomUpdate) must_==  true
      }
    }
    "when delete a room" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformRoom(usedPersistence))

      persistence.delete(roomUpdate)
      "not contain the deleted room in persistence" >> {
        val rooms = persistence.read.asInstanceOf[List[Room]]
        rooms contains(roomUpdate) must_==  false
      }
    }
  }
}