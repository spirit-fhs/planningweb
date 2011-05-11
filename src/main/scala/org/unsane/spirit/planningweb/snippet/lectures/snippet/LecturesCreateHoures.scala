package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This trait is the view to add the semester periods per week to a lecture
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import net.liftweb._
import http._
import scala.xml._
import scala.collection.mutable.Set
import org.unsane.spirit.planningweb


trait LecturesCreateHoures extends LecturesCreateHelper {

  // this function represents the screen to select the semester periods per week
  def addHoures() = {

    var hoursOfLecture = "0"
    var hoursOfTutorial = "0"

    val addHouresOfLectureText = SHtml.text(Houres._1.toString, hl => hoursOfLecture = hl.trim)
    val addHouresOfTutorialText = SHtml.text(Houres._2.toString, ht => hoursOfTutorial = ht.trim)

    def next() = {
      try {
        Houres((hoursOfLecture.toInt,hoursOfTutorial.toInt))
      } catch {
          case e: Exception => S.notice(hoursOfLecture + " or " + hoursOfTutorial + " is not a number!")
      }

      Houres.is match {
        case (0,0) => S.notice("Input is empty!")
        case (l,_) if l == 0 =>
                  S.notice("you have to select min. 1h for the lecture per week!")
        case (_,t) if !(Dozents.is filter(_._3 == true)).isEmpty && t <= 0 =>
                  S.notice("Please select time for a tutorial!")
        case _ => Status(LecturesCreateHelper.AddedHoure)
                  S.redirectTo(thisSide)
      }
    }

    def back() = {
      Status(LecturesCreateHelper.AddedSemester)
      S.redirectTo(thisSide)
    }

    val nextButton = SHtml.submit("Weiter", next)
    val backButton = SHtml.submit("Zurück", back)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val addHoureMenue = <table id="table-plane">
                        <tr>
                         <th>{"Wochenstunden Vorlesung:"}</th>
                         <th>{"Wochenstunden Übung:"}</th>
                        </tr>
                        <tr>
                         <th>{addHouresOfLectureText}</th>
                         <th>{addHouresOfTutorialText}</th>
                        </tr>
                       </table> ++ backButton ++ cancleButton ++ nextButton

    addHoureMenue
  }
}