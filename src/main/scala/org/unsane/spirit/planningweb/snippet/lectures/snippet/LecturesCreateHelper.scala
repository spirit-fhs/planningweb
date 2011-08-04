package org.unsane.spirit.planningweb.snippet.lectures.snippet

import net.liftweb._
import http._
import scala.xml._
import net.liftweb.util.Props
import scala.collection.mutable.Set
import org.unsane.spirit.planningweb
import planningweb.transform._
import planningweb.persistence._
import planningweb.coursemanagement.impl.Course
import planningweb.dozentmanagement.impl.Dozent


/**
 * this object is very important because all LectureCreate-Traits need the same
 * instance of the staus objects
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
object LecturesCreateHelper {

  // all these objects are necessary to switch between the screens while creating a lecture
  object InitialStatus
  object AddedName
  object AddedCourse
  object AddedSemester
  object AddedDozent
  object AddedHoure
}

/**
 * this trait provides the LecturesCreate-Traits with all important values etc.
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
trait LecturesCreateHelper {

  val usedPersistence = Props.get("spirit.pers.layer") openOr ""
  val whence = S.referer openOr "/"
  val thisSide = "/lecture/create"
  val lectureManagement = "/lecture/management"

  /**  this object stores the current Status of the LectureCreate menue */
  object Status extends SessionVar[Any](LecturesCreateHelper.InitialStatus)

  /** this object is necessary to set a create lecture process apart from a update lecture process */
  object IsUpdate extends SessionVar[Boolean](false)

  /**
     * this object is necessary for the update process, because if somebody changes the name
     * of a lecture during the update process, we will lose the important values
     * to update the lecture
     */
  object NameBeforeUpdate extends SessionVar[String]("")

  // all these type aliases are necessary to understand the definition of the session variables easier
  type LectureName = String
  type TypeOfLecture = String
  type CourseName = String
  type SemesterNumber = Int
  type DozentName = String
  type GiveAlecture = Boolean
  type GiveAtutorial = Boolean
  type LectureHoures = Int
  type TutorialHoures = Int

  // all these values are the initial values for the session variables
  val initialName = ("","")
  val initialCourses = Set[CourseName]()
  val initialSemesters = Set[(CourseName,SemesterNumber)]()
  val initialDozents = Set[(DozentName,GiveAlecture,GiveAtutorial)]()
  val initialHoures = (0,0)

  // all these session variables are necessary to sotre the selected values between the different screens
  object Name extends SessionVar[(LectureName,TypeOfLecture)](initialName)
  object Courses extends SessionVar[Set[CourseName]](initialCourses)
  object Semesters extends SessionVar[Set[(CourseName,SemesterNumber)]](initialSemesters)
  object Dozents extends SessionVar[Set[(DozentName,GiveAlecture,GiveAtutorial)]](initialDozents)
  object Houres extends SessionVar[(LectureHoures,TutorialHoures)](initialHoures)

  val persistenceC:IPersistence = PersistenceFactory
                                   .createPersistence(TransformFactory
                                   .createTransformCourse(usedPersistence))

  val courses = persistenceC.read.asInstanceOf[List[Course]]

  val persistenceD:IPersistence = PersistenceFactory
                                    .createPersistence(TransformFactory
                                    .createTransformDozent(usedPersistence))

  val dozents = persistenceD.read.asInstanceOf[List[Dozent]]

  /** if somebody uses the cancel button the cancel function will be called */
  def cancle() = {
    Name(initialName)
    Courses(initialCourses)
    Semesters(initialSemesters)
    Dozents(initialDozents)
    Houres(initialHoures)
    Status(LecturesCreateHelper.InitialStatus)
    S.redirectTo(lectureManagement)
  }

  /** this function checks from where the user comes */
  def checkWhereWasTheUserBefore(status: Boolean) {
    if(IsUpdate.is == status) {
      Name(initialName)
      Courses(initialCourses)
      Semesters(initialSemesters)
      Dozents(initialDozents)
      Houres(initialHoures)
      Status(LecturesCreateHelper.InitialStatus)
      IsUpdate(!status)
      S.redirectTo(thisSide)
    }
  }
}