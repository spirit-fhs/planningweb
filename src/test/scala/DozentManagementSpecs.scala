package org.unsane.spirit.planningweb


/**
 * Developer: Christoph Schmidt
 */

import net.liftweb.util.Props
import org.specs._
import persistence._
import net.liftweb.mongodb._
import dozentmanagement.impl._
import worktimemanagement.impl._
import roommanagement.impl._
import transform._

object DozentManagementSpecs extends Specification("DozentManagement Specification") {

  val usedPersistence = Props.get("spirit.pers.layer") openOr ""

  usedPersistence match {
    case "mongoDB" =>
      MongoDB.defineDbAuth(DefaultMongoIdentifier,
        MongoAddress(MongoHost("127.0.0.1", 27017), "spirit_curriculum"),
        "spirit_curriculum",
        "spirit_curriculum")
      case _ =>
  }

  val dozentType = new DozentType("TestType",5,true)
  val dozentTypeUpdate = new DozentType("TestTypeUpdate",5,true)
  val dozent = new Dozent("TestDozent", "self-management", 2.5, dozentType)
  val dozentUpdate = new Dozent("TestDozentUpdate", "self-management-update", 1.1, dozentTypeUpdate)
  val fhsdozent = FHSDozent("fhsId",dozent)
  val fhsdozentUpdate = FHSDozent("fhsIdUpdate",dozentUpdate)
  val room = Room("H","202",50,List("Beamer"))
  val timeSlot = TimeSlot("Mo",1,false,false)
  val worktime = Worktime(fhsdozent.dozent,List(timeSlot),List(room))

  "DozentType" should {
    "when add a dozent-type" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformDozentType(usedPersistence))
      persistence create(dozentType)
      "contain this dozent-type in persistence" >> {
        val dozentTypes = persistence.read.asInstanceOf[List[DozentType]]
        dozentTypes contains(dozentType) must_==  true
      }
    }
    "when update a dozent-type" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformDozentType(usedPersistence))
      persistence.update(dozentType,dozentTypeUpdate)
      "contain the updated dozent-type in persistence" >> {
        val dozentTypes = persistence.read.asInstanceOf[List[DozentType]]
        dozentTypes contains(dozentTypeUpdate) must_==  true
      }
    }
    "when delete a dozent-type" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformDozentType(usedPersistence))
      persistence.delete(dozentTypeUpdate)
      "not contain the deleted dozent-type in persistence" >> {
        val dozentTypes = persistence.read.asInstanceOf[List[DozentType]]
        dozentTypes contains(dozentTypeUpdate) must_==  false
      }
    }
    "When delete a dozent-type which is allready in use" >> {
      val persistenceD:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformDozent(usedPersistence))

      val persistenceT:IPersistence = PersistenceFactory
                                        .createPersistence(TransformFactory
                                        .createTransformDozentType(usedPersistence))

      persistenceT.create(dozentType)
      persistenceD.create(dozent)
      persistenceT.delete(dozentType)
      "contain this dozent-type in persistence" >> {
        val dozentTypes = persistenceT.read.asInstanceOf[List[DozentType]]
        dozentTypes contains(dozentType) must_== true

        persistenceD.delete(dozent)
        persistenceT.delete(dozentType)
      }
    }
  }

  "Dozent" should {
    "when add a dozent" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformDozent(usedPersistence))
      persistence create(dozent)
      "contain this dozent in persistence" >> {
        val dozents = persistence.read.asInstanceOf[List[Dozent]]
        dozents contains(dozent) must_==  true
      }
    }
    "when update a dozent-type" >> {
      val persistenceD:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformDozent(usedPersistence))

      val persistenceT:IPersistence = PersistenceFactory
                                        .createPersistence(TransformFactory
                                        .createTransformDozentType(usedPersistence))

      persistenceT.create(dozentTypeUpdate)
      persistenceD.update(dozent,dozentUpdate)
      "contain the updated dozent in persistence" >> {
        val dozents = persistenceD.read.asInstanceOf[List[Dozent]]
        dozents contains(dozentUpdate) must_==  true
      }
    }
    "when delete a dozent" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformDozent(usedPersistence))

      val persistenceT:IPersistence = PersistenceFactory
                                        .createPersistence(TransformFactory
                                        .createTransformDozentType(usedPersistence))

      persistence.delete(dozentUpdate)
      "not contain the deleted dozent in persistence" >> {
        val dozents = persistence.read.asInstanceOf[List[Dozent]]
        dozents contains(dozentUpdate) must_==  false
        persistenceT.delete(dozentTypeUpdate)
      }
    }
  }

  "FHSDozent" should {
    "when add a fhsdozent" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformFHSDozent(usedPersistence))
      persistence create(fhsdozent)
      "contain this fhsdozent in persistence" >> {
        val fhsdozents = persistence.read.asInstanceOf[List[FHSDozent]]
        fhsdozents contains(fhsdozent) must_==  true
      }
    }
    "when delete a fhsdozent" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformFHSDozent(usedPersistence))
      persistence.delete(fhsdozent)
      "not contain the deleted fhsdozent in persistence" >> {
        val fhsdozents = persistence.read.asInstanceOf[List[FHSDozent]]
        fhsdozents contains(fhsdozent) must_==  false
      }
    }
    "when update a fhsdozent" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformFHSDozent(usedPersistence))
      persistence create(fhsdozent)
      persistence update(fhsdozent,fhsdozentUpdate)
      "contain the updated fhsdozent in persistence" >> {
        val fhsdozents = persistence.read.asInstanceOf[List[FHSDozent]]
        fhsdozents contains(fhsdozentUpdate) must_==  true
        fhsdozents contains(fhsdozent) must_==  false
        persistence delete(fhsdozentUpdate)
      }
    }
    "when delete a fhsdozent" >> {
      val persistenceFHSDozent:IPersistence = PersistenceFactory
                                               .createPersistence(TransformFactory
                                               .createTransformFHSDozent(usedPersistence))

      val persistenceWorktime:IPersistence = PersistenceFactory
                                               .createPersistence(TransformFactory
                                               .createTransformWorktime(usedPersistence))

      persistenceFHSDozent create(fhsdozent)
      persistenceWorktime create (worktime)
      persistenceFHSDozent delete(fhsdozent)
      "not contain the deleted fhsdozent in persistence and the worktimes of the dozent should also be deleted" >> {
        val fhsdozents = persistenceFHSDozent.read.asInstanceOf[List[FHSDozent]]
        val worktimes = persistenceWorktime.read.asInstanceOf[List[Worktime]]
        fhsdozents contains(fhsdozent) must_==  false
        worktimes contains(worktime) must_== false
      }
    }
  }
}