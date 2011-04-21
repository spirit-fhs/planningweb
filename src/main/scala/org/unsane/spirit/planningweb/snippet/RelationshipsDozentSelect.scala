package org.unsane.spirit.planningweb.snippet

/**
 * This trait is the view to add dozents to a group
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
import org.unsane.spirit.planningweb.dozentmanagement.impl.Dozent


trait RelationshipsDozentSelect extends RelationshipHelper {
  // this function creates the menue to select dozents for a group
  def addDozents() = {
    var dozents = List[Dozent]()
    val toAdd = Set[Dozent]()

    // every course has the same dozents by default
    val dozentInfos = ToAddRelation.is
          .head.courseInfos
          .head.semesterInfos
          .head.dozentInfos

    // this is necessary to set dozents for lecture apart from dozents for tutorial
    if(IsTutorial.is == false) {
      val lectureDozents = (dozentInfos.filter(_.giveLecture == true))
                             .map(_.dozent)

      dozents = lectureDozents
    } else {
        val tutorialDozents = (dozentInfos.filter(_.giveTutorial == true))
                                .map(_.dozent)

        dozents = tutorialDozents
      }

    def next() = {
      toAdd.toList match {
        case List() => S.notice("Please select a dozent!")
        case _ => Dozents(toAdd)
                  Status(RelationshipHelper.SelectedDozents)
                  S.redirectTo(thisSide)
      }
    }

    def back() = {
      Status(RelationshipHelper.SelectedLecture)
      S.redirectTo(thisSide)
    }

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"Speichern:"}</th>
                       <th>{"Dozent:"}</th>
                      </thead>
                      { dozents.flatMap {
                         d => <tr>
                               <th>{SHtml.checkbox(if (Dozents.is.contains(d)) {
                                                     true
                                                   }
                                                   else {
                                                     false
                                                   }, if (_) toAdd += d)}</th>
                               <th>{d.name}</th>
                              </tr>
                        }
                      }
                     </table>

    val nextButton = SHtml.submit("Weiter", next)
    val backButton = SHtml.submit("Zurück", back)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val addDozentsMenue = checkboxes ++ backButton ++ cancleButton ++ nextButton

    addDozentsMenue
  }
}