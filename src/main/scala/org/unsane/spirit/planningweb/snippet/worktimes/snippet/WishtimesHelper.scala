package org.unsane.spirit.planningweb.snippet.worktimes.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml._
import net.liftweb.util.Props
import org.unsane.spirit.planningweb
import planningweb.worktimemanagement.impl._
import planningweb.dozentmanagement.impl._
import planningweb.persistence._
import planningweb.transform._
import de.codecarving.fhsldap.fhsldap
import de.codecarving.fhsldap.model.User

/**
 * This trait provides the Wishtime traits and classes with necessary informations
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
trait WishtimesHelper {

  val persLayer = Props.get("spirit.pers.layer") openOr ""

  val persistenceWManager:IPersistence = PersistenceFactory
                                           .createPersistence(TransformFactory
                                           .createTransformWorktimeManager(persLayer))

  val persistenceFHSDozent:IPersistence = PersistenceFactory
                                            .createPersistence(TransformFactory
                                            .createTransformFHSDozent(persLayer))

  val persistenceWorktime:IPersistence = PersistenceFactory
                                           .createPersistence(TransformFactory
                                           .createTransformWorktime(persLayer))

  val worktimeManagers = persistenceWManager.read.asInstanceOf[List[WorktimeManager]]
  val fhsdozents = persistenceFHSDozent.read.asInstanceOf[List[FHSDozent]]
  val worktimes = persistenceWorktime.read.asInstanceOf[List[Worktime]]

  val initialTimeSlots =
    List(TimeSlot("Mo",1,false,false),TimeSlot("Tu",1,false,false),TimeSlot("We",1,false,false),TimeSlot("Th",1,false,false),TimeSlot("Fr",1,false,false),
         TimeSlot("Mo",2,false,false),TimeSlot("Tu",2,false,false),TimeSlot("We",2,false,false),TimeSlot("Th",2,false,false),TimeSlot("Fr",2,false,false),
         TimeSlot("Mo",3,false,false),TimeSlot("Tu",3,false,false),TimeSlot("We",3,false,false),TimeSlot("Th",3,false,false),TimeSlot("Fr",3,false,false),
         TimeSlot("Mo",4,false,false),TimeSlot("Tu",4,false,false),TimeSlot("We",4,false,false),TimeSlot("Th",4,false,false),TimeSlot("Fr",4,false,false),
         TimeSlot("Mo",5,false,false),TimeSlot("Tu",5,false,false),TimeSlot("We",5,false,false),TimeSlot("Th",5,false,false),TimeSlot("Fr",5,false,false),
         TimeSlot("Mo",6,false,false),TimeSlot("Tu",6,false,false),TimeSlot("We",6,false,false),TimeSlot("Th",6,false,false),TimeSlot("Fr",6,false,false))


  // to set the status of the worktime capture
  var status = false
  worktimeManagers match {
    case List() => status = false
    case _ => status = worktimeManagers.head.status
  }

  val currentUserId = User.currentUserId.openOr("not available")
  var isFHSDozent = fhsdozents.filter(_.fhsId == currentUserId)

  // to set the worktime of a dozent
  var worktimeOfDozent = isFHSDozent match {
    case List() => List()
    case _ =>  worktimes.filter(_.dozent.name == isFHSDozent.head.dozent.name)
  }

  // to set the notes of a Dozent
  var noteTextOfDozent = worktimeOfDozent match {
    case List() => ""
    case _ => worktimeOfDozent.head.notes
  }

  // the different timeslots
  var oneMo = TimeSlot("Mo",1,false,false)
  var twoMo = TimeSlot("Mo",2,false,false)
  var threeMo = TimeSlot("Mo",3,false,false)
  var fourMo = TimeSlot("Mo",4,false,false)
  var fiveMo = TimeSlot("Mo",5,false,false)
  var sixMo = TimeSlot("Mo",6,false,false)

  var oneTu = TimeSlot("Tu",1,false,false)
  var twoTu = TimeSlot("Tu",2,false,false)
  var threeTu = TimeSlot("Tu",3,false,false)
  var fourTu = TimeSlot("Tu",4,false,false)
  var fiveTu = TimeSlot("Tu",5,false,false)
  var sixTu  = TimeSlot("Tu",6,false,false)

  var oneWe = TimeSlot("We",1,false,false)
  var twoWe = TimeSlot("We",2,false,false)
  var threeWe = TimeSlot("We",3,false,false)
  var fourWe = TimeSlot("We",4,false,false)
  var fiveWe = TimeSlot("We",5,false,false)
  var sixWe  = TimeSlot("We",6,false,false)

  var oneTh = TimeSlot("Th",1,false,false)
  var twoTh = TimeSlot("Th",2,false,false)
  var threeTh = TimeSlot("Th",3,false,false)
  var fourTh = TimeSlot("Th",4,false,false)
  var fiveTh = TimeSlot("Th",5,false,false)
  var sixTh = TimeSlot("Th",6,false,false)

  var oneFr = TimeSlot("Fr",1,false,false)
  var twoFr = TimeSlot("Fr",2,false,false)
  var threeFr = TimeSlot("Fr",3,false,false)
  var fourFr = TimeSlot("Fr",4,false,false)
  var fiveFr = TimeSlot("Fr",5,false,false)
  var sixFr = TimeSlot("Fr",6,false,false)
}
