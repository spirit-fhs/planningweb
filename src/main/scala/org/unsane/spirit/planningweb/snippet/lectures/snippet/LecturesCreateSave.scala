package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This trait is the view to save a lecture in persistence
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
import org.unsane.spirit.planningweb
import planningweb.transform._
import planningweb.persistence._
import planningweb.lecturemanagement.impl._


trait  LecturesCreateSave extends LecturesCreateHelper {

  // this function represents the screen to save a lecture
  def saveLecture() = {

    val dozentInfos = Dozents.is.map(dozent => DozentInformation((dozents filter(_.name == dozent._1)).head,
                                                                 dozent._2,
                                                                 dozent._3))

    val toSave = Lecture(Name.is._1,
                         LectureType(Name.is._2),
                         Courses.is.toList.map(course =>
                           CourseInformation((courses filter(_.name == course)).head,
                                             (Semesters.is.toList.filter(cs => cs._1 == course)).map(
                                               cs =>
                                                 SemesterInformation(cs._2, dozentInfos.toList)))),
                         List(),
                         List(),
                         Houres._1,
                         Houres._2,
                         false,
                         false)

    // this function has two tasks, one is to store a new lecture in persistence
    // another is to update a existing lecture
    def save() = {
      val persistenceL:IPersistence = PersistenceFactory
                                        .createPersistence(TransformFactory
                                        .createTransformLecture(usedPersistence))

      val lectures = persistenceL.read.asInstanceOf[List[Lecture]]
      val alreadyAvailable = lectures.filter(_.name == Name.is._1)

      if (alreadyAvailable.isEmpty && IsUpdate.is == false) {
        persistenceL.create(toSave)
      }
      else {
        if (IsUpdate.is == false) {
          S.notice("Lecture already exist!")
        }
        else {
          val before = lectures.filter(_.name == NameBeforeUpdate.is)

          before match {
            case List() => persistenceL.create(toSave)
            case _ => persistenceL.update(before.head,toSave)
          }
          IsUpdate(false)
          NameBeforeUpdate("")
        }
      }

      Name(initialName)
      Courses(initialCourses)
      Semesters(initialSemesters)
      Dozents(initialDozents)
      Houres(initialHoures)
      Status(LecturesCreateHelper.InitialStatus)
      S.redirectTo(lectureManagement)
    }

    def back() = {
      Status(LecturesCreateHelper.AddedDozent)
      S.redirectTo(thisSide)
    }

    val saveButton = SHtml.submit("Speichern", save)
    val backButton = SHtml.submit("Zurück", back)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val saveLectureMenue = <table id="table-box">
                            <thead>
                             <th>{"Lehrveranstaltung:"}</th>
                             <th>{"Typ:"}</th>
                             <th>{"SWS Vorlesung:"}</th>
                             <th>{"SWS Übung:"}</th>
                            </thead>
                            <tr>
                             <th>{toSave.name}</th>
                             <th>{toSave.lectureType.name}</th>
                             <th>{toSave.hoursOfLecture.toString}</th>
                             <th>{toSave.hoursOfTutorial.toString}</th>
                            </tr>
                           </table> ++
                           <table id="table-box">
                            <thead>
                             <th>{"Studiengang:"}</th>
                             <th>{"Semester:"}</th>
                            </thead>
                             { toSave.courseInfos.map {
                                 ci => <tr>
                                       <th>{ci.course.name}</th>
                                       <th>{ci.semesterInfos.sortBy(_.semester) map(_.semester.toString ++ <br />)}</th>
                                      </tr>
                                 }
                             }
                           </table> ++
                           <table id="table-box">
                            <thead>
                             <th>{"Dozent:"}</th>
                             <th>{"Hält Vorlesung:"}</th>
                             <th>{"Hält Übung:"}</th>
                            </thead>
                             { toSave.courseInfos.head.semesterInfos.head.dozentInfos.map {
                                 di => <tr>
                                        <th>{di.dozent.name}</th>
                                        <th>{di.giveLecture}</th>
                                        <th>{di.giveTutorial}</th>
                                       </tr>
                               }
                             }
                           </table> ++ backButton ++ cancleButton ++ saveButton

    saveLectureMenue
  }
}