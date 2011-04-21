package org.unsane.spirit.planningweb

/**
 * Developer: Christoph Schmidt
 */

import net.liftweb.util.Props
import org.specs._
import persistence._
import net.liftweb.mongodb._
import worktimemanagement.impl._
import dozentmanagement.impl._
import roommanagement.impl._
import transform._

object WorktimeManagementSpecs extends Specification("WorktimeManagement Specification") {
  val usedPersistence = Props.get("spirit.pers.layer") openOr ""

  usedPersistence match {
    case "mongoDB" =>
      MongoDB.defineDbAuth(DefaultMongoIdentifier,
        MongoAddress(MongoHost("127.0.0.1", 27017), "spirit_curriculum"),
        "spirit_curriculum",
        "spirit_curriculum")
      case _ =>
  }

  val worktimeManager = WorktimeManager(false)
  val worktimeManagerUpdate = WorktimeManager(true)

  val dozentType = DozentType("TestType",5,true)
  val dozentTypeUpdate = new DozentType("TestTypeUpdate",5,true)
  val dozent = Dozent("TestDozent", "self-management", 2.5, dozentType)
  val dozentUpdate = new Dozent("TestDozentUpdate", "self-management-update", 1.1, dozentTypeUpdate)

  val room = Room("H","202",50,List("Beamer"))

  val timeSlot = TimeSlot("Mo",1,false,false)

  val worktime = Worktime(dozent,List(timeSlot),List(room))
  val worktimeUpdate = Worktime(dozentUpdate,List(),List())

  "WorktimeManager" should {
    "when add a worktimemanager" >> {
       val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformWorktimeManager(usedPersistence))
      persistence create(worktimeManager)
      "contain this worktimemanager in persistence" >> {
        val worktimemanagers = persistence.read.asInstanceOf[List[WorktimeManager]]
        worktimemanagers contains(worktimeManager) must_==  true
      }
    }
    "when delete a worktimemanager" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformWorktimeManager(usedPersistence))
      persistence.delete(worktimeManager)
      "not contain the deleted worktimemanager in persistence" >> {
        val worktimemanagers = persistence.read.asInstanceOf[List[WorktimeManager]]
        worktimemanagers contains(worktimeManager) must_==  false
      }
    }
    "when update a worktimemanager" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformWorktimeManager(usedPersistence))
      persistence create(worktimeManager)
      persistence update(worktimeManager,worktimeManagerUpdate)
      "contain the updated worktimemanager in persistence" >> {
        val worktimemanagers = persistence.read.asInstanceOf[List[WorktimeManager]]
        worktimemanagers contains(worktimeManagerUpdate) must_==  true
        worktimemanagers contains(worktimeManager) must_==  false
       persistence delete(worktimeManagerUpdate)
      }
    }
  }

  "Worktime" should {
    "when add a worktime" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformWorktime(usedPersistence))
      persistence create(worktime)
      "contain this worktime in persistence" >> {
        val worktimes = persistence.read.asInstanceOf[List[Worktime]]
        worktimes contains(worktime) must_==  true
      }
    }
    "when delete a worktime" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformWorktime(usedPersistence))
      persistence.delete(worktime)
      "not contain the deleted worktime in persistence" >> {
        val worktimes = persistence.read.asInstanceOf[List[Worktime]]
        worktimes contains(worktime) must_==  false
      }
    }
    "when update a worktime" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformWorktime(usedPersistence))
      persistence create(worktime)
      persistence update(worktime,worktimeUpdate)
      "contain the updated worktime in persistence" >> {
        val worktimes = persistence.read.asInstanceOf[List[Worktime]]
        worktimes contains(worktimeUpdate) must_==  true
        worktimes contains(worktime) must_==  false
        persistence delete(worktimeUpdate)
      }
    }
  }
}