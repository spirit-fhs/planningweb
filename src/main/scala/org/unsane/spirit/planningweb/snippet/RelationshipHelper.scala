package org.unsane.spirit.planningweb.snippet

/**
 * This provides all Relationship traits and classes with necessary informations
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
import scala.xml._
import scala.collection.mutable.Set
import net.liftweb.util.Props
import org.unsane.spirit.planningweb
import planningweb.transform._
import planningweb.persistence._
import planningweb.lecturemanagement.impl._
import planningweb.dozentmanagement.impl.Dozent

// this object is very important because all Relationship-Traits need the same
// instance of the staus objects
object RelationshipHelper {
  object InitialStatus
  object SelectedLecture
  object SelectedGroups
  object SelectedDozents
}

// this trait provides the Relationship-Traits with all important values etc
trait RelationshipHelper {

  val usedPersistence = Props.get("spirit.pers.layer") openOr ""
  val whence = S.referer openOr "/"
  val thisSide = "/lecture/lecturerelation"
  val lectureManagement = "/lecture/management"

  val persistence:IPersistence = PersistenceFactory
                                  .createPersistence(TransformFactory
                                  .createTransformLecture(usedPersistence))

  val lectures = persistence.read.asInstanceOf[List[Lecture]]

  // to set a lecture relationship apart from a tutorial relationship
  //object IsLecture extends SessionVar[Boolean](false)
  object IsTutorial extends SessionVar[Boolean](false)

  // all these type aliases are necessary to understand the definition of the session variables easier
  type GroupName = String
  type SemesterNumber = Int

  // we store in this object the lecture for which we want to create a relationship
  object ToAddRelation extends SessionVar[Box[Lecture]](Empty)

  // this object stores the current Status of the relationship menue
  object Status extends SessionVar[Any](RelationshipHelper.InitialStatus)

  // all these values are the initial values for the session variables
  val initialGroups = Set[(GroupName,SemesterNumber)]()
  val initialDozents = Set[Dozent]()

  // all these session variables are necessary to sotre the selected values between the different screens
  object Groups extends SessionVar[Set[(GroupName,SemesterNumber)]](initialGroups)
  object Dozents extends SessionVar[Set[Dozent]](initialDozents)

  // if somebody uses the cancel button the cancel function will be called
  def cancle() = {
    ToAddRelation(Empty)
    Groups(initialGroups)
    Dozents(initialDozents)
    Status(RelationshipHelper.InitialStatus)
    IsTutorial(false)
    S.redirectTo(lectureManagement)
  }

  // this function checks from where the user comes
  def checkWhereWasTheUserBefore(status: Boolean) {
    if(IsTutorial.is == status) {
      ToAddRelation(Empty)
      Groups(initialGroups)
      Dozents(initialDozents)
      Status(RelationshipHelper.InitialStatus)
      IsTutorial(!status)
      S.redirectTo(thisSide)
    }
  }
}