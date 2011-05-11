package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This class is the view to select a lecture for summer or winter semester
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
import scala.collection.mutable.Set
import net.liftweb.util.Props

import org.unsane.spirit.planningweb
import planningweb.persistence._
import planningweb.transform._
import planningweb.lecturemanagement.impl._


class SelectSemester {

  val usedPersistence = Props.get("spirit.pers.layer") openOr ""
  val persistence = PersistenceFactory
                     .createPersistence(TransformFactory
                     .createTransformLecture(Props.get("spirit.pers.layer") openOr ""))

  // this function builds the menue to select a lecture for summer or winter semester
  def select() = {
    var lectures = persistence.read.asInstanceOf[List[Lecture]]
    val toSS = Set[Lecture]()
    val toWS = Set[Lecture]()

    // this function saves the selected lectures
    def save() = {

      val enableSS = true
      val disableSS = false
      val enableWS = true
      val disableWS = false
      val set = true
      val selectSem = true
      val unselectSem = false

      // this helpfunction stores the updatet lectures in persistence
      def setSemesterStatus(listOfLectures: List[Lecture], ss: Boolean, ws: Boolean, status: Boolean) {

        def setSS(lecture: Lecture): Boolean = {
          if(ss == true && status == true) {
            true
          } else {
            if(ss == true && status == false) {
              false
            } else {
                if(ss == false && status == false) {
                  lecture.inSummerSemester
                } else {
                    lecture.inSummerSemester
                  }
              }
            }
        }

        def setWS(lecture: Lecture): Boolean = {
          if(ws == true && status == true) {
            true
          } else {
            if(ws == true && status == false) {
              false
            } else {
                if(ws == false && status == false) {
                  lecture.inWinterSemester
                } else {
                    lecture.inWinterSemester
                  }
              }
            }
        }

        listOfLectures.map(before => persistence.update(before,
                                                        Lecture(before.name,
                                                                before.lectureType,
                                                                before.courseInfos,
                                                                before.hasLectureTogetherWith,
                                                                before.hasTutorialTogetherWith,
                                                                before.hoursOfLecture,
                                                                before.hoursOfTutorial,
                                                                setSS(before),
                                                                setWS(before))))
      }

      val ssToSetUnselected = lectures.diff(toSS.toList)

      setSemesterStatus(ssToSetUnselected, enableSS, disableWS, unselectSem)

      val ssToSetSelected = toSS.toList

      setSemesterStatus(ssToSetSelected, enableSS, disableWS, selectSem)

      lectures = persistence.read.asInstanceOf[List[Lecture]]

      val wsToSetUnselected = lectures.diff(toWS.toList)

      setSemesterStatus(wsToSetUnselected, disableSS, enableWS, unselectSem)

      lectures = persistence.read.asInstanceOf[List[Lecture]]

      val wsToSetSelected = lectures.filter(l=> toWS.exists(_.name == l.name))

      setSemesterStatus(wsToSetSelected, disableSS, enableWS, selectSem)

      S.notice("Your changes were saved!")
    }

    val checkboxes = <table id="table-box">
                      <thead>
                      <tr>
                       <th>{"Lehrveranstaltung:"}</th>
                       <th>{"Studiengang:"}</th>
                       <th>{"SS:"}</th>
                       <th>{"WS:"}</th>
                      </tr>
                      </thead>
                       { lectures.flatMap {
                           lecture => <tr>
                                       <th>{lecture.name}</th>
                                       <th>{lecture.courseInfos.map(ci => ci.course.name ++ <br /> ++
                                                                          ci.semesterInfos.foldLeft("")(
                                                                            _ + _.semester.toString + " "
                                                                          )
                                                                    ++ <br />)}</th>
                                       <th>{SHtml.checkbox(lecture.inSummerSemester, if (_) toSS += lecture)}</th>
                                       <th>{SHtml.checkbox(lecture.inWinterSemester, if (_) toWS += lecture)}</th>
                                      </tr>
                         }
                       }

                     </table>

    val saveButton = SHtml.submit("Speichern", save)

    val selectSemesterMenue = checkboxes ++ saveButton

    selectSemesterMenue
  }

  def render = {
    "#select *" #> select
  }
}