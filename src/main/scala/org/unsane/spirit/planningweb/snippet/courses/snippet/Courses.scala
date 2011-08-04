package org.unsane.spirit.planningweb.snippet.courses.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml._
import net.liftweb.util.Props
import js._
import JsCmds._
import JE._
import org.unsane.spirit.planningweb
import planningweb.coursemanagement.impl.{Course, Semester}
import planningweb.lecturemanagement.impl._
import planningweb.persistence._
import planningweb.transform._

/**
 * This class is the representation of the view to manage the diffenrent courses
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

class Courses {
  val perLayer = Props.get("spirit.pers.layer") openOr ""

  val persistenceLecture:IPersistence = PersistenceFactory
                                          .createPersistence(TransformFactory
                                          .createTransformLecture(perLayer))

  val persistence:IPersistence = PersistenceFactory.createPersistence(TransformFactory
                                                   .createTransformCourse(perLayer))

  val courses = persistence.read.asInstanceOf[List[Course]]

  val lectures = persistenceLecture.read.asInstanceOf[List[Lecture]]
  var usedCourses = List[Course]()
  lectures match {
    case List() => usedCourses = List()
    case _ => usedCourses = lectures.map(l => l.courseInfos.map(_.course)).flatten
  }

  val alreadyInUse = usedCourses.filter(courses.contains(_))

  /** to add a course */
  def add() = {

    var name = ""
    var shortcut = ""
    var semesterCount = "1"
    var semesters:List[Semester] = List()
    val initSemesterCount = "6"

    // to store a course object
    def addCourse() {
      val minSem = 1
      val minMembers = 0
      val alreadyAvailable = courses.filter {_.name == name}
      val semCount = try {
                       semesterCount.toInt
                     } catch {
                         case e: Exception => S.notice(semesterCount + " is not a Number!")
                                              minSem
                       }

      semesters = (minSem to semCount).toList map(semester => Semester(semester, minMembers))

      if (name != "" &&
          shortcut != "" &&
          semCount >= minSem &&
          alreadyAvailable.isEmpty) {
        persistence.create(Course(name, shortcut, semesters))
      }
      else {
        S.notice("Course already exist or Input is empty!")
      }
    }

    // to add a name for a course
    val nameToAddText = SHtml.text("", n => name = n.trim)
    // to add a shortcut for a course
    val shortcutToAddText = SHtml.text("", s => shortcut = s.trim)
    // to add the semester-count of a course
    val semesterCountToAddText = SHtml.text(initSemesterCount, sem => semesterCount = sem.trim)
    // a button to store a coures object
    val addButton = SHtml.submit("Hinzufügen", addCourse)
    // the menue to add a course
    val addMenue = <table id="table-plane">
                    <tr>
                     <th>
                       {"Name:"}<br />{nameToAddText}
                     </th>
                     <th>
                       {"Abkürzung:"}<br />{shortcutToAddText}
                     </th>
                    </tr>
                    <tr>
                     <th>
                       {"Semesteranzahl:"}<br />{semesterCountToAddText}
                     </th>
                     <th>
                     </th>
                    </tr>
                   </table> ++ addButton

    addMenue
  }

  /** to delet a course */
  def delete () = {
    import scala.collection.mutable.Set
    val toDelete = Set[Course]()

     // to delete a course object
    def deleteCourses(toDelete: Set[Course]) = {
      toDelete foreach {
        course => if(!alreadyInUse.contains(course)) {
                    persistence.delete(course)
                  } else {S.warning("Could not delete course, because " + course.name + " is already in use!")}
      }
    }

    // to select some courses to delete
    val checkboxes = <table id="table-box">
                       <thead>
                        <th>{"Löschen:"}</th>
                        <th>{"Name:"}</th>
                        <th>{"Abkürzung:"}</th>
                        <th>{"Semester:"}</th>
                        <th>{"Teilnehmer:"}</th>
                       </thead>
                       { courses.flatMap {
                         course => <tr>
                                    <th>{SHtml.checkbox(false, if (_) toDelete += course)}</th>
                                    <th>{course.name}</th>
                                    <th>{course.shortcut}</th>
                                    <th>{course.semesters map(_.name.toString ++ <br />)}</th>
                                    <th>{course.semesters map(_.members.toString ++ <br />)}</th>
                                   </tr>
                         }
                       }
                     </table>

    // a button to delete some courses
    val delete = SHtml.submit("Löschen", () => deleteCourses(toDelete))
    checkboxes ++ delete
  }

  /** to update a course */
  def update() = {

    var shortcut = ""
    var members = ""
    object CourseName extends SessionVar[String]("")
    object SemesterName extends SessionVar[String]("")
    val thisSide = "/course/course"


    val course = courses filter(course => course.name == CourseName.is)
    val semestersForCourse = if (course.isEmpty) {
                                List[Semester]()
                              }
                              else {
                                course.head.semesters
                              }

    // to set CourseName and SemesterName
    def setSession(set: String): JsCmd = {

      val isCourse = courses.filter(course => course.name == set).isEmpty

      isCourse match {
        case true if set == "" =>
        case true => SemesterName(set)
        case false => CourseName(set)
                      SemesterName("")
      }

      RedirectTo(thisSide)
    }

    val (name2, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                       s => setSession(s))
    // to update a course object
    def updateCourse() {
      if (CourseName.is != "" &&
          SemesterName.is != "" &&
          shortcut != "" &&
          members != "") {

        val before = courses.filter {_.name == CourseName.is}.head

        try {
          val semesterUpdate = semestersForCourse.map(sem => if (sem.name.toString == SemesterName.is) {
                                                               Semester(sem.name, members.toInt)
                                                             }
                                                             else {
                                                               Semester(sem.name, sem.members)
                                                             }
                                                     )
          if(!alreadyInUse.contains(before)) {
            persistence.update(before,
                               Course(CourseName.is,
                               shortcut,
                               semesterUpdate))
          } else {S.warning("Could not update couse, becaurse " + before.name + " is already in use!")}
        } catch {
            case e: Exception => S.notice(SemesterName.is +
                                          " or " +
                                          members +
                                          " is not a Number!")
          }
      }
      else {
        S.notice("No course or semester selected!")
      }
    }

    // to select a course to update
    def courseUpdateChoice() = {
      SHtml.untrustedSelect(("","")::(courses map (course => (course.name, course.name))),
                            Full(CourseName.is),
                            () => _,
                            "onchange" -> js.toJsCmd)
    }

    // to add a new shortcut to update
    def shortcutUpdateText() = {
      SHtml.text(if (CourseName.is == "") {
                   ""
                 }
                 else {
                   val preview = courses.filter(course => course.name == CourseName.is)
                   courses match {
                     case List() => ""
                     case _ if preview.isEmpty
                       => ""
                     case _ =>
                       preview.head
                              .shortcut
                   }
                 }, s => shortcut = s.trim)
    }
    // to select a semester to update
    def semesterUpdateChoice() = {

      SHtml.untrustedSelect(("","")::(semestersForCourse map (semester => (semester.name.toString,
                                                                           semester.name.toString))),
                            if (SemesterName.is == "") {
                              Full("")
                            }
                            else {
                              val preview =  semestersForCourse.filter(sem =>
                                                                       sem.name.toString ==
                                                                       SemesterName.is)
                              semestersForCourse match {
                                case List() => Full("")
                                case _ if preview.isEmpty
                                  => Full("")
                                case _ =>
                                  Full(preview.head
                                              .name
                                              .toString)
                              }
                            },
                            () => _,
                            "onchange" -> js.toJsCmd)
    }

    // to add number of members to update a course
    def membersUpdateText() = {
      SHtml.text(if (SemesterName.is == "") {
                   ""
                 }
                 else {
                   val preview = semestersForCourse.filter(sem => sem.name.toString == SemesterName.is)
                   semestersForCourse match {
                     case List() => ""
                     case _ if preview.isEmpty
                       => ""
                     case _ =>
                       preview.head
                              .members
                              .toString
                   }
                 }, m => members = m.trim)
    }
    // button to update a course
    def updateButton() = {
      SHtml.submit("Ändern", updateCourse)
    }
    // the menue to update a course
    val updateMenue =
      <table id="table-plane">
       <tr>
        <th>
         {"Name:"}<br />{courseUpdateChoice}
        </th>
        <th>
         {"Abkürzung:"}<br />{shortcutUpdateText}
        </th>
       </tr>
        <tr>
         <th>
          {"Semester:"}<br />{semesterUpdateChoice}
         </th>
         <th>
          {"Teilnehmer:"}<br />{membersUpdateText}
         </th>
        </tr>
       </table> ++ updateButton

    updateMenue
  }

  // to render the course menue
  def render = {
    "#add *" #> add &
    "#delete *" #> delete &
    "#update *" #> update
  }
}