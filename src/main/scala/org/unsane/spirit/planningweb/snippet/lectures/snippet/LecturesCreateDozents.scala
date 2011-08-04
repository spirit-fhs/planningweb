package org.unsane.spirit.planningweb.snippet.lectures.snippet

import net.liftweb._
import http._
import scala.xml._
import scala.collection.mutable.Set
import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.Dozent

/**
 * This trait is the representation of the view to add some dozents to a lecture
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
trait LecturesCreateDozents extends LecturesCreateHelper {

  /** this function represents the screen to add some dozents to a lecture */
  def addDozents() = {

    val toAdd = Set[(DozentName,GiveAlecture,GiveAtutorial)]()
    var giveLecture = false
    var giveTutorial = false

    def add(dozent: Dozent) = {
      toAdd += ((dozent.name,giveLecture,giveTutorial))
      giveLecture = false
      giveTutorial = false
    }

    def next() = {
      toAdd.toList match {
        case List() => S.notice("Please select a dozent!")
        case _ if !(toAdd filter(dozent => dozent._2 == false && dozent._3 == false)).isEmpty =>
                  S.notice("Wrong Input!")
        case _ if (toAdd filter(dozent => dozent._2 == true)).isEmpty =>
                  S.notice("At least one dozent have to give the lecture!")
        case _ => Dozents(toAdd)
                  Status(LecturesCreateHelper.AddedDozent)
                  S.redirectTo(thisSide)
      }
    }

    def back() = {
      Status(LecturesCreateHelper.AddedCourse)
      S.redirectTo(thisSide)
    }

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"Dozent:"}</th>
                       <th>{"Hält Vorlesung:"}</th>
                       <th>{"Hält Übung:"}</th>
                       <th>{"Speichern:"}</th>
                      </thead>
                      { dozents.flatMap {
                          dozent => <tr>
                                     <th>{dozent.name}</th>
                                     <th>{SHtml.checkbox(if (Dozents.is.contains((dozent.name,true,false))) {
                                                           true
                                                         }
                                                         else {
                                                           if  (Dozents.is.contains((dozent.name,true,true))) {
                                                             true
                                                           }
                                                           else {
                                                             false
                                                           }
                                                         }
                                                         , if (_) giveLecture = true)}</th>
                                     <th>{SHtml.checkbox(if (Dozents.is.contains((dozent.name,false,true))) {
                                                           true
                                                         }
                                                         else {
                                                           if  (Dozents.is.contains((dozent.name,true,true))) {
                                                             true
                                                           }
                                                           else {
                                                             false
                                                           }
                                                         }
                                                         , if (_) giveTutorial = true)}</th>
                                     <th>{SHtml.checkbox(false, if (_) add(dozent))}</th>
                                    </tr>
                        }
                      }
                     </table>

    val nextButton = SHtml.submit("Weiter", next)
    val backButton = SHtml.submit("Zurück", back)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val addDozentMenue = checkboxes ++ backButton ++ cancleButton ++ nextButton

    addDozentMenue
  }
}