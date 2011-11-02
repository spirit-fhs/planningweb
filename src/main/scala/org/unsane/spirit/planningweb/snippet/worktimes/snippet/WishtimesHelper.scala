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
  object oneMo extends SessionVar[TimeSlot](TimeSlot("Mo",1,false,false))
  object twoMo extends SessionVar[TimeSlot](TimeSlot("Mo",2,false,false))
  object threeMo extends SessionVar[TimeSlot](TimeSlot("Mo",3,false,false))
  object fourMo extends SessionVar[TimeSlot](TimeSlot("Mo",4,false,false))
  object fiveMo extends SessionVar[TimeSlot](TimeSlot("Mo",5,false,false))
  object sixMo extends SessionVar[TimeSlot](TimeSlot("Mo",6,false,false))

  object oneTu extends SessionVar[TimeSlot](TimeSlot("Tu",1,false,false))
  object twoTu extends SessionVar[TimeSlot](TimeSlot("Tu",2,false,false))
  object threeTu extends SessionVar[TimeSlot](TimeSlot("Tu",3,false,false))
  object fourTu extends SessionVar[TimeSlot](TimeSlot("Tu",4,false,false))
  object fiveTu extends SessionVar[TimeSlot](TimeSlot("Tu",5,false,false))
  object sixTu  extends SessionVar[TimeSlot](TimeSlot("Tu",6,false,false))

  object oneWe extends SessionVar[TimeSlot](TimeSlot("We",1,false,false))
  object twoWe extends SessionVar[TimeSlot](TimeSlot("We",2,false,false))
  object threeWe extends SessionVar[TimeSlot](TimeSlot("We",3,false,false))
  object fourWe extends SessionVar[TimeSlot](TimeSlot("We",4,false,false))
  object fiveWe extends SessionVar[TimeSlot](TimeSlot("We",5,false,false))
  object sixWe  extends SessionVar[TimeSlot](TimeSlot("We",6,false,false))

  object oneTh extends SessionVar[TimeSlot](TimeSlot("Th",1,false,false))
  object twoTh extends SessionVar[TimeSlot](TimeSlot("Th",2,false,false))
  object threeTh extends SessionVar[TimeSlot](TimeSlot("Th",3,false,false))
  object fourTh extends SessionVar[TimeSlot](TimeSlot("Th",4,false,false))
  object fiveTh extends SessionVar[TimeSlot](TimeSlot("Th",5,false,false))
  object sixTh extends SessionVar[TimeSlot](TimeSlot("Th",6,false,false))

  object oneFr extends SessionVar[TimeSlot](TimeSlot("Fr",1,false,false))
  object twoFr extends SessionVar[TimeSlot](TimeSlot("Fr",2,false,false))
  object threeFr extends SessionVar[TimeSlot](TimeSlot("Fr",3,false,false))
  object fourFr extends SessionVar[TimeSlot](TimeSlot("Fr",4,false,false))
  object fiveFr extends SessionVar[TimeSlot](TimeSlot("Fr",5,false,false))
  object sixFr extends SessionVar[TimeSlot](TimeSlot("Fr",6,false,false))

  // a lift of alle Slots
  val slots = List(oneMo,oneTu,oneWe,oneTh,oneFr,
                   twoMo,twoTu,twoWe,twoTh,twoFr,
                   threeMo,threeTu,threeWe,threeTh,threeFr,
                   fourMo,fourTu,fourWe,fourTh,fourFr,
                   fiveMo,fiveTu,fiveWe,fiveTh,fiveFr,
                   sixMo,sixTu,sixWe,sixTh,sixFr)

}
